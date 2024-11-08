const fs = require('fs').promises;
const csv = require('csv-parse');
const database = require('../config/database');
const logger = require('../utils/logger');
const Validators = require('../utils/validators');

class CSVProcessor {
    constructor() {
        this.db = null;
        this.collection = process.env.MONGODB_COLLECTION;
    }

    readFile(filePath) {
        return new Promise((resolve, reject) => {
            fs.readFile(filePath, 'utf-8')
                .then(content => {
                    logger.info(`Archivo leído correctamente: ${filePath}`);
                    resolve(content);
                })
                .catch(error => {
                    logger.error(`Error leyendo archivo: ${error.message}`);
                    reject(error);
                });
        });
    }

    parseCSV(fileContent) {
        return new Promise((resolve, reject) => {
            csv.parse(fileContent, {
                columns: true,
                skip_empty_lines: true,
                trim: true
            }, (error, records) => {
                if (error) {
                    logger.error(`Error parseando CSV: ${error.message}`);
                    reject(error);
                } else {
                    logger.info(`CSV parseado correctamente. Registros encontrados: ${records.length}`);
                    resolve(records);
                }
            });
        });
    }

    validateRecords(records) {
        return Promise.all(records.map(record => 
            Validators.validateRecord(record)
        )).then(validatedRecords => {
            logger.info(`${validatedRecords.length} registros validados correctamente`);
            return validatedRecords;
        });
    }

    saveToMongoDB(records) {
        return new Promise((resolve, reject) => {
            this.db.collection(this.collection).insertMany(records)
                .then(result => {
                    logger.info(`${result.insertedCount} documentos insertados en MongoDB`);
                    resolve({
                        success: true,
                        insertedCount: result.insertedCount,
                        documentIds: result.insertedIds
                    });
                })
                .catch(error => {
                    logger.error(`Error guardando en MongoDB: ${error.message}`);
                    reject(error);
                });
        });
    }

    processCSVFile(filePath) {
        return database.connect()
            .then(db => {
                this.db = db;
                return this.readFile(filePath);
            })
            .then(fileContent => this.parseCSV(fileContent))
            .then(records => this.validateRecords(records))
            .then(validatedRecords => this.saveToMongoDB(validatedRecords))
            .catch(error => {
                logger.error('Error en el proceso:', error);
                throw error;
            })
            .finally(() => {
                return database.disconnect();
            });
    }
}

module.exports = CSVProcessor;
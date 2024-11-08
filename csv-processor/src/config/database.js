require('dotenv').config();
const { MongoClient } = require('mongodb');
const logger = require('../utils/logger');

class Database {
    constructor() {
        this.client = null;
        this.uri = process.env.MONGODB_URI;
        this.dbName = process.env.MONGODB_DB_NAME;
    }

    connect() {
        return new Promise((resolve, reject) => {
            MongoClient.connect(this.uri)
                .then(client => {
                    this.client = client;
                    logger.info('Conexión exitosa a MongoDB');
                    resolve(client.db(this.dbName));
                })
                .catch(error => {
                    logger.error('Error conectando a MongoDB:', error);
                    reject(error);
                });
        });
    }

    disconnect() {
        return new Promise((resolve, reject) => {
            if (this.client) {
                this.client.close()
                    .then(() => {
                        logger.info('Desconexión exitosa de MongoDB');
                        resolve();
                    })
                    .catch(error => {
                        logger.error('Error al desconectar de MongoDB:', error);
                        reject(error);
                    });
            } else {
                resolve();
            }
        });
    }
}

module.exports = new Database();
require('dotenv').config();
const path = require('path');
const CSVProcessor = require('./services/CSVProcessor');
const logger = require('./utils/logger');

const csvPath = path.join(__dirname, '../data/input/datos.csv');
const processor = new CSVProcessor();

logger.info('Iniciando procesamiento de CSV...');

processor.processCSVFile(csvPath)
    .then(result => {
        logger.info('Proceso completado exitosamente:', result);
        process.exit(0);
    })
    .catch(error => {
        logger.error('Error en el proceso:', error);
        process.exit(1);
    });

// Manejo de errores no capturados
process.on('unhandledRejection', (error) => {
    logger.error('Error no manejado (Promise):', error);
    process.exit(1);
});

process.on('uncaughtException', (error) => {
    logger.error('Error no manejado (Exception):', error);
    process.exit(1);
});
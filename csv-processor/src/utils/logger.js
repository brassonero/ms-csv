const winston = require('winston');
const path = require('path');

// Configuración de formato personalizado
const customFormat = winston.format.combine(
    winston.format.timestamp({
        format: 'YYYY-MM-DD HH:mm:ss'
    }),
    winston.format.printf(({ level, message, timestamp, ...metadata }) => {
        let msg = `${timestamp} [${level.toUpperCase()}]: ${message}`;
        if (Object.keys(metadata).length > 0) {
            msg += JSON.stringify(metadata);
        }
        return msg;
    })
);

const logger = winston.createLogger({
    level: process.env.LOG_LEVEL || 'info',
    format: customFormat,
    transports: [
        // Escribir en archivo para errores
        new winston.transports.File({ 
            filename: path.join(__dirname, '../../logs/error.log'), 
            level: 'error'
        }),
        // Escribir en archivo para todos los logs
        new winston.transports.File({ 
            filename: path.join(__dirname, '../../logs/combined.log')
        })
    ]
});

// Si no estamos en producción, también log a la consola
if (process.env.NODE_ENV !== 'production') {
    logger.add(new winston.transports.Console({
        format: winston.format.combine(
            winston.format.colorize(),
            winston.format.simple()
        )
    }));
}

module.exports = logger;
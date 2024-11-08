const logger = require('./logger');

class Validators {
    static validateEmail(email) {
        return new Promise((resolve, reject) => {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!email || !emailRegex.test(email)) {
                reject(new Error(`Email inválido: ${email}`));
            }
            resolve(email);
        });
    }

    static validateAge(age) {
        return new Promise((resolve, reject) => {
            const numAge = parseInt(age);
            if (isNaN(numAge) || numAge < 0 || numAge > 120) {
                reject(new Error(`Edad inválida: ${age}`));
            }
            resolve(numAge);
        });
    }

    static validateDate(date) {
        return new Promise((resolve, reject) => {
            const parsedDate = new Date(date);
            if (isNaN(parsedDate.getTime())) {
                reject(new Error(`Fecha inválida: ${date}`));
            }
            resolve(parsedDate);
        });
    }

    static validateRecord(record) {
        return new Promise((resolve, reject) => {
            const requiredFields = ['nombre', 'edad', 'email'];
            const missingFields = requiredFields.filter(field => !record[field]);

            if (missingFields.length > 0) {
                reject(new Error(`Campos requeridos faltantes: ${missingFields.join(', ')}`));
            }

            Promise.all([
                this.validateEmail(record.email),
                this.validateAge(record.edad),
                record.fecha_registro ? this.validateDate(record.fecha_registro) : Promise.resolve(new Date())
            ])
            .then(([validEmail, validAge, validDate]) => {
                resolve({
                    ...record,
                    email: validEmail,
                    edad: validAge,
                    fecha_registro: validDate,
                    processedAt: new Date()
                });
            })
            .catch(error => {
                reject(error);
            });
        });
    }
}

module.exports = Validators;
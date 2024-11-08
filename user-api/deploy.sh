#!/bin/bash

# Función para imprimir mensajes con formato
print_message() {
    echo "----------------------------------------"
    echo "🚀 $1"
    echo "----------------------------------------"
}

# Verificar prerrequisitos
if ! command -v kubectl &> /dev/null; then
    echo "❌ kubectl no está instalado"
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven no está instalado"
    exit 1
fi

# Construir el proyecto
print_message "Construyendo el proyecto con Maven..."
mvn clean package -DskipTests || {
    echo "❌ Error en la construcción Maven"
    exit 1
}

# Construir imagen Docker
print_message "Construyendo imagen Docker..."
docker build -t users-api:latest . || {
    echo "❌ Error construyendo la imagen Docker"
    exit 1
}

# Crear namespace
print_message "Creando namespace users-api..."
kubectl create namespace users-api 2>/dev/null || true

# Aplicar configuraciones
print_message "Aplicando configuraciones..."
kubectl apply -f kubernetes/postgres/secret.yml
kubectl apply -f kubernetes/postgres/pvc.yml
kubectl apply -f kubernetes/postgres/deployment.yml
kubectl apply -f kubernetes/app/configmap.yml

# Esperar a que PostgreSQL esté listo
print_message "Esperando a que PostgreSQL esté listo..."
kubectl wait --namespace=users-api --for=condition=available deployment/postgres --timeout=300s || {
    echo "❌ Timeout esperando a PostgreSQL"
    exit 1
}

# Desplegar la aplicación
print_message "Desplegando Users API..."
kubectl apply -f kubernetes/app/deployment.yml

# Esperar a que la aplicación esté lista
print_message "Esperando a que la aplicación esté lista..."
kubectl wait --namespace=users-api --for=condition=available deployment/users-api --timeout=300s

# Mostrar información del despliegue
print_message "¡Despliegue completado!"
echo ""
echo "📊 Estado de los pods:"
kubectl get pods -n users-api

echo ""
echo "🌐 Servicios disponibles:"
echo "- API: http://localhost:30080/api/usuarios"
echo "- Swagger UI: http://localhost:30080/swagger-ui.html"
echo "- Actuator: http://localhost:30080/actuator"
echo ""

# Verificar servicios
echo "🔍 Verificando servicios..."
kubectl get services -n users-api

# Instrucciones adicionales
echo ""
echo "📝 Para ver los logs:"
echo "kubectl logs -n users-api -l app=users-api"
echo ""
echo "🔄 Para reiniciar el despliegue:"
echo "./deploy.sh"
echo ""
echo "❌ Para eliminar todo:"
echo "kubectl delete namespace users-api"
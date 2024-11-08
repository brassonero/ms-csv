#!/bin/bash

# Create namespace
kubectl create namespace csv-processor

# Apply MongoDB resources
echo "Deploying MongoDB..."
kubectl apply -f kubernetes/mongodb/

# Wait for MongoDB to be ready
echo "Waiting for MongoDB to be ready..."
kubectl wait --namespace=csv-processor --for=condition=available deployment/mongodb --timeout=300s

# Deploy Mongo Express
echo "Deploying Mongo Express..."
kubectl apply -f kubernetes/mongo-express/

# Deploy CSV Processor application
echo "Deploying CSV Processor application..."
kubectl apply -f kubernetes/app/

# Wait for all deployments
echo "Waiting for all deployments to be ready..."
kubectl wait --namespace=csv-processor --for=condition=available deployment/mongo-express --timeout=300s
kubectl wait --namespace=csv-processor --for=condition=available deployment/csv-processor --timeout=300s

echo "Deployment completed!"
echo "MongoDB is running on mongodb-service:27017"
echo "Mongo Express UI is available at: http://localhost:30081"
echo "CSV Processor application is ready"

# Show pod status
echo "Pod status:"
kubectl get pods -n csv-processor
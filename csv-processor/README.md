# CSV Processor

A Node.js application that processes CSV files and stores the data in MongoDB.

## Architecture

```mermaid
graph TB
    CSV[CSV File] --> App[CSV Processor]
    App --> Validation[Data Validation]
    Validation --> MongoDB[(MongoDB)]
    MongoDB --> MongoExpress[Mongo Express UI]
    
    subgraph Kubernetes Cluster
        App
        MongoDB
        MongoExpress
    end

    subgraph Data Flow
        CSV --> |Read| App
        App --> |Process| Validation
        Validation --> |Store| MongoDB
        MongoDB --> |View| MongoExpress
    end
```

## Tech Stack
- Node.js
- MongoDB
- Docker
- Kubernetes

## Project Structure
```
csv-processor/
├── src/
│   ├── config/
│   │   └── database.js
│   ├── services/
│   │   └── CSVProcessor.js
│   └── utils/
│       ├── logger.js
│       └── validators.js
├── kubernetes/
│   ├── mongodb/
│   ├── mongo-express/
│   └── app/
├── data/
│   └── input/
├── deploy.sh
├── Dockerfile
└── package.json
```

## Prerequisites
- Node.js v14+
- Docker
- Kubernetes cluster (or Minikube/Docker Desktop with Kubernetes)
- kubectl CLI

## Installation

1. Clone the repository
```bash
git clone [repository-url]
cd csv-processor
```

2. Install dependencies
```bash
npm install
```

3. Create .env file
```env
MONGODB_URI=mongodb://root:example@localhost:27017
MONGODB_DB_NAME=csv_processor_db
MONGODB_COLLECTION=processed_data
LOG_LEVEL=info
```

## Deployment

1. Build and deploy to Kubernetes
```bash
# Build Docker image
docker build -t csv-processor:latest .

# Deploy to Kubernetes
./deploy.sh
```

2. Verify deployment
```bash
kubectl get pods -n csv-processor
```

## Usage

1. Place your CSV file in `data/input/` directory

2. Access services:
- Mongo Express UI: http://localhost:30081
  - Username: admin
  - Password: pass

## Monitoring

View application logs:
```bash
kubectl logs -n csv-processor -l app=csv-processor
```

## Cleanup

Remove all resources:
```bash
kubectl delete namespace csv-processor
```
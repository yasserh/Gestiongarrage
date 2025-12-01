# Guide de Démarrage - Microservice Renault

## 🚀 Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.9+
- Docker & Docker Compose

### Option 1 : Démarrage avec Docker (Recommandé)

```bash
# 1. Cloner le projet
git clone <repository-url>
cd Renault

# 2. Démarrer tous les services
docker-compose up -d

# 3. Vérifier que tous les services sont démarrés
docker-compose ps

# 4. Accéder à l'application
# API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

### Option 2 : Démarrage en Local

```bash
# 1. Démarrer PostgreSQL et Kafka
docker-compose up -d postgres kafka zookeeper

# 2. Compiler le projet
mvn clean install

# 3. Lancer l'application
mvn spring-boot:run

# 4. L'application sera accessible sur http://localhost:8080/api
```

## 📚 Documentation API

### Swagger UI
Une fois l'application démarrée, accédez à la documentation interactive :
```
http://localhost:8080/api/swagger-ui.html
```

### Endpoints Principaux

#### Garages
```
GET    /api/garages                          # Liste paginée
POST   /api/garages                          # Créer un garage
GET    /api/garages/{id}                     # Détails d'un garage
PUT    /api/garages/{id}                     # Modifier un garage
DELETE /api/garages/{id}                     # Supprimer un garage
GET    /api/garages/search/by-name           # Recherche par nom
GET    /api/garages/search/by-city           # Recherche par ville
GET    /api/garages/search/by-fuel-type      # Recherche par carburant
GET    /api/garages/search/by-accessory-type # Recherche par accessoire
GET    /api/garages/available-capacity       # Garages avec capacité
```

#### Véhicules
```
POST   /api/vehicles/garage/{garageId}       # Ajouter un véhicule
GET    /api/vehicles/{id}                    # Détails d'un véhicule
GET    /api/vehicles/garage/{garageId}       # Véhicules d'un garage
PUT    /api/vehicles/{id}                    # Modifier un véhicule
DELETE /api/vehicles/{id}                    # Supprimer un véhicule
GET    /api/vehicles/search/by-model         # Recherche par modèle
GET    /api/vehicles/search/by-fuel-type     # Recherche par carburant
GET    /api/vehicles/eco-friendly            # Véhicules écologiques
```

#### Accessoires
```
POST   /api/accessories/vehicle/{vehicleId}  # Ajouter un accessoire
GET    /api/accessories/{id}                 # Détails d'un accessoire
GET    /api/accessories/vehicle/{vehicleId}  # Accessoires d'un véhicule
PUT    /api/accessories/{id}                 # Modifier un accessoire
DELETE /api/accessories/{id}                 # Supprimer un accessoire
GET    /api/accessories/search/by-type       # Recherche par type
GET    /api/accessories/vehicle/{vehicleId}/total-price # Prix total
```

## 🧪 Tests

### Exécuter tous les tests
```bash
mvn test
```

### Tests avec couverture de code
```bash
mvn clean test jacoco:report
```

Le rapport de couverture sera disponible dans :
```
target/site/jacoco/index.html
```

### Types de tests
- **Tests unitaires** : Services, Mappers, Validateurs
- **Tests d'intégration** : Controllers, Repositories
- **Tests de contraintes** : Validation métier (quota, horaires)

## 📊 Monitoring

### Actuator Endpoints
```
http://localhost:8080/api/actuator/health    # Santé de l'application
http://localhost:8080/api/actuator/info      # Informations
http://localhost:8080/api/actuator/metrics   # Métriques
```

## 🐳 Docker

### Arrêter les services
```bash
docker-compose down
```

### Arrêter et supprimer les volumes
```bash
docker-compose down -v
```

### Voir les logs
```bash
docker-compose logs -f app      # Logs de l'application
docker-compose logs -f kafka    # Logs Kafka
docker-compose logs -f postgres # Logs PostgreSQL
```

## 🔧 Configuration

### Variables d'environnement

Vous pouvez surcharger la configuration via des variables d'environnement :

```bash
# Base de données
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/renault_garage_db
export SPRING_DATASOURCE_USERNAME=renault_user
export SPRING_DATASOURCE_PASSWORD=renault_password

# Kafka
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092

# JWT
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
```

## 📝 Exemples de Requêtes

### Créer un Garage
```bash
curl -X POST http://localhost:8080/api/garages \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Garage Renault Paris",
    "address": "123 Rue de la République, 75001 Paris",
    "telephone": "+33123456789",
    "email": "paris@renault.com",
    "openingHours": {
      "MONDAY": [
        {"startTime": "08:00", "endTime": "12:00"},
        {"startTime": "14:00", "endTime": "18:00"}
      ]
    }
  }'
```

### Ajouter un Véhicule
```bash
curl -X POST http://localhost:8080/api/vehicles/garage/1 \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Renault",
    "model": "Clio",
    "yearOfManufacture": 2023,
    "fuelType": "ESSENCE",
    "color": "Bleu",
    "mileage": 0
  }'
```

### Rechercher des Garages
```bash
# Par nom
curl "http://localhost:8080/api/garages/search/by-name?name=Paris"

# Par type de carburant
curl "http://localhost:8080/api/garages/search/by-fuel-type?fuelType=ELECTRIQUE"

# Avec capacité disponible
curl "http://localhost:8080/api/garages/available-capacity"
```

## 🔍 Kafka

### Vérifier les événements Kafka

```bash
# Se connecter au container Kafka
docker exec -it renault-kafka bash

# Lister les topics
kafka-topics --list --bootstrap-server localhost:9092

# Consommer les événements
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic vehicle-created-events \
  --from-beginning
```

## ❓ Dépannage

### L'application ne démarre pas
1. Vérifier que PostgreSQL et Kafka sont démarrés :
   ```bash
   docker-compose ps
   ```

2. Vérifier les logs :
   ```bash
   docker-compose logs app
   ```

### Erreur de connexion à la base de données
1. Vérifier que PostgreSQL est accessible :
   ```bash
   docker-compose logs postgres
   ```

2. Tester la connexion :
   ```bash
   docker exec -it renault-postgres psql -U renault_user -d renault_garage_db
   ```

### Erreur Kafka
1. Vérifier que Zookeeper et Kafka sont démarrés
2. Vérifier les logs Kafka :
   ```bash
   docker-compose logs kafka
   ```

## 📞 Support

Pour toute question ou problème, consultez :
- La documentation Swagger : http://localhost:8080/api/swagger-ui.html
- Les logs de l'application
- Le README principal du projet

---

**Version:** 1.0.0  
**Dernière mise à jour:** 2025-12-01

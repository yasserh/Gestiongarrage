# Renault Garage Management Microservice

## 📋 Description

Microservice de gestion des garages, véhicules et accessoires pour le réseau Renault. Ce projet implémente une architecture Clean Architecture avec DDD (Domain-Driven Design), TDD (Test-Driven Development) et les principes SOLID.

## 🚀 Technologies Utilisées

- **Framework:** Spring Boot 3.2.0
- **Langage:** Java 17
- **Base de données:** PostgreSQL 16
- **Messaging:** Apache Kafka
- **Sécurité:** Spring Security + JWT
- **Documentation API:** Swagger/OpenAPI 3
- **Build:** Maven
- **Conteneurisation:** Docker + Docker Compose
- **Tests:** JUnit 5, Mockito, TestContainers
- **Mapping:** MapStruct
- **Utilitaires:** Lombok

## 🏗️ Architecture

### Clean Architecture (4 couches)

```
Presentation → Application → Domain → Infrastructure
```

- **Presentation:** Controllers REST, Exception Handlers
- **Application:** DTOs, Mappers, Validators
- **Domain:** Services métier, Business Rules, Exceptions
- **Infrastructure:** Repositories, Kafka, Database

### Design Patterns Implémentés

1. **Repository Pattern** - Abstraction de la persistance
2. **DTO Pattern** - Transfert de données
3. **Builder Pattern** - Construction d'objets complexes
4. **Strategy Pattern** - Stratégies de recherche
5. **Factory Pattern** - Création d'événements
6. **Observer Pattern** - Kafka pub/sub
7. **Specification Pattern** - Requêtes dynamiques

## 📦 Structure du Projet

```
com.renault.garage
├── config/              # Configurations Spring
├── domain/              # Modèle de domaine, Services, Repositories
├── application/         # DTOs, Mappers, Validators
├── infrastructure/      # Kafka, Security
└── presentation/        # Controllers, Exception Handlers
```

## 🔧 Prérequis

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 16 (ou via Docker)
- Apache Kafka (ou via Docker)

## 🚀 Démarrage Rapide

### Avec Docker Compose (Recommandé)

```bash
# Cloner le projet
git clone <repository-url>
cd Renault

# Démarrer tous les services
docker-compose up -d

# L'application sera accessible sur http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

### En local

```bash
# 1. Démarrer PostgreSQL et Kafka
docker-compose up -d postgres kafka zookeeper

# 2. Compiler le projet
mvn clean install

# 3. Lancer l'application
mvn spring-boot:run
```

## 📚 Documentation API

Une fois l'application démarrée, accédez à la documentation Swagger:

```
http://localhost:8080/api/swagger-ui.html
```

## 🧪 Tests

```bash
# Exécuter tous les tests
mvn test

# Tests avec couverture de code
mvn clean test jacoco:report

# Rapport de couverture disponible dans:
# target/site/jacoco/index.html
```

## 🔒 Sécurité

L'API est sécurisée avec JWT (JSON Web Tokens). Pour accéder aux endpoints protégés:

1. Obtenir un token via `/api/auth/login`
2. Inclure le token dans le header: `Authorization: Bearer <token>`

## 📊 Contraintes Métier

- **Quota véhicules:** Maximum 50 véhicules par garage
- **Horaires d'ouverture:** Validation startTime < endTime
- **Champs obligatoires:**
  - Garage: name, address, telephone, email, openingHours
  - Vehicle: brand, model, yearOfManufacture, fuelType
  - Accessory: name, description, price, type

## 🎯 Fonctionnalités

### Gestion des Garages
- ✅ CRUD complet (Create, Read, Update, Delete)
- ✅ Liste paginée avec tri
- ✅ Recherche par critères

### Gestion des Véhicules
- ✅ CRUD complet
- ✅ Association à un garage
- ✅ Recherche par modèle
- ✅ Publication d'événements Kafka

### Gestion des Accessoires
- ✅ CRUD complet
- ✅ Association à un véhicule
- ✅ Recherche par type

### Kafka Integration
- ✅ Producer: Publication d'événements lors de la création de véhicules
- ✅ Consumer: Consommation et traitement des événements

## 🌐 Endpoints Principaux

### Garages
- `GET /api/garages` - Liste paginée
- `GET /api/garages/{id}` - Détails d'un garage
- `POST /api/garages` - Créer un garage
- `PUT /api/garages/{id}` - Modifier un garage
- `DELETE /api/garages/{id}` - Supprimer un garage
- `GET /api/garages/search` - Recherche avancée

### Véhicules
- `GET /api/garages/{garageId}/vehicles` - Véhicules d'un garage
- `POST /api/garages/{garageId}/vehicles` - Ajouter un véhicule
- `PUT /api/vehicles/{id}` - Modifier un véhicule
- `DELETE /api/vehicles/{id}` - Supprimer un véhicule

### Accessoires
- `GET /api/vehicles/{vehicleId}/accessories` - Accessoires d'un véhicule
- `POST /api/vehicles/{vehicleId}/accessories` - Ajouter un accessoire
- `PUT /api/accessories/{id}` - Modifier un accessoire
- `DELETE /api/accessories/{id}` - Supprimer un accessoire

## 🛠️ Configuration

### Variables d'environnement

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/renault_garage_db
SPRING_DATASOURCE_USERNAME=renault_user
SPRING_DATASOURCE_PASSWORD=renault_password

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092

# JWT
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

## 📈 Monitoring

Actuator endpoints disponibles:
- `/api/actuator/health` - Santé de l'application
- `/api/actuator/info` - Informations sur l'application
- `/api/actuator/metrics` - Métriques

## 🤝 Contribution

Ce projet suit les principes:
- **SOLID** - Principes de conception orientée objet
- **DDD** - Domain-Driven Design
- **TDD** - Test-Driven Development
- **Clean Code** - Code propre et maintenable

## 📝 Licence

© 2024 Renault - Tous droits réservés

## 👥 Auteurs

Renault Team

---

**Version:** 1.0.0  
**Dernière mise à jour:** 2025-12-01

# Guide de Test - Swagger UI

## 🎯 Objectif
Tester tous les endpoints de l'API Renault Garage Management via Swagger UI.

## 📍 Accès Swagger UI
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🧪 Scénarios de Test

### Scénario 1 : Créer un Garage

**Endpoint:** `POST /api/garages`

**Body JSON:**
```json
{
  "name": "Garage Renault Paris Centre",
  "address": "123 Avenue des Champs-Élysées, 75008 Paris",
  "telephone": "+33142563789",
  "email": "paris.centre@renault.com",
  "openingHours": {
    "MONDAY": [
      {
        "startTime": "08:00:00",
        "endTime": "12:00:00"
      },
      {
        "startTime": "14:00:00",
        "endTime": "18:00:00"
      }
    ],
    "TUESDAY": [
      {
        "startTime": "08:00:00",
        "endTime": "12:00:00"
      },
      {
        "startTime": "14:00:00",
        "endTime": "18:00:00"
      }
    ],
    "WEDNESDAY": [
      {
        "startTime": "08:00:00",
        "endTime": "12:00:00"
      },
      {
        "startTime": "14:00:00",
        "endTime": "18:00:00"
      }
    ],
    "THURSDAY": [
      {
        "startTime": "08:00:00",
        "endTime": "12:00:00"
      },
      {
        "startTime": "14:00:00",
        "endTime": "18:00:00"
      }
    ],
    "FRIDAY": [
      {
        "startTime": "08:00:00",
        "endTime": "12:00:00"
      },
      {
        "startTime": "14:00:00",
        "endTime": "18:00:00"
      }
    ]
  }
}
```

**Résultat attendu:** `201 Created` avec l'ID du garage créé

---

### Scénario 2 : Ajouter un Véhicule au Garage

**Endpoint:** `POST /api/vehicles/garage/{garageId}`

Remplacer `{garageId}` par l'ID du garage créé (ex: 1)

**Body JSON:**
```json
{
  "brand": "Renault",
  "model": "Clio V",
  "yearOfManufacture": 2023,
  "fuelType": "ESSENCE",
  "vin": "VF1RJA00H66123456",
  "color": "Bleu Cosmos",
  "mileage": 5000
}
```

**Résultat attendu:** `201 Created` + **Événement Kafka publié** ✅

---

### Scénario 3 : Ajouter un Accessoire au Véhicule

**Endpoint:** `POST /api/accessories/vehicle/{vehicleId}`

**Body JSON:**
```json
{
  "name": "Tapis de sol premium",
  "description": "Tapis de sol en caoutchouc haute qualité, parfaitement adaptés à la Clio V",
  "price": 89.99,
  "type": "INTERIEUR"
}
```

**Résultat attendu:** `201 Created`

---

### Scénario 4 : Lister tous les Garages

**Endpoint:** `GET /api/garages`

**Paramètres:**
- `page`: 0
- `size`: 20
- `sort`: name,asc

**Résultat attendu:** `200 OK` avec liste paginée

---

### Scénario 5 : Rechercher des Garages par Ville

**Endpoint:** `GET /api/garages/search/by-city`

**Paramètres:**
- `city`: Paris

**Résultat attendu:** `200 OK` avec garages contenant "Paris" dans l'adresse

---

### Scénario 6 : Lister les Véhicules d'un Garage

**Endpoint:** `GET /api/vehicles/garage/{garageId}`

**Résultat attendu:** `200 OK` avec liste des véhicules

---

### Scénario 7 : Calculer le Prix Total des Accessoires

**Endpoint:** `GET /api/accessories/vehicle/{vehicleId}/total-price`

**Résultat attendu:** `200 OK` avec le prix total (ex: 89.99)

---

### Scénario 8 : Rechercher des Véhicules Écologiques

**Endpoint:** `GET /api/vehicles/eco-friendly`

**Résultat attendu:** `200 OK` avec véhicules ELECTRIQUE ou HYBRIDE

---

### Scénario 9 : Tester la Contrainte de Quota (50 véhicules max)

**Endpoint:** `POST /api/vehicles/garage/{garageId}` (répéter 51 fois)

**Résultat attendu:** 
- 50 premiers: `201 Created` ✅
- 51ème: `409 Conflict` avec message "quota maximum de 50 véhicules" ✅

---

### Scénario 10 : Rechercher par Type de Carburant

**Endpoint:** `GET /api/garages/search/by-fuel-type`

**Paramètres:**
- `fuelType`: ELECTRIQUE

**Résultat attendu:** `200 OK` avec garages ayant des véhicules électriques

---

## ✅ Points de Vérification

### Fonctionnalités CRUD
- ✅ Créer un garage
- ✅ Lire un garage
- ✅ Modifier un garage
- ✅ Supprimer un garage
- ✅ Créer un véhicule
- ✅ Créer un accessoire

### Contraintes Métier
- ✅ Quota 50 véhicules par garage
- ✅ Validation des horaires d'ouverture
- ✅ Champs obligatoires validés
- ✅ Email unique par garage

### Recherches Avancées
- ✅ Recherche par nom
- ✅ Recherche par ville
- ✅ Recherche par type de carburant
- ✅ Recherche par type d'accessoire
- ✅ Recherche par modèle de véhicule
- ✅ Véhicules écologiques

### Kafka
- ✅ Événement publié lors de la création d'un véhicule
- ✅ Consumer consomme l'événement (vérifier les logs)

---

## 🔍 Vérifier les Événements Kafka

### Dans les logs Docker
```bash
docker-compose logs -f app | grep "Vehicle created event"
```

**Résultat attendu:**
```
INFO  - Publishing vehicle created event for vehicle ID: 1
INFO  - Vehicle created event published successfully
INFO  - Received vehicle created event from partition 0
INFO  - Vehicle details - ID: 1, Brand: Renault, Model: Clio V
```

---

## 📊 Codes de Réponse HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| 200 | OK | GET réussi |
| 201 | Created | POST réussi |
| 204 | No Content | DELETE réussi |
| 400 | Bad Request | Données invalides |
| 404 | Not Found | Ressource inexistante |
| 409 | Conflict | Quota dépassé |
| 500 | Internal Error | Erreur serveur |

---

## 🎯 Checklist Complète

- [ ] Créer un garage
- [ ] Ajouter 3 véhicules différents (ESSENCE, DIESEL, ELECTRIQUE)
- [ ] Ajouter des accessoires à chaque véhicule
- [ ] Tester toutes les recherches
- [ ] Vérifier le quota de 50 véhicules
- [ ] Vérifier les événements Kafka dans les logs
- [ ] Tester la pagination
- [ ] Tester les validations (email invalide, horaires invalides)
- [ ] Calculer le prix total des accessoires
- [ ] Lister les véhicules écologiques

---

**Bon test ! 🚀**

# SIOP Spring Boot Application

Application Spring Boot pour la gestion des rapports SIOP.

## 🚀 Fonctionnalités

### ✅ **Fonctionnalités Principales**

- **Exécution automatique** : Scheduler Spring pour remplacer cron
- **Génération Excel** : Fichiers Excel avec style professionnel
- **Envoi d'emails** : Emails individuels avec pièces jointes
- **API REST** : Endpoints pour la gestion manuelle
- **Monitoring** : Actuator pour la surveillance
- **Sécurité** : Configuration sécurisée des credentials

## 📋 Prérequis

### **Environnement**

- **Java** : 17 ou supérieur
- **Maven** : 3.6 ou supérieur
- **Oracle Database** : Accès à la base de données
- **SMTP Server** : Serveur email configuré

### **Dépendances**

- **Oracle JDBC Driver** : Inclus dans le pom.xml
- **Spring Boot** : 3.2.0
- **Apache POI** : Pour Excel
- **Spring Mail** : Pour l'envoi d'emails

## 🏗️ Architecture

### **Structure du Projet**

```
src/main/java/com/rawbank/siop/
├── SiopApplication.java          # Application principale
├── config/                       # Configurations
│   ├── OracleConfig.java
│   ├── SecurityConfig.java
│   └── SiopProperties.java
├── controller/                   # Contrôleurs REST
│   └── SiopController.java
├── service/                      # Services métier
│   ├── SiopService.java
│   ├── EmailService.java
│   └── ExcelService.java
├── entity/                       # Entités JPA
│   └── SiopOperation.java
├── dto/                          # DTOs
│   ├── SiopReportDto.java
│   └── ManagerReportDto.java
└── scheduler/                    # Scheduler
    └── SiopScheduler.java
```

### **Flux de Données**

1. **Scheduler** → Déclenchement automatique
2. **SiopService** → Exécution requête Oracle
3. **Groupement** → Par gestionnaire (email_gest)
4. **ExcelService** → Génération fichiers Excel
5. **EmailService** → Envoi emails individuels

## ⚙️ Configuration

### **1. Configuration de Base (application.yml)**

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//your-server:1521/your-service
    username: your-username
    password: your-password

  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password

siop:
  email:
    cc:
      - eminence@rawbank.cd
      - supervision@rawbank.cd
  scheduler:
    enabled: true
    cron: "0 */15 * * * *" # Toutes les 15 minutes
```

### **2. Configuration de Production (application-prod.yml)**

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//prod-server:1521/PRODDB
    username: ${ORACLE_USERNAME}
    password: ${ORACLE_PASSWORD}

  mail:
    host: ${SMTP_HOST}
    username: ${SMTP_USERNAME}
    password: ${SMTP_PASSWORD}
```

### **3. Variables d'Environnement**

```bash
export ORACLE_USERNAME=siop_user
export ORACLE_PASSWORD=siop_password
export SMTP_HOST=smtp.rawbank.cd
export SMTP_USERNAME=siop@rawbank.cd
export SMTP_PASSWORD=siop_password
```

## 🚀 Déploiement

### **1. Compilation**

```bash
mvn clean compile
```

### **2. Tests**

```bash
mvn test
```

### **3. Package**

```bash
# Générer le fichier WAR
mvn clean package

# Le fichier WAR sera généré dans : target/siop-spring-boot.war
```

### **4. Exécution**

#### **Option A : Exécution standalone (JAR)**

```bash
# Développement
java -jar target/siop-spring-boot-1.0.0.jar

# Production
java -jar target/siop-spring-boot-1.0.0.jar --spring.profiles.active=prod
```

#### **Option B : Déploiement WAR sur serveur d'application**

**Tomcat :**

```bash
# Copier le fichier WAR dans le dossier webapps de Tomcat
cp target/siop-spring-boot.war $TOMCAT_HOME/webapps/

# Redémarrer Tomcat
$TOMCAT_HOME/bin/shutdown.sh
$TOMCAT_HOME/bin/startup.sh
```

**WebLogic/WebSphere :**

- Déployer via la console d'administration du serveur
- Ou utiliser les outils de déploiement du serveur

### **5. Docker (Optionnel)**

#### **Option A : Docker avec JAR**

```dockerfile
FROM openjdk:17-jre-slim
COPY target/siop-spring-boot-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### **Option B : Docker avec WAR (Tomcat)**

```dockerfile
FROM tomcat:9-jre17
COPY target/siop-spring-boot.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

#### **Option C : Docker avec WAR (Jetty)**

```dockerfile
FROM jetty:9-jre17
COPY target/siop-spring-boot.war /var/lib/jetty/webapps/
EXPOSE 8080
CMD ["java", "-jar", "/usr/local/jetty/start.jar"]
```

## 📡 API Endpoints

### **Endpoints Disponibles**

#### **1. Vérification de l'état**

```http
GET /api/siop/health
```

**Réponse :**

```json
{
  "status": "UP",
  "timestamp": 1698000000000,
  "service": "SIOP Spring Boot Application",
  "version": "1.0.0"
}
```

#### **2. Génération manuelle**

```http
POST /api/siop/generate?date=20/10/2025
```

**Réponse :**

```json
{
  "success": true,
  "message": "Rapport SIOP généré avec succès",
  "date": "20/10/2025",
  "timestamp": 1698000000000
}
```

#### **3. Exécution pour une date**

```http
POST /api/siop/execute/20/10/2025
```

#### **4. Test de connexion**

```http
GET /api/siop/test-connection
```

#### **5. Statut de l'application**

```http
GET /api/siop/status
```

### **Monitoring**

```http
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

## 🔧 Configuration du Scheduler

### **Expressions Cron**

```yaml
siop:
  scheduler:
    cron: "0 */15 * * * *"    # Toutes les 15 minutes
    cron: "0 0 8 * * *"       # Tous les jours à 8h
    cron: "0 0 8 * * 1-5"     # Lundi-Vendredi à 8h
    cron: "0 0 8,18 * * 1-5"  # 8h et 18h, Lundi-Vendredi
```

### **Désactivation**

```yaml
siop:
  scheduler:
    enabled: false
```

## 📊 Monitoring et Logs

### **Logs**

- **Fichier** : `logs/siop-application.log`
- **Rotation** : 10MB, 30 jours
- **Niveaux** : Configurables par package

### **Métriques**

- **Health Check** : `/actuator/health`
- **Métriques** : `/actuator/metrics`
- **Info** : `/actuator/info`

### **Surveillance**

```bash
# Logs en temps réel
tail -f logs/siop-application.log

# Vérification de l'état
curl http://localhost:8080/actuator/health

# Métriques
curl http://localhost:8080/actuator/metrics
```

## 🔒 Sécurité

### **Configuration Sécurisée**

- **Credentials** : Variables d'environnement
- **HTTPS** : Configuration SSL/TLS
- **Authentification** : Spring Security
- **Audit** : Logs de sécurité

### **Bonnes Pratiques**

1. **Ne jamais commiter** les mots de passe
2. **Utiliser des variables d'environnement**
3. **Limiter l'accès** aux endpoints
4. **Surveiller les logs** de sécurité

## 🧪 Tests

### **Tests Unitaires**

```bash
mvn test
```

### **Tests d'Intégration**

```bash
mvn verify
```

### **Tests de Performance**

```bash
# Test de charge avec JMeter
jmeter -n -t siop-load-test.jmx
```

## 🆘 Dépannage

### **Problèmes Courants**

#### **1. Erreur de connexion Oracle**

```
Caused by: java.sql.SQLException: ORA-12541: TNS:no listener
```

**Solution :**

- Vérifier l'URL de connexion
- Vérifier l'accès réseau
- Vérifier les credentials

#### **2. Erreur SMTP**

```
Caused by: javax.mail.AuthenticationFailedException
```

**Solution :**

- Vérifier les paramètres SMTP
- Utiliser un mot de passe d'application
- Vérifier le port et SSL

#### **3. Erreur de scheduler**

```
@Scheduled method not found
```

**Solution :**

- Vérifier `@EnableScheduling`
- Vérifier la configuration cron
- Vérifier les logs

### **Logs de Dépannage**

```bash
# Logs détaillés
tail -f logs/siop-application.log | grep ERROR

# Vérification de l'état
curl http://localhost:8080/actuator/health

# Test de connexion
curl http://localhost:8080/api/siop/test-connection
```

## 🎯 Avantages de la Version Spring Boot

### **✅ Performance**

- Pool de connexions optimisé
- Cache intelligent
- Exécution asynchrone

### **✅ Monitoring**

- Métriques en temps réel
- Health checks automatiques
- Logs structurés

### **✅ Sécurité**

- Gestion sécurisée des credentials
- Authentification et autorisation
- Audit des actions

### **✅ Maintenance**

- Code structuré et testable
- Documentation complète
- Tests automatisés

### **✅ Évolutivité**

- Architecture modulaire
- API REST extensible
- Déploiement flexible (JAR/WAR)
- Support serveurs d'application

### **✅ Déploiement WAR**

- **Tomcat** : Déploiement direct
- **WebLogic/WebSphere** : Support enterprise
- **Jetty** : Alternative légère
- **Docker** : Images optimisées

---

**Version** : 1.0.0  
**Dernière mise à jour** : Octobre 2025  
**Auteur** : Micro-Apps Integration Team

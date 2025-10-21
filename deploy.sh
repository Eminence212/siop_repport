#!/bin/bash
# Script de déploiement pour SIOP Spring Boot Application

echo "=== Déploiement SIOP Spring Boot Application ==="
echo ""

# Configuration
APP_NAME="siop-spring-boot"
JAR_FILE="target/siop-spring-boot-1.0.0.jar"
PROFILE="prod"
PORT="8080"

# Vérification des prérequis
echo "1. Vérification des prérequis..."

# Vérification de Java
if ! command -v java &> /dev/null; then
    echo "❌ Java non installé"
    exit 1
fi

# Vérification de Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven non installé"
    exit 1
fi

echo "✅ Prérequis satisfaits"
echo ""

# Compilation
echo "2. Compilation de l'application..."
if mvn clean compile; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur lors de la compilation"
    exit 1
fi
echo ""

# Tests
echo "3. Exécution des tests..."
if mvn test; then
    echo "✅ Tests réussis"
else
    echo "⚠️  Tests échoués, continuation du déploiement"
fi
echo ""

# Package
echo "4. Création du package..."
if mvn clean package -DskipTests; then
    echo "✅ Package créé avec succès"
else
    echo "❌ Erreur lors de la création du package"
    exit 1
fi
echo ""

# Vérification du JAR
if [[ ! -f "$JAR_FILE" ]]; then
    echo "❌ Fichier JAR non trouvé: $JAR_FILE"
    exit 1
fi

echo "✅ Fichier JAR trouvé: $JAR_FILE"
echo ""

# Configuration des variables d'environnement
echo "5. Configuration des variables d'environnement..."

# Vérification des variables requises
REQUIRED_VARS=("ORACLE_USERNAME" "ORACLE_PASSWORD" "SMTP_HOST" "SMTP_USERNAME" "SMTP_PASSWORD")
MISSING_VARS=()

for var in "${REQUIRED_VARS[@]}"; do
    if [[ -z "${!var}" ]]; then
        MISSING_VARS+=("$var")
    fi
done

if [[ ${#MISSING_VARS[@]} -gt 0 ]]; then
    echo "⚠️  Variables d'environnement manquantes:"
    for var in "${MISSING_VARS[@]}"; do
        echo "   - $var"
    done
    echo ""
    echo "Configuration par défaut utilisée (à modifier dans application.yml)"
fi

echo "✅ Configuration terminée"
echo ""

# Arrêt de l'ancienne version
echo "6. Arrêt de l'ancienne version..."
if pgrep -f "$APP_NAME" > /dev/null; then
    echo "   Arrêt de l'ancienne version..."
    pkill -f "$APP_NAME"
    sleep 5
    echo "✅ Ancienne version arrêtée"
else
    echo "   Aucune ancienne version trouvée"
fi
echo ""

# Démarrage de la nouvelle version
echo "7. Démarrage de la nouvelle version..."

# Création du répertoire de logs
mkdir -p logs

# Démarrage en arrière-plan
nohup java -jar "$JAR_FILE" --spring.profiles.active="$PROFILE" > logs/siop-application.log 2>&1 &
APP_PID=$!

echo "   Application démarrée avec PID: $APP_PID"
echo "   Port: $PORT"
echo "   Profil: $PROFILE"
echo "   Logs: logs/siop-application.log"
echo ""

# Attente du démarrage
echo "8. Attente du démarrage..."
sleep 10

# Vérification du statut
echo "9. Vérification du statut..."

# Test de l'endpoint de santé
for i in {1..30}; do
    if curl -s http://localhost:$PORT/actuator/health > /dev/null 2>&1; then
        echo "✅ Application démarrée avec succès"
        break
    else
        echo "   Tentative $i/30..."
        sleep 2
    fi
    
    if [[ $i -eq 30 ]]; then
        echo "❌ Application non accessible après 60 secondes"
        echo "   Vérifiez les logs: tail -f logs/siop-application.log"
        exit 1
    fi
done

echo ""

# Affichage des informations
echo "=== DÉPLOIEMENT TERMINÉ ==="
echo ""
echo "📋 INFORMATIONS:"
echo "   • Application: $APP_NAME"
echo "   • PID: $APP_PID"
echo "   • Port: $PORT"
echo "   • Profil: $PROFILE"
echo "   • Logs: logs/siop-application.log"
echo ""
echo "🔧 COMMANDES UTILES:"
echo "   • Vérifier l'état: curl http://localhost:$PORT/actuator/health"
echo "   • Voir les logs: tail -f logs/siop-application.log"
echo "   • Arrêter: kill $APP_PID"
echo "   • Redémarrer: ./deploy.sh"
echo ""
echo "📡 ENDPOINTS API:"
echo "   • Santé: http://localhost:$PORT/actuator/health"
echo "   • Génération: POST http://localhost:$PORT/api/siop/generate"
echo "   • Test: GET http://localhost:$PORT/api/siop/test-connection"
echo ""
echo "✅ Déploiement terminé avec succès!"

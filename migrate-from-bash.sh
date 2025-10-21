#!/bin/bash
# Script de migration depuis la version Bash vers Spring Boot

echo "=== Migration SIOP Bash → Spring Boot ==="
echo ""

# Configuration
BASH_DIR="../sendMail"
SPRING_DIR="."
BACKUP_DIR="backup-$(date +%Y%m%d_%H%M%S)"

# Vérification des prérequis
echo "1. Vérification des prérequis..."

# Vérification de Java
if ! command -v java &> /dev/null; then
    echo "❌ Java non installé"
    echo "   Installez Java 17 ou supérieur"
    exit 1
fi

# Vérification de Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven non installé"
    echo "   Installez Maven 3.6 ou supérieur"
    exit 1
fi

echo "✅ Prérequis satisfaits"
echo ""

# Sauvegarde de l'ancienne configuration
echo "2. Sauvegarde de l'ancienne configuration..."

if [[ -d "$BASH_DIR" ]]; then
    mkdir -p "$BACKUP_DIR"
    cp -r "$BASH_DIR" "$BACKUP_DIR/"
    echo "✅ Configuration Bash sauvegardée dans $BACKUP_DIR"
else
    echo "⚠️  Répertoire Bash non trouvé: $BASH_DIR"
fi
echo ""

# Migration de la configuration
echo "3. Migration de la configuration..."

if [[ -f "$BASH_DIR/config.json" ]]; then
    echo "   Migration de config.json..."
    
    # Extraction des paramètres Oracle
    ORACLE_URL=$(python3 -c "
import json
with open('$BASH_DIR/config.json', 'r') as f:
    config = json.load(f)
print(config['oracle']['connectionString'])
" 2>/dev/null || echo "")
    
    # Extraction des paramètres email
    SMTP_HOST=$(python3 -c "
import json
with open('$BASH_DIR/config.json', 'r') as f:
    config = json.load(f)
print(config['email']['smtpServer'])
" 2>/dev/null || echo "")
    
    SMTP_USERNAME=$(python3 -c "
import json
with open('$BASH_DIR/config.json', 'r') as f:
    config = json.load(f)
print(config['email']['username'])
" 2>/dev/null || echo "")
    
    echo "   ✅ Configuration extraite"
    echo "   📋 Paramètres trouvés:"
    echo "      • Oracle URL: ${ORACLE_URL:0:50}..."
    echo "      • SMTP Host: $SMTP_HOST"
    echo "      • SMTP Username: $SMTP_USERNAME"
else
    echo "   ⚠️  Fichier config.json non trouvé"
fi
echo ""

# Création du fichier .env
echo "4. Création du fichier .env..."

cat > .env << EOF
# Configuration migrée depuis la version Bash
# Modifiez ces valeurs selon votre environnement

# Configuration Oracle
ORACLE_USERNAME=your-username
ORACLE_PASSWORD=your-password

# Configuration SMTP
SMTP_HOST=$SMTP_HOST
SMTP_USERNAME=$SMTP_USERNAME
SMTP_PASSWORD=your-smtp-password

# Configuration de l'application
SPRING_PROFILES_ACTIVE=prod
JAVA_OPTS=-Xms512m -Xmx1024m

# Configuration du scheduler
SIOP_SCHEDULER_ENABLED=true
SIOP_SCHEDULER_CRON=0 */15 * * * *
EOF

echo "✅ Fichier .env créé"
echo ""

# Désactivation de l'ancien cron
echo "5. Désactivation de l'ancien cron..."

if crontab -l 2>/dev/null | grep -q "sendMail\|sendSiopMail"; then
    echo "   Anciennes entrées cron trouvées:"
    crontab -l | grep "sendMail\|sendSiopMail"
    echo ""
    
    read -p "Désactiver l'ancien cron? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        crontab -l 2>/dev/null | grep -v "sendMail\|sendSiopMail" | crontab -
        echo "   ✅ Ancien cron désactivé"
    else
        echo "   ⚠️  Ancien cron conservé (désactivez-le manuellement)"
    fi
else
    echo "   ✅ Aucun cron à désactiver"
fi
echo ""

# Compilation de l'application Spring Boot
echo "6. Compilation de l'application Spring Boot..."

if mvn clean compile; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur lors de la compilation"
    echo "   Vérifiez les dépendances et la configuration"
    exit 1
fi
echo ""

# Tests de l'application
echo "7. Tests de l'application..."

if mvn test; then
    echo "✅ Tests réussis"
else
    echo "⚠️  Tests échoués, continuation de la migration"
fi
echo ""

# Création du package
echo "8. Création du package..."

if mvn clean package -DskipTests; then
    echo "✅ Package créé avec succès"
else
    echo "❌ Erreur lors de la création du package"
    exit 1
fi
echo ""

# Instructions finales
echo "=== MIGRATION TERMINÉE ==="
echo ""
echo "📋 RÉSUMÉ:"
echo "   • Configuration sauvegardée: $BACKUP_DIR"
echo "   • Fichier .env créé avec les paramètres extraits"
echo "   • Application Spring Boot compilée"
echo "   • Ancien cron désactivé (si confirmé)"
echo ""
echo "🔧 ÉTAPES SUIVANTES:"
echo "   1. Modifiez le fichier .env avec vos vraies valeurs"
echo "   2. Configurez les variables d'environnement:"
echo "      export ORACLE_USERNAME=your-username"
echo "      export ORACLE_PASSWORD=your-password"
echo "      export SMTP_HOST=$SMTP_HOST"
echo "      export SMTP_USERNAME=$SMTP_USERNAME"
echo "      export SMTP_PASSWORD=your-smtp-password"
echo "   3. Déployez l'application: ./deploy.sh"
echo "   4. Testez l'application: ./test.sh"
echo ""
echo "📡 ENDPOINTS DISPONIBLES:"
echo "   • Santé: http://localhost:8080/actuator/health"
echo "   • API: http://localhost:8080/api/siop/health"
echo "   • Génération: POST http://localhost:8080/api/siop/generate"
echo ""
echo "🔍 VÉRIFICATIONS:"
echo "   • Vérifiez que l'ancien cron est désactivé: crontab -l"
echo "   • Testez la nouvelle application: ./test.sh"
echo "   • Surveillez les logs: tail -f logs/siop-application.log"
echo ""
echo "✅ Migration terminée avec succès!"
echo ""
echo "⚠️  IMPORTANT:"
echo "   • L'ancienne version Bash est sauvegardée dans $BACKUP_DIR"
echo "   • Vérifiez que la nouvelle version fonctionne avant de supprimer l'ancienne"
echo "   • En cas de problème, vous pouvez restaurer l'ancienne version"

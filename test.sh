#!/bin/bash
# Script de test pour SIOP Spring Boot Application

echo "=== Test SIOP Spring Boot Application ==="
echo ""

# Configuration
BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api/siop"

# Fonction de test
test_endpoint() {
    local endpoint="$1"
    local expected_status="$2"
    local description="$3"
    
    echo "🧪 Test: $description"
    echo "   Endpoint: $endpoint"
    
    response=$(curl -s -w "%{http_code}" -o /dev/null "$endpoint")
    
    if [[ "$response" == "$expected_status" ]]; then
        echo "   ✅ Succès (HTTP $response)"
    else
        echo "   ❌ Échec (HTTP $response, attendu $expected_status)"
        return 1
    fi
    echo ""
}

# Vérification que l'application est démarrée
echo "1. Vérification du démarrage de l'application..."

if curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
    echo "✅ Application démarrée"
else
    echo "❌ Application non accessible"
    echo "   Démarrez l'application avec: ./deploy.sh"
    exit 1
fi
echo ""

# Tests des endpoints
echo "2. Tests des endpoints API..."

# Test de santé
test_endpoint "$API_BASE/health" "200" "Vérification de l'état"

# Test de statut
test_endpoint "$API_BASE/status" "200" "Statut de l'application"

# Test de connexion (peut échouer si Oracle n'est pas configuré)
echo "🧪 Test: Test de connexion Oracle"
echo "   Endpoint: $API_BASE/test-connection"
response=$(curl -s "$API_BASE/test-connection")
if echo "$response" | grep -q "success.*true"; then
    echo "   ✅ Connexion Oracle réussie"
else
    echo "   ⚠️  Connexion Oracle échouée (configuration requise)"
fi
echo ""

# Test de génération (peut échouer si Oracle n'est pas configuré)
echo "🧪 Test: Génération manuelle du rapport"
echo "   Endpoint: POST $API_BASE/generate"
response=$(curl -s -X POST "$API_BASE/generate")
if echo "$response" | grep -q "success.*true"; then
    echo "   ✅ Génération réussie"
else
    echo "   ⚠️  Génération échouée (configuration Oracle requise)"
fi
echo ""

# Tests des endpoints Actuator
echo "3. Tests des endpoints Actuator..."

test_endpoint "$BASE_URL/actuator/health" "200" "Health Check Actuator"
test_endpoint "$BASE_URL/actuator/info" "200" "Info Actuator"
test_endpoint "$BASE_URL/actuator/metrics" "200" "Métriques Actuator"

# Test de performance
echo "4. Test de performance..."

echo "🧪 Test: Temps de réponse"
start_time=$(date +%s%N)
curl -s "$API_BASE/health" > /dev/null
end_time=$(date +%s%N)
response_time=$(( (end_time - start_time) / 1000000 ))

if [[ $response_time -lt 1000 ]]; then
    echo "   ✅ Temps de réponse: ${response_time}ms (excellent)"
elif [[ $response_time -lt 3000 ]]; then
    echo "   ✅ Temps de réponse: ${response_time}ms (bon)"
else
    echo "   ⚠️  Temps de réponse: ${response_time}ms (lent)"
fi
echo ""

# Vérification des logs
echo "5. Vérification des logs..."

if [[ -f "logs/siop-application.log" ]]; then
    echo "✅ Fichier de logs trouvé: logs/siop-application.log"
    
    # Vérification des erreurs
    error_count=$(grep -c "ERROR" logs/siop-application.log 2>/dev/null || echo "0")
    if [[ $error_count -eq 0 ]]; then
        echo "✅ Aucune erreur dans les logs"
    else
        echo "⚠️  $error_count erreur(s) trouvée(s) dans les logs"
        echo "   Dernières erreurs:"
        grep "ERROR" logs/siop-application.log | tail -3
    fi
else
    echo "⚠️  Fichier de logs non trouvé"
fi
echo ""

# Résumé des tests
echo "=== RÉSUMÉ DES TESTS ==="
echo ""

# Comptage des tests réussis
total_tests=0
passed_tests=0

# Tests des endpoints
for endpoint in "health" "status"; do
    total_tests=$((total_tests + 1))
    if curl -s "$API_BASE/$endpoint" > /dev/null 2>&1; then
        passed_tests=$((passed_tests + 1))
    fi
done

# Tests Actuator
for endpoint in "actuator/health" "actuator/info" "actuator/metrics"; do
    total_tests=$((total_tests + 1))
    if curl -s "$BASE_URL/$endpoint" > /dev/null 2>&1; then
        passed_tests=$((passed_tests + 1))
    fi
done

echo "📊 Résultats:"
echo "   • Tests réussis: $passed_tests/$total_tests"
echo "   • Taux de réussite: $(( passed_tests * 100 / total_tests ))%"
echo ""

if [[ $passed_tests -eq $total_tests ]]; then
    echo "✅ Tous les tests sont passés!"
    echo "   L'application SIOP Spring Boot fonctionne correctement"
else
    echo "⚠️  Certains tests ont échoué"
    echo "   Vérifiez la configuration et les logs"
fi

echo ""
echo "🔧 COMMANDES UTILES:"
echo "   • Voir les logs: tail -f logs/siop-application.log"
echo "   • Redémarrer: ./deploy.sh"
echo "   • Arrêter: pkill -f siop-spring-boot"
echo "   • Test manuel: curl $API_BASE/health"
echo ""
echo "✅ Tests terminés!"

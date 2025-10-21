package com.rawbank.siop.controller;

import com.rawbank.siop.scheduler.SiopScheduler;
import com.rawbank.siop.service.SiopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'API SIOP
 * 
 * Endpoints disponibles :
 * - GET /api/siop/health : Vérification de l'état
 * - POST /api/siop/generate : Génération manuelle du rapport
 * - GET /api/siop/status : Statut de l'application
 */
@RestController
@RequestMapping("/api/siop")
public class SiopController {
    
    private static final Logger logger = LoggerFactory.getLogger(SiopController.class);
    
    @Autowired
    private SiopService siopService;
    
    @Autowired
    private SiopScheduler siopScheduler;
    
    /**
     * Vérification de l'état de l'application
     * 
     * @return Statut de l'application
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service", "SIOP Spring Boot Application");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Génération manuelle du rapport SIOP
     * 
     * @param date Date optionnelle (format DD/MM/YYYY)
     * @return Résultat de la génération
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestParam(required = false) String date) {
        
        try {
            String queryDate = date != null ? date : 
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            logger.info("Génération manuelle du rapport SIOP pour la date : {}", queryDate);
            
            siopService.generateAndSendReport(queryDate);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rapport SIOP généré avec succès");
            response.put("date", queryDate);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la génération manuelle du rapport", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur lors de la génération du rapport");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Exécution du rapport pour une date spécifique
     * 
     * @param date Date au format DD/MM/YYYY
     * @return Résultat de l'exécution
     */
    @PostMapping("/execute/{date}")
    public ResponseEntity<Map<String, Object>> executeForDate(
            @PathVariable String date) {
        
        try {
            logger.info("Exécution du rapport SIOP pour la date : {}", date);
            
            siopScheduler.executeSiopReportForDate(date);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rapport SIOP exécuté avec succès");
            response.put("date", date);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution du rapport pour la date : {}", date, e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur lors de l'exécution du rapport");
            response.put("error", e.getMessage());
            response.put("date", date);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Statut de l'application
     * 
     * @return Informations sur l'état de l'application
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "SIOP Spring Boot");
        response.put("version", "1.0.0");
        response.put("status", "RUNNING");
        response.put("timestamp", System.currentTimeMillis());
        response.put("uptime", System.currentTimeMillis() - getStartTime());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Test de connexion Oracle
     * 
     * @return Résultat du test de connexion
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        try {
            // Test de connexion via le service
            siopService.generateAndSendReport("01/01/2025"); // Date de test
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Connexion Oracle réussie");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors du test de connexion Oracle", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur de connexion Oracle");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Obtient le temps de démarrage de l'application
     * 
     * @return Timestamp de démarrage
     */
    private long getStartTime() {
        // Utilisation du temps système comme approximation
        return System.currentTimeMillis() - (Runtime.getRuntime().totalMemory() / 1024 / 1024);
    }
}

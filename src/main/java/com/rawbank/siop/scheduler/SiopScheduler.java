package com.rawbank.siop.scheduler;

import com.rawbank.siop.config.SiopProperties;
import com.rawbank.siop.service.SiopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Scheduler pour l'exécution automatique des rapports SIOP
 * 
 * Exécute le rapport SIOP selon la configuration cron
 * Remplace la fonctionnalité cron du système
 */
@Component
@ConditionalOnProperty(name = "siop.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class SiopScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(SiopScheduler.class);
    
    @Autowired
    private SiopService siopService;
    
    @Autowired
    private SiopProperties siopProperties;
    
    /**
     * Exécution automatique du rapport SIOP
     * 
     * Exécuté selon la configuration cron définie dans application.yml
     * Par défaut : toutes les 15 minutes
     */
    @Scheduled(cron = "${siop.scheduler.cron:0 */15 * * * *}")
    public void executeSiopReport() {
        if (!siopProperties.getScheduler().isEnabled()) {
            logger.debug("Scheduler SIOP désactivé");
            return;
        }
        
        try {
            logger.info("Début de l'exécution automatique du rapport SIOP");
            
            // Utilisation de la date du jour
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            // Exécution du rapport
            siopService.generateAndSendReport(today);
            
            logger.info("Exécution automatique du rapport SIOP terminée avec succès");
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution automatique du rapport SIOP", e);
        }
    }
    
    /**
     * Exécution du rapport pour une date spécifique
     * 
     * @param date Date au format DD/MM/YYYY
     */
    public void executeSiopReportForDate(String date) {
        try {
            logger.info("Exécution du rapport SIOP pour la date : {}", date);
            
            siopService.generateAndSendReport(date);
            
            logger.info("Rapport SIOP exécuté avec succès pour la date : {}", date);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution du rapport SIOP pour la date : {}", date, e);
            throw e;
        }
    }
}

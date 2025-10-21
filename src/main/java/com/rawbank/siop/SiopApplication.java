package com.rawbank.siop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application principale pour la gestion des rapports SIOP
 * 
 * Fonctionnalités :
 * - Exécution automatique de requêtes Oracle
 * - Génération de rapports Excel
 * - Envoi d'emails individuels aux gestionnaires
 * - Scheduler pour l'exécution périodique
 * 
 * @author ScolarisPlus
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class SiopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiopApplication.class, args);
    }
}

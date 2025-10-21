package com.rawbank.siop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration des propriétés SIOP
 * 
 * Permet de configurer les paramètres de l'application
 * via le fichier application.yml
 */
@Component
@ConfigurationProperties(prefix = "siop")
public class SiopProperties {
    
    private Email email = new Email();
    private Query query = new Query();
    private Scheduler scheduler = new Scheduler();
    
    // Getters et Setters
    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }
    
    public Query getQuery() { return query; }
    public void setQuery(Query query) { this.query = query; }
    
    public Scheduler getScheduler() { return scheduler; }
    public void setScheduler(Scheduler scheduler) { this.scheduler = scheduler; }
    
    // Classes internes pour la configuration
    public static class Email {
        private List<String> cc;
        private String subject = "Virement SIOP nécessitant votre attention";
        
        public List<String> getCc() { return cc; }
        public void setCc(List<String> cc) { this.cc = cc; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
    }
    
    public static class Query {
        private String defaultDate = "14/10/2025";
        private String sqlFile = "siop_send_mail.sql";
        
        public String getDefaultDate() { return defaultDate; }
        public void setDefaultDate(String defaultDate) { this.defaultDate = defaultDate; }
        
        public String getSqlFile() { return sqlFile; }
        public void setSqlFile(String sqlFile) { this.sqlFile = sqlFile; }
    }
    
    public static class Scheduler {
        private boolean enabled = true;
        private String cron = "0 */15 * * * *"; // Toutes les 15 minutes
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public String getCron() { return cron; }
        public void setCron(String cron) { this.cron = cron; }
    }
}

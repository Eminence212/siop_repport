package com.rawbank.siop.service;

import com.rawbank.siop.config.SiopProperties;
import com.rawbank.siop.dto.ManagerReportDto;
import com.rawbank.siop.dto.SiopReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service principal pour la gestion des rapports SIOP
 * 
 * Responsable de :
 * - Exécution des requêtes Oracle
 * - Groupement des données par gestionnaire
 * - Orchestration des services
 */
@Service
public class SiopService {
    
    private static final Logger logger = LoggerFactory.getLogger(SiopService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ExcelService excelService;
    
    @Autowired
    private SiopProperties siopProperties;
    
    /**
     * Génère et envoie le rapport SIOP
     * 
     * @param queryDate Date de la requête (format DD/MM/YYYY)
     */
    public void generateAndSendReport(String queryDate) {
        logger.info("Début de génération du rapport SIOP pour la date : {}", queryDate);
        
        try {
            // 1. Exécution de la requête SQL
            List<SiopReportDto> operations = executeSiopQuery(queryDate);
            
            if (operations.isEmpty()) {
                logger.info("Aucune opération trouvée pour la date : {}", queryDate);
                return;
            }
            
            logger.info("{} opération(s) trouvée(s)", operations.size());
            
            // 2. Groupement par gestionnaire
            Map<String, List<SiopReportDto>> groupedByManager = 
                operations.stream()
                    .filter(op -> op.getEmailGest() != null && !op.getEmailGest().trim().isEmpty())
                    .collect(Collectors.groupingBy(SiopReportDto::getEmailGest));
            
            logger.info("{} gestionnaire(s) trouvé(s)", groupedByManager.size());
            
            // 3. Envoi d'emails individuels
            for (Map.Entry<String, List<SiopReportDto>> entry : groupedByManager.entrySet()) {
                String emailGest = entry.getKey();
                List<SiopReportDto> managerOperations = entry.getValue();
                
                ManagerReportDto managerReport = new ManagerReportDto(
                    emailGest,
                    managerOperations.get(0).getNomGest(),
                    managerOperations.get(0).getPrenomGest(),
                    managerOperations.get(0).getCanal(),
                    managerOperations.size()
                );
                managerReport.setOperations(managerOperations);
                
                sendEmailToManager(managerReport, queryDate);
            }
            
            logger.info("Rapport SIOP généré et envoyé avec succès");
            
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du rapport SIOP", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }
    
    /**
     * Exécute la requête SQL SIOP
     * 
     * @param queryDate Date de la requête
     * @return Liste des opérations SIOP
     */
    private List<SiopReportDto> executeSiopQuery(String queryDate) {
        try {
            // Chargement du script SQL
            String sqlQuery = loadSqlQuery();
            
            // Remplacement de la date dans la requête
            sqlQuery = sqlQuery.replace("TO_DATE('14/10/2025', 'DD/MM/YYYY')", 
                "TO_DATE('" + queryDate + "', 'DD/MM/YYYY')");
            
            logger.debug("Exécution de la requête SQL pour la date : {}", queryDate);
            
            // Exécution de la requête
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
                SiopReportDto dto = new SiopReportDto();
                
                dto.setDateCreation(rs.getTimestamp("dcre") != null ? 
                    rs.getTimestamp("dcre").toLocalDateTime() : null);
                dto.setFilename(rs.getString("filename"));
                dto.setCanal(rs.getString("canal"));
                dto.setService(rs.getString("service"));
                dto.setTypeMsg(rs.getString("typemsg"));
                dto.setBeneficiaire(rs.getString("benef"));
                dto.setMontantTx(rs.getBigDecimal("montant_tx"));
                dto.setMotif(rs.getString("motif"));
                dto.setFrais(rs.getString("frais"));
                dto.setMsgStatus(rs.getString("msgstatus"));
                dto.setLotStatus(rs.getString("lotstatus"));
                dto.setTxtStatus(rs.getString("txtstatus"));
                dto.setErrorMsg(rs.getString("errormsg"));
                
                // Informations du gestionnaire
                dto.setNomGest(rs.getString("nom_gest"));
                dto.setPrenomGest(rs.getString("prenom_gest"));
                dto.setEmailGest(rs.getString("email_gest"));
                dto.setPhoneGest(rs.getString("phone_gest"));
                
                return dto;
            });
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution de la requête SQL", e);
            throw new RuntimeException("Erreur lors de l'exécution de la requête", e);
        }
    }
    
    /**
     * Charge le script SQL depuis les ressources
     * 
     * @return Contenu du script SQL
     */
    private String loadSqlQuery() {
        try {
            ClassPathResource resource = new ClassPathResource("sql/siop_send_mail.sql");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Erreur lors du chargement du script SQL", e);
            throw new RuntimeException("Impossible de charger le script SQL", e);
        }
    }
    
    /**
     * Envoie un email à un gestionnaire
     * 
     * @param managerReport Rapport du gestionnaire
     * @param queryDate Date de la requête
     */
    private void sendEmailToManager(ManagerReportDto managerReport, String queryDate) {
        try {
            // Génération du fichier Excel
            byte[] excelFile = excelService.generateExcel(managerReport.getOperations());
            
            // Envoi de l'email
            emailService.sendEmailToManager(managerReport, queryDate, excelFile);
            
            logger.info("Email envoyé à {} ({} opérations)", 
                managerReport.getEmailGest(), managerReport.getCount());
                
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}", 
                managerReport.getEmailGest(), e);
        }
    }
    
    /**
     * Génère le rapport pour la date du jour
     */
    public void generateAndSendReport() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        generateAndSendReport(today);
    }
}

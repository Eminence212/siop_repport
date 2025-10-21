package com.rawbank.siop.service;

import com.rawbank.siop.config.SiopProperties;
import com.rawbank.siop.dto.ManagerReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

/**
 * Service pour l'envoi d'emails
 * 
 * Responsable de :
 * - Envoi d'emails individuels aux gestionnaires
 * - Gestion des pièces jointes Excel
 * - Configuration des CC
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SiopProperties siopProperties;
    
    /**
     * Envoie un email à un gestionnaire
     * 
     * @param managerReport Rapport du gestionnaire
     * @param queryDate Date de la requête
     * @param excelFile Fichier Excel en pièce jointe
     */
    public void sendEmailToManager(ManagerReportDto managerReport, String queryDate, byte[] excelFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Configuration de l'email
            helper.setTo(managerReport.getEmailGest());
            helper.setSubject(buildSubject(managerReport));
            helper.setText(buildEmailBody(managerReport, queryDate), false);
            
            // Ajout des CC
            List<String> ccList = siopProperties.getEmail().getCc();
            if (ccList != null && !ccList.isEmpty()) {
                helper.setCc(ccList.toArray(new String[0]));
            }
            
            // Pièce jointe Excel
            if (excelFile != null && excelFile.length > 0) {
                String filename = buildExcelFilename(managerReport.getEmailGest(), queryDate);
                helper.addAttachment(filename, new ByteArrayResource(excelFile));
            }
            
            // Envoi de l'email
            mailSender.send(message);
            
            logger.info("Email envoyé avec succès à {} (CC: {})", 
                managerReport.getEmailGest(), ccList);
                
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email à {}", 
                managerReport.getEmailGest(), e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
    
    /**
     * Construit le sujet de l'email
     * 
     * @param managerReport Rapport du gestionnaire
     * @return Sujet de l'email
     */
    private String buildSubject(ManagerReportDto managerReport) {
        return String.format("Virement SIOP nécessitant votre attention - Canal %s", 
            managerReport.getCanal());
    }
    
    /**
     * Construit le corps de l'email
     * 
     * @param managerReport Rapport du gestionnaire
     * @param queryDate Date de la requête
     * @return Corps de l'email
     */
    private String buildEmailBody(ManagerReportDto managerReport, String queryDate) {
        StringBuilder body = new StringBuilder();
        
        body.append(String.format("Bonjour %s %s,\n\n", 
            managerReport.getPrenomGest(), managerReport.getNomGest()));
        
        body.append(String.format("%d virement(s) venant du canal %s nécessite(nt) votre attention.\n\n", 
            managerReport.getCount(), managerReport.getCanal()));
        
        body.append("Détails:\n");
        body.append(String.format("- Nombre d'opérations: %d\n", managerReport.getCount()));
        body.append(String.format("- Date: %s\n", queryDate));
        body.append(String.format("- Canal: %s\n\n", managerReport.getCanal()));
        
        body.append("Veuillez traiter ces opérations dans les plus brefs délais.\n\n");
        body.append("Le fichier Excel en pièce jointe contient tous les détails de vos opérations.\n\n");
        body.append("Cordialement,\n");
        body.append("Système SIOP");
        
        return body.toString();
    }
    
    /**
     * Construit le nom du fichier Excel
     * 
     * @param emailGest Email du gestionnaire
     * @param queryDate Date de la requête
     * @return Nom du fichier Excel
     */
    private String buildExcelFilename(String emailGest, String queryDate) {
        String cleanEmail = emailGest.replace("@", "_").replace(".", "_");
        String cleanDate = queryDate.replace("/", "_");
        return String.format("siop_operations_%s_%s.xlsx", cleanEmail, cleanDate);
    }
}

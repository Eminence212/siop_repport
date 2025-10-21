package com.rawbank.siop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les rapports SIOP
 * 
 * Contient les informations nécessaires pour la génération
 * des rapports et l'envoi d'emails
 */
public class SiopReportDto {
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateCreation;
    
    private String filename;
    private String canal;
    private String service;
    private String typeMsg;
    private String beneficiaire;
    private BigDecimal montantTx;
    private String motif;
    private String frais;
    private String msgStatus;
    private String lotStatus;
    private String txtStatus;
    private String errorMsg;
    
    // Informations du gestionnaire
    private String nomGest;
    private String prenomGest;
    private String emailGest;
    private String phoneGest;
    
    // Constructeurs
    public SiopReportDto() {}
    
    public SiopReportDto(String emailGest, String nomGest, String prenomGest, String canal) {
        this.emailGest = emailGest;
        this.nomGest = nomGest;
        this.prenomGest = prenomGest;
        this.canal = canal;
    }
    
    // Getters et Setters
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    
    public String getTypeMsg() { return typeMsg; }
    public void setTypeMsg(String typeMsg) { this.typeMsg = typeMsg; }
    
    public String getBeneficiaire() { return beneficiaire; }
    public void setBeneficiaire(String beneficiaire) { this.beneficiaire = beneficiaire; }
    
    public BigDecimal getMontantTx() { return montantTx; }
    public void setMontantTx(BigDecimal montantTx) { this.montantTx = montantTx; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public String getFrais() { return frais; }
    public void setFrais(String frais) { this.frais = frais; }
    
    public String getMsgStatus() { return msgStatus; }
    public void setMsgStatus(String msgStatus) { this.msgStatus = msgStatus; }
    
    public String getLotStatus() { return lotStatus; }
    public void setLotStatus(String lotStatus) { this.lotStatus = lotStatus; }
    
    public String getTxtStatus() { return txtStatus; }
    public void setTxtStatus(String txtStatus) { this.txtStatus = txtStatus; }
    
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    
    public String getNomGest() { return nomGest; }
    public void setNomGest(String nomGest) { this.nomGest = nomGest; }
    
    public String getPrenomGest() { return prenomGest; }
    public void setPrenomGest(String prenomGest) { this.prenomGest = prenomGest; }
    
    public String getEmailGest() { return emailGest; }
    public void setEmailGest(String emailGest) { this.emailGest = emailGest; }
    
    public String getPhoneGest() { return phoneGest; }
    public void setPhoneGest(String phoneGest) { this.phoneGest = phoneGest; }
}

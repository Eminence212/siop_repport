package com.rawbank.siop.dto;

import java.util.List;

/**
 * DTO pour les rapports par gestionnaire
 * 
 * Contient les informations groupées par gestionnaire
 * pour l'envoi d'emails individuels
 */
public class ManagerReportDto {
    
    private String emailGest;
    private String nomGest;
    private String prenomGest;
    private String canal;
    private Integer count;
    private List<SiopReportDto> operations;
    
    // Constructeurs
    public ManagerReportDto() {}
    
    public ManagerReportDto(String emailGest, String nomGest, String prenomGest, String canal, Integer count) {
        this.emailGest = emailGest;
        this.nomGest = nomGest;
        this.prenomGest = prenomGest;
        this.canal = canal;
        this.count = count;
    }
    
    // Getters et Setters
    public String getEmailGest() { return emailGest; }
    public void setEmailGest(String emailGest) { this.emailGest = emailGest; }
    
    public String getNomGest() { return nomGest; }
    public void setNomGest(String nomGest) { this.nomGest = nomGest; }
    
    public String getPrenomGest() { return prenomGest; }
    public void setPrenomGest(String prenomGest) { this.prenomGest = prenomGest; }
    
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public List<SiopReportDto> getOperations() { return operations; }
    public void setOperations(List<SiopReportDto> operations) { this.operations = operations; }
}

package com.rawbank.siop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant une opération SIOP
 * 
 * Cette entité mappe les résultats de la requête SQL Oracle
 * et contient toutes les informations nécessaires pour le rapport
 */
@Entity
@Table(name = "siop_operations")
public class SiopOperation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dcre")
    private LocalDateTime dateCreation;
    
    @Column(name = "incomtime")
    private LocalDateTime incomTime;
    
    @Column(name = "dco")
    private LocalDateTime dateCo;
    
    @Column(name = "filename")
    private String filename;
    
    @Column(name = "canal")
    private String canal;
    
    @Column(name = "service")
    private String service;
    
    @Column(name = "idmsg_ampl")
    private String idmsgAmpl;
    
    @Column(name = "idlot")
    private String idlot;
    
    @Column(name = "idtx")
    private String idtx;
    
    @Column(name = "msgid_file")
    private String msgidFile;
    
    @Column(name = "typemsg")
    private String typeMsg;
    
    @Column(name = "nbtx_msg")
    private Integer nbtxMsg;
    
    @Column(name = "nbtx_lot")
    private Integer nbtxLot;
    
    @Column(name = "donneur_ordre")
    private String donneurOrdre;
    
    @Column(name = "total_lot")
    private String totalLot;
    
    @Column(name = "benef")
    private String beneficiaire;
    
    @Column(name = "montant_tx")
    private BigDecimal montantTx;
    
    @Column(name = "net_payer")
    private BigDecimal netPayer;
    
    @Column(name = "motif")
    private String motif;
    
    @Column(name = "frais")
    private String frais;
    
    @Column(name = "base")
    private String base;
    
    @Column(name = "pmtmtd")
    private String pmtmtd;
    
    @Column(name = "compta")
    private String compta;
    
    @Column(name = "msgstatus")
    private String msgStatus;
    
    @Column(name = "lotstatus")
    private String lotStatus;
    
    @Column(name = "txtstatus")
    private String txtStatus;
    
    @Column(name = "age")
    private String age;
    
    @Column(name = "ope")
    private String ope;
    
    @Column(name = "eve")
    private String eve;
    
    @Column(name = "typ")
    private String typ;
    
    @Column(name = "date_eve")
    private LocalDateTime dateEve;
    
    @Column(name = "eve_status")
    private String eveStatus;
    
    @Column(name = "desc1")
    private String desc1;
    
    @Column(name = "desc2")
    private String desc2;
    
    @Column(name = "desc3")
    private String desc3;
    
    @Column(name = "desc4")
    private String desc4;
    
    @Column(name = "desc5")
    private String desc5;
    
    @Column(name = "bic")
    private String bic;
    
    @Column(name = "ncp1")
    private String ncp1;
    
    @Column(name = "ncp2")
    private String ncp2;
    
    @Column(name = "etab")
    private String etab;
    
    @Column(name = "guib")
    private String guib;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "domi")
    private String domi;
    
    @Column(name = "adb2")
    private String adb2;
    
    @Column(name = "gest_code")
    private String gestCode;
    
    @Column(name = "nom_gest")
    private String nomGest;
    
    @Column(name = "prenom_gest")
    private String prenomGest;
    
    @Column(name = "email_gest")
    private String emailGest;
    
    @Column(name = "phone_gest")
    private String phoneGest;
    
    @Column(name = "uti")
    private String uti;
    
    @Column(name = "errormsg")
    private String errorMsg;
    
    // Constructeurs
    public SiopOperation() {}
    
    public SiopOperation(String emailGest, String nomGest, String prenomGest, String canal) {
        this.emailGest = emailGest;
        this.nomGest = nomGest;
        this.prenomGest = prenomGest;
        this.canal = canal;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public LocalDateTime getIncomTime() { return incomTime; }
    public void setIncomTime(LocalDateTime incomTime) { this.incomTime = incomTime; }
    
    public LocalDateTime getDateCo() { return dateCo; }
    public void setDateCo(LocalDateTime dateCo) { this.dateCo = dateCo; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    
    public String getIdmsgAmpl() { return idmsgAmpl; }
    public void setIdmsgAmpl(String idmsgAmpl) { this.idmsgAmpl = idmsgAmpl; }
    
    public String getIdlot() { return idlot; }
    public void setIdlot(String idlot) { this.idlot = idlot; }
    
    public String getIdtx() { return idtx; }
    public void setIdtx(String idtx) { this.idtx = idtx; }
    
    public String getMsgidFile() { return msgidFile; }
    public void setMsgidFile(String msgidFile) { this.msgidFile = msgidFile; }
    
    public String getTypeMsg() { return typeMsg; }
    public void setTypeMsg(String typeMsg) { this.typeMsg = typeMsg; }
    
    public Integer getNbtxMsg() { return nbtxMsg; }
    public void setNbtxMsg(Integer nbtxMsg) { this.nbtxMsg = nbtxMsg; }
    
    public Integer getNbtxLot() { return nbtxLot; }
    public void setNbtxLot(Integer nbtxLot) { this.nbtxLot = nbtxLot; }
    
    public String getDonneurOrdre() { return donneurOrdre; }
    public void setDonneurOrdre(String donneurOrdre) { this.donneurOrdre = donneurOrdre; }
    
    public String getTotalLot() { return totalLot; }
    public void setTotalLot(String totalLot) { this.totalLot = totalLot; }
    
    public String getBeneficiaire() { return beneficiaire; }
    public void setBeneficiaire(String beneficiaire) { this.beneficiaire = beneficiaire; }
    
    public BigDecimal getMontantTx() { return montantTx; }
    public void setMontantTx(BigDecimal montantTx) { this.montantTx = montantTx; }
    
    public BigDecimal getNetPayer() { return netPayer; }
    public void setNetPayer(BigDecimal netPayer) { this.netPayer = netPayer; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public String getFrais() { return frais; }
    public void setFrais(String frais) { this.frais = frais; }
    
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    
    public String getPmtmtd() { return pmtmtd; }
    public void setPmtmtd(String pmtmtd) { this.pmtmtd = pmtmtd; }
    
    public String getCompta() { return compta; }
    public void setCompta(String compta) { this.compta = compta; }
    
    public String getMsgStatus() { return msgStatus; }
    public void setMsgStatus(String msgStatus) { this.msgStatus = msgStatus; }
    
    public String getLotStatus() { return lotStatus; }
    public void setLotStatus(String lotStatus) { this.lotStatus = lotStatus; }
    
    public String getTxtStatus() { return txtStatus; }
    public void setTxtStatus(String txtStatus) { this.txtStatus = txtStatus; }
    
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    
    public String getOpe() { return ope; }
    public void setOpe(String ope) { this.ope = ope; }
    
    public String getEve() { return eve; }
    public void setEve(String eve) { this.eve = eve; }
    
    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }
    
    public LocalDateTime getDateEve() { return dateEve; }
    public void setDateEve(LocalDateTime dateEve) { this.dateEve = dateEve; }
    
    public String getEveStatus() { return eveStatus; }
    public void setEveStatus(String eveStatus) { this.eveStatus = eveStatus; }
    
    public String getDesc1() { return desc1; }
    public void setDesc1(String desc1) { this.desc1 = desc1; }
    
    public String getDesc2() { return desc2; }
    public void setDesc2(String desc2) { this.desc2 = desc2; }
    
    public String getDesc3() { return desc3; }
    public void setDesc3(String desc3) { this.desc3 = desc3; }
    
    public String getDesc4() { return desc4; }
    public void setDesc4(String desc4) { this.desc4 = desc4; }
    
    public String getDesc5() { return desc5; }
    public void setDesc5(String desc5) { this.desc5 = desc5; }
    
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }
    
    public String getNcp1() { return ncp1; }
    public void setNcp1(String ncp1) { this.ncp1 = ncp1; }
    
    public String getNcp2() { return ncp2; }
    public void setNcp2(String ncp2) { this.ncp2 = ncp2; }
    
    public String getEtab() { return etab; }
    public void setEtab(String etab) { this.etab = etab; }
    
    public String getGuib() { return guib; }
    public void setGuib(String guib) { this.guib = guib; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDomi() { return domi; }
    public void setDomi(String domi) { this.domi = domi; }
    
    public String getAdb2() { return adb2; }
    public void setAdb2(String adb2) { this.adb2 = adb2; }
    
    public String getGestCode() { return gestCode; }
    public void setGestCode(String gestCode) { this.gestCode = gestCode; }
    
    public String getNomGest() { return nomGest; }
    public void setNomGest(String nomGest) { this.nomGest = nomGest; }
    
    public String getPrenomGest() { return prenomGest; }
    public void setPrenomGest(String prenomGest) { this.prenomGest = prenomGest; }
    
    public String getEmailGest() { return emailGest; }
    public void setEmailGest(String emailGest) { this.emailGest = emailGest; }
    
    public String getPhoneGest() { return phoneGest; }
    public void setPhoneGest(String phoneGest) { this.phoneGest = phoneGest; }
    
    public String getUti() { return uti; }
    public void setUti(String uti) { this.uti = uti; }
    
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
}

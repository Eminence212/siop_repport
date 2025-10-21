SELECT 
  BKMOPMSG.dcre,
  BKMOPMSG.incomtime,
  BKMOPMSG.dco,
  BKMOPMSG.nomfic AS filename,
  BKMOPMSG.canal,
  BKMOPLOT.service_val AS service,
  BKMOPMSG.idmsg AS idmsg_ampl,
  BKMOPLOT.idlot,
  BKMOPTX.idtx,
  BKMOPMSG.msgid AS msgid_file,
  CASE 
    WHEN TRIM(BKMOPMSG.typmsg) = '001' THEN 'Transfert'
    WHEN TRIM(BKMOPMSG.typmsg) = '007' THEN 'Extourne prélèvement'
    WHEN TRIM(BKMOPMSG.typmsg) = '008' THEN 'Prélèvement'
    ELSE 'Aucun'
  END AS typemsg,
  BKMOPMSG.nbtx AS nbtx_msg,
  BKMOPLOT.nbtx AS nbtx_lot,
  BKMOPLOT.name AS donneur_ordre,
  BKMOPMSG.ctrlsum || ' ' || BKMOPLOT.acc_dev AS total_lot,
  BKMOPTX.cdtr_name AS benef,
  BKMOPTX.instdamt_amt AS montant_tx,
  BKEVE.mon2 AS net_payer,
  BKEVE.lib1 AS motif,
  CASE 
    WHEN TRIM(BKMOPTX.fees) = 'O' THEN 'Oui'
    ELSE 'Non'
  END AS frais,
  BKMOPMSG.flowindicator AS base,
  BKMOPLOT.pmtmtd,
  CASE 
    WHEN TRIM(BKMOPLOT.btchbookg) = '0' THEN 'Unitaire'
    WHEN TRIM(BKMOPLOT.btchbookg) = '1' THEN 'Cumul'
    ELSE 'Aucune'
  END AS compta,
  CASE 
    WHEN TRIM(BKMOPMSG.status) = 'OK' THEN 'Accepté'
    WHEN TRIM(BKMOPMSG.status) = 'RJ' THEN 'Rejeté'
    ELSE 'Aucun'
  END AS msgstatus,
  CASE 
    WHEN TRIM(BKMOPLOT.eta) = 'TR' THEN 'Traité'
    WHEN TRIM(BKMOPLOT.eta) = 'ER' THEN 'En erreur'
    WHEN TRIM(BKMOPLOT.eta) = 'CO' THEN 'Corrigé'
    WHEN TRIM(BKMOPLOT.eta) = 'RJ' THEN 'Rejeté'
    WHEN TRIM(BKMOPLOT.eta) = 'AB' THEN 'Abandonné'
    WHEN TRIM(BKMOPLOT.eta) = 'CT' THEN 'Contrôle Technique-lot différé effectué'
    WHEN TRIM(BKMOPLOT.eta) = 'VA' THEN 'Intégré correct non traité'
    ELSE 'Aucun'
  END AS lotstatus,
  CASE 
    WHEN TRIM(BKMOPTX.eta) = 'VA' THEN 'Intégré non traité'
    WHEN TRIM(BKMOPTX.eta) = 'TR' THEN 'Traité'
    WHEN TRIM(BKMOPTX.eta) = 'ER' THEN 'En erreur'
    WHEN TRIM(BKMOPTX.eta) = 'RJ' THEN 'Rejeté'
    WHEN TRIM(BKMOPTX.eta) = 'AB' THEN 'Abandonné'
    ELSE 'Aucun'
  END AS txtstatus,
  BKMOPTX.age,
  BKMOPTX.ope,
  BKMOPTX.eve,
  BKMOPTX.typ,
  BKMOPTX.deve AS date_eve,
  BKEVE.eta AS eve_status,
  BKEVE.desc1,
  BKEVE.desc2,
  BKEVE.desc3,
  BKEVE.desc4,
  BKEVE.desc5,
  BKMOPLOT.bank_bic AS bic,
  BKMOPLOT.age || '-' || BKMOPLOT.ncp AS ncp1,
  CASE 
    WHEN TRIM(BKMOPTX.age2) = '-' THEN BKMOPTX.age2 || '-' || BKMOPTX.ncp2
    ELSE BKMOPTX.cdtracc_nb
  END AS ncp2,
  BKEVE.etab,
  BKEVE.guib,
  BKEVE.nome,
  BKEVE.domi,
  BKEVE.adb2,
  BKEVE.ges1 AS gest_code,
  BKNOM.lib1 AS nom_gest,
  BKNOM.lib2 AS prenom_gest,
  BKNOM.lib4 AS email_gest,
  BKNOM.lib5 AS phone_gest,
  BKEVE.uti,
  (
    SELECT LISTAGG(TRIM(mess) || ' au niveau de ' || lvl, ' | ') 
           WITHIN GROUP (ORDER BY lvl)
    FROM BKMOPERROR 
    WHERE idtx = BKMOPTX.idtx 
       OR idlot = BKMOPLOT.idlot 
       OR idmsg = BKMOPMSG.idmsg
  ) AS errormsg
FROM BKMOPMSG 
LEFT JOIN BKMOPLOT ON BKMOPMSG.idmsg = BKMOPLOT.idmsg
LEFT JOIN BKMOPTX ON BKMOPLOT.idlot = BKMOPTX.idlot
LEFT JOIN BKEVE ON BKMOPTX.age = BKEVE.age AND BKMOPTX.ope = BKEVE.ope AND BKMOPTX.eve = BKEVE.eve AND BKMOPTX.typ = BKEVE.typ
LEFT JOIN BKNOM ON BKEVE.ges1 = BKNOM.cacc AND BKNOM.ctab = '035'
WHERE BKMOPMSG.dcre = TO_DATE('14/10/2025', 'DD/MM/YYYY') AND (BKMOPMSG.status NOT IN ('OK')  OR bkmoptx.eta NOT IN ('TR') OR bkmoptx.eta NOT IN ('TR') OR bkeve.eta NOT IN ('VA'))
  AND BKMOPMSG.canal = 'VODACOM'
ORDER BY BKMOPMSG.dcre, BKMOPMSG.incomtime DESC;

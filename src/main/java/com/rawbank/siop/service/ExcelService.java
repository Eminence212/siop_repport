package com.rawbank.siop.service;

import com.rawbank.siop.dto.SiopReportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service pour la génération de fichiers Excel
 * 
 * Responsable de :
 * - Génération de fichiers Excel avec style
 * - Formatage des données
 * - Optimisation des colonnes
 */
@Service
public class ExcelService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    
    private static final String[] HEADERS = {
        "Date", "Fichier", "Canal", "Service", "Type Message", 
        "Bénéficiaire", "Montant", "Motif", "Frais", 
        "Status Message", "Status Lot", "Status TX", "Erreur"
    };
    
    /**
     * Génère un fichier Excel à partir des opérations SIOP
     * 
     * @param operations Liste des opérations SIOP
     * @return Fichier Excel sous forme de byte array
     */
    public byte[] generateExcel(List<SiopReportDto> operations) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Opérations SIOP");
            
            // Création des styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            
            // Création des en-têtes
            createHeaders(sheet, headerStyle);
            
            // Remplissage des données
            fillData(sheet, operations, dataStyle, dateStyle, numberStyle);
            
            // Ajustement automatique des colonnes
            autoSizeColumns(sheet);
            
            // Conversion en byte array
            return convertToByteArray(workbook);
            
        } catch (IOException e) {
            logger.error("Erreur lors de la génération du fichier Excel", e);
            throw new RuntimeException("Erreur lors de la génération Excel", e);
        }
    }
    
    /**
     * Crée le style pour les en-têtes
     * 
     * @param workbook Workbook Excel
     * @return Style des en-têtes
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Police en gras et blanc
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        // Couleur de fond bleue
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Alignement centré
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // Bordures
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }
    
    /**
     * Crée le style pour les données
     * 
     * @param workbook Workbook Excel
     * @return Style des données
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Bordures
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Alignement
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }
    
    /**
     * Crée le style pour les dates
     * 
     * @param workbook Workbook Excel
     * @return Style des dates
     */
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        
        // Format de date
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        
        return style;
    }
    
    /**
     * Crée le style pour les nombres
     * 
     * @param workbook Workbook Excel
     * @return Style des nombres
     */
    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        
        // Format numérique
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
        
        return style;
    }
    
    /**
     * Crée les en-têtes du tableau
     * 
     * @param sheet Feuille Excel
     * @param headerStyle Style des en-têtes
     */
    private void createHeaders(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    
    /**
     * Remplit les données dans le tableau
     * 
     * @param sheet Feuille Excel
     * @param operations Liste des opérations
     * @param dataStyle Style des données
     * @param dateStyle Style des dates
     * @param numberStyle Style des nombres
     */
    private void fillData(Sheet sheet, List<SiopReportDto> operations, 
                         CellStyle dataStyle, CellStyle dateStyle, CellStyle numberStyle) {
        
        int rowNum = 1;
        for (SiopReportDto operation : operations) {
            Row row = sheet.createRow(rowNum++);
            
            // Date
            Cell dateCell = row.createCell(0);
            if (operation.getDateCreation() != null) {
                dateCell.setCellValue(operation.getDateCreation());
                dateCell.setCellStyle(dateStyle);
            }
            
            // Fichier
            Cell filenameCell = row.createCell(1);
            filenameCell.setCellValue(operation.getFilename());
            filenameCell.setCellStyle(dataStyle);
            
            // Canal
            Cell canalCell = row.createCell(2);
            canalCell.setCellValue(operation.getCanal());
            canalCell.setCellStyle(dataStyle);
            
            // Service
            Cell serviceCell = row.createCell(3);
            serviceCell.setCellValue(operation.getService());
            serviceCell.setCellStyle(dataStyle);
            
            // Type Message
            Cell typeMsgCell = row.createCell(4);
            typeMsgCell.setCellValue(operation.getTypeMsg());
            typeMsgCell.setCellStyle(dataStyle);
            
            // Bénéficiaire
            Cell benefCell = row.createCell(5);
            benefCell.setCellValue(operation.getBeneficiaire());
            benefCell.setCellStyle(dataStyle);
            
            // Montant
            Cell montantCell = row.createCell(6);
            if (operation.getMontantTx() != null) {
                montantCell.setCellValue(operation.getMontantTx().doubleValue());
                montantCell.setCellStyle(numberStyle);
            }
            
            // Motif
            Cell motifCell = row.createCell(7);
            motifCell.setCellValue(operation.getMotif());
            motifCell.setCellStyle(dataStyle);
            
            // Frais
            Cell fraisCell = row.createCell(8);
            fraisCell.setCellValue(operation.getFrais());
            fraisCell.setCellStyle(dataStyle);
            
            // Status Message
            Cell msgStatusCell = row.createCell(9);
            msgStatusCell.setCellValue(operation.getMsgStatus());
            msgStatusCell.setCellStyle(dataStyle);
            
            // Status Lot
            Cell lotStatusCell = row.createCell(10);
            lotStatusCell.setCellValue(operation.getLotStatus());
            lotStatusCell.setCellStyle(dataStyle);
            
            // Status TX
            Cell txtStatusCell = row.createCell(11);
            txtStatusCell.setCellValue(operation.getTxtStatus());
            txtStatusCell.setCellStyle(dataStyle);
            
            // Erreur
            Cell errorCell = row.createCell(12);
            errorCell.setCellValue(operation.getErrorMsg());
            errorCell.setCellStyle(dataStyle);
        }
    }
    
    /**
     * Ajuste automatiquement la largeur des colonnes
     * 
     * @param sheet Feuille Excel
     */
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
            
            // Limite la largeur maximale à 50 caractères
            int currentWidth = sheet.getColumnWidth(i);
            int maxWidth = 50 * 256; // 50 caractères en unités Excel
            if (currentWidth > maxWidth) {
                sheet.setColumnWidth(i, maxWidth);
            }
        }
    }
    
    /**
     * Convertit le workbook en byte array
     * 
     * @param workbook Workbook Excel
     * @return Byte array du fichier Excel
     * @throws IOException Erreur d'écriture
     */
    private byte[] convertToByteArray(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}

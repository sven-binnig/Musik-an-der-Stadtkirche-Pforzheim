/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author svenina
 */
public class EnsembleAwareProbenplanWordWriter extends AbstractEnsembleAwareProbenplanWriter {

    private File directory;
    private final Map<String, EnsembleAwareProbenplanWordWriter.WordCoordinates> ensembleMap = new HashMap<>();
    private int cellIndex = 0;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void doFor(CalendarEvent event, Ensemble ensemble) {
        try {
            WordCoordinates coordinates = forEnsemble(ensemble);
            //print("--> use " + coordinates);
            /*if (coordinates.isFirstRow()) {
                XWPFTableRow tableRowOne = coordinates.getPdfTable().getRow(0);
                insertCellText(tableRowOne.getCell(0), event.getWochentag());
                insertCellText(tableRowOne.addNewTableCell(), DATE_FORMATTER.format(event.getBeginnDatum()));
                insertCellText(tableRowOne.addNewTableCell(), super.formatUhrzeitConformingToStadtkircheCI(event.getBeginnUhrzeit()));
                insertCellText(tableRowOne.addNewTableCell(), DATE_FORMATTER.format(event.getEndeDatum()));
                insertCellText(tableRowOne.addNewTableCell(), super.formatUhrzeitConformingToStadtkircheCI(event.getEndeUhrzeit()));
                insertCellText(tableRowOne.addNewTableCell(), event.getBeschreibung());
                insertCellText(tableRowOne.addNewTableCell(), event.getOrt());
                coordinates.setFirstRow(false);
            } else {
                XWPFTableRow tableRowTwo = coordinates.getPdfTable().createRow();
                insertCellText(tableRowTwo.getCell(0), event.getWochentag());
                insertCellText(tableRowTwo.getCell(1), DATE_FORMATTER.format(event.getBeginnDatum()));
                insertCellText(tableRowTwo.getCell(2), super.formatUhrzeitConformingToStadtkircheCI(event.getBeginnUhrzeit()));
                insertCellText(tableRowTwo.getCell(3), DATE_FORMATTER.format(event.getEndeDatum()));
                insertCellText(tableRowTwo.getCell(4), super.formatUhrzeitConformingToStadtkircheCI(event.getEndeUhrzeit()));
                insertCellText(tableRowTwo.getCell(5), event.getBeschreibung());
                insertCellText(tableRowTwo.getCell(6), event.getOrt());
            }*/
            XWPFTableRow tableRow;
            cellIndex = 0;
            if (coordinates.isFirstRow()) {
                tableRow = coordinates.getPdfTable().getRow(0);
                super.getCelldataOfRow(event, DATE_FORMATTER).stream().forEach((cellEvent) -> {
                    if (cellIndex == 0) {
                        insertCellText(tableRow.getCell(0), cellEvent);
                    } else {
                        insertCellText(tableRow.addNewTableCell(), cellEvent);
                    }
                    ++cellIndex;
                });
                coordinates.setFirstRow(false);
            } else {
                tableRow = coordinates.getPdfTable().createRow();
                super.getCelldataOfRow(event, DATE_FORMATTER).stream().forEach((cellEvent) -> {
                    insertCellText(tableRow.getCell(cellIndex), cellEvent);
                    ++cellIndex;
                });
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException | InvalidFormatException | IOException ex) {
            Logger.getLogger(EnsembleAwareProbenplanWordWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void insertCellText(XWPFTableCell cell, String text) {
        if (cell != null) {
            cell.setText(text != null ? text : " ");
        } else {
            print("cell is null");
        }
    }

    @Override
    public void useDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public void close() {
        for (WordCoordinates coordinates : this.ensembleMap.values()) {
            try {
                coordinates.getPdfDocument().write(coordinates.getWriter());
                coordinates.getWriter().close();
                //print("close " + coordinates);
            } catch (IOException ex) {
                Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private WordCoordinates forEnsemble(Ensemble ensemble) throws FileNotFoundException, URISyntaxException, InvalidFormatException, IOException {
        WordCoordinates coordinates = this.ensembleMap.get(ensemble.getName());
        if (coordinates != null) {
            return coordinates;
        }
        print(" -- neues Dokument für " + ensemble.getName());
        // TODO use template for probenpläne from dcops
        // FIXME so funktioniert das leider NICHT: --------------------------
        String documentBaseName = ensemble.getName() != null ? ensemble.getName() + ".docx" : "probenplan.docx";
        //print("=> Basis für Probenplan: " + documentBaseName);
        //URL url = getClass().getResource(documentBaseName);
        //if (url == null) {
        //    url = getClass().getResource("probenplan.docx");
        //}
        
        //File vorlage = new File(url.toExternalForm());
        //print("---- verwende Vorlage " + vorlage.getAbsolutePath());
        InputStream vorlage = getClass().getResourceAsStream(documentBaseName);
        if(vorlage == null) {
            vorlage = getClass().getResourceAsStream("probenplan.docx");
        }
        OPCPackage template = OPCPackage.open(vorlage);
        XWPFDocument pdf = new XWPFDocument(template);
        //-------------------------------------------------------------------
        //XWPFDocument pdf = new XWPFDocument();
        FileOutputStream writer = new FileOutputStream(new File(this.directory, ensemble.getName() + ".docx"));

        XWPFTable pdfTable = pdf.createTable();
        coordinates = new WordCoordinates(writer, pdf, pdfTable);
        this.ensembleMap.put(ensemble.getName(), coordinates);
        return coordinates;
    }

    private void print(String msg) {
        //System.out.println("[" + getClass().getSimpleName() + "] " + msg);
        super.fireReaderWriterMessage(getClass().getSimpleName(), msg);
    }

    //----------------------------------------
    private class WordCoordinates {

        private final FileOutputStream writer;
        private final XWPFDocument pdfDocument;
        private final XWPFTable pdfTable;
        private boolean firstRow = true;

        public WordCoordinates(FileOutputStream writer, XWPFDocument pdfDocument, XWPFTable pdfTable) {
            this.writer = writer;
            this.pdfDocument = pdfDocument;
            this.pdfTable = pdfTable;
        }

        public FileOutputStream getWriter() {
            return writer;
        }

        public XWPFDocument getPdfDocument() {
            return pdfDocument;
        }

        public XWPFTable getPdfTable() {
            return pdfTable;
        }

        public boolean isFirstRow() {
            return firstRow;
        }

        public void setFirstRow(boolean firstRow) {
            this.firstRow = firstRow;
        }

        @Override
        public String toString() {
            return "WordCoordinates{" + "writer=" + writer + ", pdfDocument=" + pdfDocument + ", pdfTable=" + pdfTable + '}';
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sbg
 */
public class EnsembleAwareProbenplanPDFWriter extends AbstractEnsembleAwareProbenplanWriter {

    private File directory;
    private final Map<String, PDFCoordinates> ensembleMap = new HashMap<>();
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static Font FONT_CECILIA_BIGGER_BOLD;
    private static Font FONT_CECILIA;
    private static Font FONT_CECILIA_SMALLER;

    static {
        //String fontHome = System.getenv("windir") + "/fonts/";
        URL url = null;
        try {
            url = EnsembleAwareProbenplanPDFWriter.class.getResource("CaeciliaLTStd-Roman.otf");
            Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.INFO, url.toString());
            File fontFile = new File(url.toExternalForm());
            if (fontFile.exists()) {
                BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.WINANSI, BaseFont.EMBEDDED);

                FONT_CECILIA_BIGGER_BOLD = new Font(bf, 12, Font.BOLD);
                FONT_CECILIA = new Font(bf, 8, Font.NORMAL);
                FONT_CECILIA_SMALLER = new Font(bf, 7, Font.NORMAL);
            } else {
                FONT_CECILIA = FontFactory.getFont(BaseFont.HELVETICA, 8, Font.NORMAL);
                FONT_CECILIA_SMALLER = FontFactory.getFont(BaseFont.HELVETICA, 7, Font.NORMAL);
                FONT_CECILIA_BIGGER_BOLD = FontFactory.getFont(BaseFont.HELVETICA, 12, Font.BOLD);
            }

        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, null, ex);
            if(ex instanceof URISyntaxException) {
                Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, url.toString());
            }
        } catch(Throwable ex) {
            Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, null, ex);
            if(url != null) {
                Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, url.toString());
            }
        }
    }

    private void print(String msg) {
        //System.out.println("[" + getClass().getSimpleName() + "] " + msg);
        super.fireReaderWriterMessage(getClass().getSimpleName(), msg);
    }

    @Override
    public void doFor(CalendarEvent event, Ensemble ensemble) {
        try {
            PDFCoordinates pdfCoordinates = forEnsemble(ensemble);
            /*pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(event.getWochentag(), FONT_CECILIA)));
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(DATE_FORMATTER.format(event.getBeginnDatum()), FONT_CECILIA)));
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(super.formatUhrzeitConformingToStadtkircheCI(event.getBeginnUhrzeit()), FONT_CECILIA)).withRightAlignment());
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(DATE_FORMATTER.format(event.getEndeDatum()), FONT_CECILIA)));
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(super.formatUhrzeitConformingToStadtkircheCI(event.getEndeUhrzeit()), FONT_CECILIA)).withRightAlignment());
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(event.getBeschreibung(), FONT_CECILIA)));
            pdfCoordinates.getPdfTable().addCell(new ProbenplanTableCell(new Phrase(event.getOrt(), FONT_CECILIA)));*/

            super.getCelldataOfRow(event, DATE_FORMATTER).stream().forEach((cellEvent) -> {
                //print("#### pdf table for " + ensemble.getName() + ": " + pdfCoordinates.getPdfTable());
                //print("#### font: " + FONT_CECILIA);
                pdfCoordinates.getPdfTable().addCell(new Phrase(cellEvent, FONT_CECILIA));
            });

            pdfCoordinates.getPdfDocument().add(pdfCoordinates.getPdfTable());

        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void useDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public void close() {
        for (PDFCoordinates pdfCoordinates : this.ensembleMap.values()) {
            try {
                pdfCoordinates.getPdfTable().setComplete(true);
                pdfCoordinates.getPdfDocument().add(pdfCoordinates.getPdfTable());
                pdfCoordinates.getPdfDocument().close();

            } catch (DocumentException ex) {
                Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private PDFCoordinates forEnsemble(Ensemble ensemble) throws FileNotFoundException, DocumentException {
        PDFCoordinates pdfCoordinates = this.ensembleMap.get(ensemble.getName());
        if (pdfCoordinates != null) {
            return pdfCoordinates;
        }
        print(" -- neues Dokument für " + ensemble.getName());
        Document pdf = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(pdf, new FileOutputStream(new File(this.directory, ensemble.getName() + ".pdf")));
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onOpenDocument(PdfWriter writer, Document document) {
                try {
                    pdf.add(new Paragraph("Probenplan " + ensemble.getName() + ", Stand " + DATE_FORMATTER.format(new Date()), FONT_CECILIA_BIGGER_BOLD));
                    pdf.add(new Paragraph("Evang. Kantorat - Melanchthonstr. 1 - 75173 Pforzheim", FONT_CECILIA_SMALLER));
                    pdf.add(new Paragraph("Tel: 07231/2 33 39 - Fax: 07231/29 07 57 - Mail: bezirkskantorat.pforzheim@gmx.de", FONT_CECILIA_SMALLER));

                    pdf.add(new Paragraph(" "));

                } catch (DocumentException ex) {
                    Logger.getLogger(EnsembleAwareProbenplanPDFWriter.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                super.onOpenDocument(writer, document); //To change body of generated methods, choose Tools | Templates.
            }

        });
        pdf.open();
        PdfPTable pdfTable = new PdfPTable(super.getCellsPerRow());
        //pdfTable.setHeaderRows(1);
        pdfTable.setComplete(false);
        pdfTable.setSplitRows(false);
        pdfTable.setWidthPercentage(100);
        // set relative columns width
        pdfTable.setWidths(new float[]{0.5f, 1.6f, 5.0f, 1.0f});
        pdfCoordinates = new PDFCoordinates(writer, pdf, pdfTable);
        this.ensembleMap.put(ensemble.getName(), pdfCoordinates);

        //pdf.add(new Paragraph("Probenplan " + ensemble.getName() + ", Stand " + DATE_FORMATTER.format(new Date()), FONT));
        //pdf.add(new Paragraph(" "));
        return pdfCoordinates;

    }

    //----------------------------------------
    private class PDFCoordinates {

        private final PdfWriter writer;
        private final Document pdfDocument;
        private final PdfPTable pdfTable;

        public PDFCoordinates(PdfWriter writer, Document pdfDocument, PdfPTable pdfTable) {
            this.writer = writer;
            this.pdfDocument = pdfDocument;
            this.pdfTable = pdfTable;
        }

        public PdfWriter getWriter() {
            return writer;
        }

        public Document getPdfDocument() {
            return pdfDocument;
        }

        public PdfPTable getPdfTable() {
            return pdfTable;
        }

    }
}

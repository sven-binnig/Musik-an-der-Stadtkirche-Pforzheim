/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author svenina
 */
public class ProbenplanTableCell extends PdfPCell {

    public ProbenplanTableCell() {
    }

    public ProbenplanTableCell(Phrase phrase) {
        super(phrase);
    }

    public ProbenplanTableCell(PdfPTable table, PdfPCell style) {
        super(table, style);
    }
    
    
    public ProbenplanTableCell withRightAlignment() {
        this.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return this;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.test;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author svenina
 */
public class ExcelFileCreator {

    private final File excelFile;
    private final String sheetName;

    public ExcelFileCreator(File excelFile, String sheetName) {
        this.excelFile = excelFile;
        this.sheetName = sheetName;
    }

    public void create(Collection<CalendarEvent> event) {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet(sheetName);
        SheetWriter<CalendarEvent> writer = sheet.getBeanWriter(CalendarEvent.class);
        
        writer.write(event);
        xcelite.write(excelFile);
    }
}

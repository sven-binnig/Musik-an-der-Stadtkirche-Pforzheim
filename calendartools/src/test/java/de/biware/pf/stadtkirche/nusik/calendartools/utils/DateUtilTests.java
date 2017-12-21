/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author svenina
 */
public class DateUtilTests {
    @Test
    public void may18thIn2018IsAFriday() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Assert.assertTrue(DateUtils.dateSuitsToWeekday(sdf.parse("20180518"), "Fr."));
    }
}

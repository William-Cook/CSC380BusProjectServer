/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.time.LocalDateTime;
import junit.framework.TestCase;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;

/**
 *
 * @author bill
 */
public class UseStatsTest extends TestCase {

    public void testInitialize() {
        UseStats.initialize();
        assertNotNull(UseStats.records);
    }

    public void testAddUse() {
        UseStats.initialize();
        UseStats.addUse("busID", "stopID", "userID", LocalDateTime.now());
        assertNotNull(UseStats.records.get("busID"));
    }

    public void testAddPoll() {
        UseStats.initialize();
        int start = UseStats.totalPolls;
        UseStats.addPoll();
        int end = UseStats.totalPolls;
        assertTrue(start != end);
    }

    public void testAddUserLogin() {
        UseStats.initialize();
        UseStats.addUserLogin("userID");
        assertTrue(UseStats.logins.get("userID") > 0);
    }

    public void testExportStats() throws FileNotFoundException {
        UseStats.initialize();
        UseStats.exportStats();
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new File(cl.getResource("usage_stats.txt").getFile());
        Scanner fs = new Scanner(f);
        assertTrue(fs.hasNext());
    }

}

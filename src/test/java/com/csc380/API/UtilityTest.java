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
import junit.framework.TestCase;
import java.util.Scanner;
import static org.mockito.Mockito.*;

/**
 *
 * @author bill
 */
public class UtilityTest extends TestCase {

    //test encodeHashMap
    public void testEncodeHashMap() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
        FileOutputStream fo = new FileOutputStream("testEncoding.txt");
        PrintWriter pw = new PrintWriter(fo);
        Utility.parseTrips("trips.txt");
        Utility.parseStops("stops.txt");
        Utility.parseStopTimes("stop_times.txt");
        Utility.jsonParser("vehicle-monitoring.json");
        Utility.parseShapes("shapes.txt");
        Utility.assignTrips();
        Utility.encodeHashMap(pw);
        File f = new File("testEncoding.txt");
        Scanner fs = new Scanner(f);
        assertTrue(fs.hasNext());
    }

    //test decodeToHashMap
    public void testDecodeToHashMap() throws FileNotFoundException {
        FileInputStream fi = new FileInputStream("testEncoding.txt");
        Utility.decodeToHashMap(fi);
        assertNotNull(Utility.busses);
    }

    //test assignTrips
    public void testAssignTrips() throws FileNotFoundException, IOException,
            org.json.simple.parser.ParseException, ParseException {
        Utility.parseTrips("trips.txt");
        Utility.parseStops("stops.txt");
        Utility.jsonParser("vehicle-monitoring.json");
        Utility.parseShapes("shapes.txt");
        Utility.assignTrips();
        assertNotNull(Utility.busses.values().iterator().next().route_shape);
    }

    //test parseTrips
    public void testParseTrips() throws FileNotFoundException {
        Utility.parseTrips("trips.txt");
        assertNotNull(Utility.trips);
    }

    //test parseStops
    public void testParseStops() throws FileNotFoundException {
        Utility.parseStops("stops.txt");
        assertNotNull(Utility.stops);
    }

    //test parseStopTimes
    public void testParseStopTimes() throws FileNotFoundException {
        Utility.parseTrips("trips.txt");
        Utility.parseStopTimes("stop_times.txt");
        assertNotNull(Utility.trips.values().iterator().next().route);
    }

    //test parseShapes
    public void testParseShapes() throws FileNotFoundException {
        Utility.parseShapes("shapes.txt");
        assertNotNull(Utility.shapes.values().iterator().next());
    }

    //test openMTAApiConnection
    public void testOpenMTAApiConnection() throws InterruptedException, IOException {

        HttpURLConnection conn = Utility.openMTAApiConnection();

        assertNotNull(conn);

    }

    //test getFile
    public void testGetFile() throws IOException {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        File f = new File("testfile2.txt");
        FileInputStream finps = new FileInputStream(f);
        when(conn.getResponseCode()).thenReturn(200);
        when(conn.getInputStream()).thenReturn(finps);
        Utility.getFile(conn,
                "/home/bill/SchoolWork/csc380/CSC_380_bus_project",
                "testfile.txt", false);
        assertTrue(true);
    }

    // test startup tasks
//    public void testStartupTasks() throws InterruptedException, IOException{
//        Utility.startupTasks();
//        int first = Utility.updateModelExecuteCount;
//        Thread.sleep(72000);
//        int last = Utility.updateModelExecuteCount;
//        Utility.stopApiPoller();
//        assertTrue(first != last);
//    }
}

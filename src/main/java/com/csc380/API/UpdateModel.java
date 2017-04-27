/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import static com.csc380.API.Utility.jsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bill
 */
public class UpdateModel extends TimerTask {

    public void run() {
        synchronized (Utility.lock) {
            System.out.println("Executing TimerTask UpdateModel");
            Utility.updateModelExecuteCount++;
            ClassLoader cl = Utility.class.getClassLoader();
            HashMap<String, Bus> busses_local = Utility.busses;
            try {
                HttpURLConnection conn = Utility.openMTAApiConnection();
                Utility.getFile(conn, cl.getResource("vehicle-monitoring.json").getPath(), "vehicle-monitoring.json", false);

            } catch (IOException ex) {
                System.out.println("IO Exception getting Vehicle Data.");
            }
            try {
                Utility.jsonParser("vehicle-monitoring.json");
                Utility.assignTrips();
            } catch (IOException ex) {
                System.out.println("IO Exception parsing Vehicle Data.");
                Utility.busses = busses_local;
            } catch (ParseException ex) {
                System.out.println("Parse Exception parsing Vehicle Data.");
                Utility.busses = busses_local;
            } catch (org.json.simple.parser.ParseException ex) {
                System.out.println("Parse Exception parsing Vehicle Data.");
                Utility.busses = busses_local;
            }
            
        }
    }

}

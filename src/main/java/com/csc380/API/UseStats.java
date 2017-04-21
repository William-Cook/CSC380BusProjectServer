/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author bill
 */
public final class UseStats {
    
    static HashMap<String, ArrayList<UseRecord>> records; // Key busID, value is an ArrayList of UseRecords
    static HashMap<String, Integer> logins; // Key userID, value is # of logins since init
    static LocalDateTime initDateTime;
    static int totalPolls;
    
    private UseStats () {}
    
    public static void initialize () {
        if (records == null) {
            records = new HashMap();
            logins = new HashMap();
            initDateTime = LocalDateTime.now();
            totalPolls = 0;
        }
    }
    
    public static void addUse (String busID, String stopID, String userID,
            LocalDateTime recordTime) {
        UseRecord useRec = new UseRecord(busID, stopID, userID, recordTime);
        if (records.containsKey(busID)) {
            records.get(busID).add(useRec);
        } else {
            ArrayList<UseRecord> al = new ArrayList();
            al.add(useRec);
            records.put(busID, al);
        }
    }
    
    public static void addPoll () {
        totalPolls++;
    }
    
    public static void addUserLogin (String userID) {
        if (logins.containsKey(userID)){
            logins.put(userID, logins.get(userID) + 1);
        } else {
            logins.put(userID, 1);
        }
    }
    
    public static void exportStats () throws FileNotFoundException {
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new 
            File(cl.getResource("usage_stats.txt").getFile());
        PrintWriter out = new PrintWriter(f);
        out.println("Usage Statistics since " + initDateTime.toString());
        out.println("--------------------------------------------------------");
        out.println("Total Polls for Bus Data: " + totalPolls);
        out.println("--------------------------------------------------------");
        int totalLogins = 0;
        for (int i : UseStats.logins.values()) {
            totalLogins += i;
        }
        out.println("Total Logins: " + totalLogins);
        out.println("--------------------------------------------------------");
        for (String id : UseStats.logins.keySet()) {
            out.println(id + ": " + UseStats.logins.get(id) + " login(s)");
        }
        out.println("--------------------------------------------------------");
        for (ArrayList<UseRecord> a : records.values()) {
            out.println("BusID: " + a.get(0).busID);
            for (UseRecord r : a) {
                out.println("\tStopID: " + r.stopID + "\tUserID: " + r.userID +
                        "\tRecordTime: " + r.recordTime.toString());
            }
            out.println();
        }
        out.close();
    }
    
}

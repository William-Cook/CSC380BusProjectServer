/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 *
 * Responsible for responding to requests from client for: HashMap<String, Bus>
 *
 * Recieving from client: Usage Statistics
 *
 *
 * @author bill
 */
public class MyServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("loggedIn", "false");
        }
        PrintWriter out = response.getWriter();
        if (request.getParameter("RequestType").equals("init")
                && !Utility.initialized) {
            try {
                Utility.startupTasks();
                out.println("Servlet Successfully Initialized.");
            } catch (FileNotFoundException ex) {
                out.println("Unable to initialize. FileNotFound Exception.");
            } catch (ParseException ex) {
                out.println("Unable to initialize. Parse Exception.");
            } catch (org.json.simple.parser.ParseException ex) {
                out.println("Unable to initialize. Parse Exception.");
            }
        } else if (Utility.initialized) {
            if (request.getParameter("RequestType").equals("login")) {
                UseStats.addUserLogin(request.getParameter("UserID"));
                session.setAttribute("loggedIn", "true");
                out.println("Login Successful.");
            } else if (request.getParameter("RequestType").equals("getUseStats")) {
                UseStats.exportStats();
                ClassLoader cl = Utility.class.getClassLoader();
                File f = new File(cl.getResource("usage_stats.txt").getFile());
                Scanner fs = new Scanner(f);
                while (fs.hasNextLine()) {
                    out.println(fs.nextLine());
                }
            } else if (request.getParameter("RequestType").equals("getBusses")) {
                String query = request.getParameter("RouteID");
                synchronized (Utility.lock) {
                    Utility.encodeHashMap(out, query);
                }
                UseStats.addPoll();
            } else if (request.getParameter("RequestType").equals("useStats")) {
                UseStats.addUse(request.getParameter("BusID"),
                        request.getParameter("StopID"),
                        request.getParameter("UserID"),
                        LocalDateTime.parse(request.getParameter("RecordTime")));
            } else {
                out.println("Invalid Request Paremeters.");
            }
        } else {
            out.println("Servlet not initialized.");
        }
    }

    //no login check yet
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            session.setAttribute("loggedIn", "false");
        }
        PrintWriter out = response.getWriter();
        if (request.getParameter("RequestType").equals("init")
                && !Utility.initialized) {
            try {
                Utility.startupTasks();
                out.println("Servlet Successfully Initialized.");
            } catch (FileNotFoundException ex) {
                out.println("Unable to initialize. FileNotFound Exception.");
            } catch (ParseException ex) {
                out.println("Unable to initialize. Parse Exception.");
            } catch (org.json.simple.parser.ParseException ex) {
                out.println("Unable to initialize. Parse Exception.");
            }
        } else if (Utility.initialized) {
            if (request.getParameter("RequestType").equals("login")) {
                UseStats.addUserLogin(request.getParameter("UserID"));
                session.setAttribute("loggedIn", "true");
                out.println("Login Successful.");
            } else if (request.getParameter("RequestType").equals("getUseStats")) {
                UseStats.exportStats();
                ClassLoader cl = Utility.class.getClassLoader();
                File f = new File(cl.getResource("usage_stats.txt").getFile());
                Scanner fs = new Scanner(f);
                while (fs.hasNextLine()) {
                    out.println(fs.nextLine());
                }
            } else if (request.getParameter("RequestType").equals("getBusses")) {
                String query = request.getParameter("RouteID");
                Utility.encodeHashMap(out, query);
                UseStats.addPoll();
            } else if (request.getParameter("RequestType").equals("useStats")) {
                UseStats.addUse(request.getParameter("BusID"),
                        request.getParameter("StopID"),
                        request.getParameter("UserID"),
                        LocalDateTime.parse(request.getParameter("RecordTime")));
            } else {
                out.println("Invalid Request Paremeters.");
            }
        } else {
            out.println("Servlet not initialized.");
        }
    }

}

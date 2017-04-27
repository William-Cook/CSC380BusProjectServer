/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.awt.Mutex;

public final class Utility {

    private static Timer apiPoller;
    private static TimerTask apiPollerTask;
    public static int updateModelExecuteCount;
    public static HashMap<String, Bus> busses;
    public static HashMap<String, Stop> stops;
    public static HashMap<String, Trip> trips;
    public static HashMap<String, Shape> shapes;
    public static boolean initialized = false;
    public static final Mutex lock = new Mutex();

    private Utility() {
    }

    //encodes hashmap into printwriter provided
    public static void encodeHashMap(PrintWriter out, String query) {
        for (Bus b : busses.values()) {
            if (b != null && b.busID != null
                    && b.id != null && b.destinationName != null
                    && b.expectedArrivalTime != null
                    && b.id.equals(query)) {
                // Declare new bus
                out.println("NEWBUS");
                // Everything for bus constructor
                out.println(b.longitude + "," + b.lattitude
                        + "," + b.id + "," + b.destinationName + ","
                        + b.expectedArrivalTime + "," + b.expectedDepartureTime
                        + "," + b.direction + "," + b.busID);
                // Declare new route
                out.println("NEWROUTE");
                if (b.busRoute != null) {
                    // Each line contains everything for stop constructor
                    for (Stop s : b.busRoute) {
                        if (s.stop_id != null && s.stop_name != null) {
                            out.println(s.stop_id + "," + s.stop_name + "," + s.stop_lat
                                    + "," + s.stop_lon + "," + s.location_type);
                        }
                    }
                }
                // Declare route end
                out.println("ENDROUTE");
                if (b.route_shape != null && b.route_shape.shape_id != null) {
                    // Declare new shape
                    out.println("NEWSHAPE");
                    // shape_id for shape constructor
                    out.println(b.route_shape.shape_id);
                    // Declare points start
                    out.println("NEWPOINTS");
                    for (Point p : b.route_shape.points) {
                        // everything for point constructor
                        out.println(p.shape_id + "," + p.shape_pt_lat + ","
                                + p.shape_pt_lon + "," + p.shape_pt_sequence);
                    }
                    // Declare points end
                    out.println("ENDPOINTS");
                }
                // Declare bus end
                out.println("ENDBUS");
            }
        }

        out.close();
        return;
    }

    //decodes input to hashmap
    public static void decodeToHashMap(InputStream is) {
        HashMap<String, Bus> bussesLocal = new HashMap();
        Bus b = null;
        ArrayList<Stop> route = null;
        Stop s = null;
        Scanner iss;
        String buffer;
        String buffer_s[];
        iss = new Scanner(is);

        //decode
        while (iss.hasNextLine()) {
            buffer = iss.nextLine();
            if (buffer.equals("NEWBUS")) {
                buffer = iss.nextLine();
                buffer_s = buffer.split(",");
                b = new Bus(Float.parseFloat(buffer_s[0]),
                        Float.parseFloat(buffer_s[1]), buffer_s[2], buffer_s[3],
                        buffer_s[4], buffer_s[5], Integer.parseInt(buffer_s[6]));
                b.busID = buffer_s[7];
            } else if (buffer.equals("NEWROUTE")) {
                route = new ArrayList();
                while (iss.hasNext()) {
                    buffer = iss.nextLine();
                    if (buffer.equals("ENDROUTE")) {
                        b.busRoute = new Stop[route.size()];
                        route.toArray(b.busRoute);
                        break;
                    } else {
                        buffer_s = buffer.split(",");
                        route.add(new Stop(buffer_s[0], buffer_s[1],
                                Double.parseDouble(buffer_s[2]),
                                Double.parseDouble(buffer_s[3]),
                                Integer.parseInt(buffer_s[4])));
                    }
                }
            } else if (buffer.equals("NEWSHAPE")) {
                buffer = iss.nextLine();
                b.route_shape = new Shape(buffer);
            } else if (buffer.equals("NEWPOINTS")) {
                while (iss.hasNextLine()) {
                    buffer = iss.nextLine();
                    if (buffer.equals("ENDPOINTS")) {
                        break;
                    } else {
                        buffer_s = buffer.split(",");
                        b.route_shape.points.add(new Point(buffer_s[0],
                                Double.parseDouble(buffer_s[1]),
                                Double.parseDouble(buffer_s[2]),
                                Integer.parseInt(buffer_s[3])));
                    }
                }
            } else if (buffer.equals("ENDBUS")) {
                bussesLocal.put(b.busID, b);
            }
        }

        busses = bussesLocal;
        return;
    }

    //parse shapes file into HashMap<shape_id, Shape>
    public static void parseShapes(String fn) throws FileNotFoundException {
        shapes = new HashMap();
        Shape s = null;
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new File(cl.getResource(fn).getFile());
        Scanner fs = new Scanner(f);
        String buffer = fs.nextLine();

        while (fs.hasNextLine()) {
            buffer = fs.nextLine();
            String buffer_s[] = buffer.split(",");
            if (s != null && !buffer_s[0].equals(s.shape_id)) {
                shapes.put(s.shape_id, s);
            }
            if (s == null || !s.shape_id.equals(buffer_s[0])) {
                s = new Shape(buffer_s[0]);
            }
            Point p = new Point(buffer_s[0], Double.parseDouble(buffer_s[1]),
                    Double.parseDouble(buffer_s[2]), Integer.parseInt(buffer_s[3]));
            s.points.add(p);
            if (!fs.hasNextLine()) {
                shapes.put(s.shape_id, s);
            }
        }
        return;
    }

    // associate correct trip information with bus objects, return updated 
    // HashMap of Busses
    public static void assignTrips() {
        for (Bus b : busses.values()) {
            for (Trip t : trips.values()) {
                if (!b.id.contains("SBS")) {
                    // Regular bus
                    if (t.route_id.equals(b.id)
                            && t.direction_id == b.direction) {
                        Stop route[] = new Stop[t.route.size()];
                        for (int i = 0; i < route.length; i++) {
                            route[i] = stops.get(t.route.get(i));
                        }
                        b.busRoute = route;
                        b.route_shape = shapes.get(t.shape_id);
                    }
                } else // Select Bus Service
                 if (t.trip_headsign.equals(b.destinationName)
                            && t.direction_id == b.direction) {
                        Stop route[] = new Stop[t.route.size()];
                        for (int i = 0; i < route.length; i++) {
                            route[i] = stops.get(t.route.get(i));
                        }
                        b.busRoute = route;
                        b.route_shape = shapes.get(t.shape_id);
                    }
            }
        }

        return;
    }

    // parses trips in txt file and returns hashmap of <trip_id, Trip>
    public static void parseTrips(String fn) throws FileNotFoundException {
        trips = new HashMap();
        Trip t = null;
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new File(cl.getResource(fn).getFile());
        Scanner fs = new Scanner(f);
        String buffer = fs.nextLine();

        while (fs.hasNextLine()) {
            buffer = fs.nextLine();
            String buffer_s[] = buffer.split(",");
            t = new Trip(buffer_s[0], buffer_s[1], buffer_s[2], buffer_s[3],
                    Integer.parseInt(buffer_s[4]), buffer_s[5]);
            trips.put(t.trip_id, t);
        }

        return;
    }

    // parses Stops from txt file and returns HashMap<stop_id, Stop>
    public static void parseStops(String fn) throws FileNotFoundException {
        stops = new HashMap();
        Stop s = null;
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new File(cl.getResource(fn).getFile());
        Scanner fs = new Scanner(f);
        String buffer = fs.nextLine();

        while (fs.hasNextLine()) {
            buffer = fs.nextLine();
            String buffer_s[] = buffer.split(",");
            s = new Stop(buffer_s[0], buffer_s[1],
                    Double.parseDouble(buffer_s[3]),
                    Double.parseDouble(buffer_s[4]),
                    Integer.parseInt(buffer_s[7]));
            stops.put(s.stop_id, s);
        }

        return;
    }

    // parses Stop Times from txt file and returns HashMap<trip_id, Trip>
    public static void parseStopTimes(String fn) throws FileNotFoundException {
        ClassLoader cl = Utility.class.getClassLoader();
        File f = new File(cl.getResource(fn).getFile());
        Scanner fs = new Scanner(f);
        String buffer = fs.nextLine();

        while (fs.hasNextLine()) {
            buffer = fs.nextLine();
            String buffer_s[] = buffer.split(",");
            String trip_id = buffer_s[0];
            String stop_id = buffer_s[3];
            trips.get(trip_id).route.add(stop_id);
        }

        return;
    }

    // opens connection to MTA api
    public static HttpURLConnection openMTAApiConnection() throws MalformedURLException, IOException {
        //URL urlToGet = new URL("https://bustime.mta.info/api/siri/vehicle-monitoring.json?key=7a22c3e8-61a7-40ff-9d54-714e36f56880");
        URL urlToGet = new URL("http://api.prod.obanyc.com/api/siri/vehicle-monitoring.json?key=7a22c3e8-61a7-40ff-9d54-714e36f56880");

        return (HttpURLConnection) urlToGet.openConnection();
    }

    // downloads File and returns File Name
    public static String getFile(URLConnection conn, String saveLocation,
            String fileName, boolean https) throws MalformedURLException, IOException {
        int responseCode;
        if (https) {
            responseCode = ((HttpsURLConnection) conn).getResponseCode();
        } else {
            responseCode = ((HttpURLConnection) conn).getResponseCode();
        }
        if (responseCode == 200) {
            InputStream inputStream = conn.getInputStream();
            ClassLoader cl = Utility.class.getClassLoader();
            FileOutputStream outputStream = new 
                FileOutputStream(cl.getResource(fileName).getFile());
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        }
        if (https) {
            ((HttpsURLConnection) conn).disconnect();
        } else {
            ((HttpURLConnection) conn).disconnect();
        }

        return fileName;
    }

    // begins apiPoller task executed regularly to update vehicle-data
    public static void startupTasks() throws FileNotFoundException, IOException, 
            ParseException, org.json.simple.parser.ParseException {
        UseStats.initialize();
        parseTrips("trips.txt");
        parseStops("stops.txt");
        parseStopTimes("stop_times.txt");
        jsonParser("vehicle-monitoring.json");
        parseShapes("shapes.txt");
        assignTrips();
        apiPoller = new Timer();
        apiPollerTask = new UpdateModel();
        apiPoller.schedule(apiPollerTask, 0, 70000);
        initialized = true;
    }

    // stops the apiPoller task that is regularly executed
    public static void stopApiPoller() {
        apiPollerTask.cancel();
        apiPoller.cancel();
        initialized = false;
    }

    public static void jsonParser(String fn) throws IOException, ParseException, org.json.simple.parser.ParseException {
        busses = new HashMap<String, Bus>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM uu  hh:mm");

        JSONParser parser = new JSONParser();
        ClassLoader cl = Utility.class.getClassLoader();
        Object objfile = parser.parse(new FileReader(cl.getResource(fn).getFile()));//Add your file location before fn: Ex. "C://steve/"+fn

        JSONObject obj = (JSONObject) objfile;//START OBJECT

        JSONObject siri = (JSONObject) obj.get("Siri");//SIRI OBJECT
        JSONObject serviceDelivery = (JSONObject) siri.get("ServiceDelivery");//SERVICE DELIVERY OBJECT
        JSONArray vmd = (JSONArray) serviceDelivery.get("VehicleMonitoringDelivery");
        JSONObject vmdP = (JSONObject) vmd.get(0);//VEHICLE MONITORING DELIVERY OBJECT
        JSONArray va = (JSONArray) vmdP.get("VehicleActivity"); //VEHICLE ACTIVTY ARRAY

        for (int i = 0; i < va.size(); i++) {
            JSONObject mvj = (JSONObject) va.get(i);

            JSONObject mvjP = (JSONObject) mvj.get("MonitoredVehicleJourney");//MONITORED VEHICLE JOURNEY OBJECT

            JSONObject vl = (JSONObject) mvjP.get("VehicleLocation");//VEHICLE LOCATION OBJECT

            JSONObject mc = (JSONObject) mvjP.get("MonitoredCall");//MONITORED CALL OBJECT

            String busIDRoute = mvjP.get("PublishedLineName").toString();
            if (busIDRoute.charAt(0) == 'M') {
                int direction = Integer.parseInt(mvjP.get("DirectionRef").toString());
                String destinationName = mvjP.get("DestinationName").toString();
                float longitude = Float.parseFloat(vl.get("Longitude").toString());
                float latitude = Float.parseFloat(vl.get("Latitude").toString());
                String busID = mvjP.get("VehicleRef").toString();
                if (!mc.containsKey("ExpectedArrivalTime") || !mc.containsKey("ExpectedDepartureTime")) {
                    continue;
                }
                String expectedArrivalTime = formatter.format(ZonedDateTime.parse(mc.get("ExpectedArrivalTime").toString()));
                String expectedDepartureTime = formatter.format(ZonedDateTime.parse(mc.get("ExpectedDepartureTime").toString()));
                Bus newBus = new Bus(longitude, latitude, busIDRoute, destinationName, expectedArrivalTime, expectedDepartureTime, direction);
                newBus.busID = busID;
                busses.put(busID, newBus);

            }
        }

        return;

    }

}

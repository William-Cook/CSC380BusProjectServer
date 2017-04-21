package com.csc380.API;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;
import org.junit.Test;

public class BusTest {

    /* @Test
    public void getExpectedArrivalTimeTest() {
        Bus testBus = new Bus();
        Stop testStop = new Stop();

        testBus.destinationName = "TheFirstStop";
        testBus.expectedArrivalTime = LocalTime.now().plusMinutes(15);
        testBus.longitude = (float) -74.00;
        testBus.latitude = (float) 40.65;
        testBus.longitudeOfLastPolling = (float) -74.00;
        testBus.latitudeOfLastPolling = (float) 40.63;
        testBus.busRoute = new Stop[3];
        testBus.busRoute[0] = new Stop();
        testBus.busRoute[1] = new Stop();
        testBus.busRoute[2] = new Stop();

        testStop.stopName = "TheSecondStop";
        testStop.latitude = (float) 40.75;
        testStop.longitude = (float) -73.9;

        testBus.busRoute[0].latitude = (float) 40.7;
        testBus.busRoute[0].longitude = (float) -74.00;
        testBus.busRoute[0].stopName = "TheFirstStop";
        testBus.busRoute[1].latitude = testStop.latitude;
        testBus.busRoute[1].longitude = testStop.longitude;
        testBus.busRoute[1].stopName = testStop.stopName;
        testBus.busRoute[2].latitude = (float) 40.8;
        testBus.busRoute[2].longitude = (float) -73.8;
        testBus.busRoute[2].stopName = "TheThirdStop";

        LocalTime testTime = testBus.getExpectedArrivalTime(testStop);
        LocalTime expectedTime = LocalTime.now().plusMinutes((long) 11).plusSeconds((long) 40);
        assertEquals(expectedTime, testTime);
    }

    @Test
    public void getExpectedArrivalTimeWrongStopInputTest() throws IllegalArgumentException {
        Bus testBus = new Bus();
        Stop testStop = new Stop();

        testBus.destinationName = "TheFirstStop";
        testBus.busRoute = new Stop[3];
        testBus.busRoute[0] = new Stop();
        testBus.busRoute[1] = new Stop();
        testBus.busRoute[2] = new Stop();
        testBus.busRoute[0].stopName = "TheFirstStop";
        testBus.busRoute[1].stopName = "TheSecondStop";
        testBus.busRoute[2].stopName = "TheThirdStop";

        testStop.stopName = "NotInBusRoute";

        LocalTime testTime = testBus.getExpectedArrivalTime(testStop);
        assertNull(testTime);
    }

    // @author dt817
    @Test
    public void VehicleLocationTest() {
        Bus testBus = new Bus();
        Bus testBus2 = new Bus();
        HashMap<String, Bus> busses = new HashMap();
        testBus.destinationName = "TheFirstStop";
        testBus.longitude = (float) -74.00;
        testBus.lattitude = (float) 40.65;
        testBus2.longitude = (float) 23.54;
        testBus2.lattitude = (float) 50.00;
        busses.put("M4-32", testBus);
        busses.put("M2-56", testBus2);

        VehicleLocation testLocation = new VehicleLocation(busses);
        testLocation.getVehicleLocation();
    }*/
}

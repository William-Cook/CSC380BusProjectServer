package com.csc380.API;

import java.time.LocalDateTime;

public class Bus {

    public float longitude, lattitude;
    public String id, destinationName;
    public String expectedArrivalTime, expectedDepartureTime;
    public int direction;
    public Stop busRoute[];
    public Shape route_shape;
    public String busID;
    
    public Bus(){};

    public Bus(float lo, float la, String i, String DN, String ex, String t, int d) {
        longitude = lo;
        lattitude = la;
        id = i;
        destinationName = DN;
        expectedArrivalTime = ex;
        expectedDepartureTime = t;
        direction = d;
    }

    /*public LocalTime getExpectedArrivalTime(Stop s) {
		LocalTime estimatedArrivalTime = null;
		double distancesBetweenStops[] = new double[busRoute.length];
		int stopIndex = -1;
		int nextStopIndex = -1;
		
		//Returns the MTA provided arrival time if s is the next stop.
		if (s.stopName.compareTo(destinationName) == 1) {
			estimatedArrivalTime = expectedArrivalTime;

		//Case where s is not the next stop. 
		} else if (s.stopName.compareTo(destinationName) != 1) {
			
			//Find the index of stop, s, in the busRoute array.  
			for (int j = 0; j < busRoute.length; j++) {
				if (s.stopName.compareTo(busRoute[j].stopName) == 0) {
					stopIndex = j;
					break;
				}
			}
			
			//Make sure the stop, s, is contained in the busRoute. 
			if (stopIndex >= 0) {
				
				//Converting latitude-longitude pairs to easting-northing pairs and find distances between stops
				//contained in busRoute.
				for (int i = 0; i < busRoute.length-1; i++) {
					LatLng latLngBusRoute2 = new LatLng(busRoute[i + 1].latitude, busRoute[i + 1].longitude);
					LatLng latLngBusRoute1 = new LatLng(busRoute[i].latitude, busRoute[i].longitude);
					distancesBetweenStops[i] = latLngBusRoute2.distance(latLngBusRoute1);
				}
				
				//Find the index of the next bus stop in the array busRoute. 
				for (int j = 0; j <= busRoute.length; j++) {
					if (destinationName.compareTo(busRoute[j].stopName) == 0) {
						nextStopIndex = j;
						break;
					}
				}
				
				//Converting lat-lon to easting-northing and find distance between the bus and the next stop.
				LatLng latLngNextStop = new LatLng(busRoute[nextStopIndex].latitude, busRoute[nextStopIndex].longitude);
				LatLng latLngBus = new LatLng(latitude, longitude);
				LatLng latLngBusLastPolling = new LatLng(latitudeOfLastPolling, longitudeOfLastPolling);

				double distanceFromBusToFirstStop = latLngBus.distance(latLngNextStop);
				
				//The bus speed in meters/minute.
				double averageBusVelocity = latLngBus.distance(latLngBusLastPolling);
								
				//Sum distances along the bus route from the bus to stop, s. 
				double totalDistanceToStop = distanceFromBusToFirstStop;
				for (int i = nextStopIndex; i <= stopIndex; i++) {
					totalDistanceToStop = totalDistanceToStop + distancesBetweenStops[i];
				}
				
				totalDistanceToStop = Math.round(totalDistanceToStop * 10.0)/10.0;
				averageBusVelocity = Math.round(averageBusVelocity * 10.0)/10.0;
				
				double timeToAdd = (double) Math.round(100 * totalDistanceToStop/averageBusVelocity)/100;
				String timeToAddString = Double.toString(timeToAdd);
				int timeToAddMinutes = Integer.parseInt(timeToAddString.split("\\.")[0]);
				int timeToAddSeconds = Integer.parseInt(timeToAddString.split("\\.")[1]);
				timeToAddSeconds = timeToAddSeconds * 60/100;
				
				estimatedArrivalTime = LocalTime.now().plusMinutes(timeToAddMinutes).plusSeconds(timeToAddSeconds);
			} 
		}
		return estimatedArrivalTime;
	}*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csc380.API;

import java.time.LocalDateTime;

/**
 *
 * @author bill
 */
public class UseRecord {
    
    String busID, stopID, userID;
    LocalDateTime recordTime;
    
    public UseRecord (String busID, String stopID, String userID,
            LocalDateTime recordTime) {
        this.busID = busID;
        this.stopID = stopID;
        this.userID = userID;
        this.recordTime = recordTime;
    }
    
}

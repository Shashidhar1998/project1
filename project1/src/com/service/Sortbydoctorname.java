package com.service;

import java.util.Comparator;

import com.pojo.patientBookingDetails;
import com.pojo.weeklySchedule;

public class Sortbydoctorname implements Comparator<weeklySchedule> {
	public int compare(weeklySchedule a, weeklySchedule b) 
    { 
        return a.getDoctorname().compareTo(b.getDoctorname()); 
    } 
}
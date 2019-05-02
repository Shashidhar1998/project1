package com.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.service.AuthService;
//import com.pojo.addhospitalmodel;
 
//import com.pojo.addhospitalmodel;
//import com.service.RegistrationService;
import com.pojo.User;
import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.loginmodel;
import com.pojo.patientBookingDetails;
import com.pojo.reportData;
import com.service.AppointmentService;

@Component
@Controller
@RequestMapping("/appointment")

public class AppointmentController {
	@Autowired
	AppointmentService appointmentService;
	  /*@RequestMapping(value = "/redirecttoslotselector", method = RequestMethod.POST)
		 public ModelAndView redirecttoregisteruser(ModelAndView model, @ModelAttribute Appointment appointment)
		 {
		
		  	System.out.println(appointment.getDate_selected().toString());
			model.addObject("appointment", appointment);
			model.setViewName("slotselector");
			return model;

		     
		 }
		 */
	  
	  @RequestMapping(value = "/bookAppointment", method = RequestMethod.POST)
		 public String saveAppointment(@RequestParam("slots")String slots, HttpSession session,HttpServletRequest request , HttpServletResponse response)
		 {
		
		 // date.replaceAll ("\\&#8206;","");
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		  String date = (String) session.getAttribute("AppointmentDate");
		  
		  System.out.println("date:"+ date + " " + "slots"+ slots);
		  System.out.println("In the bookAppointment Controller");
		    session = request.getSession();
			String patientname = (String) session.getAttribute("name");
			String user = (String) session.getAttribute("user");
		  appointmentService.saveAppointment(session, slots,patientname,user);
		  JOptionPane.showMessageDialog(null, "Booking Confirmed!");
		  	return "userdash";
			}
			else
			{
				return "index";
			}

		     
		 }
	  
	  
	  @RequestMapping(value = "/AppointmentBooking", method = RequestMethod.GET)
		 public String AppointmentBooking( HttpSession session, HttpServletResponse response)
		 {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		 
		 return "appoint";
			}
			else
			{
				return "index";
			}
		 }
	  
	  @RequestMapping(value = "/OneStopWellness", method = RequestMethod.GET)
		 public String Dashboard( HttpSession session, HttpServletResponse response)
		 {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		
		 return "userdash";
			}
			else
			{
				
				return "index";
			}
		 }
		 
	  @RequestMapping(value = "/reports", method = RequestMethod.POST)
		 public ModelAndView report( HttpSession session, HttpServletResponse response)
		 {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		  ModelAndView mv = new ModelAndView();
          mv.setViewName("report");
          loginmodel display1 = new loginmodel();
          display1.setUserID("Vineetha");
          display1.setEmail("VineethaMuralidhar");
          mv.addObject("display","VineethaMuralidhar");
          mv.addObject("display2",display1);
          return mv;
			}
			else
			{
				ModelAndView mv = new ModelAndView();
				mv.setViewName("index");
				return mv;
			}
		 }
	
	
	  @RequestMapping(value = "/Hosloc", method = RequestMethod.GET)
		 public ModelAndView hosloc()
		 {
	      ModelAndView mv = new ModelAndView();
          mv.setViewName("Hosloc");
          return mv;
		 }
	  
	  @RequestMapping(value = "/DownloadReport", method = RequestMethod.GET)
	    public ModelAndView validateUsr( HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
	     String userId = (String) session.getAttribute("user");
         System.out.println("User Name using Session" + userId);
		  List<patientBookingDetails> PatientList =  appointmentService.getPatientList(userId);

		  List<reportData> report =  new ArrayList<reportData>();
		  reportData reportObj = new reportData();
		  for(patientBookingDetails obj : PatientList)
		  {		
			  reportObj = new reportData();
			  reportObj.setId(obj.getId());
			  reportObj.setHospital_id(obj.getHospital_id());
			  reportObj.setDoctorid(obj.getDoctorid());
			  reportObj.setUserID(obj.getUserID());
			  reportObj.setPatientname(obj.getPatientname());
			  reportObj.setDisease(obj.getDisease());
			  reportObj.setPrescriptionprovided(obj.getPrescriptionprovided());
			  reportObj.setSlots(obj.getSlots());
			  reportObj.setDate(obj.getDate());
			  System.out.println(" Hospital Id" + obj.getHospital_id());
			  
			  adddoc docdetails = appointmentService.getDoctorDetails(obj.getHospital_id(),obj.getDoctorid());
			  reportObj.setDoctor_name(docdetails.getDoctorname());
			  reportObj.setDoctor_qualification(docdetails.getQualification());
			  reportObj.setDoctor_speciality(docdetails.getSpeciality());
			  reportObj.setDoctor_email(docdetails.getEmail());
			  reportObj.setDoctor_mobileno(docdetails.getMobileno());
			  
			  System.out.println(" Doctor Id"+ docdetails.getDoctorid());
			  System.out.println(" Doctor Name"+ docdetails.getDoctorname());
			  addhospitalmodel hospitaldetails = appointmentService.gethospitaldetails(obj.getHospital_id());
			  reportObj.setHosiptal_name(hospitaldetails.getName());
			  reportObj.setHosiptal_address1(hospitaldetails.getAddress1());
			  reportObj.setHosiptal_address2(hospitaldetails.getAddress2());
			  reportObj.setHosiptal_state(hospitaldetails.getState());
			  reportObj.setHosiptal_city(hospitaldetails.getCity());
			  reportObj.setHosiptal_pin(hospitaldetails.getPin());
			  report.add(reportObj);
			 
			  System.out.println(" patient Id"+ obj.getUserID());
			  
		  }
		 for(reportData obj : report)
		 {
			 System.out.println(" patient Id"+ obj.getUserID());
			 System.out.println(" patient User"+ obj.getPatientname());
			 System.out.println(" Hospital Name"+ obj.getHosiptal_name());
			 System.out.println(" Doctor Name "+ obj.getDoctor_name());
			 System.out.println(" Date"+ obj.getDate());
			 System.out.println(" Slot"+ obj.getSlots());
			 System.out.println("\n\n\n ");
		 }
		 System.out.println("Size of the final report:"+report.size());
		 ModelAndView mv = new ModelAndView();
		 mv.setViewName("report");
		 mv.addObject("report",report);
		  return mv;
			}
			else
			{
				ModelAndView mv = new ModelAndView();
				mv.setViewName("index");
				return mv;
			}
	       }
	

}

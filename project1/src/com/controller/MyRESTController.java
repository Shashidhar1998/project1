package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.patientBookingDetails;
import com.service.AppointmentService;
import com.service.AuthService;

@Controller
@RequestMapping(value = "/MyData")



public class MyRESTController {
	
	
	 @Autowired
	    private AppointmentService appointmentService; 
	 
		@RequestMapping(value="/searchDoctor", method = RequestMethod.GET)
	    public @ResponseBody List<adddoc> getMySlot(@RequestParam String name,HttpSession session){

	             
	    System.out.println("In the controller...searchDoctor");

	    String hospitalID = (String) session.getAttribute("HospitalID");
	    System.out.println("Hospital ID in the session "+hospitalID);

	    List<adddoc> doctorList = appointmentService.searchdoctors(hospitalID, name);

	         return doctorList;

	    }

	
	@RequestMapping(value="/slotselect", method = RequestMethod.POST)
    //public @ResponseBody String getMyDoctors(@RequestParam String hos,@RequestParam String doc, @RequestParam String date)
    public @ResponseBody List<String> getMySlot(@RequestParam String hos,@RequestParam String doc, @RequestParam String date,HttpSession session)

             {
    System.out.println("In the controller.../time..POSTfinalllyyyyy");
    

    
    String hospitalID = (String) session.getAttribute("HospitalID");
    String doctorID = (String) session.getAttribute("DoctorID");
    
    
    
    System.out.println("Hospital ID in the session "+hospitalID);
    System.out.println("Doctor ID in the session "+doctorID);
    
    date.replaceAll("\\?", "");
    session.setAttribute("AppointmentDate", date);
    
    System.out.println("Entered date is "+date);
    List<String> slotList = appointmentService.getSlots(hospitalID, doctorID, date);
    
           
         return slotList;
    
    	
       // return new MyData(1234, "REST GET Call !!!");
    }
	
	

	
	@RequestMapping(value="/doctorSelection", method = RequestMethod.POST)
    public @ResponseBody List<adddoc> getDoctorDetails(@RequestParam String hosname,@RequestParam String locality, @RequestParam String city,HttpSession session) {
		
				
				
				List<adddoc> DoctorDataList = appointmentService.getDoctors(hosname,locality,city);
				
				if (DoctorDataList.size() > 0)
				{
				            String hospitalID = DoctorDataList.get(0).getHospitalid();
				            System.out.println("Hospital id to be stored in the session.."+hospitalID);
				            session.setAttribute("HospitalID", hospitalID);
							System.out.println("its in rowselection controller");
							return DoctorDataList;
				}
				else
				{
					DoctorDataList = null;
					return DoctorDataList;
				}
			
							
							
							
	}
	
	
	@RequestMapping(value="/doctorId", method = RequestMethod.POST)
    public @ResponseBody void  setDoctorId(@RequestParam String doctorid,@RequestParam String doctorname, HttpSession session) {
		
		 session.setAttribute("DoctorID", doctorid);
		 session.setAttribute("DoctorName", doctorname);
		 
			System.out.println("its in rowselection controller");	
			System.out.println("Doctor name set in session "+doctorname);
			String DoctorID = (String) session.getAttribute("DoctorID");
			System.out.println(DoctorID);
			
	 						
	}
	
	
	@RequestMapping(value="/hospitalNameSelection", method = RequestMethod.GET)
    public @ResponseBody List<addhospitalmodel> getMyDatanew(@RequestParam String name)
             {
    System.out.println("In the controller.../hospitalNameSelection..GET");
 
    List<addhospitalmodel> HospitalDataList = appointmentService.getHospitalName(name);
         
          
         return HospitalDataList;

    }
    


}
package com.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pojo.DoctorSchedule;
import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.feedback;
import com.pojo.patientBookingDetails;
import com.pojo.testModel;
import com.pojo.weeklySchedule;
import com.service.AuthService;
import com.service.HospitalService;

@Component
@Controller
@RequestMapping("/hospital")
public class HospitalAdminController {

	 @Autowired
	 private AuthService authenticateService1;
	 
	 @Autowired
	 private HospitalService hospitalService;
	
	 @RequestMapping(value = "/adddoc")  
    public ModelAndView display(ModelAndView model)  
    {  
		 adddoc userObj = new adddoc();
		 ModelAndView mv = new ModelAndView();
         mv.setViewName("addDoc");
         mv.addObject("userObj",userObj); 
        return mv;  
    }
	@RequestMapping(value = "/adddoctor")  
    public String addDoctor(@ModelAttribute adddoc userObj,HttpServletRequest request,HttpServletResponse response,HttpSession session)  
    {  
		 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		 System.out.println(userObj.getDoctorid());
		 System.out.println(userObj.getDoctorname());
		 System.out.println(userObj.getEmail());
		 System.out.println(userObj.getHospitalid());
		 System.out.println(userObj.getGender());
		  session = request.getSession();
		 String Hospital_unique_id = (String) session.getAttribute("hospitaluniqueid");
		 userObj.setHospitalid(Hospital_unique_id);
		 Random rnd = new Random();
		 int number = rnd.nextInt(999999);
		 String id = Integer.toString(number);
		 userObj.setDoctorid(id);
		 boolean temp = hospitalService.addDoctortodb(userObj);
		 
		 if(temp)
		 {
			 JOptionPane.showMessageDialog(null, "Database Saved Successfully");
		 }
		 else
		 {
			 JOptionPane.showMessageDialog(null, "Database Not Saved");
		 }
		 return "redirect:adddoc";
			}
			else
			{
				return "index";
			}
	 
	 
    }
	
	@RequestMapping(value = "/today")  
    public ModelAndView todaySchedule(HttpServletRequest request,ModelAndView model,HttpServletResponse response,HttpSession session)  
    {  
		
		 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		
		                  List<patientBookingDetails> patient_list = new ArrayList<patientBookingDetails>();
		                  session = request.getSession();
		                  String hospitaluniqueidinmap = (String) session.getAttribute("hospitaluniqueid");
		                  System.out.println("Session value in today Hospital Unique :  " + hospitaluniqueidinmap);
		
		                  patient_list = hospitalService.displayPatientsForIndividualDoctor(hospitaluniqueidinmap);
		
		                         if(patient_list == null)
		                                {
			                              ModelAndView mv = new ModelAndView();
	                                      mv.setViewName("TSchedule");
	                                      System.out.println("Before going to the view TodaySchedule.... when patients list is null");
	                                      String list = "Zero";
	                                      mv.addObject("Schedule",list);
	        
	                                              return mv;
		                                  }
		
		else
		{
		List<DoctorSchedule> doclist = new ArrayList<DoctorSchedule>();
		
		// create an object of doctorlist to store doctorname and slots,patients
		DoctorSchedule docObj = null;
		
		String olddoc = "abcfgh";
		String newdoc = null;
		
		boolean firstTime = true;
		for(patientBookingDetails obj : patient_list)
		{
			//System.out.println("Id :" + obj.getId());
			//System.out.println("Doctor Id :" + obj.getDoctorid() );
			//System.out.println("Doctor Name :" + obj.getDoctor_name());
			//System.out.println("Patient Name:" + obj.getPatientname());
			//System.out.println("Date :" +obj.getDate() );
			//System.out.println("Slots :" + obj.getSlots());
			
			if(!olddoc.equals(obj.getDoctor_name()) )
			{
				System.out.println("NotEquals");
				
				if(firstTime == false)
				{					//add to list
				    
					doclist.add(docObj);
				   
				}
				firstTime = false; 
				docObj = new DoctorSchedule(); 
				
				// store doctorname and slots with patient in hashmap
				docObj.setDocname(obj.getDoctor_name());
				 
				docObj.docSlot.put(obj.getSlots(),obj.getPatientname());
			}
			else{
				
				docObj.docSlot.put(obj.getSlots(),obj.getPatientname());				
				System.out.println("Equals");
			  }
			
			olddoc=obj.getDoctor_name();
			
			
		}
		
		
		doclist.add(docObj);
		System.out.println("Doctor Name      Slot");
		List<DoctorSchedule> doclist1 = new ArrayList<DoctorSchedule>();
		for(DoctorSchedule obj1 : doclist )
		{
			System.out.println(obj1.getDocname()+"           "  + obj1.getDocSlot().values());
			DoctorSchedule docObj1 = docObj = new DoctorSchedule();
			docObj1.setDocname(obj1.getDocname());
			
			if(obj1.getDocSlot().containsKey("09-10"))
			{
				System.out.println("Value 09-10 : " + obj1.getDocSlot().get("09-10"));
				docObj1.docSlot.put("1",obj1.getDocSlot().get("09-10"));
			}
			else
			{
				System.out.println("Value 09-10 : free");
				docObj1.docSlot.put("1","Available");
			}
			
			if(obj1.getDocSlot().containsKey("10-11"))
			{
				System.out.println("Value 10-11 : " + obj1.getDocSlot().get("10-11"));
				docObj1.docSlot.put("2",obj1.getDocSlot().get("10-11"));
			}
			else
			{
				System.out.println("Value 10-11 : free");
				docObj1.docSlot.put("2","Available");
			}
			
			if(obj1.getDocSlot().containsKey("11-12"))
			{
				System.out.println("Value 11-12 : " + obj1.getDocSlot().get("11-12"));
				docObj1.docSlot.put("3",obj1.getDocSlot().get("11-12"));
			}
			else
			{
				System.out.println("Value 11-12 : free");
				docObj1.docSlot.put("3","Available");
			}
			
			if(obj1.getDocSlot().containsKey("12-01"))
			{
				System.out.println("Value 12-01 : " + obj1.getDocSlot().get("12-01"));
				docObj1.docSlot.put("4",obj1.getDocSlot().get("12-01"));
			}
			else
			{
				System.out.println("Value 12-01 : free");
				docObj1.docSlot.put("4","Available");
			}
		
			if(obj1.getDocSlot().containsKey("01-02"))
			{
				System.out.println("Value 01-02 : " + obj1.getDocSlot().get("01-02"));
				docObj1.docSlot.put("5",obj1.getDocSlot().get("01-02"));
			}
			else
			{
				System.out.println("Value 01-02 : free");
				docObj1.docSlot.put("5","Lunch");
			}
			
			if(obj1.getDocSlot().containsKey("02-03"))
			{
				System.out.println("Value 02-03 : " + obj1.getDocSlot().get("02-03"));
				docObj1.docSlot.put("6",obj1.getDocSlot().get("02-03"));
			}
			else
			{
				System.out.println("Value 02-03 : free");
				docObj1.docSlot.put("6","Available");
			}
			
			if(obj1.getDocSlot().containsKey("03-04"))
			{
				System.out.println("Value 03-04 : " + obj1.getDocSlot().get("03-04"));
				docObj1.docSlot.put("7",obj1.getDocSlot().get("03-04"));
			}
			else
			{
				System.out.println("Value 03-04 : free");
				docObj1.docSlot.put("7","Available");
			}
			
			if(obj1.getDocSlot().containsKey("04-05"))
			{
				System.out.println("Value 04-05 : " + obj1.getDocSlot().get("04-05"));
				docObj1.docSlot.put("8",obj1.getDocSlot().get("04-05"));
			}
			else
			{
				System.out.println("Value 04-05 : free");
				docObj1.docSlot.put("8","Available");
			}
			if(obj1.getDocSlot().containsKey("05-06"))
			{
				System.out.println("Value 05-06 : " + obj1.getDocSlot().get("05-06"));
				docObj1.docSlot.put("9",obj1.getDocSlot().get("05-06"));
			}
			else
			{
				System.out.println("Value 05-06 : free");
				docObj1.docSlot.put("9","Available");
			}
			
			doclist1.add(docObj1);
			docObj1=null;
			//System.out.println(obj1.getDocSlot().keySet());
			//System.out.println(obj1.getDocSlot().values());
		}
		
		
		ModelAndView mv = new ModelAndView();
		//HttpSession session = request.getSession();
		//session.setAttribute("doclist", doclist);
        mv.setViewName("TSchedule");
        mv.addObject("doclist1",doclist1);
        System.out.println("Before going to the view PList.jsp....");
        
      
      return mv;
      
		}
			}
			else
			{
				ModelAndView mv = new ModelAndView();
				//HttpSession session = request.getSession();
				//session.setAttribute("doclist", doclist);
		        mv.setViewName("index");
		        return mv;
			}
			
	 
    }
	

	
	@RequestMapping(value = "/weekly")   
    public ModelAndView weeklySchedule(HttpServletRequest request,ModelAndView model,HttpServletResponse response,HttpSession session)  
    {  
		
		 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		
		 session = request.getSession();
		String hospitaluniqueidinmap = (String) session.getAttribute("hospitaluniqueid");
		System.out.println("Session value in today Hospital Unique :  " + hospitaluniqueidinmap);  
		List<weeklySchedule> final_weekly_list = new ArrayList<weeklySchedule>(); 
		
		for(int i=1; i<8; i++)
		{
			
		String day = hospitalService.dateforspecificdays(i);
		//System.out.println("date in controller: "+day);
		List<weeklySchedule> weekly_list = new ArrayList<weeklySchedule>();
		weekly_list = hospitalService.displayWeeklyScheduleForIndividualDoctor(hospitaluniqueidinmap,day);
		
		
		System.out.println("Size of Weekly List :"+ weekly_list.size());
		if(weekly_list!=null)
		{
		final_weekly_list.addAll(weekly_list);
		
		}
	
		}
		

		
		System.out.println("Size of final list :" +final_weekly_list.size());
		if(final_weekly_list.size() == 0)
		{
			ModelAndView mv = new ModelAndView();
	          mv.setViewName("WSchedule");
	          System.out.println("Before going to the view TodaySchedule.... when patients list is null");
	          String list = "Zero";
	          mv.addObject("Schedule",list);
	        
	        return mv;
		}
		else
		{
		List<weeklySchedule> final_weekly_listwithorder = new ArrayList<weeklySchedule>();
		final_weekly_listwithorder = hospitalService.weeklyScheduleToOrder(final_weekly_list);
		// Collections.sort(final_weekly_list, new Sortbyroll());
		
		
		for (weeklySchedule obj : final_weekly_listwithorder)
        {
   		 
          System.out.println("Doctor ID :" + obj.getDoctorname() +  "     Date : " + obj.getDate() + "      Slots : " + obj.getCountOfSlots());
        
        }
		List<DoctorSchedule> doclist = new ArrayList<DoctorSchedule>();
		
		// create an object of doctorlist to store doctorname and slots,patients
		DoctorSchedule docObj = null;
		
		String olddoc = "abcfgh";
		String newdoc = null;
		
		boolean firstTime = true;
		for(weeklySchedule obj : final_weekly_listwithorder)
		{
			//System.out.println("Id :" + obj.getId());
			//System.out.println("Doctor Id :" + obj.getDoctorid() );
			//System.out.println("Doctor Name :" + obj.getDoctor_name());
			//System.out.println("Patient Name:" + obj.getPatientname());
			//System.out.println("Date :" +obj.getDate() );
			//System.out.println("Slots :" + obj.getSlots());
			
			if(!olddoc.equals(obj.getDoctorname()) )
			{
				System.out.println("NotEquals");
				
				if(firstTime == false)
				{					//add to list
				    
					doclist.add(docObj);
				   
				}
				firstTime = false; 
				docObj = new DoctorSchedule(); 
				
				// store doctorname and slots with patient in hashmap
				docObj.setDocname(obj.getDoctorname());
				 
				docObj.docSlot.put(obj.getDate(),obj.getCountOfSlots());
			}
			else{
				
				docObj.docSlot.put(obj.getDate(),obj.getCountOfSlots());				
				System.out.println("Equals");
			  }
			
			olddoc=obj.getDoctorname();
			
			
		}
		
		
		doclist.add(docObj);
		List<DoctorSchedule> doclist1 = new ArrayList<DoctorSchedule>();
		List<String> date = new ArrayList<String>();
	       for(int i=1; i<8; i++)
				  { 
		    	   String day = hospitalService.dateforspecificdays(i);
					date.add(day);
				  }
	       
		for(DoctorSchedule obj1 : doclist )
		{
		//System.out.println(obj1.getDocname()+"           "  + obj1.getDocSlot().values());
	       DoctorSchedule docObj1 = new DoctorSchedule();
	       docObj1.setDocname(obj1.getDocname());
	
		for(int i=1; i<8; i++)
		  {
			String day = hospitalService.dateforspecificdays(i);
			
			if(obj1.docSlot.containsKey(day))
			{
				//System.out.println("Value 04-05 : " + obj1.getDocSlot().get(day));
				docObj1.docSlot.put(day,obj1.getDocSlot().get(day));
			}
			else
			{
				//System.out.println("Value 04-05 : free");
				docObj1.docSlot.put(day,"No Slots");
			}
			
		  }
		doclist1.add(docObj1);
		 docObj1=null;
		  }
		
		ModelAndView mv = new ModelAndView();
		//HttpSession session = request.getSession();
		//session.setAttribute("doclist", doclist);
        mv.setViewName("WSchedule");
        mv.addObject("doclist1",doclist1);
        mv.addObject("date",date);
		return mv;
		}
	 
    }
    
	else
	{
		ModelAndView mv = new ModelAndView();
		//HttpSession session = request.getSession();
		//session.setAttribute("doclist", doclist);
        mv.setViewName("index");
        return mv;
	}
    }
	
 
	/*@RequestMapping(value = "/weekly")   
    public ModelAndView display3(HttpServletRequest request,ModelAndView model)  
    {
		List<adddoc> doctor_list = new ArrayList<adddoc>();
		HttpSession session = request.getSession();
	    String hospitaluniqueidinmap = (String) session.getAttribute("hospitaluniqueid");
	    doctor_list = hospitalService.getdoctorsbyid(hospitaluniqueidinmap);
	    
	    for(int i=0; i<doctor_list.size(); i++)
	    {
	    	for(int j =1; j<8; j++)
	    	{
	    		
	    	}
	    }
		return null;
    }
    */
	  @RequestMapping(value = "/plist")  
	  public ModelAndView displaypatient(HttpServletRequest request,ModelAndView model,HttpServletResponse response,HttpSession session)
	  {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
			  List<patientBookingDetails> list=new ArrayList<patientBookingDetails>(); 
		         //List<testModel>PatientList =  authenticateService1.test2();
				 session = request.getSession();
				 
			     String hospitaluniqueidinmap = (String) session.getAttribute("hospitaluniqueid");
			     System.out.println("Value of session in Patients List"+hospitaluniqueidinmap);
				 list = hospitalService.listpatients(hospitaluniqueidinmap);
			     
				 
				 if(list == null)
					{
						 ModelAndView mv = new ModelAndView();
						  mv.setViewName("PList");
				          System.out.println("Before going to the view patients List.... when patients list is null");
				          String list1 = "Zero";
				          mv.addObject("Schedule",list1);
				        
				        return mv;
					}
				 
				 
				for (patientBookingDetails obj : list)
		        {
		   		  System.out.println("Patient Id :" + obj.getUserID());
		          System.out.println("Doctor ID :" + obj.getDoctorid());
		          System.out.println("Disease :" + obj.getDisease());
		          System.out.println("Status :" + obj.getStatus());
		          System.out.println("Prescription Provided :" + obj.getPrescriptionprovided());
		     
		        }
		          ModelAndView mv = new ModelAndView();
		          mv.setViewName("PList");
		          System.out.println("Before going to the view PList.jsp....");
		          mv.addObject("list2",list);
		        
		        return mv;
			}
		    
			else
			{
				ModelAndView mv = new ModelAndView();
				//HttpSession session = request.getSession();
				//session.setAttribute("doclist", doclist);
		        mv.setViewName("index");
		        return mv;
			}
        
		 
		
	  }
	  
	  @RequestMapping("/send_feedback")  
	    public ModelAndView feedbacks(HttpServletRequest request,HttpServletResponse response,HttpSession session)  
	    {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		  //System.out.println("Display Feedback page");
		  ModelAndView mv = new ModelAndView();
           mv.setViewName("feedbackform");
       
        return mv;
			}
		    
			else
			{
				ModelAndView mv = new ModelAndView();
				//HttpSession session = request.getSession();
				//session.setAttribute("doclist", doclist);
		        mv.setViewName("index");
		        return mv;
			}
	    }
	  
	  
	  
	  @RequestMapping(value = "/feedback_sent", method = RequestMethod.POST)
	    public ModelAndView feedbacksent(@RequestParam("name")String name,@RequestParam("email")String email,@RequestParam("subject")String subject,@RequestParam("message")String message,HttpServletRequest request,HttpServletResponse response,HttpSession session)  
	    {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
		  //System.out.println("Display Feedback page");
		  System.out.println("Feedback Sent");
		 // System.out.println("Feedback Sent Name :" + name);
		 // System.out.println("Feedback Sent Email :" + email);
		 // System.out.println("Feedback Sent Subject :" + subject);
		 // System.out.println("Feedback Sent Message :" + message);
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();
		   String feeddate = dtf.format(now).toString();
		   String readstatus = "Unread";
		   feedback feedbackobj = new feedback();
		   feedbackobj.setName(name);
		   feedbackobj.setEmail(email);
		   feedbackobj.setSubject(subject);
		   feedbackobj.setMessage(message);
		   feedbackobj.setDateandtime(feeddate);
		   feedbackobj.setReadstatus(readstatus);
		   hospitalService.addFeedbackToDatabase(feedbackobj);
		   ModelAndView mv = new ModelAndView();
           mv.setViewName("feedbackform");
       
        return mv;
	    }
	    
		else
		{
			ModelAndView mv = new ModelAndView();
			//HttpSession session = request.getSession();
			//session.setAttribute("doclist", doclist);
	        mv.setViewName("index");
	        return mv;
		}
	    }
	    
	  
	  @RequestMapping(value = "/UpdateDetails/{id}", method = RequestMethod.GET)  
      public ModelAndView UpdateDetails(@PathVariable("id") int id,HttpServletRequest request,HttpServletResponse response,HttpSession session)  
      {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
          //System.out.println("Display Feedback page");
          System.out.println(id);
          patientBookingDetails patientUpdate = new patientBookingDetails();
          patientUpdate =  hospitalService.patientDetails(id);;
          ModelAndView mv = new ModelAndView();
          
       mv.setViewName("update");
       mv.addObject("demo11",patientUpdate);
       return mv;
      }
	    
		else
		{
			ModelAndView mv = new ModelAndView();
			//HttpSession session = request.getSession();
			//session.setAttribute("doclist", doclist);
	        mv.setViewName("index");
	        return mv;
		}
      }
	  
	  
	  @RequestMapping(value = "/MoreDetails/{id}", method = RequestMethod.GET)  
	  
      public ModelAndView MoreDetails(@PathVariable("id") int id,HttpServletRequest request,HttpServletResponse response,HttpSession session)  
      {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
          //System.out.println("Display Feedback page");
          System.out.println(id);
          patientBookingDetails patientUpdate = new patientBookingDetails();
          patientUpdate =  hospitalService.patientDetails(id);;
          ModelAndView mv = new ModelAndView();
          
       mv.setViewName("patientdetails");
       mv.addObject("demo11",patientUpdate);
       return mv;
      }
	    
		else
		{
			ModelAndView mv = new ModelAndView();
			//HttpSession session = request.getSession();
			//session.setAttribute("doclist", doclist);
	        mv.setViewName("index");
	        return mv;
		}
      }
    
    @RequestMapping(value = "/save_updated_details/{id1}", method = RequestMethod.POST)
      public ModelAndView saveupdatedetails(@PathVariable("id1") int id,@RequestParam("disease")String disease,@RequestParam("prescription")String prescription,HttpServletRequest request,HttpServletResponse response,HttpSession session)  
      {
          
    	 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{//System.out.println("Display Feedback page");
          System.out.println("Details Save");
        System.out.println("Details Save disease :" + disease);
          System.out.println("Details Save prescription :" + prescription);
        // System.out.println("Feedback Sent Subject :" + subject);
        // System.out.println("Feedback Sent Message :" + message);
           
          patientBookingDetails savedetails = new patientBookingDetails();
  
           boolean save=hospitalService.addDetailsToDatabase(disease,prescription,id);
           if(save)
           {
                JOptionPane.showMessageDialog(null, "Database Saved");
           }
           else
           {
                JOptionPane.showMessageDialog(null, "Database Not Saved");
           }
           List<patientBookingDetails> list=new ArrayList<patientBookingDetails>(); 
           //List<testModel>PatientList =  authenticateService1.test2();
  		 session = request.getSession();
  		 
  	     String hospitaluniqueidinmap = (String) session.getAttribute("hospitaluniqueid");
  	     System.out.println("Value of session in Patients List"+hospitaluniqueidinmap);
  		 list = hospitalService.listpatients(hospitaluniqueidinmap);
  	     
  		 
  		 if(list == null)
  			{
  				 ModelAndView mv = new ModelAndView();
  				  mv.setViewName("PList");
  		          System.out.println("Before going to the view patients List.... when patients list is null");
  		          String list1 = "Zero";
  		          mv.addObject("Schedule",list1);
  		        
  		        return mv;
  			}
  		 
  		 
  		for (patientBookingDetails obj : list)
          {
     		  System.out.println("Patient Id :" + obj.getUserID());
            System.out.println("Doctor ID :" + obj.getDoctorid());
            System.out.println("Disease :" + obj.getDisease());
            System.out.println("Status :" + obj.getStatus());
            System.out.println("Prescription Provided :" + obj.getPrescriptionprovided());
       
          }
            ModelAndView mv = new ModelAndView();
            mv.setViewName("PList");
            System.out.println("Before going to the view PList.jsp....");
            mv.addObject("list2",list);
          
          return mv;
           }
    
	else
	{
		ModelAndView mv = new ModelAndView();
		//HttpSession session = request.getSession();
		//session.setAttribute("doclist", doclist);
        mv.setViewName("index");
        return mv;
	}
      }

	 
	
}

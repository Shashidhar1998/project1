package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.service.AppointmentService;
import com.service.AuthService;
import com.service.HospitalService;
import com.pojo.DoctorSchedule;
import com.pojo.User;
import com.pojo.addhospitalmodel;
import com.pojo.loginmodel;
import com.pojo.patientBookingDetails;


@Component
@Controller
@RequestMapping("/user")
public class LoginController {

	    @Autowired
	    private AuthService authenticateService; 
	 
		@Autowired
		AppointmentService appointmentService;
		
	    @Autowired
		 private HospitalService hospitalService;
	
	    @RequestMapping(value = "/validateuser", method = RequestMethod.POST)
	    public ModelAndView validateUser(HttpServletRequest request,HttpServletResponse response,@RequestParam("username")String username, @RequestParam("password")String password,RedirectAttributes redirectAttributes, HttpSession session) 
	    {

	    	
	        System.out.println("Before saving in the database...");
	        
	        boolean isValid = authenticateService.findUser(username, password);
	    	String type = authenticateService.findUsertype(username, password);
	    	String username1 = authenticateService.findUsername(username, password);
	        System.out.println("Type in second :"  + type);
	         
	       
	        	if(isValid && type.equalsIgnoreCase("A"))
	        	{
	        		//HttpSession session = request.getSession();
	        		
	        		              session.setAttribute("user", username1);
	        		              
	        		              
	        	                  List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
	        	                  hospitallist = authenticateService.listhospitals();
	        	                  int notifications=0;
	        	                  for (addhospitalmodel obj : hospitallist)
	                                  {
	        		                  System.out.println("Hospital Id :" + obj.getId());
	                                  System.out.println("Hospital Name :" + obj.getName());
	                                  System.out.println("Hospital Name :" + obj.getStatus());
	                                  notifications++;        
	                                  }
	        	                 System.out.println("Number of Notifications : "+notifications);
	        	
	        	                 session.setAttribute("notifications", notifications);
	                             ModelAndView mv = new ModelAndView();
	                             mv.setViewName("dashboard");
	                             mv.addObject("list",hospitallist);
	                             session.setAttribute("notifications", notifications);
	                             return mv;
	        	}
	        	else if (isValid && type.equalsIgnoreCase("RH"))
	        	{
	        		
	        		String hospitaluniqueidinmap = authenticateService.findHospitalUserInMapTable(username);
	        		System.out.println("Hospital Unique Id :" + hospitaluniqueidinmap);
	        		List<addhospitalmodel> hospitallistforadmin=new ArrayList<addhospitalmodel>(); 
	        		hospitallistforadmin = authenticateService.listhospitalsbyUniqueId(hospitaluniqueidinmap);
	        		for (addhospitalmodel obj : hospitallistforadmin)
		            {
		        		System.out.println("Hospital Id :" + obj.getId());
		               System.out.println("Hospital Name :" + obj.getName());
		               System.out.println("Hospital Name :" + obj.getStatus());
		               System.out.println("Hospital Name :" + obj.getUniqueid());
		                        
		            }
	        		//HttpSession session = request.getSession();
	        		 session.setAttribute("user", username1);
	        		 session.setAttribute("hospitaluniqueid", hospitaluniqueidinmap);
	        		 HospitalAdminController HadminC = new HospitalAdminController();
	        		 
	        		    List<patientBookingDetails> patient_list = new ArrayList<patientBookingDetails>();
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
	        			
	        			       String olddoc = "abc";
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
	        				//System.out.println(obj1.getDocname()+"           "  + obj1.getDocSlot().values());
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
	        	        
	        	        return mv;
	        	         }
	        	}
	        	else if (isValid && type.equalsIgnoreCase("UH"))
	        	{
	        		
	        		return new ModelAndView("hospitaladmin");
	        	}
	        	else if (isValid && type.equalsIgnoreCase("U"))
	        	{
	        		session.setAttribute("user", username1);
	        		User userobj = new User();
	        		userobj = appointmentService.getUserDetails(username);
	        		String name =userobj.getFirst_name()+userobj.getLast_name() ;
	        		session.setAttribute("name", name);
	        		return new ModelAndView("userdash");
	        		
	        	}
	        	else 
	        	{
	        		JOptionPane.showMessageDialog(null, "Invalid Credentials!!!");
	        		return new ModelAndView("index");
	        	}

	        	
	       // end of loginfunction
	        
	    }
	    

/*	   
        @RequestMapping(value = "/dashboard1")
	    public String dashboard1()
	    {
	    	return "dashboard";
	    }
	    @RequestMapping(value = "/index1")
	    public String index1()
	    {
	    	return "index";
	    }
*/
	    
	     @RequestMapping(value = "/logout")
		 public String logout(HttpServletRequest request,HttpServletResponse response, HttpSession session)
		 {
	    	//HttpSession session = request.getSession(false);
	    	session.setAttribute("user", "InvalidUser");
	    	//session.invalidate();
			 return "index";
		 }
	    
	     @RequestMapping(value = "/headerr")
		 public String redirecttoregister()
		 {
			 return "header";
		 }
	     
	     
	     
	     @RequestMapping(value = "/report")
		 public ModelAndView report()
		 {
	    	 ModelAndView mv = new ModelAndView();
             mv.setViewName("index1");
             
             return mv;
		 }
	     
	     @RequestMapping(value = "/redirect")
		 public String redirect()
		 {
			 return "index";
		 }
	     @RequestMapping(value = "/Main_Page")
		 public String redirecttomainpage()
		 {
			 return "index";
		 }
	     
	     @RequestMapping(value = "/forgotpassword")
		 public String redirecttoforgotpasswordpage()
		 {
	    	 
			 return "Forgot";
		 }
	     
	     @RequestMapping(value = "/forgot")
		 public String changePassword(@RequestParam("username")String username, @RequestParam("password")String password,@RequestParam("email")String email)
		 {
	    	 boolean isValid = authenticateService.findUserAndMail(username,email);
	    	 if(isValid)
	    	 {
	    		 
	    		 authenticateService.changePass(username,password);
	    		 JOptionPane.showMessageDialog(null, "Password Change Successfully!");
	    		 return "index";
	    	 }
	    	 else
	    	 {
	    	JOptionPane.showMessageDialog(null, "Please Check Your Username And Password");
			 return "Forgot";
	    	 }
			
		 }
	     
	
}

package com.controller;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import com.service.AuthService;
import com.service.RegistrationService;
import com.pojo.User;
import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.feedback;
import com.pojo.loginmodel;
import com.pojo.patientBookingDetails;
import com.pojo.userhospitalmodel;

import java.util.UUID;

import com.fasterxml.uuid.Generators;

@Component
@Controller
@RequestMapping("/newuser")

public class RegisterController {
	
	 @Autowired
	 private RegistrationService authenticateService; 
	 @Autowired
	 private AuthService authenticateService1; 
	
	 @RequestMapping(value = "/redirectregister")
	 public String redirecttoregister()
	 {
		 return "redirect:registerlink1";
	 }
	  @RequestMapping(value = "/registerlink1")
	    public String dashboard1()
	    {
	    	return "addhospitalrequest";
	    }
	  
	  @RequestMapping("/notifications")  
	    public ModelAndView notifications(HttpServletRequest request,HttpServletResponse response, HttpSession session)  
	    {  
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
      	List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
      	hospitallist = authenticateService1.listhospitals();
      	int notifications=0;
      	for (addhospitalmodel obj : hospitallist)
          {
      		 System.out.println("Hospital Id :" + obj.getId());
             System.out.println("Hospital Name :" + obj.getName());
             System.out.println("Hospital Name :" + obj.getStatus());
             notifications++;        
          }
      	System.out.println("Number of Notifications : "+notifications);
      //	HttpSession session = request.getSession();
      	session.setAttribute("notifications", notifications);
      	List<addhospitalmodel> hospitallist1=new ArrayList<addhospitalmodel>(); 
      	hospitallist1 = authenticateService1.listhospitals1();
            ModelAndView mv = new ModelAndView();
            mv.setViewName("dashboard");
            mv.addObject("list",hospitallist);

            mv.addObject("notifications",notifications);
          
            return mv;
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}
	    }     
	
	  @RequestMapping("/feedback")  
	    public ModelAndView displayFeedbacks(HttpServletRequest request,HttpServletResponse response, HttpSession session)  
	    {
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
		  //System.out.println("Display Feedback page");
		  List<feedback> feedslistunread = new ArrayList<feedback>();
		  feedslistunread = authenticateService1.getunreadfeedbacks(); 
		  for(feedback obj : feedslistunread)
		  {
			  System.out.println("FeedBack Id :" + obj.getId());
			  System.out.println("FeedBack Given By : " + obj.getName() + " and Email : " +obj.getEmail());
			  System.out.println("FeedBack Subject" + obj.getSubject());
			  System.out.println("FeedBack Message" + obj.getMessage());
			  System.out.println("FeedBack Dateandtime " + obj.getDateandtime());
			  System.out.println("FeedBack Status" + obj.getReadstatus());
		  }
		  List<feedback> feedslistread = new ArrayList<feedback>();
		  feedslistread = authenticateService1.getreadfeedbacks();
		  ModelAndView mv = new ModelAndView();
          mv.setViewName("displayFeedback");
          mv.addObject("feedslistunread",feedslistunread);
          mv.addObject("feedslistread",feedslistread);
         
          return mv;
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}
	    }
	    
	   
	     @RequestMapping(value = "/MarkAsRead/{id}", method = RequestMethod.GET)
	    public ModelAndView markAsRead(@PathVariable("id") int id,HttpServletRequest request,HttpServletResponse response, HttpSession session)  
	    {
		  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
			  
			  System.out.println(id);
			  boolean markasread = authenticateService.updateReadStatusAsRead(id);
			  List<feedback> feedslistunread = new ArrayList<feedback>();
			  feedslistunread = authenticateService1.getunreadfeedbacks(); 
			  List<feedback> feedslistread = new ArrayList<feedback>();
			  feedslistread = authenticateService1.getreadfeedbacks();
			  ModelAndView mv = new ModelAndView();
	          mv.setViewName("displayFeedback");
	          mv.addObject("feedslistunread",feedslistunread);
	          mv.addObject("feedslistread",feedslistread);
	          return mv;
			
			}
		  else
		  {
			  ModelAndView mv = new ModelAndView();
				mv.setViewName("index");
				return mv;
		  }
		    
	    }
	  @RequestMapping("/hospitals")  
	    public ModelAndView hospitalsList(HttpServletRequest request,HttpServletResponse response, HttpSession session)  
	    {  
         System.out.println("Session object : "+session.getAttribute("user"));
         
         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         
		  if (!session.getAttribute("user").equals("InvalidUser"))
			{
				List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
				hospitallist = authenticateService1.listhospitals();
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
				List<addhospitalmodel> hospitallist1=new ArrayList<addhospitalmodel>(); 
				hospitallist1 = authenticateService1.listhospitals1();
				ModelAndView mv = new ModelAndView();
				mv.setViewName("hospitallist");
				mv.addObject("list",hospitallist1);

				return mv;
			}
			else
			{
				ModelAndView mv = new ModelAndView();
				mv.setViewName("index");
				return mv;
			}
			
				
	    }  
	  
	  
	
	  @RequestMapping("/aboutus")  
	    public String display2()  
	    {  
	        return "index";  
	    }     
	
	  
	  @RequestMapping(value = "/redirectregisteruser")
		 public String redirecttoregisteruser()
		 {
			 return "redirect:registerlink2";
		 }
		  @RequestMapping(value = "/registerlink2")
		    public String dashboard2()
		    {
		    	return "registerlink";
		    }
	
	  @RequestMapping(value = "/register", method = RequestMethod.POST)
	  public String validateUsr(@RequestParam("username")String username, @RequestParam("password")String password, @RequestParam("email")String email, @RequestParam("contact")String contact,RedirectAttributes redirectAttributes) {

	    	
	        loginmodel userobj = new loginmodel();
	        userobj.setUserID(username);
	        userobj.setPassword(password);
	        userobj.setEmail(email);
	        userobj.setContactNumber(contact);
	        
	        boolean isValid = authenticateService.registerUser(userobj);
	    	
	        System.out.println("\n\nTo check data in register /n Email  :"+userobj.getEmail());
	        System.out.println("\n\n Contact  :"+userobj.getContactNumber());
	        System.out.println("\n\n Password  :"+userobj.getPassword());
	        System.out.println("\n\n UserId  :"+userobj.getUserID());
	        
	         
	        if(isValid) {
	        	System.out.println("After saving in the db...");
	        	return "index";
	        } else {
	        	
	        	return "redirect:registerlink1";
	        }
	 
	        
	    }
	  
	  
	   
	  @RequestMapping(value = "/addhospital", method = RequestMethod.POST)
	  public String addHospital(HttpServletRequest request , HttpServletResponse response) {

		     String username= request.getParameter("uid");
		     String email= request.getParameter("email");
		     String contact= request.getParameter("contact");
		     String hospitalname= request.getParameter("hospital_name");
		     String adr1= request.getParameter("addr1");
		     String adr2= request.getParameter("addr2");
		     String state= request.getParameter("stt");
		     String city= request.getParameter("hcity");
		     String pin= request.getParameter("pin");
		     String certifications= request.getParameter("certifications");
		     String success_op= request.getParameter("success_op");
		     String achieve = request.getParameter("achieve");
		     String Password = request.getParameter("Password");
		     int len = 6;
	         String password = authenticateService.generate_Password(len);
	         
	         
	        loginmodel log = new loginmodel();
	        log.setUserID(username);
	        log.setEmail(email);
	        log.setPassword(Password);
	        String type = "UH";
	        log.setType(type);
	        log.setContactNumber(contact);
	       
	        
	        UUID uuid1 = Generators.timeBasedGenerator().generate();
			System.out.println("UUID : "+uuid1);
			String strUUID = uuid1.toString();
			System.out.println("UUID Version : "+uuid1.version());
			System.out.println("UUID Variant : "+uuid1.variant());

	        userhospitalmodel userhos = new userhospitalmodel();
	        userhos.setUserID(username);
	        userhos.setHosid(strUUID);
	        
	        
	        
	        
	        addhospitalmodel userobj = new addhospitalmodel();
	        userobj.setUniqueid(strUUID);
	        userobj.setName(hospitalname);
	        userobj.setAddress1(adr1);
	        userobj.setAddress2(adr2);
	        userobj.setState(state);
	        userobj.setCity(city);
	        userobj.setPin(pin);
	        userobj.setCertifications(certifications);
	        userobj.setSuccess(success_op);
	        int status = 0;
	        userobj.setStatus(status);
	        userobj.setOptional("NA");
	        userobj.setAchievements(achieve);
	        
	        System.out.println(username);
	        System.out.println(email);
	        System.out.println(contact);
	        System.out.println(uuid1);
	        System.out.println(hospitalname);
	        System.out.println(adr1);
	        System.out.println(adr2);
	        System.out.println(state);
	        System.out.println(city);
	        System.out.println(pin);
	        System.out.println(certifications);
	        System.out.println(success_op);
	        System.out.println(achieve);
	        
	        System.out.println("\n\nTo check data in email  :"+log.getEmail());
	        System.out.println("\n\nTo check data in email  :"+log.getContactNumber());
	        System.out.println("\n\nTo check data in email  :"+log.getPassword());
	        System.out.println("\n\nTo check data in email  :"+log.getUserID());
	        
	        boolean isvalid = authenticateService.registerhospital(log,userobj,userhos);
	        if(isvalid)
	        	{
	        	 JOptionPane.showMessageDialog(null, "Hospital Registered Successfully");
	        	   return "index";
	        	}
	        else
	        {
	        	JOptionPane.showMessageDialog(null, "Database Not Saved");
	        	return "addhospitalrequest";
	        }
	       
	 
	        
	    }
	  
	  @RequestMapping(value = "/accept/{id}", method = RequestMethod.GET)
	  public ModelAndView acceptHospital(HttpServletRequest request , HttpServletResponse response,@PathVariable("id") int id,Model model, HttpSession session)
	  {
		 
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
		  System.out.println("id :" + id); 
	
		 List<addhospitalmodel> list=new ArrayList<addhospitalmodel>(); 
		 list = authenticateService1.listhospitalsbyid(id);
	     
		for (addhospitalmodel obj : list)
         {
     		System.out.println("Hospital Id :" + obj.getId());
            System.out.println("Hospital Name :" + obj.getName());
            System.out.println("Hospital Status :" + obj.getStatus());
            System.out.println("Hospital unique id  :" + obj.getUniqueid());
            String uniqueid = obj.getUniqueid();
            String userid = authenticateService1.mapUserByHospitaluniqueid(uniqueid);
            boolean update1 = authenticateService.accepthos(id,userid);     
         }
		
		List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
      	hospitallist = authenticateService1.listhospitals();
      	int notifications=0;
      	for (addhospitalmodel obj : hospitallist)
          {
      		System.out.println("Hospital Id :" + obj.getId());
             System.out.println("Hospital Name :" + obj.getName());
             System.out.println("Hospital Name :" + obj.getStatus());
            notifications++;        
          }
      	System.out.println("Number of Notifications : "+notifications);
      	//HttpSession session = request.getSession();
      	session.setAttribute("notifications", notifications);
      	List<addhospitalmodel> hospitallist1=new ArrayList<addhospitalmodel>(); 
      	hospitallist1 = authenticateService1.listhospitals1();
            ModelAndView mv = new ModelAndView();
            mv.setViewName("dashboard");
            mv.addObject("list",hospitallist);

            mv.addObject("notifications",notifications);
          
            return mv;
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}
		 
		
	  }
	  
	  
	  
	  
	  
	  @RequestMapping(value = "/ignore/{id}", method = RequestMethod.GET)
	  public ModelAndView ignorehospital(HttpServletRequest request , HttpServletResponse response,@PathVariable("id") int id,Model model, HttpSession session)
	  {
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
		  System.out.println("id :" + id); 
		 
		 List<addhospitalmodel> list=new ArrayList<addhospitalmodel>(); 
		 list = authenticateService1.listhospitalsbyid(id);
	
		for (addhospitalmodel obj : list)
         {
     		System.out.println("Hospital Id :" + obj.getId());
            System.out.println("Hospital Name :" + obj.getName());
            System.out.println("Hospital Status :" + obj.getStatus());
            String uniqueid = obj.getUniqueid(); 
            
            String userid = authenticateService1.mapUserByHospitaluniqueid(uniqueid);
            boolean update1 = authenticateService.ignorehos(id,userid);
            
         }
		List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
      	hospitallist = authenticateService1.listhospitals();
      	int notifications=0;
      	for (addhospitalmodel obj : hospitallist)
          {
      		System.out.println("Hospital Id :" + obj.getId());
             System.out.println("Hospital Name :" + obj.getName());
             System.out.println("Hospital Name :" + obj.getStatus());
            notifications++;        
          }
      	System.out.println("Number of Notifications : "+notifications);
      //	HttpSession session = request.getSession();
      	session.setAttribute("notifications", notifications);
      	List<addhospitalmodel> hospitallist1=new ArrayList<addhospitalmodel>(); 
      	hospitallist1 = authenticateService1.listhospitals1();
            ModelAndView mv = new ModelAndView();
            mv.setViewName("dashboard");
            mv.addObject("list",hospitallist);

            mv.addObject("notifications",notifications);
          
            return mv;
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}
	
	  }
	  
	  
	  
	  @RequestMapping(value = "/ignore1/{id}", method = RequestMethod.GET)
	  public ModelAndView ignorehospital1(HttpServletRequest request ,@PathVariable("id") int id,Model model, HttpSession session, HttpServletResponse response)
	  {
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
		  System.out.println("id :" + id); 
		 
		 List<addhospitalmodel> list=new ArrayList<addhospitalmodel>(); 
		 list = authenticateService1.listhospitalsbyid(id);
	
		for (addhospitalmodel obj : list)
         {
     		System.out.println("Hospital Id :" + obj.getId());
            System.out.println("Hospital Name :" + obj.getName());
            System.out.println("Hospital Status :" + obj.getStatus());
            String uniqueid = obj.getUniqueid(); 
            
            String userid = authenticateService1.mapUserByHospitaluniqueid(uniqueid);
            boolean update1 = authenticateService.ignorehos(id,userid);
            
         }
		 List<addhospitalmodel> hospitallist=new ArrayList<addhospitalmodel>(); 
	      	hospitallist = authenticateService1.listhospitals();
	      	int notifications=0;
	      	for (addhospitalmodel obj : hospitallist)
	          {
	      		System.out.println("Hospital Id :" + obj.getId());
	             System.out.println("Hospital Name :" + obj.getName());
	             System.out.println("Hospital Name :" + obj.getStatus());
	            notifications++;        
	          }
	      	System.out.println("Number of Notifications : "+notifications);
	      //	HttpSession session = request.getSession();
	      	session.setAttribute("notifications", notifications);
		  List<addhospitalmodel> hospitallist1=new ArrayList<addhospitalmodel>(); 
   	  hospitallist1 = authenticateService1.listhospitals1();
		  ModelAndView mv = new ModelAndView();
       mv.setViewName("hospitallist");
       mv.addObject("list",hospitallist1);
       
       return mv; 
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}
		 
		
	  }
	  
	  
	  @RequestMapping(value = "/viewHospitals/{id1}", method = RequestMethod.GET)
	  public ModelAndView dashboard(HttpServletRequest request , HttpServletResponse response,@PathVariable("id1") String hospital_unique_id,Model model,HttpSession session)
	  {
		  System.out.println("Session object : "+session.getAttribute("user"));
	         
	         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	         
			  if (!session.getAttribute("user").equals("InvalidUser"))
				{
		                                      ModelAndView mv = new ModelAndView();
		                                      System.out.println("Hospital_Unique_id :" + hospital_unique_id); 
		                                      List<patientBookingDetails> patients_list = new ArrayList<patientBookingDetails>();
		                                      List<adddoc> doctors_list = new ArrayList<adddoc>();
		                                      addhospitalmodel hospitaldetails = new addhospitalmodel();
		                                      hospitaldetails = authenticateService1.getHospitalDetailsByUniqueid(hospital_unique_id);
		                                                    if(hospitaldetails == null)
		                                                      {
			                                                        return new ModelAndView("ErrorPage"); 
		                                                      }
		                                                    else
		                                                      {
			                                                        patients_list = authenticateService1.getpatientsbyid(hospital_unique_id);
		                                                            doctors_list = authenticateService1.getdoctorsbyid(hospital_unique_id);
		               
		                                                            mv.setViewName("hospitaldetails");
		                                                            mv.addObject("hospitaldetails",hospitaldetails);
		                                                            System.out.println(patients_list);
		                                                   if(patients_list==null)
		                                                     {
		            	
		            	                                            String Zero = "Zero";
		            	                                            mv.addObject("list",Zero);
		            	  
		                                                     }
		                                                      else
		                                                     {
		            	   
		            	                                           mv.addObject("list","more");
		            	                                           mv.addObject("patients_list",patients_list);
		                                                     }
		               
		                                                   if(doctors_list==null)
		                                                     {
		            	  
		            	                                           String Zero = "Zero";
		            	                                           mv.addObject("list1",Zero);
		            	                                           System.out.println("There are no doctors..");
		                                                     }
		                                                  else
		                                                     {
		            	  
		            	                                           for(adddoc obj : doctors_list)
			                                                          {
			            	                                            System.out.println("Doctor Id :"+obj.getDoctorid());
			            	                                            System.out.println("Doctor Id :"+obj.getHospitalid());
			                                                           }
		            	                                           System.out.println("There are doctors..."+doctors_list);
		            	                                           mv.addObject("list1","more");
		            	                                           mv.addObject("doctors_list",doctors_list);
		                                                      } 
		               return mv;

	                     }  
		                                      
				}
				else
				{
					ModelAndView mv = new ModelAndView();
					mv.setViewName("index");
					return mv;
				}                       
		                                      
	  }
	  
 
		 
	  @RequestMapping(value = "/registerUserObjectionCreation")
		    public ModelAndView dashboard2(ModelAndView model)
		    {
		    User userObj= new User();
			model.addObject("userObj", userObj);
			model.setViewName("registerlink");
			return model;
		        
		    }
	 

		@RequestMapping(value = "/RegisterUser", method = RequestMethod.POST)
		public String saveEmployee(@ModelAttribute User userObj) {
			
			loginmodel obj = new loginmodel();
			obj.setUserID(userObj.getUsername());
			obj.setPassword(userObj.getPassword());
			obj.setEmail(userObj.getEmail());
			obj.setContactNumber(userObj.getPhone());
			obj.setType("U");
			
			authenticateService.addUser(userObj,obj);
			 JOptionPane.showMessageDialog(null, "Successfully Registered!");
			
			return "index";

			 
			 
		}
	  
}

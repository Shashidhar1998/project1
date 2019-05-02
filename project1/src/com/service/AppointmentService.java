package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.pojo.User;
import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.loginmodel;
import com.pojo.patientBookingDetails;

public class AppointmentService {

	 private HibernateTemplate hibernateTemplate;
	  
	    private AppointmentService() { }

	    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
	        this.hibernateTemplate = hibernateTemplate;
	    }

	    
	    public List<addhospitalmodel> getHospitalName(String givenInput)
	    {
	    	
	    	Session session = hibernateTemplate.getSessionFactory().openSession();
	    	Transaction tx = session.beginTransaction();
	    	
	    	String strQuery = "SELECT * FROM hospitaldetails where status=1 AND hospital_name LIKE '" + givenInput + "%'";
	    	
	    	
	    	SQLQuery qry = session.createSQLQuery(strQuery);
	    	 qry.addEntity(addhospitalmodel.class);
	    	
	    	List<addhospitalmodel> lstHospitalData = qry.list();
	    	
	    	
	    	for (addhospitalmodel objHospitalData :lstHospitalData )
	    	{
	    		System.out.println("Hospital Name "+objHospitalData.getName());
	    	}
	    	
	    	return lstHospitalData;
	    	
	    }// of getHospitalName
	    
	    public List<adddoc> searchdoctors(String hospitalID, String name)
	    {
	    	Session session = hibernateTemplate.getSessionFactory().openSession();
	    	Transaction tx = session.beginTransaction();
	    	
	    	String strQuery = "SELECT * FROM project1.doctordetails where hospital_id='"+ hospitalID +"' and doctor_name like '"+name+"'";
	    	System.out.println("Sql Query "+strQuery);
	    	
	    	
	    	SQLQuery qry = session.createSQLQuery(strQuery);
	    	 qry.addEntity(adddoc.class);
	    	
	    	List<adddoc> lstDoctors = qry.list();
	    	
	    	
	    	for (adddoc objDoctorData :lstDoctors )
	    	{
	    		System.out.println("Doctor Name "+objDoctorData.getDoctorname());
	    	}
	    	
	    	return lstDoctors;
	    	
	    	
	    	
	   
	    }
	    
	    
	    public List<adddoc> getDoctors(String hosname, String locality, String city)
	    {
	    	Session session = hibernateTemplate.getSessionFactory().openSession();
	    	Transaction tx = session.beginTransaction();
	    	
	    	String strQuery = "SELECT * FROM doctordetails WHERE hospital_id IN(SELECT hospital_unique_id FROM hospitaldetails WHERE hospital_name='"+ hosname +"' AND addressline_1='"+ locality +"' AND city='"+ city +"')";
	    	System.out.println("Sql Query "+strQuery);
	    	
	    	
	    	SQLQuery qry = session.createSQLQuery(strQuery);
	    	 qry.addEntity(adddoc.class);
	    	
	    	List<adddoc> lstDoctors = qry.list();
	    	
	    	
	    	for (adddoc objDoctorData :lstDoctors )
	    	{
	    		System.out.println("Doctor Name "+objDoctorData.getDoctorname());
	    	}
	    	
	    	return lstDoctors;
	    	
	    	
	    	
	   
	    }
	    
	    
	    public List<String> getSlots(String hospitalID, String doctorID, String date)
	    {
	    	Session session = hibernateTemplate.getSessionFactory().openSession();
	    	Transaction tx = session.beginTransaction();
	    	
	    	String strQuery = "select slots from all_the_slots where slots NOT IN (select slots from patientbookingdetails where doctor_id='"+ doctorID +"' AND hospital_id='" + hospitalID +"'AND date='"+date+"') order by id";
	    	System.out.println("Sql Query "+strQuery);
    	
	    	SQLQuery qry = session.createSQLQuery(strQuery);

	    	List patientBookingDetailsList = qry.list();
	    	
            System.out.println(patientBookingDetailsList);
            
            ArrayList <String> slotList = new ArrayList<String>();
            for (Object patientBookingDetailsObj : patientBookingDetailsList)
            {
            	System.out.println("Slots..."+patientBookingDetailsObj.toString());
            	slotList.add(patientBookingDetailsObj.toString());
            }
	    	
	    	return slotList;	    	
	   
	    }
	    

	    
	    public List<String> saveAppointment(HttpSession ServletSession, String slots, String patientname, String user)
	    {
	    	
		    	
	    	
	    	patientBookingDetails patientBookingDetailsObj = new patientBookingDetails();
	       	patientBookingDetailsObj.setDoctorid((String)ServletSession.getAttribute("DoctorID"));
	    	patientBookingDetailsObj.setHospital_id((String)ServletSession.getAttribute("HospitalID"));
	    	patientBookingDetailsObj.setUserID(user);
	    	patientBookingDetailsObj.setPatientname(patientname);
	    	patientBookingDetailsObj.setSlots(slots);
	    	patientBookingDetailsObj.setDate((String)ServletSession.getAttribute("AppointmentDate"));
	    	patientBookingDetailsObj.setStatus("TIP");
	    	patientBookingDetailsObj.setDoctor_name((String)ServletSession.getAttribute("DoctorName"));
	    	
	    	
	    	hibernateTemplate.save(patientBookingDetailsObj);
	    	

	    	return null;
	    	
	   
	    }

		public User getUserDetails(String username) {
			// TODO Auto-generated method stub
			
			 boolean isValidUser = false;
		        //String sqlQuery = "from User where name='"+uname + "' and password='"+upwd+"'";
		        String uemail=username;
		        String sqlQuery = "from User u where u.username=?";
		        
		        System.out.println("User entered data Username : " + username);
		        
		        try {
		        	
		        	
		            List<User> userObj = (List) hibernateTemplate.find(sqlQuery, username);
		            
		           
		      
		           
		                //log.info("Id= " + userObj.get(0)).getId() + ", Name= " + userObj.get(0).getName() + ", Password= " + userObj.get(0).getPassword());
		            	for(User obj :userObj)
		            	{
		            		return obj;
		            	}
		               
		            
		           } 
		        catch(Exception e) 
		        {
		            isValidUser = false;
		           System.out.println("An error occurred while fetching the user details from the database");   
		        }
		       
			return null;
		}
		
		   public List<patientBookingDetails> getPatientList(String userId)
		    {
		    	
		    	Session session = hibernateTemplate.getSessionFactory().openSession();
		    	Transaction tx = session.beginTransaction();
		    	
		    	
		    	String strQuery = "SELECT * FROM patientbookingdetails where UserID='"+userId+"'";
		    	
		    	
		    	System.out.println("Sql Query "+strQuery);
		    	
		    	
		    	SQLQuery qry = session.createSQLQuery(strQuery);
		    	 qry.addEntity(patientBookingDetails.class);
		    	
		    	List<patientBookingDetails> lstPatients = qry.list();
		    	
		    	
		    	for (patientBookingDetails objPatientData : lstPatients )
		    	{
		    		System.out.println("Patient Name "+objPatientData.getPatientname());
		    	}
		    	
		    	return lstPatients;
		    	
		    }// of getHospitalName

			public adddoc getDoctorDetails(String hospital_id, String doctorid) {
				// TODO Auto-generated method stub
				
				Session session = hibernateTemplate.getSessionFactory().openSession();
		    	Transaction tx = session.beginTransaction();
		    	
		    	String strQuery = "SELECT * FROM doctordetails WHERE hospital_id='"+hospital_id+"' AND doctor_id='"+doctorid+"'";
		    	System.out.println("Sql Query "+strQuery);
		    	
		    	
		    	SQLQuery qry = session.createSQLQuery(strQuery);
		    	 qry.addEntity(adddoc.class);
		    	
		    	List<adddoc> lstDoctors = qry.list();
		    	
		    	
		    	for (adddoc objDoctorData :lstDoctors )
		    	{
		    		return objDoctorData;
		    	}
		    	
		    	return null;
			
			}

			public addhospitalmodel gethospitaldetails(String hospital_id) {
				// TODO Auto-generated method stub
				Session session = hibernateTemplate.getSessionFactory().openSession();
		    	Transaction tx = session.beginTransaction();
		    	
		    	String strQuery = "SELECT * FROM hospitaldetails where hospital_unique_id = '" + hospital_id + "'";
		    	
		    	
		    	SQLQuery qry = session.createSQLQuery(strQuery);
		    	 qry.addEntity(addhospitalmodel.class);
		    	
		    	List<addhospitalmodel> lstHospitalData = qry.list();
		    	
		    	
		    	for (addhospitalmodel objHospitalData :lstHospitalData )
		    	{
		    		return objHospitalData;
		    	}
		    	
		    	
				return null;
			}
	    
	    
}

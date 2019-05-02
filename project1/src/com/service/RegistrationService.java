package com.service;

import java.util.List;
import java.util.Random;

//import org.apache.catalina.Session;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.pojo.User;
import com.pojo.addhospitalmodel;
import com.pojo.loginmodel;
import com.pojo.userhospitalmodel;

public class RegistrationService {

	private HibernateTemplate hibernateTemplate;

 
    private RegistrationService() { }
 
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

 
    public boolean registerUser(loginmodel obj)
    {
    	
   	    boolean valid= false;
   	    try
   	    {
    	hibernateTemplate.save(obj);
    	valid = true;
   	    }
   	 catch(Exception e)
        {
        	System.out.println("Registering In the Auth Catch ");
        	e.printStackTrace();
        }
    	//hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
    	
    	
    	return valid;
    	
    	
    }

	public boolean registerhospital(loginmodel log, addhospitalmodel userobj, userhospitalmodel userhos) {
		// TODO Auto-generated method stub
		 System.out.println("In the Auth Service ");
        System.out.println("\n\nTo check data in email  :"+log.getEmail());
        System.out.println("\n\nTo check data in email  :"+log.getContactNumber());
        System.out.println("\n\nTo check data in email  :"+log.getPassword());
        System.out.println("\n\nTo check data in email  :"+log.getUserID());
        try
        {
        	hibernateTemplate.save(userobj);
        	
        	hibernateTemplate.save(log);
        	hibernateTemplate.save(userhos);
        	
        	//hibernateTemplate.flush();
        }
        catch(Exception e)
        {
        	
        	System.out.println("In the Auth Catch ");
        	e.printStackTrace();
        }
		//hibernateTemplate.save(userobj);
		return true;
	}

	public boolean registerhospital1(loginmodel log) {
		// TODO Auto-generated method stub
		//hibernateTemplate1.save(log);
		 System.out.println("In the Auth Service 23 ");
		return true;
	}



	public boolean accepthos(int id, String userid) {
		// TODO Auto-generated method stub
		
	      boolean isValid = false;
	        try {
	        	int status = 1; 
	            String type="RH";
	        	hibernateTemplate.bulkUpdate("update addhospitalmodel set status='" + status +"' where id = '"+ id +"'");
	        	hibernateTemplate.bulkUpdate("update loginmodel set type='" + type  +"' where UserID = '"+ userid +"'");
	        //     Session session = sessionFactory.getCurrentSession();
	               isValid = true;
	               
	        }

	        catch (Exception e) {
	               System.out.println("In the catch block of update..."+e);
	               isValid = false;
	               e.printStackTrace();
	               
	        }

	        return isValid;
		
	}

	public boolean ignorehos(int id, String userid) {
	
		boolean isValid = false;
        try {
       
           
        	hibernateTemplate.bulkUpdate("delete addhospitalmodel where id = '"+ id+"'");
        //     Session session = sessionFactory.getCurrentSession();
        	hibernateTemplate.bulkUpdate("delete loginmodel where UserID = '"+ userid +"'");
        	
        	hibernateTemplate.bulkUpdate("delete userhospitalmodel where UserID = '"+ userid +"'");
               isValid = true;
               
        }

        catch (Exception e) {
               System.out.println("In the catch block of delete..."+e);
               isValid = false;
               e.printStackTrace();
               
        }

        return isValid;
	}


	public String generate_Password(int len) 
    { 
        System.out.println("Generating password using random() : "); 
        System.out.print("Your new password is : "); 
  
        // A strong password has Cap_chars, Lower_chars, 
        // numeric value and symbols. So we are using all of 
        // them to generate our password 
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
        String numbers = "0123456789"; 
                String symbols = "!@#$%&*_)"; 
  
  
        String values = Capital_chars + Small_chars + 
                        numbers + symbols; 
  
    
        Random rndm_method = new Random(); 
  
        char[] password = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            password[i] = 
              values.charAt(rndm_method.nextInt(values.length())); 
  
        } 
        return password.toString(); 
    }

	public boolean updateReadStatusAsRead(int id) {
		// TODO Auto-generated method stub
		try
		{
			String status="Read";
	    	hibernateTemplate.bulkUpdate("update feedback set readstatus='" + status +"' where id = '"+ id +"'");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
		
		
		
	}

	
	


	public void addUser(User userObj, loginmodel obj) {
		// TODO Auto-generated method stub
		hibernateTemplate.save(userObj);
		hibernateTemplate.save(obj);
		
	}


	
	
	
}

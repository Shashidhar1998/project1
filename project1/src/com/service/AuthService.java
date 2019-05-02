package com.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.feedback;
import com.pojo.loginmodel;
import com.pojo.patientBookingDetails;
import com.pojo.userhospitalmodel;
import com.service.AuthService;

public class AuthService {

	
	
	 private HibernateTemplate hibernateTemplate;
	    private static Logger log = Logger.getLogger(AuthService.class);
	 
	    private AuthService() { }
	 
	    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
	        this.hibernateTemplate = hibernateTemplate;
	    }
	 
	    @SuppressWarnings( { "unchecked", "deprecation" } )
	   

		public boolean findUser(String username, String password)
	    {
			// TODO Auto-generated method stub
	        boolean isValidUser = false;
	        //String sqlQuery = "from User where name='"+uname + "' and password='"+upwd+"'";
	        String uemail=username;
	        String sqlQuery = "from loginmodel u where u.UserID=?  and u.Password=?";
	        String sqlQuery1 = "from loginmodel u where u.Email=?  and u.Password=?";
	        System.out.println("User entered data Username : " + username + " \n pwd "+password);
	        
	        try {
	        	
	        	
	            List<loginmodel> userObj = (List) hibernateTemplate.find(sqlQuery, username, password);
	            List<loginmodel> userObj1 = (List) hibernateTemplate.find(sqlQuery1, username, password);
	           
	      
	            if((userObj != null && userObj.size() > 0) || (userObj1 != null && userObj1.size() > 0))
	            {
	                //log.info("Id= " + userObj.get(0)).getId() + ", Name= " + userObj.get(0).getName() + ", Password= " + userObj.get(0).getPassword());
	            	System.out.println("Credentials Validating  Username :"+userObj.size());
	            	System.out.println("Credentials Validating  Mail :"+userObj1.size());
	                isValidUser = true;
	            }
	           } 
	        catch(Exception e) 
	        {
	            isValidUser = false;
	           System.out.println("An error occurred while fetching the user details from the database");   
	        }
	        return isValidUser;
		}

		public List<addhospitalmodel> listhospitals() {
			// TODO Auto-generated method stub
			int status = 0;
			String sqlQuery = "from addhospitalmodel where status = ? ";
			List<addhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,status);
			
			
			return userObj;
		}

		public List<addhospitalmodel> listhospitals1() {
			// TODO Auto-generated method stub
			int status = 1;
			String sqlQuery = "from addhospitalmodel where status = ? ";
			List<addhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,status);
			
			
			return userObj;
		}

		public List<addhospitalmodel> listhospitalsbyid(int id) {
			// TODO Auto-generated method stub
			String sqlQuery = "from addhospitalmodel where hospital_id = ? ";
			List<addhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,id);
			
			return userObj;
		}



		public String findUsertype(String username, String password) {
			// TODO Auto-generated method stub
			 String sqlQuery = "from loginmodel u where u.UserID=? and u.Password=?";
		       
		       List<loginmodel> userObj = (List) hibernateTemplate.find(sqlQuery, username, password);
		if((userObj != null && userObj.size() > 0))
		{
		                for (loginmodel obj : userObj)
		                {
		                    System.out.println("login Type : "  +  obj.getType() );
		                    return obj.getType();      
		                }
		}
		else
		{
			String sqlQuery1 = "from loginmodel u where u.Email=?  and u.Password=?";
			List<loginmodel> userObj1 = (List) hibernateTemplate.find(sqlQuery1, username, password);
			if((userObj1 != null && userObj1.size() > 0))
			{
				 for (loginmodel obj : userObj1)
	                {
	                    System.out.println("login Type : "  +  obj.getType() );
	                    return obj.getType();      
	                }
			}
		}
		           
		           
			return null;
		}

		public String mapUserByHospitaluniqueid(String uniqueid) {
			// TODO Auto-generated method stub
			
			String sqlQuery = "from userhospitalmodel where hosid = ? ";
			List<userhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,uniqueid);
			 for (userhospitalmodel obj : userObj)
             {
                 System.out.println("User ID : "  +  obj.getUserID() );
                 return obj.getUserID() ;    
             }
			return null;
		}

		public String findHospitalUserInMapTable(String username) {
			
			String sqlQuery = "from userhospitalmodel where UserID = ? ";
			List<userhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,username);
			for (userhospitalmodel obj : userObj)
            {
                System.out.println("User ID : "  +  obj.getUserID() );
                return obj.getHosid() ;    
            }
			return username;
			// TODO Auto-generated method stub
			
		}

		public List<addhospitalmodel> listhospitalsbyUniqueId(String hospitaluniqueidinmap) {
			// TODO Auto-generated method stub
			String sqlQuery = "from addhospitalmodel where hospital_unique_id = ? ";
			List<addhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,hospitaluniqueidinmap);
			
			return userObj;
		}

		public List<patientBookingDetails> getpatientsbyid(String hospital_unique_id) {
			// TODO Auto-generated method stub
			String sqlQuery = "from patientBookingDetails where hospital_id = ? ";
			try{
				
			List<patientBookingDetails> userObj = (List) hibernateTemplate.find(sqlQuery,hospital_unique_id);
			
			if(userObj.size()<=0)
			{
				return null;
			}
			else
				return userObj;
			}
			catch(Exception e)
			{
				
			}
			return null;
		}

		public addhospitalmodel getHospitalDetailsByUniqueid(String hospital_unique_id) {
			// TODO Auto-generated method stub
			
			String sqlQuery = "from addhospitalmodel where hospital_unique_id = ? ";
			try{
				
				List<addhospitalmodel> userObj = (List) hibernateTemplate.find(sqlQuery,hospital_unique_id);
				if(userObj!=null)
				{
				for(addhospitalmodel obj : userObj)
				{
					return obj;
				}
				}
				else
				{
					return  null;
				}
			}catch(Exception e)
			{
				return null;
			}
			return null;
		}

		public List<adddoc> getdoctorsbyid(String hospital_unique_id) {
			// TODO Auto-generated method stub
			String sqlQuery = "from adddoc where hospitalid = ? ";
			try{
				
				List<adddoc> userObj = (List) hibernateTemplate.find(sqlQuery,hospital_unique_id);
				if(userObj.size() > 0)
				{
				    return userObj;          
				}
				else
				{
					return  null;
				}
			}catch(Exception e)
			{
				return null;
			}
			
		}

		public List<feedback> getunreadfeedbacks() {
			// TODO Auto-generated method stub
			String sqlQuery = "from feedback where readstatus = ? ";
			String status = "Unread";
                try{
				
				List<feedback> userObj = (List) hibernateTemplate.find(sqlQuery,status);
				if(userObj.size() > 0)
				{
				    return userObj;          
				}
				else
				{
					return  null;
				}
			}catch(Exception e)
			{
				return null;
			}
		}

		public List<feedback> getreadfeedbacks() {
			// TODO Auto-generated method stub
			String sqlQuery = "from feedback where readstatus = ? ";
			String status = "Read";
                try{
				
				List<feedback> userObj = (List) hibernateTemplate.find(sqlQuery,status);
				if(userObj.size() > 0)
				{
				    return userObj;          
				}
				else
				{
					return  null;
				}
			}catch(Exception e)
			{
				return null;
			}
		}

		public boolean findUserAndMail(String username, String email) {
			// TODO Auto-generated method stub
			String sqlQuery = "from loginmodel u where u.UserID=?  and u.Email=?";
			 boolean isValidUser = false;
			try {
		        	
		        	
		            List<loginmodel> userObj = (List) hibernateTemplate.find(sqlQuery, username, email);
		            
		            if(userObj != null && userObj.size() > 0)
		            {
		                //log.info("Id= " + userObj.get(0)).getId() + ", Name= " + userObj.get(0).getName() + ", Password= " + userObj.get(0).getPassword());
		                isValidUser = true;
		            }
		           } 
		        catch(Exception e) 
		        {
		            isValidUser = false;
		           System.out.println("An error occurred while fetching the user details from the database");   
		        }
		        return isValidUser;
		}

		public boolean changePass(String username, String password) {
			// TODO Auto-generated method stub
			boolean isValid = false;
	        try {
			hibernateTemplate.bulkUpdate("update loginmodel set Password='" + password +"' where UserID = '"+ username +"'");
			isValid = true;
            
	        }

	        catch (Exception e) {
	               System.out.println("In the catch block of update..."+e);
	               isValid = false;
	               e.printStackTrace();
	               
	        }

	        return isValid;
		
		}

		public String findUsername(String username, String password) {
			// TODO Auto-generated method stub
			String sqlQuery = "from loginmodel u where u.UserID=? and u.Password=?";
		       
		       List<loginmodel> userObj = (List) hibernateTemplate.find(sqlQuery, username, password);
		if((userObj != null && userObj.size() > 0))
		{
		                for (loginmodel obj : userObj)
		                {
		                    System.out.println("login Type : "  +  obj.getType() );
		                    return obj.getUserID();     
		                }
		}
		else
		{
			String sqlQuery1 = "from loginmodel u where u.Email=?  and u.Password=?";
			List<loginmodel> userObj1 = (List) hibernateTemplate.find(sqlQuery1, username, password);
			if((userObj1 != null && userObj1.size() > 0))
			{
				 for (loginmodel obj : userObj1)
	                {
	                    System.out.println("login Type : "  +  obj.getType() );
	                    return obj.getUserID();   
	                }
			}
		}
		           
		           
			return null;
		}

		

		
	
	
	

	
	
	
	
}

package com.service;

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
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.pojo.adddoc;
import com.pojo.addhospitalmodel;
import com.pojo.feedback;
import com.pojo.patientBookingDetails;
import com.pojo.testModel;
import com.pojo.loginmodel;
import com.pojo.testModel;
import com.pojo.userhospitalmodel;
import com.pojo.weeklySchedule;
import com.service.AuthService;

public class HospitalService {
	
	private HibernateTemplate hibernateTemplate;
    private static Logger log = Logger.getLogger(HospitalService.class);
    private static SessionFactory factory; 
 
    private HospitalService() { }
 
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
 
    @SuppressWarnings( { "unchecked", "deprecation" } )
    
    
    public boolean addDoctortodb(adddoc userObj) {
		// TODO Auto-generated method stub
		try{
			hibernateTemplate.save(userObj);
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception :" + e);    
			return false;
		}
    	
		
	}
    


	public List<patientBookingDetails> displayPatientsForIndividualDoctor(String hospitaluniqueidinmap) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
		Date dateobj = new Date();
		System.out.println(df.format(dateobj));
		String today = df.format(dateobj).toString();
		String sqlQuery = "from patientBookingDetails where hospital_id=? and date = ?  order by doctor_id,slots";
        List<patientBookingDetails> userObj1 = (List) hibernateTemplate.find(sqlQuery,hospitaluniqueidinmap,today);
       
        if(userObj1.size() <= 0)
        {
        	return null;
        }
        else
        {
		    return userObj1;
        }
		
	}

	public List<patientBookingDetails> listpatients(String hospitaluniqueidinmap) {
		// TODO Auto-generated method stub
		String sqlQuery = "from patientBookingDetails  where hospital_id=?   ";
        List<patientBookingDetails> userObj1 = (List) hibernateTemplate.find(sqlQuery,hospitaluniqueidinmap);
        if(userObj1.size() <= 0)
        {
        	return null;
        }
        else
        {
		    return userObj1;
        }
		
	}

	public void addFeedbackToDatabase(feedback feedbackobj) {
		// TODO Auto-generated method stub
		
		hibernateTemplate.save(feedbackobj);
	}

	
	
	
	public List<weeklySchedule> displayWeeklyScheduleForIndividualDoctor(String hospitaluniqueidinmap, String day) {
		// TODO Auto-generated method stub
		Session session = hibernateTemplate.getSessionFactory().openSession();

	    Transaction tx = session.beginTransaction();
        String sql = "select doctor_name,date,count(slots) from patientbookingdetails where date = '"+day +"' and hospital_id = '"+hospitaluniqueidinmap +"' group by doctor_name order by doctor_name,date";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List data = query.list();
        
        if(data != null)
        {
        List<weeklySchedule> weekly = new ArrayList<weeklySchedule>();
        for(Object object : data) {
        	weeklySchedule obj = new weeklySchedule();
        	Map row = (Map)object;
            obj.setDoctorname(row.get("doctor_name").toString());
            obj.setDate(row.get("date").toString());
            obj.setCountOfSlots(row.get("count(slots)").toString());
           weekly.add(obj);
           System.out.println("Value in Service Name :" + obj.getDoctorname());
           System.out.println("Value in Service Slots :" + obj.getCountOfSlots());
         }
		tx.commit();
		session.close(); 


		    return weekly;
        }
        else return null;
        
	}

	public String dateforspecificdays(int i) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
		Date dateobj = new Date();
		System.out.println(df.format(dateobj));
		String input = df.format(dateobj).toString();
        String Date1="";
        
		try {
        	Date date = df.parse(input);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            System.out.println("Input " + input + " is in week " + week);
            
            if(i==1)
            {
            System.out.print("Sunday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else if(i==2)
            {
            System.out.print("Monday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else if(i==3)
            {
            System.out.print("Tuesday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else if(i==4)
            {
            System.out.print("Wednesday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else if(i==5)
            {
            System.out.print("Thursday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else if(i==6)
            {
            System.out.print("Friday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            System.out.println(df.format(cal.getTime()));
            Date1 = df.format(cal.getTime()).toString();
            }
            
            else 
            {
            System.out.print("Saturday :");
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            System.out.println(df.format(cal.getTime()));    
            Date1 = df.format(cal.getTime()).toString();
            }
            
        }   catch (ParseException e) 
		{
            System.out.println("Could not find a week in " + input);
        }
		
		return Date1;
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

	public List<weeklySchedule> weeklyScheduleToOrder(List<weeklySchedule> final_weekly_list) {
		
		
		// TODO Auto-generated method stub
		Collections.sort(final_weekly_list, new Sortbydoctorname()); 
		return final_weekly_list;
	}
	
	



public patientBookingDetails patientDetails(int id) {
	// TODO Auto-generated method stub
	 String sqlQuery = "from patientBookingDetails  where id = ? ";
     try{
          
          List<patientBookingDetails> userObj = (List) hibernateTemplate.find(sqlQuery,id);
          if(userObj.size()>0)
          {
          for(patientBookingDetails obj : userObj)
          {
               System.out.println("patientBookingDetails : " + obj.getPatientname());
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

public boolean addDetailsToDatabase(String disease, String prescription, int id) {
	// TODO Auto-generated method stub
	   try
       {
            
      String status = "TC";
     hibernateTemplate.bulkUpdate("update patientBookingDetails set prescriptionprovided = '" + prescription +"'  , disease = '" + disease +"', status = '" + status +"' where id = '"+ id +"'");
       return true;
       }
       catch(Exception e)
       {
            return false;
       }
}

}

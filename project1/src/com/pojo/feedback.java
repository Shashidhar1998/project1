package com.pojo;

public class feedback {

	private int  id;
	
	private String  name;
	private String  email;
	private String  subject;
	private String  message;
	private String  dateandtime;
	private String  readstatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDateandtime() {
		return dateandtime;
	}
	public void setDateandtime(String dateandtime) {
		this.dateandtime = dateandtime;
	}
	public String getReadstatus() {
		return readstatus;
	}
	public void setReadstatus(String readstatus) {
		this.readstatus = readstatus;
	}
}

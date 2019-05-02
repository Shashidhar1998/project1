package com.pojo;


import java.util.TreeMap;

public class DoctorSchedule {
private String docname;
public TreeMap<String,String> docSlot  = new TreeMap<String, String>(); 
public String getDocname() {
	return docname;
}
public void setDocname(String docname) {
	this.docname = docname;
}
public TreeMap<String, String> getDocSlot() {
	return docSlot;
}
public void setDocSlot(TreeMap<String, String> docSlot) {
	this.docSlot = docSlot;
}



}

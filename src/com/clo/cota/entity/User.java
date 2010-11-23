package com.clo.cota.entity;

public class User {
	private String firstname = null;
	private String lastname  = null;
	private String email = null;
	private String plz = null;
	private String city = null;
	private String address = null;
	private String firma = null;
	
	private int id;
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPlz() {
		return plz;
	}
	public String getZip() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public void setZip(String plz) {
		this.plz = plz;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	
	
}

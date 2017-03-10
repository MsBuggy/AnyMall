package io.gxu.anymall.model;

import java.sql.Date;

import org.json.JSONObject;

public class User {
	private String name;
	private long id;
	private Long gender;
	private Date dateOfBirth;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getGender() {
		return gender;
	}
	public void setGender(Long gender) {
		this.gender = gender;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	@Override
	public String toString() {
 		return new JSONObject(this).toString();
	}
}

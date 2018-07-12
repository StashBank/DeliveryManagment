package com.stashbank.deliveryManagement.models;

import java.util.Date;

public class Item
{
	private String id;
	private Date createdAt;
	private String number;
	private String address;
	private String client;
	private String mobile;
	private double amount;
	
	public Item() {}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getClient() {
		return client;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
	
	

}

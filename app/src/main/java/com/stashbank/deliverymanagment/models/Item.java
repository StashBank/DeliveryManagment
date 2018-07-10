package com.stashbank.deliverymanagment.models;

public class Item
{
	private String id;
	private String number;
	private String address;
	private String client;
	private String mobile;
	private double amount;
	
	public Item() {}
	
	/*public Item(
		String id,
		String number,
		String address,
		String client,
		String mobile,
		double amount
	) {
		this.id = id;
		this.number = number;
		this.amount = amount;
		this.address = address;
		this.client = client;
		this.mobile = mobile;
	}*/

	public void setClient(String client) {
		this.client = client;
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

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
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

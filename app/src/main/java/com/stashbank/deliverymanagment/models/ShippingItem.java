package com.stashbank.deliverymanagment.models;

public class ShippingItem extends Item
{
	private Boolean shipped;

	public ShippingItem(
	String id,
	String number,
	String address,
	String client,
	double amount,
	boolean shipped
	) {
		super(id, number, address, client, amount);
		this.shipped = shipped;
	}

	public void setShipped(Boolean shipped)
	{
		this.shipped = shipped;
	}

	public Boolean getShipped()
	{
		return shipped;
	}
}

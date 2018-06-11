package com.stashbank.deliverymanagment.models;

public class ShippingItem extends Item
{
	private Boolean shipped;

	public ShippingItem(
	int id,
	String number,
	String address,
	double amount,
	boolean shipped
	) {
		super(id, number, address, amount);
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

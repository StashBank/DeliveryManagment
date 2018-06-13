package com.stashbank.deliverymanagment.models;

public class DeliveryItem extends Item
{
	boolean delivered = false;

	public DeliveryItem(
		String id,
		String number,
		String address,
		double amount
	) {
		super(id, number, address, amount);
	}

	public void setDelivered(boolean delivered)
	{
		this.delivered = delivered;
	}

	public boolean getDelivered()
	{
		return delivered;
	}
}

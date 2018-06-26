package com.stashbank.deliverymanagment.models;

public class DeliveryItem extends Item
{
	boolean delivered = false;
	boolean payed = false;
	
	public DeliveryItem() {}

	public DeliveryItem(
		String id,
		String number,
		String address,
		String client,
		double amount
	) {
		super(id, number, address, client, amount);
	}

	public void setPayed(boolean payed)
	{
		this.payed = payed;
	}

	public boolean isPayed()
	{
		return payed;
	}

	public void setDelivered(boolean delivered)
	{
		this.delivered = delivered;
	}

	public boolean isDelivered()
	{
		return delivered;
	}
}

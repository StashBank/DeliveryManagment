package com.stashbank.deliverymanagment.models;

public class DeliveryItem extends Item
{
	Boolean delivered;

	public DeliveryItem(
		int id,
		String number,
		String address,
		double amount
	) {
		super(id, number, address, amount);
	}

	public void setDelivered(Boolean delivered)
	{
		this.delivered = delivered;
	}

	public Boolean getDelivered()
	{
		return delivered;
	}
}

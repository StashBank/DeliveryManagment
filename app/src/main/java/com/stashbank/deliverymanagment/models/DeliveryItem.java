package com.stashbank.deliverymanagment.models;

import java.util.Date;

public class DeliveryItem extends Item
{

	Date deliveryDate;
	boolean delivered = false;
	boolean payed = false;
	
	public DeliveryItem() {}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public boolean isDelivered() {
		return delivered;
	}
}

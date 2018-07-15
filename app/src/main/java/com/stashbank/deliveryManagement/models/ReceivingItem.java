package com.stashbank.deliveryManagement.models;

public class ReceivingItem extends Item
{
	private boolean received;

	public void setReceived(boolean received) {
		this.received = received;
	}

	public boolean isReceived() {
		return received;
	}
}

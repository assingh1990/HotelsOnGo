package com.hotelongo.payment_service.event;

public class PaymentFailedEvent {

	private String bookingId;
	private String reason;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}

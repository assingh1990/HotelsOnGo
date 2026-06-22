package com.hotelongo.payment_service.event;

public class PaymentFailedEvent {

	private Long bookingId;
	private String reason;

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}

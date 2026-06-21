package com.hotelsongo.booking_service.event_dto;

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


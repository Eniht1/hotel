package com.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto {

	private Long roomId;
	
	private Integer adult;
	
	private Integer child;
	
	private String checkIn;
	
	private String checkOut;
}

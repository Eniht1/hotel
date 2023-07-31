package com.hotel.dto;

import java.util.ArrayList;
import java.util.List;

import com.hotel.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	
	public ReservationHistDto(Reservation reservation) {
	}
	
	private Long reservationId;
	
	private String adult;
	
	private String child;
	
	private String checkIn;
	
	private String checkOut;
	
	private List<ReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
	
	public void addReservationRoomDto(ReservationRoomDto reservationRoomDto) {
		this.reservationRoomDtoList.add(reservationRoomDto);
	}
	
	
	
}

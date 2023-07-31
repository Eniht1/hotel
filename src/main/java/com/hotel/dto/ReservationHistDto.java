package com.hotel.dto;

import java.util.ArrayList;
import java.util.List;

import com.hotel.constant.ReservationStatus;
import com.hotel.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	
	public ReservationHistDto(Reservation reservation) {
		this.adult = reservation.getAdult();
		this.child = reservation.getChild();
		this.reservationStatus = reservation.getReservationStatus();
		this.reservationId = reservation.getId();
	}
	
	private Long reservationId;
	
	private Integer adult;
	
	private Integer child;
	
	private String checkIn;
	
	private String checkOut;
	
	private ReservationStatus reservationStatus;
	
	private List<ReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
	
	public void addReservationRoomDto(ReservationRoomDto reservationRoomDto) {
		this.reservationRoomDtoList.add(reservationRoomDto);
	}
	
	
	
}

package com.hotel.dto;

import com.hotel.entity.ReservationRoom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRoomDto {

	public ReservationRoomDto(ReservationRoom reservationRoom, String imgUrl) {
		this.roomName = reservationRoom.getRoom().getRoomName();
		this.reservationPrice = reservationRoom.getReservationPrice();
		this.imgUrl = imgUrl;
		this.adult = reservationRoom.getAdult();
		this.child = reservationRoom.getChild();
	}
	
	private String roomName;
	
	private Integer reservationPrice;
	
	private String imgUrl;
	
	private Integer adult;
	
	private Integer child;
}

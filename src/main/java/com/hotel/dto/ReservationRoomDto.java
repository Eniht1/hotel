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
	}
	
	private String roomName;
	
	private Integer reservationPrice;
	
	private String imgUrl;
}

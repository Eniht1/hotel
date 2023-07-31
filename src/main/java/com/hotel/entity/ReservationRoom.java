package com.hotel.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation_room")
@Getter
@Setter
@ToString
public class ReservationRoom extends BaseEntity {
	
	@Id
	@Column(name = "reservation_room_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private Room room;
	
	private Integer reservationPrice;
	
	private Integer adult;
	
	private Integer child;
	
	public static ReservationRoom createReservationRoom(Room room) {
		ReservationRoom reservationRoom = new ReservationRoom();
		reservationRoom.setRoom(room);
		reservationRoom.setReservationPrice(room.getPrice());
		
		return reservationRoom;
	}
	
	public void cancel() {
		this.getRoom();
	}
}
package com.hotel.entity;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.tools.Trace;

import com.hotel.dto.ReservationDto;

import groovy.transform.ToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@ToString
public class Reservation extends BaseEntity {
	
	@Id
	@Column(name = "reservation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer adult;
	
	private Integer child;
	
	private String checkIn;
	
	private String checkOut;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL,
			orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ReservationRoom> reservationRooms = new ArrayList<>();
	
	public void addReservationRoom(ReservationRoom reservationRoom) {
		this.reservationRooms.add(reservationRoom);
		reservationRoom.setReservation(this);
	}
	
	public static Reservation createReservation(Member member, List<ReservationRoom> reservationRoomList, ReservationDto reservationDto) {
		Reservation reservation = new Reservation();
		reservation.setMember(member);
		
		for(ReservationRoom reservationRoom : reservationRoomList) {
			reservation.addReservationRoom(reservationRoom);
		}
		
		reservation.setAdult(reservationDto.getAdult());
		reservation.setChild(reservationDto.getChild());
		reservation.setCheckIn(reservationDto.getCheckIn());
		reservation.setCheckOut(reservationDto.getCheckOut());
		
		return reservation;
	}
}
package com.hotel.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hotel.constant.RoomSellStatus;
import com.hotel.dto.RoomFormDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString
public class Room extends BaseEntity {
	
	@Id
	@Column(name = "room_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@OnDelete(action= OnDeleteAction.CASCADE)
	private Long id;
	
	@Column(nullable = false)
	private String roomName;
	
	@Column(nullable = false)
	private Integer price;
	
	@Column(nullable = false)
	private String roomType;
	
	@Column(nullable = false)
	private Integer maxPerson;
	
	@Lob
	@Column(nullable = false, columnDefinition = "longtext")
	private String description;
	
	//private String imgUrl; //이미지 조회 경로
	
	@Enumerated(EnumType.STRING)
	private RoomSellStatus roomSellStatus;
	
	public void updateRoom(RoomFormDto roomFormDto) {
		this.roomName = roomFormDto.getRoomName();
		this.price = roomFormDto.getPrice();
		this.roomType = roomFormDto.getRoomType();
		this.description = roomFormDto.getDescription();
		this.maxPerson = roomFormDto.getMaxPerson();
		this.roomSellStatus = roomFormDto.getRoomSellStatus();
	}
}

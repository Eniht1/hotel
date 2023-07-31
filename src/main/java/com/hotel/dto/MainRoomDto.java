package com.hotel.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainRoomDto {
	private Long id;
	
	private String roomName;
	
	private Integer price;
	
	private String imgUrl;
	
	private String roomType;
	
	private Integer maxPerson;
	
	private String description;
	
	
	@QueryProjection
	public MainRoomDto(Long id, String roomName, Integer price, String imgUrl, String roomType, 
			Integer maxPerson, String description) {
		this.id = id;
		this.roomName = roomName;
		this.price = price;
		this.imgUrl = imgUrl;
		this.roomType = roomType;
		this.maxPerson = maxPerson;
		this.description = description;
	}
}

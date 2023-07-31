package com.hotel.dto;

import com.hotel.constant.RoomSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomSearchDto {
	private String searchDateType;
	private RoomSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery = "";

}

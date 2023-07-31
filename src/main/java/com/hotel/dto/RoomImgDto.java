package com.hotel.dto;

import org.modelmapper.ModelMapper;

import com.hotel.entity.RoomImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repimgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static RoomImgDto of(RoomImg roomimg) {
		return modelMapper.map(roomimg, RoomImgDto.class);
	}
}

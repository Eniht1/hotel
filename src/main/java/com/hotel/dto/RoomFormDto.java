package com.hotel.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.hotel.entity.Room;
import com.hotel.constant.RoomSellStatus;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class RoomFormDto {
	private Long id;
	
	@NotBlank(message = "객실 이름은 필수로 입력해야 합니다.")
	private String roomName;
	
	@NotNull(message = "가격은 필수로 입력해야 합니다.")
	private Integer price;
	
	@NotBlank(message = "객실타입은 필수로 입력해야 합니다.")
	private String roomType;
	
	@NotBlank(message = "상세설명은 필수로 입력해야 합니다.")
	private String description;
	
	private String imgUrl; //이미지 조회 경로
	
	@NotNull(message = "최대인원수는 필수로 입력해야 합니다.")
	private Integer maxPerson;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	private List<RoomImgDto> roomImgDtoList = new ArrayList<>();
	
	private RoomSellStatus roomSellStatus;
	
	//상품 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도.
	private List<Long> roomImgIds = new ArrayList<>();
	
	//dto -> entity로 바꿈
	public Room createRoom() {
		return modelMapper.map(this, Room.class);
	}
	
	//entity -> dto로 바꿈	
	public static RoomFormDto of (Room room) {
		return modelMapper.map(room, RoomFormDto.class);
	}
}

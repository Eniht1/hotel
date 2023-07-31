package com.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hotel.dto.MainRoomDto;
import com.hotel.dto.RoomSearchDto;
import com.hotel.entity.Room;

public interface RoomRepositoryCustom {
	Page<Room> getAdminRoomPage(RoomSearchDto roomsearchDto, Pageable pageable);
	
	Page<MainRoomDto> getMainRoomPage(RoomSearchDto roomSearchDto, Pageable pageable);
}

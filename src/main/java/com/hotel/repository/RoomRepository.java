package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>,
										RoomRepositoryCustom{
	List<Room> findByRoomName(String roomName);

}

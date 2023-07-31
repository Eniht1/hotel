package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.entity.RoomImg;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
	List<RoomImg> findByRoomIdOrderByIdAsc(Long roomId);
	
	RoomImg findByRoomIdAndRepimgYn(Long roomId, String repimgYn);
}

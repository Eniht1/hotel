package com.hotel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.dto.RoomFormDto;
import com.hotel.entity.Room;
import com.hotel.entity.RoomImg;
import com.hotel.repository.MemberRepository;
import com.hotel.repository.RoomImgRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.dto.RoomImgDto;
import com.hotel.dto.MainRoomDto;
import com.hotel.dto.RoomSearchDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
	private final RoomRepository roomRepository;
	private final RoomImgService roomImgService;
	private final RoomImgRepository roomImgRepository;
	
	//room 테이블에 상품 등록(insert)
	public Long saveRoom(RoomFormDto roomFormDto, List<MultipartFile> roomImgFileList) throws Exception {
		Room room = roomFormDto.createRoom();
		roomRepository.save(room);
		
		for(int i=0; i<roomImgFileList.size(); i++) {
			RoomImg roomImg = new RoomImg();
			roomImg.setRoom(room);
			
			if(i == 0) {
				roomImg.setRepimgYn("Y");
			} else {
				roomImg.setRepimgYn("N");
			}
			
			roomImgService.saveRoomImg(roomImg, roomImgFileList.get(i));
			
		}
		
		return room.getId();
	}
	
	//상품 가져오기
	@Transactional(readOnly = true) //트랜잭션 읽기 전용(변경감지 수행하지 않음) -> 성능향상
	public RoomFormDto getRoomDtl(Long roomId) {
		//1.item_img 테이블의 이미지를 가져온다.
		List<RoomImg> roomImgList = roomImgRepository.findByRoomIdOrderByIdAsc(roomId);
		
		//ItemImg 엔티티 객체 -> RoomImgDto로 변환
		List<RoomImgDto> roomImgDtoList = new ArrayList<>();
		for(RoomImg roomImg : roomImgList) {
			RoomImgDto roomImgDto = RoomImgDto.of(roomImg);
			roomImgDtoList.add(roomImgDto);
		}
		
		
		//2.room 테이블에 있는 데이터를 가져온다.
		Room room = roomRepository.findById(roomId)
					              .orElseThrow(EntityNotFoundException::new);
		
		//Room 엔티티 객체 -> dto로 변환
		RoomFormDto roomFormDto = RoomFormDto.of(room);
		
		
		//3.roomFormDto에 이미지 정보(roomImgDtoList)를 넣어준다.
		roomFormDto.setRoomImgDtoList(roomImgDtoList);
		
		return roomFormDto;
	}
	
	public Long updateRoom(RoomFormDto roomFormDto, 
			List<MultipartFile> roomImgFileList) throws Exception {
		
		//1. room 엔티티 가져와서 바꾼다.
		Room room = roomRepository.findById(roomFormDto.getId())
				         .orElseThrow(EntityNotFoundException::new);
		//update쿼리문 실행
		/* ★★★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
		그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고
		엔티티 데이터만 변경해주면 된다. */
		room.updateRoom(roomFormDto);
		
		//2. item_img를 바꿔준다. -> 5개의 레코드 전부 변경
		List<Long> roomImgIds = roomFormDto.getRoomImgIds(); //상품 이미지 아이디 리스트 조회
		for(int i=0; i<roomImgFileList.size(); i++) {
			roomImgService.updateRoomImg(roomImgIds.get(0), roomImgFileList.get(0));
		}
		
		return room.getId(); //변경한 room의 id 리턴
		
	}
	
	//주문 삭제
	public void deleteRoom(Long roomId) {
		System.out.println("11111111111111");
		//★delete하기 전에 select 를 한번 해준다
		//->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
		System.out.println("22222222222222222");

		//delete
		roomRepository.delete(room);
		System.out.println("33333333333333333333");
	}
	
	@Transactional(readOnly = true)
	public Page<Room> getAdminRoomPage(RoomSearchDto roomSearchDto, Pageable pageable) {
		Page<Room> roomPage= roomRepository.getAdminRoomPage(roomSearchDto, pageable);
		return roomPage;
	}
	
	@Transactional(readOnly = true)
	public Page<MainRoomDto> getMainRoomPage(RoomSearchDto roomSearchDto, Pageable pageable) {
		Page<MainRoomDto> mainItemPage = roomRepository.getMainRoomPage(roomSearchDto, pageable);
		return mainItemPage;
	}

}

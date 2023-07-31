package com.hotel.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.hotel.dto.ReservationDto;
import com.hotel.dto.ReservationHistDto;
import com.hotel.dto.ReservationRoomDto;
import com.hotel.entity.ReservationRoom;
import com.hotel.entity.Room;
import com.hotel.entity.RoomImg;
import com.hotel.entity.Member;
import com.hotel.entity.Reservation;
import com.hotel.repository.MemberRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomImgRepository;
import com.hotel.repository.RoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
	private final RoomRepository roomRepository;
	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	private final RoomImgRepository roomImgRepository;
	
	//예약하기
	public Long reservation(ReservationDto reservationDto, String email) {
		
		//1.예약할 상품을 조회
		Room room = roomRepository.findById(reservationDto.getRoomId())
				                  .orElseThrow(EntityNotFoundException::new);
		
		//2.현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);
		
		//3.주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
		List<ReservationRoom> reservationRoomList = new ArrayList<>();
		ReservationRoom reservationRoom = ReservationRoom.createReservationRoom(room);
		reservationRoomList.add(reservationRoom);
		
		//4.회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
		Reservation reservation = Reservation.createReservation(member, reservationRoomList, reservationDto);
		reservationRepository.save(reservation);
		
		return reservation.getId();
	}
	
	@Transactional(readOnly = true)
	public List<ReservationHistDto> myReservationList(String email) {
		Member member = memberRepository.findByEmail(email);
		
		List<Reservation> memberReservationList = reservationRepository.getMemberReservationList(member.getId());
		List<ReservationHistDto> reservationHistDtoList = new ArrayList<>();
		
		for(Reservation reservation : memberReservationList) {
			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation);
			List<ReservationRoom> reservationRoomList = reservation.getReservationRooms();
			
			for(ReservationRoom reservationRoom : reservationRoomList) {
				RoomImg roomImg = roomImgRepository.findByRoomIdAndRepimgYn(reservationRoom.getRoom().getId(), "Y");
				
				ReservationRoomDto reservationRoomDto = new ReservationRoomDto(reservationRoom, roomImg.getImgUrl());
				reservationHistDto.addReservationRoomDto(reservationRoomDto);
			}
			
			reservationHistDtoList.add(reservationHistDto);
		}
		
		return reservationHistDtoList;
	}
	
	public boolean validateReservation(Long reservationId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		Reservation reservation = reservationRepository.findById(reservationId)
													   .orElseThrow(EntityNotFoundException::new);
		Member savedMember = reservation.getMember();
		
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
		
	}
	
	public void cancelReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
													   .orElseThrow(EntityNotFoundException::new);
		reservation.cancelReservation();
	}
	
	public void deleteReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
													   .orElseThrow(EntityNotFoundException::new);
		reservationRepository.delete(reservation);
	}
}

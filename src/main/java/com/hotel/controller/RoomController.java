package com.hotel.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.dto.MainRoomDto;
import com.hotel.dto.ReservationDto;
import com.hotel.dto.RoomFormDto;
import com.hotel.dto.RoomSearchDto;
import com.hotel.entity.Room;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {
	
	private final RoomService roomService;
	
	//방 전체 리스트
	@GetMapping(value = "/room/list")
	public String roomList(Model model, RoomSearchDto roomSearchDto, 
			Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainRoomDto> rooms = roomService.getMainRoomPage(roomSearchDto, pageable);
		
		model.addAttribute("rooms", rooms);
		model.addAttribute("roomSearchDto", roomSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "room/roomlist";
	}
	//방 등록 페이지
	@GetMapping(value = "/admin/room/new")
	public String roomForm(Model model) {
		model.addAttribute("roomFormDto", new RoomFormDto());
		return "room/roomForm";
	}
	
	//방 정보 보여주는 페이지
	@GetMapping(value = { "/rooms", "/rooms/{page}" })
	public String roomDetailForm(Model model, RoomSearchDto roomSearchDto, @PathVariable("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		
		Page<MainRoomDto> rooms = roomService.getMainRoomPage(roomSearchDto, pageable);
		
		model.addAttribute("rooms", rooms);
		model.addAttribute("roomSearchDto", roomSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "room/rooms";
	}
	
	//방 등록, 방 이미지 등록(insert)
	@PostMapping(value = "/admin/room/new")
	public String roomNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult,
			Model model, @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {
		if(bindingResult.hasErrors()) {
			return "room/roomForm";
		}
		
		if(roomImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다");
			return "room/roomForm";
		}
		
		try {
			roomService.saveRoom(roomFormDto, roomImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
			return "room/roomForm";
		}
		
		return "redirect:/";
	}
	
	//상품 관리페이지
	@GetMapping(value = { "/admin/rooms", "/admin/rooms/{page}" })
	public String roomManage(RoomSearchDto roomSearchDto,
			@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		
		Page<MainRoomDto> rooms = roomService.getMainRoomPage(roomSearchDto, pageable);
		
		model.addAttribute("rooms", rooms);
		model.addAttribute("roomSearchDto", roomSearchDto);
		model.addAttribute("maxPage", 5); //상품관리 페이지 하단에 보여줄 최대 페이지 번호
		
		return "room/roomMng";
	}
	
	@GetMapping(value = "rooms/detail/{roomId}")
	public String roomDetail(Model model, @PathVariable("roomId") Long roomId) {
		RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);
		model.addAttribute("room", roomFormDto);
		model.addAttribute("reservationDto", new ReservationDto());
		
		return "room/roomDetail";
	}
	
	//상품수정페이지보기
	@GetMapping(value = "/admin/room/{roomId}")
	public String roomDtl(@PathVariable("roomId") Long roomId, Model model) {
		try {
			RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);
			
			model.addAttribute("roomFormDto", roomFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품정보를 가져올때 에러가 발생했습니다.");
			//에러발생시 비어있는 객체를 넘겨준다.
			model.addAttribute("roomFormDto", new RoomFormDto());
			return "room/roomForm";
		}
		
		return "room/roomModifyForm";
	}
	
	//상품 수정(update)
	@PostMapping(value = "/admin/room/{roomId}")
	public String roomUpdate(@Valid RoomFormDto roomFormDto, Model model,
			BindingResult bindingResult, 
			@RequestParam("roomImgFile") List<MultipartFile> roomImgFileList) {

		if(bindingResult.hasErrors()) {
			return "room/roomForm";
		}
		
		//첫번째 이미지가 있는지 검사
		if(roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "room/roomForm";
		}
		
		
		try {
			roomService.updateRoom(roomFormDto, roomImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
			return "room/roomForm";
		}
		
		return "redirect:/";
	}
	
	@DeleteMapping("/admin/{roomId}/delete")
	public @ResponseBody ResponseEntity deleteRoom(@PathVariable("roomId") Long roomId, Principal principal) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<Long>(roomId, HttpStatus.OK);
	}

	
	
}

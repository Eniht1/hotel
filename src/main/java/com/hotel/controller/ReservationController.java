package com.hotel.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.dto.ReservationDto;
import com.hotel.dto.ReservationHistDto;
import com.hotel.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@PostMapping(value = "/reservation")
	public String reservation(@Valid ReservationDto reservationDto, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for (FieldError fieldError : fieldErrors) {
				// 에러 메세지 합치기
				sb.append(fieldError.getDefaultMessage());
			}
		}
		String email = principal.getName();
		Long reservationId;
		
		DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM월 dd일");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy", Locale.ENGLISH);
		LocalDate startDate = LocalDate.parse(reservationDto.getCheckIn(), formatter); 
		LocalDate endDate = LocalDate.parse(reservationDto.getCheckOut(), formatter);
		String newFormattedEndDate = endDate.format(newFormatter);
		String newFormattedStartDate = startDate.format(newFormatter);
		reservationDto.setCheckIn(newFormattedStartDate);
		reservationDto.setCheckOut(newFormattedStartDate);

		try {
			reservationId = reservationService.reservation(reservationDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	//예약내역을 보여준다.
	@GetMapping(value = {"/reservations", "/reservations/{page}"})
	public String ReservationHist(Principal principal
			, Model model) {
		List<ReservationHistDto> reservationHistDtoList = reservationService.myReservationList(principal.getName());
		

		model.addAttribute("reservations", reservationHistDtoList);
		
		return "reservation/reservationHist";
	}
	
	@PostMapping("/reservation/{reservationId}/cancel")
	public @ResponseBody ResponseEntity cancelReservation(@PathVariable("reservationId") Long reservationId, Principal principal) {
		if(!reservationService.validateReservation(reservationId, principal.getName())) {
			return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		reservationService.cancelReservation(reservationId);
		return new ResponseEntity<Long>(reservationId, HttpStatus.OK);
	}
	
	@DeleteMapping("/reservation/{reservationId}/delete")
	public @ResponseBody ResponseEntity deleteReservation(@PathVariable("reservationId") Long reservationId, Principal principal) {
		if(!reservationService.validateReservation(reservationId, principal.getName())) {
			return new ResponseEntity<String>("주문 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		reservationService.deleteReservation(reservationId);
		return new ResponseEntity<Long>(reservationId, HttpStatus.OK);
	}
}

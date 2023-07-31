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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.dto.ReservationDto;
import com.hotel.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@PostMapping(value = "/reservation")
	public String reservation(@Valid ReservationDto reservationDto, BindingResult bindingResult, Principal principal
							) {
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
	public String ReservationHist(@PathVariable("page") Optional<Integer> page, Principal principal
			, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		

		model.addAttribute("maxPage", 5);
		
		return "reservation/reservationHist";
	}

}

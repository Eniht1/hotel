package com.hotel.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query
	List<Reservation> findReservations(@Param("email") String email, Pageable pageable);
	
	@Query
	Long countReservation(@Param("email") String email);
}

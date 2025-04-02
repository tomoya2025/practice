package com.example.samuraitravel.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.ReservationRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	
	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
	
	public Page<Reservation> findReservationByUserOrderByCreatedAtDesc(User user, Pageable pageable){
		return reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
	}
	
	public boolean isCheckinBeforeCheckout(LocalDate checkinDate, LocalDate checkoutDate) {
		return checkinDate.isBefore(checkoutDate);
	}
	
	public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
		return numberOfPeople <= capacity;
	}
	
	public String getPreviousDates(LocalDate checkinDate, LocalDate checkoutDate, BindingResult bindingResult) {
		if (checkinDate != null && checkoutDate != null && !bindingResult.hasFieldErrors("checkinDate") && !bindingResult.hasFieldErrors("checkoutDate")) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedCheckinDate = checkinDate.format(dateTimeFormatter);
			String formattedCheckoutDate = checkoutDate.format(dateTimeFormatter);
		
			return formattedCheckinDate + " から " + formattedCheckoutDate;
		}
		
		return "";
	}
	
	public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
		long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
		int amount = price * (int)numberOfNights;
		
		return amount;
	}
}

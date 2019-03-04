package com.techelevator.dao;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.reservations.Reservations;

import Site.Sites;

public interface ReservationsDAO {
	
public long makeReservation(LocalDate arrival, LocalDate depature, String resName, Sites site);

public List<Reservations> returnReservations30DaysByParkId(Long parkId);

}

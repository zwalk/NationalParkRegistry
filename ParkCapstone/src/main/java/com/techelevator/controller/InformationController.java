package com.techelevator.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.dao.CampgroundsDAO;
import com.techelevator.dao.ParksDAO;
import com.techelevator.dao.ReservationsDAO;
import com.techelevator.dao.SitesDAO;
import com.techelevator.jdbcdao.JDBCCampgroundsDAO;
import com.techelevator.jdbcdao.JDBCParksDAO;
import com.techelevator.jdbcdao.JDBCReservationsDAO;
import com.techelevator.jdbcdao.JDBCSitesDAO;
import com.techelevator.parks.Parks;
import com.techelevator.reservations.Reservations;

import Site.Sites;

public class InformationController {
	ParksDAO jdbcParksDAO;
	CampgroundsDAO jdbcCampgroundsDAO;
	SitesDAO jdbcSitesDAO;
	ReservationsDAO jdbcReservationDAO;
	
	public InformationController(DataSource ds) {
		this.jdbcParksDAO = new JDBCParksDAO(ds);
		this.jdbcCampgroundsDAO = new JDBCCampgroundsDAO(ds);
		this.jdbcSitesDAO = new JDBCSitesDAO(ds);
		this.jdbcReservationDAO = new JDBCReservationsDAO(ds);
	}
	
	public List<Parks> getAllParks(){
		
		return jdbcParksDAO.getAllParks();
	}
	public List<Campgrounds> getCampgroundsByParkId(Parks userChoiceAsPark){
		return jdbcCampgroundsDAO.getAllCampgroundsByParkId(userChoiceAsPark.getParkId());
	}
	public List<Sites> getTopFiveSites(Campgrounds userCampgroundChoiceAsCampground, LocalDate userArrivalDate, LocalDate userDepartureDate) {
		return jdbcSitesDAO.getTopFiveSites(userCampgroundChoiceAsCampground, userArrivalDate, userDepartureDate);
	}
	
	public List<Sites> getTopFiveSitesAdvancedSearch(Campgrounds userCampgroundChoiceAsCampground, LocalDate userArrivalDate, LocalDate userDepartureDate, int maxOccupancy, boolean isAccessible, int maxRVLength, boolean hasUtilities) {
		return jdbcSitesDAO.getTopFiveSitesByAdvancedSearch(userCampgroundChoiceAsCampground, 
				userArrivalDate, userDepartureDate, maxOccupancy, isAccessible, maxRVLength, hasUtilities);
	}
	
	public long makeReservation(LocalDate userArrivalDate, LocalDate userDepartureDate, String resName, Sites siteChoiceAsSite) {
		return jdbcReservationDAO.makeReservation(userArrivalDate, userDepartureDate, resName, siteChoiceAsSite);
	}
	
	public ReservationsDAO getJdbcReservationDAO() {
		return jdbcReservationDAO;
	}

	public List<Sites> getTopFiveSitesByPark(Parks userChoiceAsPark, LocalDate userArrivalDate, LocalDate userDepartureDate){
		return jdbcSitesDAO.getTopFiveSitesByPark(userChoiceAsPark, userArrivalDate, userDepartureDate);
	}
	public BigDecimal getDailyFeeBasedOnCampgroundId(Sites site) {
		return jdbcSitesDAO.getDailyFeeBasedOnCampgroundId(site);
	}
	
	public String getCampNameFromDataBase(Sites site) {
		return jdbcSitesDAO.getCampNameFromDatabase(site);
	}

	public void setJdbcReservationDAO(ReservationsDAO jdbcReservationDAO) {
		this.jdbcReservationDAO = jdbcReservationDAO;
	}
	
	public List<Reservations> returnReservations30DaysByParkId(Long parkId) {
		return jdbcReservationDAO.returnReservations30DaysByParkId(parkId);
	}

	public CampgroundsDAO getJdbcCampgroundsDAO() {
		return jdbcCampgroundsDAO;
	}

	public SitesDAO getJdbcSitesDAO() {
		return jdbcSitesDAO;
	}

	public void setJdbcSitesDAO(SitesDAO jdbcSitesDAO) {
		this.jdbcSitesDAO = jdbcSitesDAO;
	}

	public void setJdbcCampgroundsDAO(CampgroundsDAO jdbcCampgroundsDAO) {
		this.jdbcCampgroundsDAO = jdbcCampgroundsDAO;
	}

	public ParksDAO getJdbcParksDAO() {
		return jdbcParksDAO;
	}

	public void setJdbcParksDAO(ParksDAO jdbcParksDAO) {
		this.jdbcParksDAO = jdbcParksDAO;
	}
	
	
}

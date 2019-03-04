package com.techelevator;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.controller.InformationController;
import com.techelevator.jdbcdao.JDBCReservationsDAO;
import com.techelevator.parks.Parks;
import com.techelevator.reservations.Reservations;

import Site.Sites;

public class InformationControllerTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationsDAO resDAO;
	private JdbcTemplate jdbc;
	private Campgrounds testCampground;
	private Parks testPark;
	private Sites testSite;
	private Reservations testRes;
	private static final String RES_NAME = "Test Reservation";
	private static final LocalDate RES_ARRIVE_DATE = LocalDate.of(2300, 02, 23);
	private static final LocalDate RES_DEPART_DATE = LocalDate.of(2300, 02, 26);
	private static final int SITE_NUMBER = 290;
	private static final int MAX_OCCUPANCY = 100;
	private static final boolean ACCESSIBLE = true;
	private static final int MAX_RV_LENGTH = 50;
	private static final boolean UTILITIES = true;
	private static final String CAMP_NAME = "Camp Name";
	private static final String OPEN_FROM = "05";
	private static final String OPEN_UNTIL = "08";
	private static final BigDecimal FEE = new BigDecimal(900);
	private static final String PARK_NAME = "Test Park";
	private static final String PARK_LOCATION = "Location";
	private static final LocalDate ESTABLISH_DATE = LocalDate.of(1990, 3, 28);
	private static final int PARK_AREA = 111111;
	private static final int PARK_VISITORS = 2222;
	private static final String DESCRIPTION = "Good";
	private InformationController infoControl;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@Before
	public void setup() {
		jdbc = new JdbcTemplate(dataSource);
		testPark = makePark(PARK_NAME, PARK_LOCATION, ESTABLISH_DATE, PARK_AREA, PARK_VISITORS, DESCRIPTION);
		insertPark(testPark);
		testCampground = makeCampground(testPark.getParkId(), CAMP_NAME, OPEN_FROM, OPEN_UNTIL, FEE);
		insertCampground(testCampground);
		testSite = makeSite(testPark.getParkId(), testCampground.getCampgroundId(), SITE_NUMBER, MAX_OCCUPANCY, ACCESSIBLE, MAX_RV_LENGTH, UTILITIES);
		insertSite(testSite);
		testRes = new Reservations();
		testRes.setFromDate(RES_ARRIVE_DATE);
		testRes.setName(RES_NAME);
		testRes.setSiteId(testSite.getSiteId());
		testRes.setToDate(RES_DEPART_DATE);
		resDAO = new JDBCReservationsDAO(dataSource);
		infoControl = new InformationController(dataSource);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_parks_gets_all_parks() {
		List<Parks> parkList = infoControl.getAllParks();
		
		Assert.assertTrue(parkList.contains(testPark));
	}
	
	@Test
	public void get_campgrounds_by_park_id_gets_campgrounds() {
		List<Campgrounds> campgroundList = infoControl.getCampgroundsByParkId(testPark);
		
		Assert.assertTrue(campgroundList.contains(testCampground));
	}
	
	@Test
	public void get_top_sites_returns_top_five_sites() {
		List<Sites> sitesList = infoControl.getTopFiveSites(testCampground, LocalDate.now().plusDays(100), LocalDate.now().plusDays(102));
		
		Assert.assertTrue(sitesList.contains(testSite));
	}
	
	@Test
	public void make_reservation_makes_reservation() {
		long resId = infoControl.makeReservation(RES_ARRIVE_DATE, RES_DEPART_DATE, RES_NAME, testSite);
		testRes.setReservationId(resId);
		String sqlGetReservations = "SELECT * FROM reservation WHERE site_id = ?;";		
		SqlRowSet results = jdbc.queryForRowSet(sqlGetReservations, testSite.getSiteId());
		List<Reservations> reservations = new ArrayList<>();
		
		while(results.next()) {
			Reservations r = mapToRowReservations(results);
			reservations.add(r);
	
		}
		
		Assert.assertTrue(reservations.contains(testRes));
	}
	
	@Test
	public void make_reservation_does_not_input_overlapping_from_date_reservations() {
		long resId = infoControl.makeReservation(RES_ARRIVE_DATE, RES_DEPART_DATE, RES_NAME, testSite);
		testRes.setReservationId(resId);
		String sqlGetReservations = "SELECT * FROM reservation WHERE site_id = ?;";		
		SqlRowSet results = jdbc.queryForRowSet(sqlGetReservations, testSite.getSiteId());
		List<Reservations> reservations = new ArrayList<>();
		
		long resId2 = infoControl.makeReservation(RES_ARRIVE_DATE.plusDays(1), RES_ARRIVE_DATE.plusDays(5), "test", testSite);
		Reservations fromDateOverlapRes = new Reservations();
		fromDateOverlapRes.setFromDate(RES_ARRIVE_DATE.plusDays(1));
		fromDateOverlapRes.setName("test");
		fromDateOverlapRes.setReservationId(resId2);
		fromDateOverlapRes.setSiteId(testSite.getSiteId());
		fromDateOverlapRes.setToDate(RES_DEPART_DATE.plusDays(5));

		
		while(results.next()) {
			Reservations r = mapToRowReservations(results);
			reservations.add(r);
	
		}
		
		Assert.assertFalse(reservations.contains(fromDateOverlapRes));
	}
	
	@Test
	public void make_reservation_does_not_input_overlapping_to_date_reservations() {
		long resId = infoControl.makeReservation(RES_ARRIVE_DATE, RES_DEPART_DATE, RES_NAME, testSite);
		testRes.setReservationId(resId);
		String sqlGetReservations = "SELECT * FROM reservation WHERE site_id = ?;";		
		SqlRowSet results = jdbc.queryForRowSet(sqlGetReservations, testSite.getSiteId());
		List<Reservations> reservations = new ArrayList<>();
		
		long resId2 = infoControl.makeReservation(RES_ARRIVE_DATE.plusDays(1), RES_ARRIVE_DATE.plusDays(5), "test", testSite);
		Reservations fromDateOverlapRes = new Reservations();
		fromDateOverlapRes.setFromDate(RES_ARRIVE_DATE.minusDays(2));
		fromDateOverlapRes.setName("test");
		fromDateOverlapRes.setReservationId(resId2);
		fromDateOverlapRes.setSiteId(testSite.getSiteId());
		fromDateOverlapRes.setToDate(RES_DEPART_DATE.minusDays(1));

		
		while(results.next()) {
			Reservations r = mapToRowReservations(results);
			reservations.add(r);
	
		}
		
		Assert.assertFalse(reservations.contains(fromDateOverlapRes));
	}
	
	@Test
	public void make_reservation_does_not_input_overlapping_from_date_and_to_date_reservations() {
		long resId = infoControl.makeReservation(RES_ARRIVE_DATE, RES_DEPART_DATE, RES_NAME, testSite);
		testRes.setReservationId(resId);
		String sqlGetReservations = "SELECT * FROM reservation WHERE site_id = ?;";		
		SqlRowSet results = jdbc.queryForRowSet(sqlGetReservations, testSite.getSiteId());
		List<Reservations> reservations = new ArrayList<>();
		
		long resId2 = infoControl.makeReservation(RES_ARRIVE_DATE.plusDays(1), RES_ARRIVE_DATE.plusDays(5), "test", testSite);
		Reservations fromDateOverlapRes = new Reservations();
		fromDateOverlapRes.setFromDate(RES_ARRIVE_DATE.minusDays(5));
		fromDateOverlapRes.setName("test");
		fromDateOverlapRes.setReservationId(resId2);
		fromDateOverlapRes.setSiteId(testSite.getSiteId());
		fromDateOverlapRes.setToDate(RES_DEPART_DATE.plusDays(5));

		
		while(results.next()) {
			Reservations r = mapToRowReservations(results);
			reservations.add(r);
	
		}
		
		Assert.assertFalse(reservations.contains(fromDateOverlapRes));
	}
	
	@Test
	public void advanced_search_returns_site() {
		List<Sites> sitesList = infoControl.getTopFiveSitesAdvancedSearch(testCampground, LocalDate.now().plusDays(100), LocalDate.now().plusDays(102), MAX_OCCUPANCY, ACCESSIBLE, MAX_RV_LENGTH, UTILITIES);
	
		Assert.assertTrue(sitesList.contains(testSite));
	}
	
	@Test
	public void get_top_five_sites_advanced_search_does_not_return_if_site_has_overlapping_reservation() {
		List<Sites> sitesList = infoControl.getTopFiveSitesAdvancedSearch(testCampground, RES_ARRIVE_DATE, RES_DEPART_DATE, MAX_OCCUPANCY, ACCESSIBLE, MAX_RV_LENGTH, UTILITIES);
		
		Assert.assertFalse(sitesList.contains(testSite));
	}
	
	@Test
	public void get_top_five_sites_by_park_id_works() {
		List<Sites> sitesList = infoControl.getTopFiveSitesByPark(testPark, LocalDate.now().plusDays(100), LocalDate.now().plusDays(102));
		
		Assert.assertTrue(sitesList.contains(testSite));
	}
	
	@Test
	public void get_top_five_sites_does_not_return_if_site_has_overlapping_reservation() {
		List<Sites> sitesList = infoControl.getTopFiveSitesByPark(testPark, RES_ARRIVE_DATE, RES_DEPART_DATE);
		
		Assert.assertFalse(sitesList.contains(testSite));
	}
	
	@Test
	public void get_top_five_sites_does_not_return_if_camp_is_in_offseason() {
		List<Sites> preSeasonSitesList = infoControl.getTopFiveSites(testCampground, LocalDate.of(2200, 4, 01), LocalDate.of(2200, 4, 05));
		List<Sites> postSeasonSitesList = infoControl.getTopFiveSites(testCampground, LocalDate.of(2200, 9, 01), LocalDate.of(2200, 9, 05));
		List<Sites> fromDateInSeasonToDatePostSeasonList = infoControl.getTopFiveSites(testCampground, LocalDate.of(2200, 8, 29), LocalDate.of(2200, 9, 05));
		List<Sites> fromDatePreSeasonToDateInSeasonList =  infoControl.getTopFiveSites(testCampground, LocalDate.of(2200, 4, 29), LocalDate.of(2200, 5, 05));
		List<Sites> fromDatePreSeasonToDatePostSeasonList = infoControl.getTopFiveSites(testCampground, LocalDate.of(2200, 4, 29), LocalDate.of(2200, 9, 05));
		
		Assert.assertFalse(preSeasonSitesList.contains(testSite));
		Assert.assertFalse(postSeasonSitesList.contains(testSite));
		Assert.assertFalse(fromDateInSeasonToDatePostSeasonList.contains(testSite));
		Assert.assertFalse(fromDatePreSeasonToDateInSeasonList.contains(testSite));
		Assert.assertFalse(fromDatePreSeasonToDatePostSeasonList.contains(testSite));
	}
	
	
	@Test
	public void get_daily_fee_by_campground_id() {
		BigDecimal dailyFee = infoControl.getDailyFeeBasedOnCampgroundId(testSite);
		
		Assert.assertTrue(dailyFee.compareTo(FEE) == 0);
	}
	
	@Test
	public void get_camp_name_from_database_gets_correct_name() {
		String campName = infoControl.getCampNameFromDataBase(testSite);
		
		Assert.assertTrue(campName.equals(testCampground.getName()));
	}
	
	@Test
	public void get_reservations_30_days_gets_next_30_days_reservations() {
		long resId = infoControl.makeReservation(LocalDate.now().plusDays(15), LocalDate.now().plusDays(20), "test", testSite);
		Reservations testRes2 = new Reservations();
		testRes2.setFromDate(LocalDate.now().plusDays(15));
		testRes2.setName("test");
		testRes2.setReservationId(resId);
		testRes2.setSiteId(testSite.getSiteId());
		testRes2.setToDate(LocalDate.now().plusDays(20));
		List<Reservations> resList = infoControl.returnReservations30DaysByParkId(testPark.getParkId());
		
		Assert.assertTrue(resList.contains(testRes2));
		
	}
	
	private Campgrounds makeCampground(Long parkId, String name, String openFromMM, String openToMM, BigDecimal dailyFee) {
		this.testCampground = new Campgrounds();
		testCampground.setParkId(parkId);
		testCampground.setName(name);
		testCampground.setOpenFromMonth(openFromMM);
		testCampground.setOpenUntilMonth(openToMM);
		testCampground.setDailyFee(dailyFee);
		
		return testCampground;
		
	}
	
	private void insertCampground(Campgrounds campground) {
		String sqlMakeTstCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?, ?, ?, ?, ?) RETURNING campground_id;";
		SqlRowSet results = jdbc.queryForRowSet(sqlMakeTstCampground, campground.getParkId(), campground.getName(), campground.getOpenFromMonth(), campground.getOpenUntilMonth(), campground.getDailyFee());
		results.next();
		campground.setCampgroundId(results.getLong("campground_id"));
	}
	
	private Parks makePark(String name, String location, LocalDate establish_date, int area, int visitors, String description) {
		this.testPark = new Parks();
		testPark.setName(name);
		testPark.setLocation(location);
		testPark.setEstablishDate(establish_date);
		testPark.setArea(area);
		testPark.setVisitors(visitors);
		testPark.setDescription(description);
		return testPark;
	}
	
	private void insertPark(Parks testPark) {
		String sqlMakeTestPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id;";
		SqlRowSet results = jdbc.queryForRowSet(sqlMakeTestPark, testPark.getName(), testPark.getLocation(), testPark.getEstablishDate(), testPark.getArea(), testPark.getVisitors(), testPark.getDescription());
		results.next();	
		testPark.setParkId(results.getLong("park_id"));
	}
	
	private Sites makeSite(Long siteId, Long campgroundId, int siteNumber, int maxOccupancy, boolean isAccessible, int maxRVLength, boolean hasUtilities) {
		this.testSite = new Sites();
		testSite.setSiteId(siteId);
		testSite.setCampgroundId(campgroundId);
		testSite.setSiteNumber(siteNumber);
		testSite.setMaxOccupancy(maxOccupancy);
		testSite.setAccessible(isAccessible);
		testSite.setMaxRVLength(maxRVLength);
		testSite.setHasUtilities(hasUtilities);
		
		return testSite;
		
	}
	
	private void insertSite(Sites testSite) {
		String sqlMakeTestSite = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (?, ?, ?, ?, ?, ?) RETURNING site_id;";
		SqlRowSet results = jdbc.queryForRowSet(sqlMakeTestSite, testSite.getCampgroundId(), testSite.getSiteNumber(), testSite.getMaxOccupancy(), testSite.isAccessible(), testSite.getMaxRVLength(), testSite.isHasUtilities());
		results.next();	
		testSite.setSiteId(results.getLong("site_id"));
	}
	
	private Reservations mapToRowReservations(SqlRowSet results) {
		Reservations res = new Reservations();
		res.setName(results.getString("name"));
		res.setCreateDate(results.getDate("create_date").toLocalDate());
		res.setFromDate(results.getDate("from_date").toLocalDate());
		res.setToDate(results.getDate("to_date").toLocalDate());
		res.setReservationId(results.getLong("reservation_id"));
		res.setSiteId(results.getLong("site_id"));
		
		return res;
	}
}

	
	


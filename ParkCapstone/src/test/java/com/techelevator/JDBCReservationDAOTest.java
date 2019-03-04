package com.techelevator;

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
import com.techelevator.jdbcdao.JDBCReservationsDAO;
import com.techelevator.parks.Parks;
import com.techelevator.reservations.Reservations;
import Site.Sites;

public class JDBCReservationDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCReservationsDAO resDAO;
	private JdbcTemplate jdbc;
	private Campgrounds testCampground;
	private Parks testPark;
	private Sites testSite;
	private Reservations testRes;
	private static final String RES_NAME = "Test Reservation";
	private static final LocalDate RES_ARRIVE_DATE = LocalDate.of(2019, 02, 23);
	private static final LocalDate RES_DEPART_DATE = LocalDate.of(2019, 02, 26);
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
	public void can_we_make_a_reservation() {
		long resId = resDAO.makeReservation(RES_ARRIVE_DATE, RES_DEPART_DATE, RES_NAME, testSite);
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

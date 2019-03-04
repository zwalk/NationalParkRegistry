package com.techelevator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
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
import com.techelevator.jdbcdao.JDBCCampgroundsDAO;
import com.techelevator.parks.Parks;

public class JDBCCampgroundsDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundsDAO campgroundsDAO;
	private JdbcTemplate jdbc;
	private Campgrounds testCampground;
	private Parks testPark;
	private static final String CAMP_NAME = "Camp Name";
	private static final String OPEN_FROM = "05";
	private static final String OPEN_UNTIL = "08";
	private static final BigDecimal FEE = new BigDecimal(60);
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
		campgroundsDAO = new JDBCCampgroundsDAO(dataSource);
		jdbc = new JdbcTemplate(dataSource);
		testPark = makePark(PARK_NAME, PARK_LOCATION, ESTABLISH_DATE, PARK_AREA, PARK_VISITORS, DESCRIPTION);
		insertPark(testPark);
		testCampground = makeCampground(testPark.getParkId(), CAMP_NAME, OPEN_FROM, OPEN_UNTIL, FEE);
		insertCampground(testCampground);
		
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
	public void get_campgrounds_by_park_id_gets_the_right_campground() {
		
		List<Campgrounds> campgroundsByParkId = campgroundsDAO.getAllCampgroundsByParkId(testPark.getParkId());
		
		Assert.assertTrue(campgroundsByParkId.contains(testCampground));
		
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
	
}

package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.jdbcdao.JDBCParksDAO;
import com.techelevator.parks.Parks;

public class JDBCParksDAOTTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCParksDAO parksDAO;
	private JdbcTemplate jdbc;
	private Parks testPark;
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
		parksDAO = new JDBCParksDAO(dataSource);
		jdbc = new JdbcTemplate(dataSource);
		testPark = makePark(PARK_NAME, PARK_LOCATION, ESTABLISH_DATE, PARK_AREA, PARK_VISITORS, DESCRIPTION);
		insertPark(testPark);
		
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	@Test
	public void get_all_parks_gets_all_the_parks() {
		List<Parks> allParks= parksDAO.getAllParks();
		
		Assert.assertTrue(allParks.contains(testPark));
		
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

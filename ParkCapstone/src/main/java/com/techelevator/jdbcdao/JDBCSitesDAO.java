package com.techelevator.jdbcdao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.dao.SitesDAO;
import com.techelevator.parks.Parks;
import Site.Sites;

public class JDBCSitesDAO implements SitesDAO{
	
	JdbcTemplate jdbcTemplate;
	
	public JDBCSitesDAO(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Sites> getTopFiveSites(Campgrounds campground, LocalDate arrival, LocalDate departure) {
		List<Sites> top5Sites = new ArrayList<>();
		String sqlGetTopFiveSites = "SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND site.site_id NOT IN ( "
				+ "SELECT site.site_id FROM site "
				+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
				+ "WHERE campground_id = ? "
				+ "AND ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? < from_date AND ? > to_date "
				+ "AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id = ? "
				+ "ORDER BY daily_fee DESC "
				+ "LIMIT 5;";

		String arrivalMonthNumericAsString = formatDate(arrival);
		String departureMonthNumericAsString = formatDate(departure);
		
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTopFiveSites, arrivalMonthNumericAsString, 
				departureMonthNumericAsString, campground.getCampgroundId(), arrival, arrival, arrival, 
				departure, departure, departure, arrival, departure, campground.getCampgroundId());
		
		while (results.next()) {
			Sites site = mapRowToSites(results);
			top5Sites.add(site);
		}
		
		
		return top5Sites;
	}
	
	public List<Sites> getTopFiveSitesByAdvancedSearch(Campgrounds campground, LocalDate arrival, LocalDate departure, 
														int maxOccupancy, boolean isAccessible, int maxRVLength, boolean hasUtilities) {
		
		List<Sites> top5Sites = new ArrayList<>();
		String sqlGetTopFiveSites = "SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND site.site_id NOT IN ( "
				+ "SELECT site.site_id FROM site "
				+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
				+ "WHERE campground_id = ? "
				+ "AND ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? < from_date AND ? > to_date "
				+ "AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id = ? "
				+ "AND site.max_occupancy >= ? " 
				+ "AND site.accessible = ? " 
				+ "AND site.max_rv_length >= ? " 
				+ "AND site.utilities = ? " 
				+ "ORDER BY daily_fee DESC "
				+ "LIMIT 5;";
		
		String arrivalMonthNumericAsString = formatDate(arrival);
		String departureMonthNumericAsString = formatDate(departure);
		
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTopFiveSites, arrivalMonthNumericAsString, 
				departureMonthNumericAsString, campground.getCampgroundId(), arrival, arrival, arrival, 
				departure, departure, departure, arrival, departure, campground.getCampgroundId(), maxOccupancy, isAccessible, maxRVLength, hasUtilities);
		
		while (results.next()) {
			Sites site = mapRowToSites(results);
			top5Sites.add(site);
		}
		
		
		return top5Sites;
	}
	
	public List<Sites> getTopFiveSitesByPark(Parks park, LocalDate arrival, LocalDate departure) {
		List<Sites> top5Sites = new ArrayList<>();
		
		String sqlGetTopFiveSites = "SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND ? BETWEEN campground.open_from_mm AND campground.open_to_mm "
				+ "AND site.site_id NOT IN ( "
				+ "SELECT site.site_id FROM site "
				+ "LEFT JOIN reservation ON reservation.site_id = site.site_id "
				+ "WHERE campground.park_id = ? "
				+ "AND ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date "
				+ "OR ? < from_date AND ? > to_date "
				+ "AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id IN ( "
				+ "SELECT campground_id FROM campground WHERE campground.park_id = ?) "
				+ "ORDER BY daily_fee DESC "
				+ "LIMIT 5;";

		String arrivalMonthNumericAsString = formatDate(arrival);
		String departureMonthNumericAsString = formatDate(departure);
		
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTopFiveSites, arrivalMonthNumericAsString, 
				departureMonthNumericAsString, park.getParkId(), arrival, arrival, arrival, 
				departure, departure, departure, arrival, departure, park.getParkId());
		
		while (results.next()) {
			Sites site = mapRowToSites(results);
			top5Sites.add(site);
		}
		
		
		return top5Sites;
	}
	
	private String formatDate(LocalDate date) {
		String monthFormattedString = "";
		
		if (date.getMonthValue() >= 10) {
			monthFormattedString = Integer.toString(date.getMonthValue());
		} else {
			monthFormattedString = ("0" + Integer.toString(date.getMonthValue()));
		}
		
		return monthFormattedString;
	}
	
	private Sites mapRowToSites(SqlRowSet results) {
		Sites newSite = new Sites();
		newSite.setCampgroundId(results.getLong("campground_id"));
		newSite.setHasUtilities(results.getBoolean("utilities"));
		newSite.setMaxOccupancy(results.getInt("max_occupancy"));
		newSite.setMaxRVLength(results.getInt("max_rv_length"));
		newSite.setAccessible(results.getBoolean("accessible"));
		newSite.setSiteNumber(results.getInt("site_number"));
		newSite.setSiteId(results.getLong("site_id"));
		
		return newSite;
	}
	
	public BigDecimal getDailyFeeBasedOnCampgroundId(Sites site) {
		String sqlGetDailyFee = "SELECT distinct daily_fee "
				+ "FROM campground JOIN site "
				+ "ON campground.campground_id = site.campground_id "
				+ "WHERE campground.campground_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyFee, site.getCampgroundId());
		results.next();
		
		BigDecimal dailyFee = results.getBigDecimal("daily_fee");
		
		return dailyFee;
	}
	
	public String getCampNameFromDatabase(Sites site) {
		String sqlGetDailyFee = "SELECT distinct campground.name "
				+ "FROM campground JOIN site "
				+ "ON campground.campground_id = site.campground_id "
				+ "WHERE campground.campground_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyFee, site.getCampgroundId());
		results.next();
		
		String campName = results.getString("name");
		
		return campName;
	}

}

package com.techelevator.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.parks.Parks;

import Site.Sites;

public interface SitesDAO {
	
	public List<Sites> getTopFiveSites(Campgrounds campground, LocalDate arrival, LocalDate departure);
	
	public List<Sites> getTopFiveSitesByAdvancedSearch(Campgrounds campground, LocalDate arrival, LocalDate departure, 
			int maxOccupancy, boolean isAccessible, int maxRVLength, boolean hasUtilities);
	
	public List<Sites> getTopFiveSitesByPark(Parks park, LocalDate arrival, LocalDate departure);
	
	public BigDecimal getDailyFeeBasedOnCampgroundId(Sites site);
	
	public String getCampNameFromDatabase(Sites site);
	
}

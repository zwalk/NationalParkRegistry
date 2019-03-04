package com.techelevator.dao;

import java.util.List;
import com.techelevator.campgrounds.Campgrounds;

public interface CampgroundsDAO {

	List<Campgrounds> getAllCampgroundsByParkId(Long parkId);
	
}

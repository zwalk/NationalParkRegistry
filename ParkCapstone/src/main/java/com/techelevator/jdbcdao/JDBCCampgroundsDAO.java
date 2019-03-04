package com.techelevator.jdbcdao;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.dao.CampgroundsDAO;

public class JDBCCampgroundsDAO implements CampgroundsDAO {

	private JdbcTemplate jdbcT;
	
	public JDBCCampgroundsDAO(DataSource ds) {
		jdbcT = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Campgrounds> getAllCampgroundsByParkId(Long parkId) {
			
			String sqlSelectCampgroundsByParkId = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee "
					+ "FROM campground WHERE park_id = ? ORDER BY name ASC";
			SqlRowSet results = jdbcT.queryForRowSet(sqlSelectCampgroundsByParkId, parkId);
			List<Campgrounds> allCampgroundsByParkId = new ArrayList<>();
			
			while (results.next()) {
				Campgrounds c = mapRowToCampgrounds(results);
				allCampgroundsByParkId.add(c);
			}
			return allCampgroundsByParkId;
		}
		
		
		private Campgrounds mapRowToCampgrounds(SqlRowSet results) {
			Campgrounds campground = new Campgrounds();
			campground.setCampgroundId(results.getLong("campground_id"));
			campground.setParkId(results.getLong("park_id"));
			campground.setName(results.getString("name"));
			campground.setOpenFromMonth(results.getString("open_from_mm"));
			campground.setOpenUntilMonth(results.getString("open_to_mm"));
			campground.setDailyFee(results.getBigDecimal("daily_fee"));
			
			
			return campground;
		}
		
}



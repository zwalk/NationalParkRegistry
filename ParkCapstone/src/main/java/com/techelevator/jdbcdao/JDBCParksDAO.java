package com.techelevator.jdbcdao;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.dao.ParksDAO;
import com.techelevator.parks.Parks;

public class JDBCParksDAO implements ParksDAO {

	private JdbcTemplate jdbcT;
	
	public JDBCParksDAO(DataSource ds) {
		jdbcT = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Parks> getAllParks() {
		String sqlSelectAllParks = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC;";
		SqlRowSet results = jdbcT.queryForRowSet(sqlSelectAllParks);
		List<Parks> allParks = new ArrayList<>();
		
		while (results.next()) {
			Parks p = mapRowToParks(results);
			allParks.add(p);
		}
		return allParks;
	}
	
	private Parks mapRowToParks(SqlRowSet results) {
		Parks park = new Parks();
		park.setParkId(results.getLong("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		
		return park;
	}

}

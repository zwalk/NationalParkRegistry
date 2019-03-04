package com.techelevator.jdbcdao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.dao.ReservationsDAO;
import com.techelevator.reservations.Reservations;
import Site.Sites;

public class JDBCReservationsDAO implements ReservationsDAO {

	JdbcTemplate jdbc;
	
	public JDBCReservationsDAO(DataSource ds) {
		this.jdbc = new JdbcTemplate(ds);
	}
	
	@Override
	public long makeReservation(LocalDate arrival, LocalDate departure, String resName, Sites site) {
		
		String sqlReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?, ?, ?, ?, current_date) RETURNING reservation_id;";
		SqlRowSet results = jdbc.queryForRowSet(sqlReservation, site.getSiteId(), resName, arrival, departure);
		results.next();
		
		long resId = results.getLong("reservation_id");
		
		return resId;
	}

	@Override
	public List<Reservations> returnReservations30DaysByParkId(Long parkId) {
		
		String sqlSelectReservationsByParkId = "SELECT reservation.name, reservation_id, site.site_id, from_date, to_date, create_date "
				+ "FROM reservation "
				+ "JOIN site ON reservation.site_id = site.site_id "
				+ "JOIN campground ON site.campground_id = campground.campground_id "
				+ "JOIN park ON campground.park_id = park.park_id WHERE from_date BETWEEN "
				+ "now() AND now() + interval '30' day AND park.park_id = ?;";
		SqlRowSet results = jdbc.queryForRowSet(sqlSelectReservationsByParkId, parkId);
		List<Reservations> allReservations30DaysByParkId = new ArrayList<>();
		
		while (results.next()) {
			Reservations res = mapRowToReservations(results);
			allReservations30DaysByParkId.add(res);
		}
		return allReservations30DaysByParkId;
	}
	
	
	private Reservations mapRowToReservations(SqlRowSet results) {
		Reservations res = new Reservations();
		res.setName(results.getString("name"));
		res.setReservationId(results.getLong("reservation_id"));;
		res.setSiteId(results.getLong("site_id"));
		res.setCreateDate(results.getDate("create_date").toLocalDate());
		res.setFromDate(results.getDate("from_date").toLocalDate());
		res.setToDate(results.getDate("to_date").toLocalDate());
		
		
		return res;
	}
	
}

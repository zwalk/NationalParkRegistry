SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC;

SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name ASC;

INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id;

SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ?;

SELECT park_id, name, open_from_mm, open_to_mm, CAST(daily_fee AS DECIMAL) FROM campground;

SELECT * FROM site 
left join reservation on reservation.site_id = site.site_id
WHERE campground_id = 1 AND from_date <= '2019-02-21' 

SELECT * from park;

DELETE FROM park WHERE park_id > 8;


Select distinct * from site
join campground on site.campground_id = campground.campground_id
where site.campground_id = ?
and site_id not in
(select site.site_id from site
join reservation on reservation.site_id = site.site_id;

site.site_id, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee

SELECT * FROM site 
JOIN campground ON campground.campground_id = site.campground_id
--LEFT JOIN reservation ON reservation.site_id = site.site_id 
WHERE site.campground_id = 1
AND site.site_id NOT IN ( 
SELECT site.site_id FROM site 
LEFT JOIN reservation ON reservation.site_id = site.site_id 
WHERE site.campground_id = 1 
AND '2019-02-23' BETWEEN from_date AND to_date OR '2019-02-23' = from_date OR '2019-02-23' = to_date 
AND '2019-02-26' BETWEEN from_date AND to_date OR '2019-02-26' = from_date OR '2019-02-26' = to_date 
AND from_date IS NOT NULL AND to_date IS NOT NULL
)
--AND site.campground_id = 1
ORDER BY daily_fee DESC
LIMIT 5;

SELECT * FROM reservation
SELECT current_date

INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?, ?, ?, ?, current_date) RETURNNIG reservation_id;

INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (?, ?, ?, ?, ?, ?) RETURNING site_id;

SELECT * FROM reservation WHERE site_id = ?;

--below is from eclipse
SELECT site.site_id, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site 
 JOIN campground ON campground.campground_id = site.campground_id 
 LEFT JOIN reservation ON reservation.site_id = site.site_id 
 WHERE site.site_id NOT IN ( 
SELECT site.site_id FROM site 
				 LEFT JOIN reservation ON reservation.site_id = site.site_id 
				 WHERE campground_id = ? 
				 AND ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date 
				 AND ? BETWEEN from_date AND to_date OR ? = from_date OR ? = to_date 
				 AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id = ? 
				 ORDER BY daily_fee DESC 
				 LIMIT 5;
				 
				 
SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site 
JOIN campground ON campground.campground_id = site.campground_id 
WHERE site.site_id NOT IN ( 
SELECT * FROM site 
LEFT JOIN reservation ON reservation.site_id = site.site_id
--JOIN campground ON campground.campground_id = site.campground_id
--WHERE '12' NOT BETWEEN open_from_mm AND open_to_mm AND '12' NOT BETWEEN open_from_mm AND open_to_mm
WHERE campground_id = 1 
AND '2/24/2019' BETWEEN from_date AND to_date OR '02/24/2019' = from_date OR '02/24/2019' = to_date 
AND '2/26/2019' BETWEEN from_date AND to_date OR '02/26/2019' = from_date OR '02/26/2019' = to_date 
AND from_date IS NOT NULL AND to_date IS NOT NULL
) 
AND site.campground_id = 1 
ORDER BY daily_fee DESC 
LIMIT 5;






site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee


SELECT * FROM site 
JOIN campground ON campground.campground_id = site.campground_id
WHERE '05' BETWEEN campground.open_from_mm AND campground.open_to_mm
AND '05' BETWEEN campground.open_from_mm AND campground.open_to_mm
AND site.site_id NOT IN ( 
SELECT site.site_id FROM site 
LEFT JOIN reservation ON reservation.site_id = site.site_id
WHERE campground_id = 1 
AND '2019-12-21' BETWEEN from_date AND to_date OR '2019-12-21' = from_date OR '2019-12-21' = to_date 
AND '2019-12-23' BETWEEN from_date AND to_date OR '2019-12-23' = from_date OR '2019-12-23' = to_date
AND '02/18/2019' < from_date AND '02/25/2019' > to_date
AND from_date IS NOT NULL AND to_date IS NOT NULL
) AND site.campground_id = 1

ORDER BY daily_fee DESC 
LIMIT 5;;

SELECT * FROM site;


SELECT reservation_id, site.site_id, from_date, to_date, create_date, daily_fee, park.park_id FROM reservation JOIN
site ON reservation.site_id = site.site_id JOIN
campground ON site.campground_id = campground.campground_id JOIN
park ON campground.park_id = park.park_id WHERE from_date
BETWEEN now() AND now() + interval '30' day AND park.park_id = 1;


SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site 
				 JOIN campground ON campground.campground_id = site.campground_id 
				 WHERE '12' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND '12' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND site.site_id NOT IN ( 
				 SELECT site.site_id FROM site 
				 LEFT JOIN reservation ON reservation.site_id = site.site_id 
			--	 WHERE campground.park_id = 1
				 AND '2019-12-02' BETWEEN from_date AND to_date OR '2019-12-02' = from_date OR '2019-12-02' = to_date 
				 AND '2019-12-05' BETWEEN from_date AND to_date OR '2019-12-05' = from_date OR '2019-12-05' = to_date 
				 AND '02/18/2019' < from_date AND '02/25/2019' > to_date
				 AND from_date IS NOT NULL AND to_date IS NOT NULL) 
			--	 AND site.campground_id IN (
				 SELECT campground_id FROM campground WHERE campground.park_id = 
			--	 )
				 AND site.max_occupancy >= ? --for user to search by max occupancy
				 AND site.accessible = ? -- for user to search by accessible
				 AND site.max_rv_length >= ? --for user to search bby rv length
				 AND site.utilities = ? -- utility search
				 ORDER BY daily_fee DESC 
				 LIMIT 5;
				 
				 
				 
				 
SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site 
				 JOIN campground ON campground.campground_id = site.campground_id 
				 WHERE '05' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND '05' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND site.site_id NOT IN ( 
				 SELECT site.site_id FROM site 
				 LEFT JOIN reservation ON reservation.site_id = site.site_id 
				 WHERE campground.park_id = 1 
				 AND '03/25/2019' BETWEEN from_date AND to_date OR '03/25/2019' = from_date OR '03/25/2019' = to_date 
				 OR '03/26/2019' BETWEEN from_date AND to_date OR '03/26/2019' = from_date OR '03/26/2019' = to_date 
				 OR '03/25/2019' < from_date AND ? > to_date 
				 AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id IN ( 
				 SELECT campground_id FROM campground WHERE campground.park_id = ? 
				 ORDER BY daily_fee DESC 
				 LIMIT 5;


SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities, campground.daily_fee FROM site 
				 JOIN campground ON campground.campground_id = site.campground_id 
				 WHERE '06' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND '06' BETWEEN campground.open_from_mm AND campground.open_to_mm 
				 AND site.site_id NOT IN ( 
				 SELECT * FROM site 
				 LEFT JOIN reservation ON reservation.site_id = site.site_id 
				 WHERE campground_id = '1' 
				 AND '02/18/2019' BETWEEN from_date AND to_date OR '02/18/2019' = from_date OR '02/18/2019' = to_date 
				 OR '02/28/2019' BETWEEN from_date AND to_date OR '02/28/2019' = from_date OR '02/28/2019' = to_date 
				 OR '02/18/2019' < from_date AND '02/28/2019' > to_date
				 AND from_date IS NOT NULL AND to_date IS NOT NULL) AND site.campground_id = '1' 
				 AND site.max_occupancy >= '6'  
				 AND site.accessible = false  
				 AND site.max_rv_length >= '0'  
				 AND site.utilities = false  
				 ORDER BY daily_fee DESC 
				 LIMIT 5;
				 

SELECT * FROM site 
				 LEFT JOIN reservation ON reservation.site_id = site.site_id 
				 WHERE campground_id = '1' 
				-- AND '02/14/2019' BETWEEN from_date AND to_date OR '02/14/2019' = from_date OR '02/14/2019' = to_date 
				 AND '02/25/2019' BETWEEN from_date AND to_date OR '02/25/2019' = from_date OR '02/25/2019' = to_date 
				  '02/14/2019' < from_date AND '02/25/2019' > to_date
				-- AND from_date IS NOT NULL AND to_date IS NOT NULL--) AND site.campground_id = '1' 

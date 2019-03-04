package com.techelevator.campgrounds;

import java.math.BigDecimal;

public class Campgrounds {

	private Long campgroundId;
	private Long parkId;
	private String name;
	private String openFromMonth;
	private String openUntilMonth;
	private BigDecimal dailyFee;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campgroundId == null) ? 0 : campgroundId.hashCode());
		result = prime * result + ((dailyFee == null) ? 0 : dailyFee.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((openFromMonth == null) ? 0 : openFromMonth.hashCode());
		result = prime * result + ((openUntilMonth == null) ? 0 : openUntilMonth.hashCode());
		result = prime * result + ((parkId == null) ? 0 : parkId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campgrounds other = (Campgrounds) obj;
		if (campgroundId == null) {
			if (other.campgroundId != null)
				return false;
		} else if (!campgroundId.equals(other.campgroundId))
			return false;
		if (dailyFee == null) {
			if (other.dailyFee != null)
				return false;
		} else if (dailyFee.compareTo(other.dailyFee) != 0)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (openFromMonth == null) {
			if (other.openFromMonth != null)
				return false;
		} else if (!openFromMonth.equals(other.openFromMonth))
			return false;
		if (openUntilMonth == null) {
			if (other.openUntilMonth != null)
				return false;
		} else if (!openUntilMonth.equals(other.openUntilMonth))
			return false;
		if (parkId == null) {
			if (other.parkId != null)
				return false;
		} else if (!parkId.equals(other.parkId))
			return false;
		return true;
	}
	
	public Long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public Long getParkId() {
		return parkId;
	}
	
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOpenFromMonth() {
		return openFromMonth;
	}
	
	public void setOpenFromMonth(String openFromMonth) {
		this.openFromMonth = openFromMonth;
	}
	
	public String getOpenUntilMonth() {
		return openUntilMonth;
	}
	
	public void setOpenUntilMonth(String openUntilMonth) {
		this.openUntilMonth = openUntilMonth;
	}
	
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	
}

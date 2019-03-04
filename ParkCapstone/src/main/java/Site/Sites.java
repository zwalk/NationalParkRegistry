package Site;

public class Sites {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campgroundId == null) ? 0 : campgroundId.hashCode());
		result = prime * result + (hasUtilities ? 1231 : 1237);
		result = prime * result + (isAccessible ? 1231 : 1237);
		result = prime * result + maxOccupancy;
		result = prime * result + maxRVLength;
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result + siteNumber;
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
		Sites other = (Sites) obj;
		if (campgroundId == null) {
			if (other.campgroundId != null)
				return false;
		} else if (!campgroundId.equals(other.campgroundId))
			return false;
		if (hasUtilities != other.hasUtilities)
			return false;
		if (isAccessible != other.isAccessible)
			return false;
		if (maxOccupancy != other.maxOccupancy)
			return false;
		if (maxRVLength != other.maxRVLength)
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (siteNumber != other.siteNumber)
			return false;
		return true;
	}

	private Long siteId;
	private Long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean isAccessible;
	private int maxRVLength;
	private boolean hasUtilities;
	
	public Long getSiteId() {
		return siteId;
	}
	
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public int getSiteNumber() {
		return siteNumber;
	}
	
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	
	public boolean isAccessible() {
		return isAccessible;
	}
	
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	
	public int getMaxRVLength() {
		return maxRVLength;
	}
	
	public void setMaxRVLength(int maxRVLength) {
		this.maxRVLength = maxRVLength;
	}
	
	public boolean isHasUtilities() {
		return hasUtilities;
	}
	
	public void setHasUtilities(boolean hasUtilities) {
		this.hasUtilities = hasUtilities;
	}
	
	
}

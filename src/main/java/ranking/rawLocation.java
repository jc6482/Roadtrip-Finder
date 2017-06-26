package ranking;

import java.io.Serializable;


import yelp.data.YelpData;

class rawLocation implements Serializable, Comparable<rawLocation> {

	private static final long serialVersionUID = 759395590118125978L;

	YelpData theLocation = null;
	double ourScore = 0.0;
	double normalizedScore = 0.0;
	public int searchTerm = -1;
	int orginBoxNumber = -1;
	double approximateScore = 1;
	public double compareScore = 0;
	
	rawLocation() {
		theLocation = new YelpData();
	}

	@Override
	public int compareTo(rawLocation o) {
		try{
			double comparedScore = o.compareScore;
			if (this.compareScore < comparedScore) {
				return 1;
			} else if (this.compareScore == comparedScore) {
				return 0;
			} else {
				return -1;
			}
		} catch (IllegalArgumentException e){
			return -1;
		}
	}

	@Override
	public int hashCode() {
		return new Double((this.theLocation.getLocation().getLatitude() + this.theLocation.getLocation().getLongitude())
				* 10000000).intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		rawLocation other = (rawLocation) obj;
		if (this.theLocation.getLocation().getLatitude() == null) {
			if (other.theLocation.getLocation().getLatitude() != null)
				return false;
		} else if (!this.theLocation.getLocation().getLatitude().equals(other.theLocation.getLocation().getLatitude()))
			return false;
		return true;
	}
}
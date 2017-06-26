package ranking;

import java.util.ArrayList;
import java.util.Collections;

public class Route {
	private ArrayList<Double> theRouteInBoxes = null;
	private ArrayList<LatLong> RoutePoints = null;
	private double distance = 0;

	public ArrayList<LatLong> getRoutePoints() {
		return RoutePoints;
	}

	public Route(ArrayList<Double> theRouteInBoxes) {
		super();
		this.theRouteInBoxes = theRouteInBoxes;

	}

	public Route(ArrayList<Double> aRouteInBoxes, String tripInfo) {
		this(aRouteInBoxes);
		this.parseTripInfo(tripInfo);
		distance = YelpCache.calculateDistance(RoutePoints.get(0).getLat(), RoutePoints.get(0).getLong(),
				RoutePoints.get(RoutePoints.size() - 1).getLat(), RoutePoints.get(RoutePoints.size() - 1).getLong());
	}

	public void parseTripInfo(String tripInfo) {
		// TODO parse the trip info into a bunch of latLongs
		// This suppose to parse the tripinfo in the main controller and turn
		// them in to latLongs
		if (RoutePoints == null || tripInfo != null) {
			String delims = ",";
			String[] tokens = tripInfo.split(delims);
			RoutePoints = new ArrayList<LatLong>();
			for (int i = 0; i + 2 < tokens.length; i += 2) {
				double pointLat = Double.parseDouble(tokens[i].trim());
				double pointLng = Double.parseDouble(tokens[i + 1].trim());
				RoutePoints.add(new LatLong(pointLat, pointLng));
			}
		}
	}

	private void setRoutePoints(ArrayList<LatLong> aRoutePoints) {
		Collections.copy(RoutePoints, aRoutePoints);
	}

	public ArrayList<Double> getTheRouteInBoxes() {
		return theRouteInBoxes;
	}

	public void setTheRouteInBoxes(ArrayList<Double> theRouteInBoxes) {
		this.theRouteInBoxes = theRouteInBoxes;
	}
	
	public double getDistanceInMiles(){
		return distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((theRouteInBoxes == null) ? 0 : theRouteInBoxes.hashCode());
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
		Route other = (Route) obj;
		if (theRouteInBoxes == null) {
			if (other.theRouteInBoxes != null)
				return false;
		} else if (!theRouteInBoxes.equals(other.theRouteInBoxes))
			return false;
		return true;
	}

	// This is for caching data to files
	public String generateFileName() {
		// Gets point of last and first box latlng sw corner summated.
		return Double.toString(this.theRouteInBoxes.get(0) + this.theRouteInBoxes.get(1)
				+ this.theRouteInBoxes.get(this.theRouteInBoxes.size() - 4)
				+ this.theRouteInBoxes.get(this.theRouteInBoxes.size() - 3));
	}
}

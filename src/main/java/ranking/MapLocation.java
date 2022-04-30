package ranking;

public class MapLocation {
	String theName = null;
	String theLat = null;
	

	String theLng = null;
	String theRating = null;
	String theYelpURL = null;
	String theAddress = null;
	String theCategory = null;
	String theImageURL = null;
	String thePhoneNumber = null;
	String theTypeOfPlace = null;
	String C = "XYZ@@#";
	
	@Override
	public String toString(){
		if(theImageURL == null){
			theImageURL = "http://d2cmub9v8qb8gq.cloudfront.net/0.15.0/static/img/no-image-available.jpg";
		}
		String theString = this.theName + C + this.theLat + C + this.theLng + C + this.theAddress + C + theImageURL + C + thePhoneNumber + C + theCategory + C + theYelpURL + C + theRating + C;
		return theString;
	
	}

	MapLocation(String theName, String theLat, String theLng, String theRating, String theYelpURL, String theAddress,
			String theCategory, String theImageURL, String thePhoneNumber, String theTypeOfPlace) {
		this.theName = theName;
		this.theLat = theLat;
		this.theLng = theLng;
		this.theRating = theRating;
		this.theYelpURL = theYelpURL;
		this.theAddress = theAddress;
		this.theCategory = theCategory;
		this.theImageURL =theImageURL;
		this.thePhoneNumber = thePhoneNumber;
		this.theTypeOfPlace = theTypeOfPlace;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getTheName() {
		return theName;
	}

	public void setTheName(String theName) {
		this.theName = theName;
	}

	public String getTheLat() {
		return theLat;
	}

	public void setTheLat(String theLat) {
		this.theLat = theLat;
	}

	public String getTheLng() {
		return theLng;
	}

	public void setTheLng(String theLng) {
		this.theLng = theLng;
	}

	public String getTheRating() {
		return theRating;
	}

	public void setTheRating(String theRating) {
		this.theRating = theRating;
	}

	public String getTheYelpURL() {
		return theYelpURL;
	}

	public void setTheYelpURL(String theYelpURL) {
		this.theYelpURL = theYelpURL;
	}

	public String getTheAddress() {
		return theAddress;
	}

	public void setTheAddress(String theAddress) {
		this.theAddress = theAddress;
	}

	public String getTheCategory() {
		return theCategory;
	}

	public void setTheCategory(String theCategory) {
		this.theCategory = theCategory;
	}

	public String getTheImageURL() {
		return theImageURL;
	}

	public void setTheImageURL(String theImageURL) {
		this.theImageURL = theImageURL;
	}

	public String getThePhoneNumber() {
		return thePhoneNumber;
	}

	public void setThePhoneNumber(String thePhoneNumber) {
		this.thePhoneNumber = thePhoneNumber;
	}

	public String getTheTypeOfPlace() {
		return theTypeOfPlace;
	}

	public void setTheTypeOfPlace(String theTypeOfPlace) {
		this.theTypeOfPlace = theTypeOfPlace;
	}

}

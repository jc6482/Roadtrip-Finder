package ranking.yelp.data.utility;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class yelpLocation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000258698009665247L;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country_code == null) ? 0 : country_code.hashCode());
		result = prime * result + ((display_address == null) ? 0 : display_address.hashCode());
		result = prime * result + ((geo_accuracy == null) ? 0 : geo_accuracy.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((postal_code == null) ? 0 : postal_code.hashCode());
		result = prime * result + ((state_code == null) ? 0 : state_code.hashCode());
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
		yelpLocation other = (yelpLocation) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country_code == null) {
			if (other.country_code != null)
				return false;
		} else if (!country_code.equals(other.country_code))
			return false;
		if (display_address == null) {
			if (other.display_address != null)
				return false;
		} else if (!display_address.equals(other.display_address))
			return false;
		if (geo_accuracy == null) {
			if (other.geo_accuracy != null)
				return false;
		} else if (!geo_accuracy.equals(other.geo_accuracy))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (postal_code == null) {
			if (other.postal_code != null)
				return false;
		} else if (!postal_code.equals(other.postal_code))
			return false;
		if (state_code == null) {
			if (other.state_code != null)
				return false;
		} else if (!state_code.equals(other.state_code))
			return false;
		return true;
	}


	String city;
	String postal_code;
	String state_code;
	String display_address = "";
	String address = "";

	//Numerical Data
	Double geo_accuracy;
	Double latitude;
	Double longitude;
	
	//String Values
	String country_code;
	public String getCountry_code() {
		return country_code;
	}


	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getPostal_code() {
		return postal_code;
	}


	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}


	public String getState_code() {
		return state_code;
	}


	public void setState_code(String state_code) {
		this.state_code = state_code;
	}


	public String getDisplay_address() {
		return display_address;
	}


	public void setDisplay_address(String display_address) {
		this.display_address = display_address;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Double getGeo_accuracy() {
		return geo_accuracy;
	}


	public void setGeo_accuracy(Double geo_accuracy) {
		this.geo_accuracy = geo_accuracy;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		
		return "yelp Location Data BEGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
				+ "\n country_code= " + country_code
				+ "\n city= " + city
				+ "\n postal_code= " + postal_code
				+ "\n state_code= " + state_code
				+ "\n display_address= " + display_address
				+ "\n address= " + address
				+ "\n geo_accuracy= " + geo_accuracy
				+ "\n latitude= " + latitude
				+ "\n longitude= " + longitude
				+"\n yelp Location Data END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
	}
	
	
@SuppressWarnings("unchecked")
public void setValues(Object name, Object value) throws IllegalArgumentException, SecurityException{
		
		try {
			String fieldName = (String) name;
			
			if(fieldName.equals("coordinate")){
				((JSONObject)value).forEach((k,v) -> this.setValues(k, v));
			}else if(fieldName.equals("address")){
				
				((JSONArray)value).forEach( ( obj) -> this.address += ((String)obj).trim() + " " );
				
			}else if(fieldName.equals("display_address")){
				
				((JSONArray)value).forEach( ( obj) -> this.display_address += ((String)obj).trim() + " " );
				
			}else{

				Class<? extends yelpLocation> me = this.getClass();
				Field thisField = me.getDeclaredField(fieldName);
				thisField.set(this, value);
			}
			
		} catch (NoSuchFieldException e) {
			System.err.println("Error: " + name + " is not a field!");
		} catch (IllegalAccessException e) {
			System.err.println("Error: " + name + " was not granted access!");
		}
		return;
	}
}

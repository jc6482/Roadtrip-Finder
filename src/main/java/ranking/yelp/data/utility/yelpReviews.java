package ranking.yelp.data.utility;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import org.json.simple.JSONObject;

public class yelpReviews implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 548926645901444545L;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((excerpt == null) ? 0 : excerpt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((printable_time_created == null) ? 0 : printable_time_created.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + ((rating_image_large_url == null) ? 0 : rating_image_large_url.hashCode());
		result = prime * result + ((rating_image_small_url == null) ? 0 : rating_image_small_url.hashCode());
		result = prime * result + ((rating_image_url == null) ? 0 : rating_image_url.hashCode());
		result = prime * result + ((time_created == null) ? 0 : time_created.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		yelpReviews other = (yelpReviews) obj;
		if (excerpt == null) {
			if (other.excerpt != null)
				return false;
		} else if (!excerpt.equals(other.excerpt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (printable_time_created == null) {
			if (other.printable_time_created != null)
				return false;
		} else if (!printable_time_created.equals(other.printable_time_created))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (rating_image_large_url == null) {
			if (other.rating_image_large_url != null)
				return false;
		} else if (!rating_image_large_url.equals(other.rating_image_large_url))
			return false;
		if (rating_image_small_url == null) {
			if (other.rating_image_small_url != null)
				return false;
		} else if (!rating_image_small_url.equals(other.rating_image_small_url))
			return false;
		if (rating_image_url == null) {
			if (other.rating_image_url != null)
				return false;
		} else if (!rating_image_url.equals(other.rating_image_url))
			return false;
		if (time_created == null) {
			if (other.time_created != null)
				return false;
		} else if (!time_created.equals(other.time_created))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}



	//Strig Variables
	String rating_image_url;
	String rating_image_small_url;	
	String rating_image_large_url;
	String id;
	public String getRating_image_url() {
		return rating_image_url;
	}



	public void setRating_image_url(String rating_image_url) {
		this.rating_image_url = rating_image_url;
	}



	public String getRating_image_small_url() {
		return rating_image_small_url;
	}



	public void setRating_image_small_url(String rating_image_small_url) {
		this.rating_image_small_url = rating_image_small_url;
	}



	public String getRating_image_large_url() {
		return rating_image_large_url;
	}



	public void setRating_image_large_url(String rating_image_large_url) {
		this.rating_image_large_url = rating_image_large_url;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getExcerpt() {
		return excerpt;
	}



	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}



	public Long getRating() {
		return rating;
	}



	public void setRating(Long rating) {
		this.rating = rating;
	}



	public Long getTime_created() {
		return time_created;
	}



	public void setTime_created(Long time_created) {
		this.time_created = time_created;
	}



	public Date getPrintable_time_created() {
		return printable_time_created;
	}



	public void setPrintable_time_created(Date printable_time_created) {
		this.printable_time_created = printable_time_created;
	}



	public yelpUser getUser() {
		return user;
	}



	public void setUser(yelpUser user) {
		this.user = user;
	}



	String excerpt;
	
	//Numerical Data
	Long rating;
	Long time_created;
	Date printable_time_created;
	
	//Custom Objects
	yelpUser user = new yelpUser();

	
	@SuppressWarnings("unchecked")
	public void setValues(Object name, Object value) throws IllegalArgumentException, SecurityException{
		
		
		try {
			String fieldName = (String) name;
			Class<? extends yelpReviews> me = this.getClass();
			Field thisField = me.getDeclaredField(fieldName);
			
			if(fieldName.equals("user")){
				//Catches custom object
				((JSONObject) value).forEach((k,v) -> this.user.setValues(k,v));
			}else{
				thisField.set(this, value);
				if(fieldName.equals("time_created")){
					printable_time_created = new Date(time_created);
				}
			}
			
			
			
		} catch (NoSuchFieldException e) {
			System.err.println("Error: " + name + " is not a field!");
		} catch (IllegalAccessException e) {
			System.err.println("Error: " + name + " was not granted access!");
		}
		return;
	}



	@Override
	public String toString() {
		
		return "yelp Review Data BEGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
				+ "\n rating_image_url=" + rating_image_url + "\n rating_image_small_url=" + rating_image_small_url
				+ "\n rating_image_large_url=" + rating_image_large_url + "\n rating=" + rating + "\n id="
				+ id + "\n time_created=" + printable_time_created + "\n excerpt=" + excerpt + "\n user=" + user +
				"\n yelp Review Data END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
	}
	
	
}

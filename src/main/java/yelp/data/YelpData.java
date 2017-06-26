package yelp.data;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ranking.yelp.data.utility.*;

public class YelpData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 19459L;
	//String Variables
	String image_url;
	String mobile_url;
	String url;
	String snippet_text;
	String rating_img_url;
	String snippet_image_url;
	String display_phone;
	String rating_img_url_large;
	String phone;
	String name;
	String rating_img_url_small;
	String id;
	String menu_provider;
	
	//Booleans
	transient Boolean is_closed;
	transient Boolean is_claimed;
	
	//Numerical Data
	Long review_count;
	double rating;
	transient Date menu_date_updated;
	
	//Custom Objects
	transient yelpReviews reviews = new yelpReviews();
	yelpLocation location = new yelpLocation();;
	yelpCategories categories = new yelpCategories();
	
	
	@Override
	public String toString() {
		return "\n yelp Data BEGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
				"image_url=" + image_url + "\n rating=" + rating + "\n mobile_url=" + mobile_url
				+ "\n review_count=" + review_count + "\n url=" + url + "\n is_claimed=" + is_claimed
				+ "\n snippet_text=" + snippet_text + "\n rating_img_url=" + rating_img_url
				+ "\n snippet_image_url=" + snippet_image_url + "\n display_phone=" + display_phone
				+ "\n reviews=" + reviews + "\n rating_img_url_large=" + rating_img_url_large + "\n phone="
				+ phone + "\n name=" + name + "\n rating_img_url_small=" + rating_img_url_small + "\n location="
				+ location + "\n categories=" + categories + "\n id=" + id + "\n is_closed=" + is_closed
				+ "\n menu_date_updated=" + menu_date_updated + "\n menu_provider=" + menu_provider
				+ "\n yelp Data END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
	}
	
	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getMobile_url() {
		return mobile_url;
	}

	public void setMobile_url(String mobile_url) {
		this.mobile_url = mobile_url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSnippet_text() {
		return snippet_text;
	}

	public void setSnippet_text(String snippet_text) {
		this.snippet_text = snippet_text;
	}

	public String getRating_img_url() {
		return rating_img_url;
	}

	public void setRating_img_url(String rating_img_url) {
		this.rating_img_url = rating_img_url;
	}

	public String getSnippet_image_url() {
		return snippet_image_url;
	}

	public void setSnippet_image_url(String snippet_image_url) {
		this.snippet_image_url = snippet_image_url;
	}

	public String getDisplay_phone() {
		return display_phone;
	}

	public void setDisplay_phone(String display_phone) {
		this.display_phone = display_phone;
	}

	public String getRating_img_url_large() {
		return rating_img_url_large;
	}

	public void setRating_img_url_large(String rating_img_url_large) {
		this.rating_img_url_large = rating_img_url_large;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRating_img_url_small() {
		return rating_img_url_small;
	}

	public void setRating_img_url_small(String rating_img_url_small) {
		this.rating_img_url_small = rating_img_url_small;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenu_provider() {
		return menu_provider;
	}

	public void setMenu_provider(String menu_provider) {
		this.menu_provider = menu_provider;
	}

	public Boolean getIs_closed() {
		return is_closed;
	}

	public void setIs_closed(Boolean is_closed) {
		this.is_closed = is_closed;
	}

	public Boolean getIs_claimed() {
		return is_claimed;
	}

	public void setIs_claimed(Boolean is_claimed) {
		this.is_claimed = is_claimed;
	}

	public Long getReview_count() {
		return review_count;
	}

	public void setReview_count(Long review_count) {
		this.review_count = review_count;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Date getMenu_date_updated() {
		return menu_date_updated;
	}

	public void setMenu_date_updated(Date menu_date_updated) {
		this.menu_date_updated = menu_date_updated;
	}

	public yelpReviews getReviews() {
		return reviews;
	}

	public void setReviews(yelpReviews reviews) {
		this.reviews = reviews;
	}

	public yelpLocation getLocation() {
		return location;
	}

	public void setLocation(yelpLocation location) {
		this.location = location;
	}

	public yelpCategories getCategories() {
		return categories;
	}

	public void setCategories(yelpCategories categories) {
		this.categories = categories;
	}

	@SuppressWarnings("unchecked")
	public void setValues(Object name, Object value) throws IllegalArgumentException, SecurityException{
		try {
			String fieldName = (String) name;
			Class<? extends YelpData> me = this.getClass();
			Field thisField = me.getDeclaredField(fieldName);
			
			if(fieldName.equals("categories")){
				JSONArray jsonCategories = (JSONArray) value;
				jsonCategories.forEach((list) -> this.categories.setValues(list));
				
			}else if(fieldName.equals("reviews")){
				JSONArray jsonReviews = (JSONArray) value;
				jsonReviews.forEach((obj) -> ((JSONObject) obj).forEach((k,v) -> this.reviews.setValues(k,v)));
				
			}else if(fieldName.equals("location")){
				JSONObject jsonLocations = (JSONObject) value;
				jsonLocations.forEach((k,v) -> this.location.setValues(k,v));
				
			}else if(fieldName.equals("menu_date_updated")){
				this.menu_date_updated = new Date((Long) value);
				
			}else{
			
				thisField.set(this, value);

			}
			
			
		} catch (NoSuchFieldException e) {
			System.err.println("Error: " + name + " is not a field!");
		} catch (IllegalAccessException e) {
			System.err.println("Error: " + name + " was not granted access!");
		}
		return;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((display_phone == null) ? 0 : display_phone.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image_url == null) ? 0 : image_url.hashCode());
		result = prime * result + ((is_claimed == null) ? 0 : is_claimed.hashCode());
		result = prime * result + ((is_closed == null) ? 0 : is_closed.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((menu_date_updated == null) ? 0 : menu_date_updated.hashCode());
		result = prime * result + ((menu_provider == null) ? 0 : menu_provider.hashCode());
		result = prime * result + ((mobile_url == null) ? 0 : mobile_url.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((rating_img_url == null) ? 0 : rating_img_url.hashCode());
		result = prime * result + ((rating_img_url_large == null) ? 0 : rating_img_url_large.hashCode());
		result = prime * result + ((rating_img_url_small == null) ? 0 : rating_img_url_small.hashCode());
		result = prime * result + ((review_count == null) ? 0 : review_count.hashCode());
		result = prime * result + ((reviews == null) ? 0 : reviews.hashCode());
		result = prime * result + ((snippet_image_url == null) ? 0 : snippet_image_url.hashCode());
		result = prime * result + ((snippet_text == null) ? 0 : snippet_text.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		YelpData other = (YelpData) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (display_phone == null) {
			if (other.display_phone != null)
				return false;
		} else if (!display_phone.equals(other.display_phone))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (image_url == null) {
			if (other.image_url != null)
				return false;
		} else if (!image_url.equals(other.image_url))
			return false;
		if (is_claimed == null) {
			if (other.is_claimed != null)
				return false;
		} else if (!is_claimed.equals(other.is_claimed))
			return false;
		if (is_closed == null) {
			if (other.is_closed != null)
				return false;
		} else if (!is_closed.equals(other.is_closed))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (menu_date_updated == null) {
			if (other.menu_date_updated != null)
				return false;
		} else if (!menu_date_updated.equals(other.menu_date_updated))
			return false;
		if (menu_provider == null) {
			if (other.menu_provider != null)
				return false;
		} else if (!menu_provider.equals(other.menu_provider))
			return false;
		if (mobile_url == null) {
			if (other.mobile_url != null)
				return false;
		} else if (!mobile_url.equals(other.mobile_url))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
			return false;
		if (rating_img_url == null) {
			if (other.rating_img_url != null)
				return false;
		} else if (!rating_img_url.equals(other.rating_img_url))
			return false;
		if (rating_img_url_large == null) {
			if (other.rating_img_url_large != null)
				return false;
		} else if (!rating_img_url_large.equals(other.rating_img_url_large))
			return false;
		if (rating_img_url_small == null) {
			if (other.rating_img_url_small != null)
				return false;
		} else if (!rating_img_url_small.equals(other.rating_img_url_small))
			return false;
		if (review_count == null) {
			if (other.review_count != null)
				return false;
		} else if (!review_count.equals(other.review_count))
			return false;
		if (reviews == null) {
			if (other.reviews != null)
				return false;
		} else if (!reviews.equals(other.reviews))
			return false;
		if (snippet_image_url == null) {
			if (other.snippet_image_url != null)
				return false;
		} else if (!snippet_image_url.equals(other.snippet_image_url))
			return false;
		if (snippet_text == null) {
			if (other.snippet_text != null)
				return false;
		} else if (!snippet_text.equals(other.snippet_text))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
 
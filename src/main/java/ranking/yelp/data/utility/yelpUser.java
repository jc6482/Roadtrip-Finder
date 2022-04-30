package ranking.yelp.data.utility;

import java.io.Serializable;
import java.lang.reflect.Field;

public class yelpUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3700591452878696438L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image_url == null) ? 0 : image_url.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		yelpUser other = (yelpUser) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	//String Values
	String name;
	String image_url;
	String id;

	@Override
	public String toString() {
		
		return "yelp User Data BEGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
				+ "\n name=" + name + "\n image_url=" + image_url
				+ "\n id=" + id + "\n rating=" +
				"\n yelp User Data END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
	}
	
	
public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getImage_url() {
		return image_url;
	}


	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


public void setValues(Object name, Object value) throws IllegalArgumentException, SecurityException{
		
		
		try {
			String fieldName = (String) name;
			Class<? extends yelpUser> me = this.getClass();
			Field thisField = me.getDeclaredField(fieldName);
			thisField.set(this, value);
				
			
			
		} catch (NoSuchFieldException e) {
			System.err.println("Error: " + name + " is not a field!");
		} catch (IllegalAccessException e) {
			System.err.println("Error: " + name + " was not granted access!");
		}
		return;
	}
}

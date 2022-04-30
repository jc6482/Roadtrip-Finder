package ranking.yelp.data.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class yelpCategories implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2651327391418380866L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myCategories == null) ? 0 : myCategories.hashCode());
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
		yelpCategories other = (yelpCategories) obj;
		if (myCategories == null) {
			if (other.myCategories != null)
				return false;
		} else if (!myCategories.equals(other.myCategories))
			return false;
		return true;
	}

	public ArrayList<String> getMyCategories() {
		return myCategories;
	}

	public void setMyCategories(ArrayList<String> myCategories) {
		this.myCategories = myCategories;
	}

	ArrayList<String> myCategories = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	public void setValues(Object list) {
		
			((ArrayList<String>)list).forEach((string) -> myCategories.add(string) );

		return;
	}
	
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		
		this.myCategories.forEach((string) -> toString.append(string + ", "));

		toString.deleteCharAt(toString.toString().lastIndexOf(','));
		return toString.toString();
	}
}

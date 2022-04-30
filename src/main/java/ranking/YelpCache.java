package ranking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import yelp.YelpAPI;
import yelp.data.YelpData;

/**
 * Created by dean on 3/28/16.
 */
public class YelpCache {

	// LoadingCache<Route, Set<rawLocation>> yelpCache = null;
	ArrayList<Double> theAverageTopicRating = null;
	LoadingCache<Route, Set<rawLocation>> yelpCache = null;
	// This contains the average yelp review count for a given box, and
	// search term.
	ArrayList<Double> theAverageTopicReviewCount = null;
	// This is a unique grouping of locations that can be cached, and are
	// unscored.
	// This contains the average yelp rating for a given box, and search
	// term.
	HashSet<rawLocation> theSetOfLocations = null;
	Semaphore yelpPermit = null;
	// This will contain the search terms sent to yelp.
	ArrayList<String> theSearchTerms = null;

	public static void main(String args[]) {
	}
	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;


		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

    private double approximateDistance(double aX, double aY, double bX, double bY){
        return (aY - bY) * (aY - bY) + (aX - bX) * (aX - bX);
    }
    private double calculatepenlizePercentage(double distance){
        double MaxDistance = 60; // this distance will get a 50 percent penalty
		if(distance > 60)
			return 0.99;
        return distance / MaxDistance * 0.9;
    }
    public void aproximateScores(Route aRoute) {
        for (rawLocation aLocation : theSetOfLocations) {
            double MinDistance = 1000000;
            int locationOnMap = -1;
            int recorder = 0;
            int placeOnMap = 0;

            for (LatLong pointOnRoute : aRoute.getRoutePoints()) {
                double distance = approximateDistance(aLocation.theLocation.getLocation().getLatitude(),
						aLocation.theLocation.getLocation().getLongitude(),
						pointOnRoute.getLat(),
                        pointOnRoute.getLong());
                if(distance < MinDistance) {
                    MinDistance = distance;
                    locationOnMap = placeOnMap;
                }
                placeOnMap++;
            }
            recorder++;

            MinDistance = calculateDistance(aLocation.theLocation.getLocation().getLatitude(),
					aLocation.theLocation.getLocation().getLongitude(),
					aRoute.getRoutePoints().get(locationOnMap).getLat(),
					aRoute.getRoutePoints().get(locationOnMap).getLong());
            if(MinDistance > 3) {
				//System.out.println("min distance " + MinDistance);
				aLocation.approximateScore = 1 - calculatepenlizePercentage(MinDistance);
			}
		}
    }

	public YelpCache() {
		yelpCache = CacheBuilder.newBuilder().build(new CacheLoader<Route, Set<rawLocation>>() {

			@Override
			public HashSet<rawLocation> load(Route key) throws Exception {

				return findLocations(key);
			}
		});
	}

	private HashSet<rawLocation> findLocations(Route aRoute) {

		theSetOfLocations = diskCaching.getCacheFromFile(aRoute);
		if (theSetOfLocations == null) {
			// This is the number of boxes, that the routeboxer will provide for
			// this instance of the algorithm.
			int theNumberBoxes = aRoute.getTheRouteInBoxes().size() / 4;
			// Yelp Variable
			initTerms();
			theSetOfLocations = new HashSet<rawLocation>();
			theAverageTopicRating = new ArrayList<Double>();
			theAverageTopicReviewCount = new ArrayList<Double>();
			yelpPermit = new Semaphore(0);
			for (int i = 0; i < theNumberBoxes; i++) {
				for (int n = 0; n < theSearchTerms.size(); n++) {

					theAverageTopicRating.add(0.0);
					theAverageTopicReviewCount.add(0.0);
					new yelpThread(i, n, aRoute.getTheRouteInBoxes().get(i * 4),
							aRoute.getTheRouteInBoxes().get(i * 4 + 1), aRoute.getTheRouteInBoxes().get(i * 4 + 2),
							aRoute.getTheRouteInBoxes().get(i * 4 + 3)).start();
				}
			}
			while (!yelpPermit.tryAcquire(theNumberBoxes * theSearchTerms.size())) {
			}

			for (rawLocation o : theSetOfLocations) {
				normalizeScore(o);
			}
			if(aRoute.getDistanceInMiles()<1500)
				aproximateScores(aRoute);
			diskCaching.storeLocationsInCache(aRoute, theSetOfLocations);
		}
		return theSetOfLocations;
	}

	private void initTerms() {
		theSearchTerms = new ArrayList<String>();
		// Shopping
		theSearchTerms.add("shops");
		// Action
		theSearchTerms.add("active");
		// Camping
		theSearchTerms.add("campgrounds");
		// Outdoor
		// Food
		theSearchTerms.add("food");
		// Relaxation
		theSearchTerms.add("relax");
		// Education
		theSearchTerms.add("museum");
		theSearchTerms.add("library");
		// Night Life
		theSearchTerms.add("nightlife");
		// General
		theSearchTerms.add("fun");
		theSearchTerms.add("fast food");
		theSearchTerms.add("restaurants");
		theSearchTerms.add("cuisine");
		theSearchTerms.add("motel");
		theSearchTerms.add("lodging");
		theSearchTerms.add("fancy hotels");
	}

	private synchronized int storeLocation(YelpData currentRawLocation, int aBoxNum, int aTermNum) {
		rawLocation currentLocation = new rawLocation();
		if (currentRawLocation.getName() != null) {
			currentLocation = new rawLocation();
			currentLocation.theLocation = currentRawLocation;
			currentLocation.ourScore = 0;
			currentLocation.orginBoxNumber = aBoxNum;
			currentLocation.searchTerm = aTermNum;
			theSetOfLocations.add(currentLocation);

			theAverageTopicRating.set((aBoxNum * theSearchTerms.size() + aTermNum),
					theAverageTopicRating.get(aBoxNum * theSearchTerms.size() + aTermNum)
							+ currentRawLocation.getRating());
			theAverageTopicReviewCount.set((aBoxNum * theSearchTerms.size() + aTermNum),
					theAverageTopicReviewCount.get(aBoxNum * theSearchTerms.size() + aTermNum)
							+ currentRawLocation.getReview_count());
			return 1;
		}
		return 0;
	}

	private void normalizeScore(rawLocation o) {
		o.normalizedScore = theAverageTopicRating.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.normalizedScore *= theAverageTopicReviewCount.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.normalizedScore += o.theLocation.getReview_count() * o.theLocation.getRating();
		o.normalizedScore /= o.theLocation.getReview_count()
				+ theAverageTopicReviewCount.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.ourScore = o.normalizedScore;
	}

	class yelpThread implements Runnable {
		int theBoxNumber = -1;
		int theTermNumber = -1;
		Double theSwLat = null;
		Double theSwLong = null;
		Double theNeLat = null;
		Double theNeLong = null;
		Thread t = null;

		yelpThread(int aBoxNumber, int aTermNumber, Double swLat, Double swLong, Double neLat, Double neLong) {
			theBoxNumber = aBoxNumber;
			theTermNumber = aTermNumber;
			theSwLat = swLat;
			theSwLong = swLong;
			theNeLat = neLat;
			theNeLong = neLong;
		}

		private void normalizeArea(int theNumberOfLocations) {
			theAverageTopicRating.set((theBoxNumber * theSearchTerms.size() + theTermNumber),
					theAverageTopicRating.get(theBoxNumber * theSearchTerms.size() + theTermNumber)
							/ theNumberOfLocations);
			theAverageTopicReviewCount.set((theBoxNumber * theSearchTerms.size() + theTermNumber),
					theAverageTopicReviewCount.get(theBoxNumber * theSearchTerms.size() + theTermNumber)
							/ theNumberOfLocations);
		}
		@Override
		public void run() {

			try {

				ArrayList<YelpData> d = YelpAPI.queryAPI(theSearchTerms.get(theTermNumber), theSwLat, theSwLong,
						theNeLat, theNeLong);
				int n = 0;
				for (YelpData y : d) {
					n += storeLocation(y, theBoxNumber, theTermNumber);
				}
				normalizeArea(n);
				yelpPermit.release();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				yelpPermit.release();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				yelpPermit.release();
			} catch (SecurityException e) {
				e.printStackTrace();
				yelpPermit.release();
			} catch (NullPointerException e) {
				e.printStackTrace();
				yelpPermit.release();
			} catch (org.scribe.exceptions.OAuthConnectionException e) {
				e.printStackTrace();
				yelpPermit.release();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				yelpPermit.release();
			}

		}

		public void start() {
			if (t == null) {

				t = new Thread(this, (Integer.toString(theBoxNumber) + " " + Integer.toString(theTermNumber)));
				t.start();
			}
		}
	}

	//////////// Proximity scores

}

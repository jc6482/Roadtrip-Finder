/* Author(s): Nicholas Hopper, Dean He
 * FileName: rankingAlgorithm.java 
 * Description: This class can receive an array of boxes and user criteria, then return an array of recommended yelp locations.
 * Assumptions: 
 * Date Created: 2/22/2016
 * Dates Modified: 
 * 					2/22/2016 - Created the constructor that receives town an array of town names and search terms,
 * 								then query yelp with the locations and terms. Finally it returns the yelpData. - Nicholas
 * 					3/12/2016 - Created the query by box function in the yelp api, 
 * 								then used this function in the main constructor. -Nicholas
 * 					3/13/2016 - Created the YelpThread class to allow for faster queries. Synchronized critical sections -Nicholas 
 * 					3/14/2016 - Cleaned up the console errors and prints, as well as removed unnecessary code. -Nicholas
 * 					3/19/2016 - Created a set to store yelp locations, hash, and equals funtions to go with it.
 * 								☺ normalized yelp Locations based on locality and type. 
 * 								☺ created a driver in the main function that querys boxes in waco and austin based on user input - Nicholas
 * 					3/20/2016 - Cleaned the code up for milestone 2, removed unneeded functions, added appropriate comments - Nicholas
 */

package ranking;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.json.simple.parser.ParseException;

import template.main.Frontend;
import yelp.YelpAPI;
import yelp.data.YelpData;

public class RankingAlgorithm {
	private final int BUDGET_TIGHT = 0;
	private final int BUDGET_RESONABLE = 1;
	private final int BUDGET_EXTRAVAGANT = 2;
	private final int AGE_OF_MATURITY = 21;
	private final int AGE_OF_FRAIL = 70;
	
	// This is the magnitude/multiplier to user proffered locations
	private final double BONUS = 1.1;
	// This is the magnitude/multiplier to locations that don't match the user's interest.
	private final double PENALTY = 0.7;

	// This is the number of boxes, that the routeboxer will provide for this instance of the algorithm.
	private static int theNumberBoxes = 0;

	// This is a unique grouping of locations that can be cached, and are unscored.
	private static Set<rawLocation> theSetOfLocations = null;
	// This is a unique grouping that is populated by theSetOfLocations, and contains personalized scores.
	private static ArrayList<rawLocation> theListOfLocations = null;
	// This is a unique grouping that correlates with theSetOfLocations, yet is frontend friendly.
	private static ArrayList<MapLocation> theListOfMapPoints = null;

	// This will contain the search terms set to yelp.
	private static ArrayList<String> theSearchTerms = null;
	// This contains the average yelp rating for a given box, and search term.
	private static ArrayList<Double> theAverageTopicRating = null;
	// This contains the average yelp review count for a given box, and search term.
	private static ArrayList<Double> theAverageTopicReviewCount = null;

	// This is where the user criteria is stored.
	private static Frontend theUser = null;
	// This is true is the trip is assumed to be overnight.
	private static boolean overNight = false;
	// This allows for the RankingAlgorithm to wait on unfinished threads.
	Semaphore yelpPermit = null;

	
	/* 
	 * 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SecurityException,
			ParseException, InterruptedException {

		Scanner reader = new Scanner(System.in);

		// User Criteria
		System.out.println("Start location:");
		String startPlace = "Waco";
		System.out.println("Waco");
		System.out.println("End location:");
		String endPlace = "Austin";
		System.out.println("Austin");
		System.out.println("Number Of People:");
		int numPeople = reader.nextInt();
		System.out.println("Type 'y' or 'n'");
		boolean[] interest = { false, false, false, false, false, false, false, false };
		System.out.println("Interrested in Camping?");
		interest[0] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in Shopping?");
		interest[1] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in OutDoors?");
		interest[2] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in NightLife?");
		interest[3] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in Food?");
		interest[4] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in Education?");
		interest[5] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in Action?");
		interest[6] = (reader.next() == "y" ? true : false);
		System.out.println("Interrested in Relaxation?");
		interest[7] = (reader.next() == "y" ? true : false);
		System.out.println("Start Day '1-31'");
		Date startTime = new Date();
		startTime.setDate(reader.nextInt());
		System.out.println("Start Month '1-12'");
		startTime.setMonth(reader.nextInt());
		startTime.setYear(2016);
		System.out.println("End Day '1-31'");
		Date endTime = new Date();
		endTime.setDate(reader.nextInt());
		System.out.println("End Month '1-12'");
		endTime.setMonth(reader.nextInt());
		endTime.setYear(2016);

		System.out.println("Enter Budget 0='tight'. 1= 'reasonable', 2= 'extravagant' ,3= 'don't care'");
		int budget = reader.nextInt();
		System.out.println("Enter the youngest persons's age");
		int young = reader.nextInt();
		System.out.println("Enter the oldest persons's age");
		int old = reader.nextInt();
		Frontend aUser = new Frontend(startPlace, endPlace, numPeople, interest, startTime, endTime, budget, young,
				old);

		// Route Boxer
		ArrayList<Double> boxLL = new ArrayList<Double>();

		// Waco Area
		boxLL.add(31.458559);
		boxLL.add(-97.299095);
		boxLL.add(31.616761);
		boxLL.add(-97.017980);

		// Austin Area
		boxLL.add(30.091865);
		boxLL.add(-98.031093);
		boxLL.add(30.571120);
		boxLL.add(-97.417037);

		RankingAlgorithm test = new RankingAlgorithm(boxLL, aUser);

		for (String s : test.listOfPersonalizedPlaces()) {
			System.out.print(s);
		}
		
		reader.close();
	}

	////////////////// Initialization

	public RankingAlgorithm(ArrayList<Double> boxCoordinates, Frontend aUser) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, ParseException, InterruptedException {

		// Initializate variables
		initTerms();
		theNumberBoxes = boxCoordinates.size() / 4;
		theUser = aUser;
		checkIfOverNight();
		yelpPermit = new Semaphore(0);
		theSetOfLocations = new HashSet<rawLocation>();
		theListOfMapPoints = new ArrayList<MapLocation>();
		theListOfLocations = new ArrayList<rawLocation>();
		theAverageTopicRating = new ArrayList<Double>();
		theAverageTopicReviewCount = new ArrayList<Double>();

		// Set up proper console output
		PrintStream out = System.out;
		PrintStream error = System.err;
		System.out.println("Querying Yelp with " + theSearchTerms.size() + " terms in " + theNumberBoxes + " boxes");
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
		//System.setOut(System.out);

		for (int i = 0; i < theNumberBoxes; i++) {
			for (int n = 0; n < theSearchTerms.size(); n++) {
				theAverageTopicRating.add(0.0);
				theAverageTopicReviewCount.add(0.0);
				new yelpThread(i, n, boxCoordinates.get(i * 4), boxCoordinates.get(i * 4 + 1),
						boxCoordinates.get(i * 4 + 2), boxCoordinates.get(i * 4 + 3)).start();
			}
		}
		while (!yelpPermit.tryAcquire(theNumberBoxes * theSearchTerms.size())) {
		}
		setToList();

		// Reconfigure console output
		System.setErr(error);
		System.setOut(out);
		System.out.println("Yelp returned " + theSetOfLocations.size() + " validLocations");
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

	////////////// Computing

	private synchronized int storeLocation(YelpData currentRawLocation, int aBoxNum, int aTermNum) {
		rawLocation currentLocation = new rawLocation();
		if (currentRawLocation.getName() != null) {
			currentLocation = new rawLocation();
			currentLocation.theLoction = currentRawLocation;
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

	private void setToList() {
		if(theListOfLocations != null)
			theListOfLocations.clear();
		else
			theListOfLocations = new ArrayList<rawLocation>();
			
		if(theListOfMapPoints != null)
			theListOfMapPoints.clear();
		else
			theListOfMapPoints = new ArrayList<MapLocation>();
		
		for (rawLocation currentLocation : theSetOfLocations) {
			normalizeScore(currentLocation);
			personalizeScore(currentLocation);
			if (!((currentLocation.searchTerm >= 12 || currentLocation.searchTerm == 2) && !overNight)) {
				theListOfLocations.add(currentLocation);
				theListOfMapPoints.add(rawLocationToMapLocation(currentLocation));
			}
		}
		if (!theListOfLocations.isEmpty()) {
			Collections.sort(theListOfLocations);
			double maxScore = theListOfLocations.get(0).ourScore;
			for (rawLocation o : theListOfLocations) {
				o.ourScore = o.ourScore / maxScore;
				o.ourScore *= 100;
			}
		}
	}

	private void personalizeScore(rawLocation theCurrentLocation) {
		if (theUser != null) {

			// Based on interest
			boolean[] interest = theUser.getInterest();
			
			if (theUserIsInterested(theCurrentLocation)) {
				theCurrentLocation.ourScore *= BONUS;
			} else {
				theCurrentLocation.ourScore *= PENALTY;
			}

			// Budget
			if (theUser.getBudget() == BUDGET_TIGHT && (theCurrentLocation.searchTerm == 12 || theCurrentLocation.searchTerm == 9)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			} else if (theUser.getBudget() == BUDGET_RESONABLE && (theCurrentLocation.searchTerm == 13 || theCurrentLocation.searchTerm == 10)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			} else if (theUser.getBudget() == BUDGET_EXTRAVAGANT && (theCurrentLocation.searchTerm == 14 || theCurrentLocation.searchTerm == 11)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			}

			// Ages
			if ((theUser.getYoung() < AGE_OF_MATURITY && theCurrentLocation.searchTerm == 7)&&! interest[3]) {
				theCurrentLocation.ourScore *= PENALTY;
			}
			if ((theUser.getOld() > AGE_OF_FRAIL && (theCurrentLocation.searchTerm == 1 || theCurrentLocation.searchTerm == 7))&&!interest[6]) {
				theCurrentLocation.ourScore *= PENALTY;
			}

			// Group size
			if (theCurrentLocation.searchTerm == 10 || theCurrentLocation.searchTerm == 11) {
				if (theUser.getNumPeople() >= 2) {
					theCurrentLocation.ourScore *= BONUS;
				} else {
					theCurrentLocation.ourScore *= PENALTY;
				}
			}

			// OverNight
			if (!overNight && theCurrentLocation.searchTerm == 7) {
				theCurrentLocation.ourScore *= PENALTY;
			}

		}
	}

	private void normalizeScore(rawLocation o){
		o.normalizedScore = theAverageTopicRating.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.normalizedScore *= theAverageTopicReviewCount
				.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.normalizedScore += o.theLoction.getReview_count() * o.theLoction.getRating();
		o.normalizedScore /= o.theLoction.getReview_count()
				+ theAverageTopicReviewCount.get(o.orginBoxNumber * theSearchTerms.size() + o.searchTerm);
		o.ourScore = o.normalizedScore;
	}
	
	private MapLocation rawLocationToMapLocation(rawLocation o) {
		if(o == null || o.theLoction == null || o.theLoction.getLocation() == null)
			return null;
		return new MapLocation(o.theLoction.getName(),
				o.theLoction.getLocation().getLatitude().toString(),
				o.theLoction.getLocation().getLongitude().toString(),
				String.format("%.0f", o.ourScore),
				o.theLoction.getUrl(),
				o.theLoction.getLocation().getAddress(),
				"",
				o.theLoction.getImage_url(),
				o.theLoction.getPhone(),
				Integer.toString(o.searchTerm));
	}

	private void checkIfOverNight() {
		if ((theUser.getEndTime().getTime() - theUser.getStartTime().getTime()) > 80400000) {
			overNight = true;
		}
	}
	
	private boolean theUserIsInterested(rawLocation currentLocation){
		boolean[] interest = theUser.getInterest();
		return ((interest[0] && currentLocation.searchTerm == 2) || (interest[1] && currentLocation.searchTerm == 0) || ((interest[2] && (currentLocation.searchTerm==1 || currentLocation.searchTerm==2)))
		|| (interest[3] && currentLocation.searchTerm == 7)
		|| (interest[4]
				&& (currentLocation.searchTerm == 3 || currentLocation.searchTerm == 9 || currentLocation.searchTerm == 10 || currentLocation.searchTerm == 11))
		|| (interest[5] && (currentLocation.searchTerm == 5 || currentLocation.searchTerm == 6)) || (interest[6] && currentLocation.searchTerm == 1)
		|| (interest[7] && currentLocation.searchTerm == 4));
	}
	//////////////////////// Data Presentation

	public ArrayList<String> listOfPersonalizedPlaces() {
		ArrayList<String> out = new ArrayList<String>();
		out.add("OurScore\tLocalScore\t Name\n");
		for (rawLocation o : theListOfLocations) {
			out.add(String.format("%.0f\t%.2f", o.ourScore, o.normalizedScore) + "\t" + o.theLoction.getName() + "\n");
		}
		return out;
	}

	@SuppressWarnings("unused")
	private void printList() {
		for (rawLocation i : theListOfLocations) {
			System.out.printf("%.3f \t %.3f \t %s\n", i.normalizedScore, i.ourScore, i.theLoction.getName());
		}
	}

	public ArrayList<MapLocation> getMapLocations() {
		return theListOfMapPoints;
	}

	
	/////////////////////////// Caching 
	public void updateUserCriteria(){
		if(theUser!=null){
			setToList();
		}
	}
	
	////////////////////////// Helper Classes/Functions
	class rawLocation implements Comparable<rawLocation> {
		YelpData theLoction = null;
		double ourScore = 0.0;
		double normalizedScore = 0.0;
		public int searchTerm = -1;
		int orginBoxNumber = -1;

		rawLocation() {
			theLoction = new YelpData();
			@SuppressWarnings("unused")
			String searchTerm = new String();
		}

		@Override
		public int compareTo(rawLocation o) {
			double comparedScore = o.ourScore;
			if (this.ourScore < comparedScore) {
				return 1;
			} else if (this.ourScore == comparedScore) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public int hashCode() {
			return new Double(
					(this.theLoction.getLocation().getLatitude() + this.theLoction.getLocation().getLongitude())
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
			if (this.theLoction.getLocation().getLatitude() == null) {
				if (other.theLoction.getLocation().getLatitude() != null)
					return false;
			} else if (!this.theLoction.getLocation().getLatitude()
					.equals(other.theLoction.getLocation().getLatitude()))
				return false;
			return true;
		}
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
		
		private void normalizeArea(int theNumberOfLocations){
			theAverageTopicRating.set((theBoxNumber * theSearchTerms.size() + theTermNumber),
					theAverageTopicRating.get(theBoxNumber * theSearchTerms.size() + theTermNumber) / theNumberOfLocations);
			theAverageTopicReviewCount.set((theBoxNumber * theSearchTerms.size() + theTermNumber),
					theAverageTopicReviewCount.get(theBoxNumber * theSearchTerms.size() + theTermNumber) / theNumberOfLocations);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				yelpPermit.release();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				yelpPermit.release();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				yelpPermit.release();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				yelpPermit.release();
			} catch (NullPointerException e) {
				yelpPermit.release();
			} catch (org.scribe.exceptions.OAuthConnectionException e) {
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
}

/* Author(s): Nicholas Hopper, Dean He
* FileName: rankingAlgorithm.java
* Description: This class can receive an array of boxes and user criteria, then return an array of recommended yelp locations.
* Assumptions:
* Date Created: 2/22/2016
* Dates Modified:
*                 2/22/2016 - Created the constructor that receives town an array of town names and search terms,
*                          then query yelp with the locations and terms. Finally it returns the yelpData. - Nicholas
*                 3/12/2016 - Created the query by box function in the yelp api,
*                          then used this function in the main constructor. -Nicholas
*                 3/13/2016 - Created the YelpThread class to allow for faster queries. Synchronized critical sections -Nicholas
*                 3/14/2016 - Cleaned up the console errors and prints, as well as removed unnecessary code. -Nicholas
*                 3/19/2016 - Created a set to store yelp locations, hash, and equals funtions to go with it.
*                          â˜º normalized yelp Locations based on locality and type.
*                          â˜º created a driver in the main function that querys boxes in waco and austin based on user input - Nicholas
*                 3/20/2016 - Cleaned the code up for milestone 2, removed unneeded functions, added appropriate comments - Nicholas
*/

package ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import org.json.simple.parser.ParseException;

import ranking.rawLocation;
import template.main.Frontend;
import yelp.YelpAPI;
import yelp.data.YelpData;

public class cachingAlgorithm {
	private final int BUDGET_TIGHT = 0;
	private final int BUDGET_RESONABLE = 1;
	private final int BUDGET_EXTRAVAGANT = 2;
	private final int AGE_OF_MATURITY = 21;
	private final int AGE_OF_FRAIL = 70;

	private final double LEVEL1 = 0.1;
	private final double LEVEL2 = 0.2;
	private final double LEVEL3 = 0.3;
	private final double OB = 0.5;
	// This is the magnitude/multiplier to user proffered locations
	private final double BONUS = 1.1;
	// This is the magnitude/multiplier to locations that don't match the user's
	// interest.
	private final double PENALTY = 0.7;
	// This is a unique grouping of locations that can be cached, and are
	// unscored.
	private static HashSet<rawLocation> theSetOfLocations = null;
	// This is a unique grouping that is populated by theSetOfLocations, and
	// contains personalized scores.
	private static ArrayList<rawLocation> theListOfLocations = null;
	// This is a unique grouping that correlates with theSetOfLocations, yet is
	// frontend friendly.
	private static ArrayList<MapLocation> theListOfMapPoints = null;

	// This is where the user criteria is stored.
	private static Frontend theUser = null;
	// This is true is the trip is assumed to be overnight.
	private static boolean overNight = false;

	YelpCache theYelpLocations = null;

	Route theRoute = null;

	/*
	 *
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SecurityException,
			ParseException, InterruptedException, ExecutionException {

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
		boxLL.add(37.458559);
		boxLL.add(-97.299095);
		boxLL.add(37.616761);
		boxLL.add(-97.017980);

		// Austin Area
		boxLL.add(28.091865);
		boxLL.add(-97.031093);
		boxLL.add(28.571120);
		boxLL.add(-96.417037);

		cachingAlgorithm test = new cachingAlgorithm();
		test.changeUser(aUser);
		Route r = new Route(boxLL);
		r.parseTripInfo(
				"31.54986,-97.14632,31.59765,-97.10886,31.63718,-97.09601,31.7901,-97.10269,31.90608,-97.09297,32.09532,-97.02426,32.16308,-96.92054,32.29042,-96.86656,32.37892,-96.86534,32.44474,-96.84904,32.60595,-96.82282,32.71596,-96.82872,32.75927,-96.80873,32.77567,-96.79795");
		test.changeRoute(r);

		for (String s : test.listOfPersonalizedPlaces()) {
			System.out.print(s);
		}

		reader.close();
	}

	////////////////// Initialization
	public cachingAlgorithm() {

		// Initializate variables
		theListOfMapPoints = new ArrayList<MapLocation>();
		theListOfLocations = new ArrayList<rawLocation>();
		theYelpLocations = new YelpCache();
		theRoute = null;
	}

	public cachingAlgorithm(ArrayList<Double> boxCoordinates, Frontend aUser) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, ParseException, InterruptedException, ExecutionException {

		// Initializate variables
		theUser = aUser;
		checkIfOverNight();
		theListOfMapPoints = new ArrayList<MapLocation>();
		theListOfLocations = new ArrayList<rawLocation>();
		theYelpLocations = new YelpCache();
		theRoute = new Route(boxCoordinates);
		findLocations();
	}

	////////////// Computing
	public void findLocations() throws ExecutionException {
		theSetOfLocations = (HashSet<rawLocation>) theYelpLocations.yelpCache.get(theRoute);
		if (theRoute != null && theUser != null)
			setToList();
		System.out.println("Yelp returned " + theSetOfLocations.size() + " validLocations");
	}

	public void changeRoute(Route aRoute) throws ExecutionException {
		if (theUser == null) {
			// theRoute = aRoute;
		} else if (theRoute != aRoute && theRoute != null && !theRoute.equals(aRoute)) {
			theRoute = aRoute;
			findLocations();
		} else if (theRoute == null) {
			theRoute = aRoute;
			findLocations();
		}
	}

	public void changeUser(Frontend aUser) throws ExecutionException {
		if (theRoute == null) {
			theUser = aUser;
			checkIfOverNight();
		} else {
			theUser = aUser;
			checkIfOverNight();
			findLocations();
		}
	}

	private void setToList() {
		if (theListOfLocations != null)
			theListOfLocations.clear();
		else
			theListOfLocations = new ArrayList<rawLocation>();

		if (theListOfMapPoints != null)
			theListOfMapPoints.clear();
		else
			theListOfMapPoints = new ArrayList<MapLocation>();

		double maxScore = 0;
		for (rawLocation currentLocation : theSetOfLocations) {
			personalizeScore(currentLocation);
			if (!((currentLocation.searchTerm >= 12 || currentLocation.searchTerm == 2) && !overNight)) {
				theListOfLocations.add(currentLocation);
			}
			if (currentLocation.ourScore > maxScore)
				maxScore = currentLocation.ourScore;
			currentLocation.compareScore = currentLocation.ourScore;
			currentLocation.compareScore *= currentLocation.approximateScore;
			for (double i = (int) theRoute.getDistanceInMiles(); i < 500; i += 100) {
				currentLocation.compareScore *= currentLocation.approximateScore;
			}
		}

		if (!theListOfLocations.isEmpty()) {
			Collections.sort(theListOfLocations);
			for (rawLocation o : theListOfLocations) {
				o.ourScore = o.ourScore / maxScore;
				o.ourScore *= 100;
			}
		}
		for (rawLocation currentLocation : theListOfLocations) {
			theListOfMapPoints.add(rawLocationToMapLocation(currentLocation));
		}
	}

	private void personalizeScore(rawLocation theCurrentLocation) {
		if (theUser != null) {

			// Based on time limits weight proximity score
			// theCurrentLocation.ourScore *=
			// theCurrentLocation.approximateScore;
			// Based on interest
			boolean[] interest = theUser.getInterest();

			if (theUserIsInterested(theCurrentLocation)) {
				theCurrentLocation.ourScore *= BONUS;
			} else {
				theCurrentLocation.ourScore *= PENALTY;
			}

			// Budget
			if (theUser.getBudget() == BUDGET_TIGHT
					&& (theCurrentLocation.searchTerm == 12 || theCurrentLocation.searchTerm == 9)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			} else if (theUser.getBudget() == BUDGET_RESONABLE
					&& (theCurrentLocation.searchTerm == 13 || theCurrentLocation.searchTerm == 10)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			} else if (theUser.getBudget() == BUDGET_EXTRAVAGANT
					&& (theCurrentLocation.searchTerm == 14 || theCurrentLocation.searchTerm == 11)) {
				theCurrentLocation.ourScore *= BONUS * BONUS;
			}

			// Ages
			if ((theUser.getYoung() < AGE_OF_MATURITY && theCurrentLocation.searchTerm == 7) && !interest[3]) {
				theCurrentLocation.ourScore *= PENALTY;
			}
			if ((theUser.getOld() > AGE_OF_FRAIL
					&& (theCurrentLocation.searchTerm == 1 || theCurrentLocation.searchTerm == 7)) && !interest[6]) {
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
			// change the score based on yelp categories
			personalizeScoreHelper(theCurrentLocation);

		}
	}

	private MapLocation rawLocationToMapLocation(rawLocation o) {
		if (o == null || o.theLocation == null || o.theLocation.getLocation() == null)
			return null;
		return new MapLocation(o.theLocation.getName(), o.theLocation.getLocation().getLatitude().toString(),
				o.theLocation.getLocation().getLongitude().toString(), String.format("%.0f", o.ourScore),
				o.theLocation.getUrl(), o.theLocation.getLocation().getAddress(), "", o.theLocation.getImage_url(),
				o.theLocation.getPhone(), Integer.toString(o.searchTerm));
	}

	private void checkIfOverNight() {
		if ((theUser.getEndTime().getTime() - theUser.getStartTime().getTime()) > 80400000) {
			overNight = true;
		} else {
			overNight = false;
		}
	}

	private boolean theUserIsInterested(rawLocation currentLocation) {
		boolean[] interest = theUser.getInterest();
		return ((interest[0] && currentLocation.searchTerm == 2) || (interest[1] && currentLocation.searchTerm == 0)
				|| ((interest[2] && (currentLocation.searchTerm == 1 || currentLocation.searchTerm == 2)))
				|| (interest[3] && currentLocation.searchTerm == 7)
				|| (interest[4] && (currentLocation.searchTerm == 3 || currentLocation.searchTerm == 9
						|| currentLocation.searchTerm == 10 || currentLocation.searchTerm == 11))
				|| (interest[5] && (currentLocation.searchTerm == 5 || currentLocation.searchTerm == 6))
				|| (interest[6] && currentLocation.searchTerm == 1)
				|| (interest[7] && currentLocation.searchTerm == 4));
	}
	//////////////////////// Data Presentation

	public ArrayList<String> listOfPersonalizedPlaces() {
		ArrayList<String> out = new ArrayList<String>();
		out.add("ProxScore\tOurScore\tLocalScore\t Name\n");
		for (rawLocation o : theListOfLocations) {
			out.add(String.format("%.2f\t\t%.0f\t\t%.2f", o.approximateScore, o.ourScore, o.normalizedScore) + "\t"
					+ o.theLocation.getName() + "\n");
		}
		return out;
	}

	@SuppressWarnings("unused")
	private void printList() {
		for (rawLocation i : theListOfLocations) {
			System.out.printf("%.3f \t %.3f \t %s\n", i.normalizedScore, i.ourScore, i.theLocation.getName());
		}
	}

	public ArrayList<MapLocation> getMapLocations(int percentile) {
		if (percentile <= 0 || percentile >= 100) {
			return theListOfMapPoints;
		}
		ArrayList<MapLocation> ret = new ArrayList<MapLocation>();
		for (int i = 0; i < (double) (theListOfMapPoints.size()) * (percentile / 100.0); i++) {
			ret.add(theListOfMapPoints.get(i));
		}
		return ret;
	}

	public ArrayList<MapLocation> getWeightedMapLocations() {
		int percentile = 0;
		double distance = theRoute.getDistanceInMiles();
		if (distance > 2500) {
			percentile = 5;
		} else if (distance < 50) {
			percentile = 50;
		} else {
			percentile = (int) ((-0.01836) * distance + 50.91836);
		}
		return getMapLocations(percentile);
	}

	/////////////////////////// Caching
	public void updateUserCriteria() {
		if (theUser != null) {
			setToList();
		}
	}

	private void penalize(double penalizePercentage, int[] term, rawLocation theCurrentLocation) {
		boolean execute = true;
		if (term == null) {
			theCurrentLocation.ourScore *= (1 - penalizePercentage);
			return;
		}
		for (int i : term) {
			if (theUser.getInterest()[i]) {
				execute = true;
			} else {
				execute = false;
			}
		}
		if (execute) {
			theCurrentLocation.ourScore *= (1 - penalizePercentage);
		}
	}

	private void bonus(double bonusPercentage, int[] term, rawLocation theCurrentLocation) {
		boolean execute = true;
		if (term == null) {
			theCurrentLocation.ourScore *= (1 + bonusPercentage);
			return;
		}
		for (int i : term) {
			if (theUser.getInterest()[i]) {
				execute = true;
			} else {
				execute = false;
			}
		}
		if (execute) {
			theCurrentLocation.ourScore *= (1 + bonusPercentage);
		}
	}

	private void bonusBasedOnYoung(rawLocation theCurrentLocation) {
		if (theUser.getYoung() <= 15) {
			theCurrentLocation.ourScore *= 1.2;
		}
	}

	private void bonusBasedOnOld(rawLocation theCurrentLocation) {
		if (theUser.getOld() >= 60) {
			theCurrentLocation.ourScore *= 1.1;
		}
	}

	private void penalizeBasedOnYoung(rawLocation theCurrentLocation) {
		if (theUser.getYoung() <= 15) {
			theCurrentLocation.ourScore *= 0.8;
		}
	}

	private void penalizeBasedOnOld(rawLocation theCurrentLocation) {
		if (theUser.getOld() >= 60) {
			theCurrentLocation.ourScore *= 0.8;
		}
	}

	////////////////////////// Helper Classes/Functions
	private void personalizeScoreHelper(rawLocation theCurrentLocation) {
		// theUser
		// rawLocation theCurrentLocation
		// theCurrentLocation.theLocation.getCategories().getMyCategories();
		// even number of categories, listed in pairs or duplicates
		// Score one location at time
		// affect theCurrentLocation.ourScore

		// camping 0
		// shopping 1
		// outdoors 2
		// nightLife 3
		// food 4
		// education 5
		// action 6
		// relaxation 7
		int[] penalizeArray;
		int[] bonusArray;
		ArrayList<String> myCategories = theCurrentLocation.theLocation.getCategories().getMyCategories();
		for (String category : myCategories) {
			switch (category) {
			case "hotels":
				if (overNight) {
					bonus(LEVEL3, null, theCurrentLocation);
				}
				break;
			case "libraries":
				bonusArray = new int[] { 5 };
				bonusBasedOnOld(theCurrentLocation);
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "Day Spas":
				bonusArray = new int[] { 7 };
				bonusBasedOnOld(theCurrentLocation);
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "Colleges & Universities":
				bonusArray = new int[] { 5 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "Escape Games":
				bonusArray = new int[] { 6 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "Fast Food":
				bonusArray = new int[] { 4 };
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "Wedding Planning":
				penalize(OB, null, theCurrentLocation);
				break; // penalize
			case "Pubs":
				if (theUser.getOld() < 21)
					penalize(1.0, null, theCurrentLocation);
				bonusArray = new int[] { 3 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "bars":
				bonusArray = new int[] { 3 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				if (theUser.getOld() < 21)
					penalize(1.0, null, theCurrentLocation);
				break;
			case "museums":
				bonusBasedOnOld(theCurrentLocation);
				bonusArray = new int[] { 5 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "sportsbars":
				penalizeBasedOnOld(theCurrentLocation);
				bonusArray = new int[] { 5 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "campgrounds":
				bonusArray = new int[] { 0 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "gyms":
				penalize(OB, null, theCurrentLocation);
				break; // p
			case "massage":
				bonusArray = new int[] { 7 };
				bonusBasedOnOld(theCurrentLocation);
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "hiking":
				bonusArray = new int[] { 6 }; // TODO
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "womenscloth":
				bonusArray = new int[] { 1 };
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "Funiture Stores":
				penalize(OB, null, theCurrentLocation);
				break;
			case "rvparks":
				bonusArray = new int[] { 5 };
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "specialtyschools":
				penalize(OB, null, theCurrentLocation);
				break;
			case "zoo":
				bonusBasedOnYoung(theCurrentLocation);
				bonusArray = new int[] { 5, 6 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "foodtrucks":
				bonusArray = new int[] { 4 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "wineries":
				bonusBasedOnOld(theCurrentLocation);
				if (theUser.getOld() < 21)
					penalize(1.0, null, theCurrentLocation);
				bonusArray = new int[] { 3 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "festivals":
				bonusArray = new int[] { 6 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "zipline":
				penalizeBasedOnOld(theCurrentLocation);
				bonusArray = new int[] { 6 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "giftshops":
				bonusArray = new int[] { 1 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "tubing":
				bonusArray = new int[] { 2, 6 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "rafting":
				bonusArray = new int[] { 2, 6 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "seafood":
				bonusArray = new int[] { 4 };
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "cocktailbars":
				if (theUser.getOld() < 21)
					penalize(1.0, null, theCurrentLocation);
				bonusArray = new int[] { 3 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "casinos":
				bonusBasedOnOld(theCurrentLocation);
				penalizeBasedOnYoung(theCurrentLocation);
				bonusArray = new int[] { 3 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				if (theUser.getOld() < 21)
					penalize(1.0, null, theCurrentLocation);
				break;
			case "karaoke":
				bonusArray = new int[] { 3 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "djs":
				bonusArray = new int[] { 3 };
				bonus(LEVEL1, bonusArray, theCurrentLocation);
				break;
			case "diners":
				bonusBasedOnOld(theCurrentLocation);
				bonusArray = new int[] { 4 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "skydiving":
				bonusArray = new int[] { 6 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "steak":
				bonusArray = new int[] { 4 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "eventphotography":
				penalize(OB, null, theCurrentLocation);
				break; // p
			case "Photo Booth Rentals":
				penalize(OB, null, theCurrentLocation);
				break; // p
			case "rugs":
				penalize(OB, null, theCurrentLocation);
				break; // p
			case "carpeting":
				penalize(OB, null, theCurrentLocation);
				break; // p
			case "bowling":
				bonusArray = new int[] { 3, 6 };
				bonusBasedOnYoung(theCurrentLocation);
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "fishing":
				bonusArray = new int[] { 0, 6 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "theater":
				bonusArray = new int[] { 5 };
				bonusBasedOnOld(theCurrentLocation);
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "skiresorts":
				if (inWinter()) {
					bonus(LEVEL3, null, theCurrentLocation);
				}
				bonusArray = new int[] { 2, 6 };
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			case "mobilephones":
				penalize(OB, null, theCurrentLocation);
				break;
			case "gourmet":
				bonusArray = new int[] { 4 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "comfortfood":
				bonusArray = new int[] { 4 };
				bonus(LEVEL2, bonusArray, theCurrentLocation);
				break;
			case "realestate":
				penalize(OB, null, theCurrentLocation);
				break;
			case "dinnertheater":
				bonusArray = new int[] { 4 };
				bonusBasedOnYoung(theCurrentLocation);
				bonus(LEVEL3, bonusArray, theCurrentLocation);
				break;
			}
		}

		System.out.println(theCurrentLocation.theLocation.getCategories().getMyCategories());

	}

	private boolean inWinter() {
		if (theUser.getStartTime().getMonth() > 10 || theUser.getStartTime().getMonth() < 3) {
			return true;
		}
		return false;
	}

}

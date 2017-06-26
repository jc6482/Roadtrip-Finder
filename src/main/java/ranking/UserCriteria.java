package ranking;

import java.sql.Time;

public class UserCriteria {

	// Start City

	// End City

	// Start Time
	Time start = null;
	// End Time
	Time end = null;
	// Number of people
	int numberOfPeople;
	// Age of Youngest
	int youngestAge;
	// Age of Oldest
	int oldestAge;
	// Budget, this is the interval num, ex 1-5, not $600
	int budget;
	// Interest
	Boolean camping = false;
	Boolean shopping = false;
	Boolean outdoorRec = false;
	Boolean nightlife = false;
	Boolean eating = false;
	Boolean educational = false;
	Boolean action = false;
	Boolean relaxing = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

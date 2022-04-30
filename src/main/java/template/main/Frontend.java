package template.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Frontend {
	private String startPlace;
	private String endPlace;
	private int numPeople;

	private int young;
	private int old;
	
	private int budget; // 0= "tight". 1= "reasonable", 2= "extravagant" ,3= "don't care"
	
	//may switch to a list later , DONT
	private boolean[] interest;
	private String[] interestStore;
	private Date startTime;
	private Date endTime;

	private Map<Integer,String> budgetList = new HashMap<>();
	
	public Frontend(String startPlace, String endPlace, int numPeople, boolean[] interest, Date startTime, Date endTime,int budget,int young, int old) {

		interestStore = new String[8];
		interestStore[0] = "Camping"; // Should leave all of these in the same index, because the RankingAlg depends on this.
		interestStore[1] = "Shopping";
		interestStore[2] = "Outdoor Rec";
		interestStore[3] = "Night Life";
		interestStore[4] = "Food";
		interestStore[5] =  "Educational";
		interestStore[6] =  "Action";
		interestStore[7] =  "Relaxation";
		
		budgetList.put(0, "tight");
		budgetList.put(1, "reasonable");
		budgetList.put(2, "extravagant");
		budgetList.put(3, "don't care");
		
		this.startPlace = startPlace;
		this.endPlace = endPlace;
		this.numPeople = numPeople;
		this.interest = interest;
		this.startTime = startTime;
		this.endTime = endTime;
		this.young = young;
		this.old = old;
		this.budget = budget;
	}
	public String getStartPlace() {
		return startPlace;
	}
	public String getEndPlace() {
		return endPlace;
	}
	public String[] getInterestStore() {
		return interestStore;
	}
	public void setInterestStore(String[] interestStore) {
		this.interestStore = interestStore;
	}
	public Map<Integer, String> getBudgetList() {
		return budgetList;
	}
	public void setBudgetList(Map<Integer, String> budgetList) {
		this.budgetList = budgetList;
	}
	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}
	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}
	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}
	public void setYoung(int young) {
		this.young = young;
	}
	public void setOld(int old) {
		this.old = old;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	public void setInterest(boolean[] interest) {
		this.interest = interest;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getNumPeople() {
		return numPeople;
	}
	public int getBudget() {
		return budget;
	}
	public boolean[] getInterest() {
		return interest;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public int getYoung(){
		return young;
	}
	public int getOld(){
		return old;
	}
	public String[] getInterestArray(){
		return interestStore;
	}
	@Override
	public String toString() {
		return "StratPlace: "+ startPlace +" EndPlace: "+endPlace 
				+ " StartTime: " + startTime.toString() + " EndTime: " + endTime.toString()
				+ " Budget: " + budgetList.get(budget) +" Age range: " + young + "-" + old
				+ " Num of People " + numPeople ;
	}

}

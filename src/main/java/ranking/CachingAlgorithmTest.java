package ranking;
import org.json.simple.parser.ParseException;
import ranking.RankingAlgorithm;
import template.main.Frontend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dean on 3/22/16.
 */
public class CachingAlgorithmTest {
    public boolean[] interests = {false, false, false, false, false, false, false, false};
    public String starPlace = "Vodka";
    public String endPlace = "Sake";
    public final int InterestSize = 8;
    public int numPeople = 3;
    public int budget;
    public int young;
    public int old;
    public Date startDate = new Date(2016, 3, 25, 12, 30);
    public Date endDate = new Date(2016, 3, 27, 12, 30);

    public static void main(String args[]) throws IllegalAccessException, InterruptedException, ParseException {

        boolean[] shopping = {false, true, false, true, false, false, false, false};
        boolean[] food = {false, false, false, false, true, false, false, false};
        boolean[] adventure= {true, false, true, true, false, false, false, false };
        boolean[] education= {false, false, false, false, false, true, false, false};
        boolean[] relax ={false, false, false, true, false, false, false, true};

        ArrayList<Double> wacoToAustin = new ArrayList<Double>();
        ArrayList<Double> wacoToNewMexico = new ArrayList<Double>();
        ArrayList<Double> wacoToOkla = new ArrayList<Double>();

        Date start = new Date(2016, 3, 25, 12, 30);
        Date end = new Date(2016, 3, 25, 23, 40);
        Date overnightEnd = new Date(2016, 3, 27, 12, 30);

        int poor = 0;
        int mid = 1;
        int rich = 2;
        int childAge = 12;
        int adultAge = 23;
        int adultAge2 = 30;
        int elderAge = 70;

        // Waco to Austin
        // Waco Area
        wacoToAustin.add(31.458559);
        wacoToAustin.add(-97.299095);
        wacoToAustin.add(31.616761);
        wacoToAustin.add(-97.017980);

        // Austin Area
        wacoToAustin.add(30.091865);
        wacoToAustin.add(-98.031093);
        wacoToAustin.add(30.571120);
        wacoToAustin.add(-97.417037);

        // Waco to NewMexico
        wacoToNewMexico.add(30.828278254166488);
        wacoToNewMexico.add(-98.40711465177992);
        wacoToNewMexico.add(31.262473603333195);
        wacoToNewMexico.add(-96.3362577529665);
        wacoToNewMexico.add(31.262473603333195);
        wacoToNewMexico.add(-99.44254310118657);
        wacoToNewMexico.add(31.696668952499895);
        wacoToNewMexico.add(-96.3362577529665);
        wacoToNewMexico.add(31.696668952499895);
        wacoToNewMexico.add(-101.51339999999999);
        wacoToNewMexico.add(32.1308643016666);
        wacoToNewMexico.add(-96.3362577529665);
        wacoToNewMexico.add(32.1308643016666);
        wacoToNewMexico.add(-102.03111422470334);
        wacoToNewMexico.add(32.5650596508333);
        wacoToNewMexico.add(-96.85397197766986);
        wacoToNewMexico.add(32.5650596508333);
        wacoToNewMexico.add(-105.13739957292347);
        wacoToNewMexico.add(32.999255000000005);
        wacoToNewMexico.add(-97.88940042707657);
        wacoToNewMexico.add(32.999255000000005);
        wacoToNewMexico.add(-105.13739957292347);
        wacoToNewMexico.add(33.433450349166705);
        wacoToNewMexico.add(-99.96025732588993);
        wacoToNewMexico.add(33.433450349166705);
        wacoToNewMexico.add(-106.17282802233012);
        wacoToNewMexico.add(33.867645698333405);
        wacoToNewMexico.add(-100.47797155059328);
        wacoToNewMexico.add(33.867645698333405);
        wacoToNewMexico.add(-106.69054224703348);
        wacoToNewMexico.add(34.73603639666681);
        wacoToNewMexico.add(-103.58425689881341);
        wacoToNewMexico.add(34.73603639666681);
        wacoToNewMexico.add(-106.69054224703348);
        wacoToNewMexico.add(35.17023174583351);
        wacoToNewMexico.add(-104.61968534822012);

        // Waco to Jackson

        wacoToOkla.add(31.771723603333196);
        wacoToOkla.add(-98.34045075562494);
        wacoToOkla.add(33.50850500000001);
        wacoToOkla.add(-97.81971037781244);
        wacoToOkla.add(33.94270034916671);
        wacoToOkla.add(-98.34045075562494);
        wacoToOkla.add(36.11367709500022);
        wacoToOkla.add(-97.81971037781244);
        wacoToOkla.add(30.903332904999793);
        wacoToOkla.add(-97.81971037781244);
        wacoToOkla.add(36.11367709500022);
        wacoToOkla.add(-96.77822962218755);
        wacoToOkla.add(30.903332904999793);
        wacoToOkla.add(-96.77822962218755);
        wacoToOkla.add(35.24528639666681);
        wacoToOkla.add(-96.25748924437505);


        CachingAlgorithmTest shoppingInterestR = new CachingAlgorithmTest(shopping, rich, adultAge, adultAge2);
        CachingAlgorithmTest shoppingInterestP = new CachingAlgorithmTest(shopping, poor, adultAge, adultAge2);
        CachingAlgorithmTest foodInterest = new CachingAlgorithmTest(food, mid, adultAge, adultAge2);
        CachingAlgorithmTest adventureInterest = new CachingAlgorithmTest(adventure, mid, adultAge, adultAge2);
        CachingAlgorithmTest educationInterest = new CachingAlgorithmTest(education, mid, adultAge, adultAge2);
        CachingAlgorithmTest relaxInterestY= new CachingAlgorithmTest(relax, mid, childAge, adultAge2);
        CachingAlgorithmTest relaxInterestO= new CachingAlgorithmTest(relax, mid, adultAge, elderAge);


        Frontend UserGroupA = new Frontend(shoppingInterestP.starPlace, shoppingInterestP.endPlace,
                shoppingInterestP.numPeople, shoppingInterestP.interests,
                shoppingInterestP.startDate, shoppingInterestP.endDate,
                shoppingInterestP.budget, shoppingInterestP.young, shoppingInterestP.old);
        Frontend UserGroupB = new Frontend(foodInterest.starPlace, foodInterest.endPlace,
                foodInterest.numPeople, foodInterest.interests,
                foodInterest.startDate, foodInterest.endDate,
                foodInterest.budget, foodInterest.young, foodInterest.old);
        Frontend UserGroupC = new Frontend(shoppingInterestP.starPlace, shoppingInterestP.endPlace,
                shoppingInterestP.numPeople, shoppingInterestP.interests,
                shoppingInterestP.startDate, shoppingInterestP.endDate,
                shoppingInterestP.budget, shoppingInterestP.young, shoppingInterestP.old);
        Frontend UserGroupD = new Frontend(shoppingInterestR.starPlace, shoppingInterestR.endPlace,
                shoppingInterestR.numPeople, shoppingInterestR.interests,
                shoppingInterestR.startDate, shoppingInterestR.endDate,
                shoppingInterestR.budget, shoppingInterestR.young, shoppingInterestR.old);
        Frontend UserGroupAyougn;
        Frontend UserGroupByoung;
        Frontend UserGroupAovernight;

        RankingAlgorithm testforLocation = new RankingAlgorithm(wacoToAustin, UserGroupA);
        RankingAlgorithm testforLocation2 = new RankingAlgorithm(wacoToNewMexico, UserGroupA);

        RankingAlgorithm testforInterest = new RankingAlgorithm(wacoToNewMexico, UserGroupB);
        RankingAlgorithm testforInterest2 = new RankingAlgorithm(wacoToNewMexico, UserGroupC);

        RankingAlgorithm testforBudget = new RankingAlgorithm(wacoToAustin, UserGroupD);

        for (String s : testforLocation.listOfPersonalizedPlaces()) {
            System.out.print(s);
        }
        for (String s : testforLocation2.listOfPersonalizedPlaces()){
            System.out.print(s);
        }

        for (String s : testforInterest.listOfPersonalizedPlaces()) {
            System.out.print(s);
        }

        for (String s: testforInterest2.listOfPersonalizedPlaces()){
            System.out.print(s);
        }
        for (String s : testforBudget.listOfPersonalizedPlaces()) {
            System.out.print(s);
        }

    }

    public void setInterests(boolean[] aInterests){
        if(aInterests.length == 8) {
            interests = aInterests.clone();
        }
    }

    public CachingAlgorithmTest(boolean[] aInterests, int aBudget, int aYoung, int aOld){
        this.interests = aInterests;
        this.budget = aBudget;
        this.young = aYoung;
        this.old = aOld;
    }




}

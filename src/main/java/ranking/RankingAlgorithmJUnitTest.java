package ranking;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import template.main.Frontend;

/**
 * Test Class for the JUnit Paramaterized Testing
 *
 */
@RunWith(Enclosed.class)
public class RankingAlgorithmJUnitTest {
	
	private static String fullResults;
	
	@RunWith(Parameterized.class)
	public static class AlgorithmTests{
		ArrayList<Double> latlngs;
		Frontend userCriteria;
	    
		public AlgorithmTests(ArrayList<Double> latlngs, Frontend userCriteria) {
			this.latlngs = latlngs;
			this.userCriteria = userCriteria;

		}
		
	    // Declares parameters here
	 	@Parameters()
	 	public static Iterable<Object[]> data1() {
	 		
	        boolean[] shopping = {false, true, false, true, false, false, false, false};
	        boolean[] food = {false, false, false, false, true, false, false, false};
	        boolean[] adventure= {true, false, true, true, false, false, false, false };
	        boolean[] education= {false, false, false, false, false, true, false, false};
	        boolean[] relax ={false, false, false, true, false, false, false, true};
	
	        Date start = new Date(2016, 3, 25, 12, 30);
	        Date end = new Date(2016, 3, 25, 23, 40);
	        Date overnightEnd = new Date(2016, 3, 27, 12, 30);
	
	        
	        ArrayList<Double> locations1 = new ArrayList<Double>();
		     // Waco to Austin
		        // Waco Area
		        locations1.add(31.458559);
		        locations1.add(-97.299095);
		        locations1.add(31.616761);
		        locations1.add(-97.017980);
			
			    // Austin Area
		        locations1.add(30.091865);
		        locations1.add(-98.031093);
		        locations1.add(30.571120);
		        locations1.add(-97.417037);
		        
		        Frontend UserGroupA = new Frontend("Waco","Austin",5,shopping,start,end,1000,10,70);
		        
		        
		        ArrayList<Double> locations2 = new ArrayList<Double>();
		     // Waco to NewMexico
		        locations2.add(30.828278254166488);
		        locations2.add(-98.40711465177992);
		        locations2.add(31.262473603333195);
		        locations2.add(-96.3362577529665);
		        locations2.add(31.262473603333195);
		        locations2.add(-99.44254310118657);
		        locations2.add(31.696668952499895);
		        locations2.add(-96.3362577529665);
		        locations2.add(31.696668952499895);
		        locations2.add(-101.51339999999999);
		        locations2.add(32.1308643016666);
		        locations2.add(-96.3362577529665);
		        locations2.add(32.1308643016666);
		        locations2.add(-102.03111422470334);
		        locations2.add(32.5650596508333);
		        locations2.add(-96.85397197766986);
		        locations2.add(32.5650596508333);
		        locations2.add(-105.13739957292347);
		        locations2.add(32.999255000000005);
		        locations2.add(-97.88940042707657);
		        locations2.add(32.999255000000005);
		        locations2.add(-105.13739957292347);
		        locations2.add(33.433450349166705);
		        locations2.add(-99.96025732588993);
		        locations2.add(33.433450349166705);
		        locations2.add(-106.17282802233012);
		        locations2.add(33.867645698333405);
		        locations2.add(-100.47797155059328);
		        locations2.add(33.867645698333405);
		        locations2.add(-106.69054224703348);
		        locations2.add(34.73603639666681);
		        locations2.add(-103.58425689881341);
		        locations2.add(34.73603639666681);
		        locations2.add(-106.69054224703348);
		        locations2.add(35.17023174583351);
		        locations2.add(-104.61968534822012);
		        
		        Frontend UserGroupB = new Frontend("Waco","New Mexico",50,food,start,end,1000,6,27);
	 		
	 		
	 		return Arrays.asList(new Object[][]{{locations1,UserGroupA},{locations2,UserGroupB},{locations1,UserGroupB},{locations2,UserGroupA}});
	 	}
	 	
	 	@Test
		public void test_algorithm() throws IllegalArgumentException, IllegalAccessException, SecurityException, ParseException, InterruptedException {
	 			RankingAlgorithm test = new RankingAlgorithm(this.latlngs,this.userCriteria);
	 			concatResults(test.listOfPersonalizedPlaces());
	 			System.out.println(returnResults());
		}
	 	
	}
 	
 	public static void concatResults(ArrayList<String> currentResults){
 		currentResults.forEach((v) -> fullResults += v);
 	}
 	
 	public static String returnResults(){
 		return fullResults;
 	}
	

}

package template.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ranking.MapLocation;
import ranking.Route;
import ranking.cachingAlgorithm;
import template.main.Frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Controller
public class MainController {
	
	ArrayList<String> searchTerms = new ArrayList<String>();
	cachingAlgorithm algorithm = new cachingAlgorithm();
	Frontend theUser = null;
	
	
	@RequestMapping(value = { "/","/first", "/first.html" })
	public String home2() {

		return "first";
	}	
	@RequestMapping(value = { "/user_criteria", "/user_criteria.html" })
	public String home() {

		return "user_criteria";
	}	

	@RequestMapping(value = {"/mapPage", "/mapPage.html"})
	public String Map_Page() {

		return "mapPage";
	}
	
	@RequestMapping(value = {"/confirmation", "/confirmation.html"})
	public String confirmation() {
		return "confirmation";
	}	
	@RequestMapping(value = {"/boxes"}, method  = RequestMethod.POST) 
	public String boxes(@RequestParam("boxData") String boxes, @RequestParam("startLoc") String sPlace, @RequestParam("endLoc") String ePlace, @RequestParam("tripInfo") String tripInfo, Model model) throws IllegalArgumentException, IllegalAccessException, SecurityException, org.json.simple.parser.ParseException, InterruptedException, FileNotFoundException, IOException, ExecutionException {

		System.out.println(boxes);
		System.out.println("tripinfo " + tripInfo);

		String data[] = boxes.split("(\\(|\\,|\\)|\\s)");
		ArrayList<Double> latlngs = new ArrayList<Double>();
		for(int i = 0; i < data.length; i++){
			if(!data[i].isEmpty() && data[i] != " "){
				latlngs.add(Double.parseDouble(data[i]));
			}
		}
		//Add latlngs to model?
		//Query Yelp and save results.
		//Send Results to ranking algorithm.
		//Generate new page with ranked results added to google map.
		//....
		//Profit?
		
		System.out.println(latlngs.toString());

		algorithm.changeRoute(new Route(latlngs, tripInfo));

		ArrayList<MapLocation> ret = algorithm.getWeightedMapLocations();
		for(MapLocation o : ret){
			System.out.println(o.getTheRating());
		}
		System.out.println("ret.toString() = " + ret.toString());
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		model.addAttribute("MAPLOCATIONS", ret.toString());
		model.addAttribute("startPlace", sPlace);
		model.addAttribute("endPlace", ePlace);
		
		return "mapPage";
	}
	
	
	
	@RequestMapping(value = {"/test"}, method  = RequestMethod.POST)
	public String submitForm(
			@RequestParam(value ="splace", required = false) String sPlace, @RequestParam(value ="eplace", required = false) String ePlace,
			@RequestParam(value ="num", required = false) String num, @RequestParam(value ="budget", required = false) String budget, 
			@RequestParam(value ="young", required = false) String young, @RequestParam(value ="old", required = false) String old,
			@RequestParam(value = "sdate", required = false) String sdate, @RequestParam(value = "edate", required = false) String edate,
			@RequestParam(value = "interest1", required = false) String interest1, @RequestParam(value = "interest2", required = false) String interest2,
			@RequestParam(value = "interest3", required = false) String interest3, @RequestParam(value = "interest4", required = false) String interest4,
			@RequestParam(value = "interest5", required = false) String interest5, @RequestParam(value = "interest6", required = false) String interest6,
			@RequestParam(value = "interest7", required = false) String interest7, @RequestParam(value = "interest8", required = false) String interest8, Model model) throws ParseException, ExecutionException {

		int numPeople = Integer.parseInt(num);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
		Date sDate = format.parse(sdate);		
		Date eDate = format.parse(edate);
		boolean[] store = new boolean[8];
		model.addAttribute("startPlace", sPlace);
		model.addAttribute("endPlace", ePlace);
		
		store[0] = interest1 != null;
		store[1] = interest2 != null;
		store[2] = interest3 != null;
		store[3] = interest4 != null;
		store[4] = interest5 != null;
		store[5] = interest6 != null;
		store[6] = interest7 != null;
		store[7] = interest8 != null;
		Frontend fe = new Frontend(sPlace,ePlace,numPeople,store,sDate,eDate,Integer.parseInt(budget),Integer.parseInt(young.trim()),Integer.parseInt(old.trim()));
		algorithm.changeUser(fe);
		System.out.println(fe.toString());
		System.out.println("Interest: ");
		String[] interest = fe.getInterestArray();
		for(int i = 0; i< 8; i++) {
			if(store[i]){
				System.out.print(interest[i]+"  ");
			}
		}
		
		this.searchTerms.clear();
		for(int i = 0; i< 8; i++) {
			if(store[i]){
				switch(i){
					case 0: this.searchTerms.add(interest1);
					break;
					case 1: this.searchTerms.add(interest2);
					break;
					case 2: this.searchTerms.add(interest3);
					break;
					case 3: this.searchTerms.add(interest4);
					break;
					case 4: this.searchTerms.add(interest5);
					break;
					case 5: this.searchTerms.add(interest6);
					break;
					case 6: this.searchTerms.add(interest7);
					break;
					case 7: this.searchTerms.add(interest8);
					break;
					
				}
			}
		}
		return "mapPage";
	}

	
	
}






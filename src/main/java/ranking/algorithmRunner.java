package ranking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.MoreObjects;
import com.google.common.cache.*;

import ranking.RankingAlgorithm.rawLocation;

/**
 * Created by dean on 3/28/16.
 */
public class algorithmRunner {
	private static CacheLoader<Route, ArrayList<rawLocation>> loader = null;
	private static LoadingCache<String, ArrayList<rawLocation>> locationCache = null;
	//public Yelp
    public static void main(String args[]){
    	
    }
    
    public algorithmRunner(){
        loader = new CacheLoader<Route, ArrayList<rawLocation>>(){ // build the cacheloader
            
			@Override
			public ArrayList<rawLocation> load(Route arg0) throws Exception {
				// TODO Auto-generated method stub
				return null;//getFromDatabase(empId);
			} 
        };
    }
        
    
}

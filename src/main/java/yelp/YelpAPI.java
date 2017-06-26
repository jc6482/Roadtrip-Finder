package yelp;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.beust.jcommander.Parameter;

import yelp.data.*;

/**
 * Code sample for accessing the Yelp API V2.
 * 
 * This program demonstrates the capability of the Yelp API version 2.0 by using
 * the Search API to query for businesses by a search term and location, and the
 * Business API to query additional information about the top result from the
 * search query.
 * 
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp
 * Documentation</a> for more info.
 * 
 */
public class YelpAPI {

	private static final String API_HOST = "api.yelp.com";
	private static final String DEFAULT_TERM = "dinner";
	private static final String DEFAULT_FILTER = " ";
	private static final String DEFAULT_LOCATION = "San Francisco, CA";
	private static final int SEARCH_LIMIT = 10;
	private static final String SEARCH_PATH = "/v2/search";
	private static final String BUSINESS_PATH = "/v2/business";


	/*
	 * Update OAuth credentials below from the Yelp Developers API site:
	 * http://www.yelp.com/developers/getting_started/api_access
	 */
	private static final String CONSUMER_KEY = "NfAxai5u2HkTaRdx-bI2Vg";
	//private static final String CONSUMER_KEY = "PZ1sFtADB_WPYruIV--C0g";
	private static final String CONSUMER_SECRET = "QDcTrrJUuawOFqhV4YWDcj5v0H4";
	//private static final String CONSUMER_SECRET = "QquivxeCGsJqtlPW-MiKXWcldlE";
	private static final String TOKEN = "Os225Jsha_FAmBnyy_Lk6NgRvJa-heXR";
	//private static final String TOKEN = "We1WDHS8gvbYhB8tQKTN2fIflGkGxWE7";
	private static final String TOKEN_SECRET = "mf3nIsSajAxpSmbmB5S9j3OV9dU";
	//private static final String TOKEN_SECRET = "B2_u0dsyMBJv_kds_Lu-p6GdBwA";

	OAuthService service;
	Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * @param consumerKey
	 *            Consumer key
	 * @param consumerSecret
	 *            Consumer secret
	 * @param token
	 *            Token
	 * @param tokenSecret
	 *            Token secret
	 */
	public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey).apiSecret(consumerSecret)
				.build();
		this.accessToken = new Token(token, tokenSecret);
	}

	/**
	 * Creates and sends a request to the Search API by term and location.
	 * <p>
	 * See
	 * <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp
	 * Search API V2</a> for more info.
	 * 
	 * @param term
	 *            <tt>String</tt> of the search term to be queried
	 * @param location
	 *            <tt>String</tt> of the location
	 * @return <tt>String</tt> JSON Response
	 */
	public Response searchForBusinessesByLocation(String term, String location, String filter) {
		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("location", location);
		request.addQuerystringParameter("category_filter", filter);
		request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
		return sendRequestAndGetResponse(request);
	}
	
	public Response searchForBusinessesByBounds(String term, String bounds, String filter) {
		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("bounds", bounds);
		request.addQuerystringParameter("category_filter", filter);
		request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and sends a request to the Business API by business ID.
	 * <p>
	 * See
	 * <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp
	 * Business API V2</a> for more info.
	 * 
	 * @param businessID
	 *            <tt>String</tt> business ID of the requested business
	 * @return <tt>String</tt> JSON Response
	 */
	public Response searchByBusinessId(String businessID) {
		OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and returns an {@link OAuthRequest} based on the API endpoint
	 * specified.
	 * 
	 * @param path
	 *            API endpoint to be queried
	 * @return <tt>OAuthRequest</tt>
	 */
	private OAuthRequest createOAuthRequest(String path) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
		return request;
	}

	/**
	 * Sends an {@link OAuthRequest} and returns the {@link Response} body.
	 * 
	 * @param request
	 *            {@link OAuthRequest} corresponding to the API request
	 * @return <tt>String</tt> body of API response
	 */
	private Response sendRequestAndGetResponse(OAuthRequest request) {
		//System.out.println("Querying " + request.getCompleteUrl() + " ...");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response;
	}

	/**
	 * Queries the Search API based on the command line arguments and takes the
	 * first result to query the Business API.
	 * 
	 * @param yelpApi
	 *            <tt>YelpAPI</tt> service instance
	 * @param yelpApiCli
	 *            <tt>YelpAPICLI</tt> command line arguments
	 * @throws ParseException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private static void queryAPI(YelpAPI yelpApi, YelpAPIDLI yelpApiCli,ArrayList<YelpData> data)
			throws IllegalArgumentException, SecurityException, ParseException {
		Response searchResponseJSON = yelpApi.searchForBusinessesByBounds(yelpApiCli.term, yelpApiCli.bounds,
				yelpApiCli.category_filter);

		JSONParser parser = new JSONParser();
		JSONObject response = null;
		try {
			response = (JSONObject) parser.parse(searchResponseJSON.getBody());
		} catch (ParseException pe) {
			System.out.println("Error: could not parse JSON response:");
			System.out.println(searchResponseJSON);
			System.exit(1);
		}

		JSONArray businesses = (JSONArray) response.get("businesses");

		if (businesses == null) {
			//System.out.println("We could not find any businesses :(");
		}

		//System.out.println(String.format("%s businesses found, here are the results....", businesses.size()));

		// Select the first business and display business details
		for (int i = 0; i < businesses.size(); i++) {
			data.add(new YelpData());
			JSONObject Buisness = (JSONObject) businesses.get(i);
			String BusinessID = Buisness.get("id").toString();
			Response businessResponseJSON = yelpApi.searchByBusinessId(BusinessID.toString());

			response = (JSONObject) parser.parse(businessResponseJSON.getBody());

			response.forEach((k, v) -> data.get(data.size() - 1).setValues(k, v));
		}
	}

	/**
	 * Command-line interface for the sample Yelp API runner.
	 */
	private static class YelpAPIDLI {
		@Parameter(names = { "-q", "--term" }, description = "Search Query Term")
		public String term = DEFAULT_TERM;

		@Parameter(names = {"--bounds" }, description = "Location to be Queried")
		public String bounds = DEFAULT_LOCATION;

		@Parameter(names = { "-cf", "--category_filter" }, description = "category_filter to be Queried")
		public String category_filter = DEFAULT_FILTER;
	}


	
	/**
	 * Function for YELP API rather than a main call, it calls the queryAPI
	 * function which populates an arraylist of locations
	 * 
	 * @throws ParseException
	 *             In case JSON can't be parsed
	 * @throws SecurityException
	 *             In case we don't have proper access
	 * @throws IllegalAccessException
	 *             In case the reflection can't access variable
	 * @throws IllegalArgumentException
	 *             In case the reflection can't find the variable
	 */
	public static ArrayList<YelpData> queryAPI(String term, Double SWlatitude, Double SWlongitude, Double NElatitude,
			Double NElongitude)
					throws ParseException, IllegalArgumentException, IllegalAccessException, SecurityException {
		YelpAPIDLI yelpApiCli = new YelpAPIDLI();
		YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
		ArrayList<YelpData> data = new ArrayList<YelpData>();

		yelpApiCli.term = term;

		yelpApiCli.bounds = (Double.toString(SWlatitude)+","+Double.toString(SWlongitude)+"|"+Double.toString(NElatitude)+","+Double.toString(NElongitude));

		yelpApiCli.category_filter = "";

		queryAPI(yelpApi, yelpApiCli,data);
		return data;
	}
	
	public static ArrayList<YelpData> queryAPI(String term, String filter, Double SWlatitude, Double SWlongitude, Double NElatitude,
			Double NElongitude)
					throws ParseException, IllegalArgumentException, IllegalAccessException, SecurityException {
		YelpAPIDLI yelpApiCli = new YelpAPIDLI();
		YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
		ArrayList<YelpData> data = new ArrayList<YelpData>();

		yelpApiCli.term = term;

		yelpApiCli.bounds = (Double.toString(SWlatitude)+","+Double.toString(SWlongitude)+"|"+Double.toString(NElatitude)+","+Double.toString(NElongitude));

		yelpApiCli.category_filter = filter;

		queryAPI(yelpApi, yelpApiCli,data);
		return data;
	}

}

	



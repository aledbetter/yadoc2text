package com.extract.web;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "resp")
@XmlAccessorType(XmlAccessType.FIELD) 
@XmlType(propOrder = { 
    "code", 
    "errors", 
    "info",
    "results",    
    "doc",
}) 
public class RestResp {

	@XmlElement(name="code", required = true)
	private int code;
	//@XmlElement(name="time", required = true)
	@XmlTransient
	private int time;
	//@XmlElement(name="version", required = true)
	@XmlTransient
	private String version;
	
	@XmlTransient
	private String timestamp;
	@XmlTransient
	private String call_id;	
	
	
	// these items are for internal use
	@XmlTransient
	private long startTime; // used to save milliseconds on create of this object
	
	@XmlElement(name="errors", required = true)
	private HashMap<String, Integer> resp_params; 
	
	// alternate string for response if needed
	@XmlElement(name="info", required = true)
	private HashMap<String, String> info; 
	
	// for multiple response sets
	@XmlElementWrapper( name="results" )
	private List<HashMap<String, String>> results = null;

	@XmlElementWrapper( name="doc" )
	private List<HashMap<String, Object>> doc = null;
	
	public RestResp(UriInfo info, 
					HttpServletRequest hsr, 
					MultivaluedMap<String, String> form_params, 
					String atok, 
					String atok_cookie) {
		
		this.time = 0;
		this.timestamp = Gtil.getCurrentTimeStamp();
	
	    //get millis at start
		this.startTime = System.currentTimeMillis();

    	this.setCode(200); // start with no problem always
    	this.setVersion("00_DEV_00");
    	
    	//if (AppDefault.isDev) {
    //		this.setVersion("00_DEV_00");    		
    //	} else {
    //		this.setVersion(AppDefault.version);
    //	}
    	this.call_id = UUID.randomUUID().toString();

    	// get the atok
    	if (atok == null || atok.isEmpty()) {
    		atok = atok_cookie;
    	}
    	
    	// get the ip address of the caller
		String ip_address = "";
		if (hsr != null) {
			ip_address = hsr.getRemoteAddr();
			if (hsr.getHeader("X-Forwarded-For") != null) {
				ip_address = hsr.getHeader("X-Forwarded-For");
			}
		}
    	
	}
	
	// get the device
	private String getDeviceFromUA(String ua) {
		if (ua == null) return "none";
		ua = ua.toLowerCase();
		//System.out.println("useragent2: " + ua);
		boolean isIOS = false;
		if (ua.contains("iphone")) {
			isIOS = true;
			return "iphone";
		}
		if (ua.contains("ipad")) {
			isIOS = true;
			return "ipad";
		}
		if (ua.contains("android")) {
			return "android";
		}
		if (ua.contains(" msie ") && ua.contains(" windows NT ")) {
			return "web_IE";
		}
		if (ua.contains("gecko") && ua.contains("chrome/")) {
			return "chrome";
		}	
		if (ua.contains("applewebkit/") && ua.contains("safari/")) {
			return "safari";
		}	
		if (ua.contains("gecko/") && ua.contains("firefox/")) {
			return "firefox";
		}

		return "web";
	}
	// get a cookie
	public String getCookie(HttpServletRequest hsr, String name) {
	    Cookie[] cookies = hsr.getCookies();
	    if (cookies != null) {
	      for (int i = 0; i < cookies.length; i++) {
	        if (cookies[i].getName().equals(name)) {
	          return cookies[i].getValue();
	        }
	      }
	    }
	    return null;
	}
	
	// get time since create
	public int timeMSSoFar() {
		return (int)(System.currentTimeMillis() - this.startTime);
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}

	public RestResp error(String param, int code) {
		this.addErrorParam(param, code);
		return this;
	}	
	public Response ret() {
		if (time == 0) done();
		return Response.status(200).entity(this.done()).build(); 
	}
	public Response ret(int code) {
		if (time == 0) done();
		return Response.status(200).entity(this.done(code)).build(); 
	}	
	public Response ret(String param, int code) {
		this.addErrorParam(param, code);
		if (time == 0) done();
		return Response.status(200).entity(this.done(code)).build(); 
	}	
	public Response retNoAuth() {
		if (time == 0) done();
		return Response.status(200).entity(this.done(520)).build(); 
	}
	// Called after API completion to get milliseconds updated for the call time and anything else needed
	public RestResp done(int code) {
		this.setCode(code);
		resultDone();
		return this;
	}
	public RestResp done() {
		resultDone();
		return this;
	}
	public void resultDone() {
		// if there was an error.. clear the results
		if (this.code >= 300 && this.code != 502) {
			this.results = null;
		}
	
		// set the time we took to process
		this.time = (int)(System.currentTimeMillis() - this.startTime);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}



	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
	/*
	 * ErrorParams
	 */
	public void addErrorParam(String name, int value) {
		if (this.resp_params == null) {
			this.resp_params = new HashMap<String, Integer>();
		}
		this.resp_params.put(name, value);		
		// if this param created an error, set the response to error
		if (value != 200) {
			this.code = value;
		}
	}
		
	public void addErrorPrams(HashMap<String, Integer> params) {
		if (params != null) {
			if (this.resp_params == null) {
				this.resp_params = params;
			} else {
				this.resp_params.putAll(params);
			}
		}
	}	
	public HashMap<String, Integer> getErrorPrams() {
		return resp_params;
	}


	/*
	 * ResponseData
	 */
	@SuppressWarnings("unchecked")
	public void setInfo(HashMap<String, String> data) {
		if (data != null) {
			this.info = (HashMap<String,String>)data.clone();
		} else {
			this.info = null;
		}
	}
	// Add parameter and values one by one into the data
	public void addInfo(String name, String value) {
		if (this.info == null) {
			this.info = new HashMap<String, String>();
		}
		this.info.put(name, value);
	}
	public HashMap<String, String> getInfo() {
		return info;
	}

	
	// Response options
	public List<HashMap<String, String>> getResults() {
		return results;
	}
	public void setResults(List<HashMap<String, String>> options) {
		this.results = options;
	}
	// adds a copy
	@SuppressWarnings("unchecked")
	public void addResult(HashMap<String, String> option) {
		if (option != null) {
			if (this.results == null) {
				this.results = new ArrayList<HashMap<String, String>>();
			}	
			if (option != null) {
				this.results.add((HashMap<String,String>)option.clone());
			}			
		}
	}
	
	// Doc Response options
	public List<HashMap<String, Object>> getDoc() {
		return doc;
	}
	public void setDoc(List<HashMap<String, Object>> options) {
		this.doc = options;
	}
	// adds actual object, so no mod after or there will be issues
	public void addDoc(HashMap<String, Object> option) {
		if (option != null) {
			if (this.doc == null) {
				this.doc = new ArrayList<HashMap<String, Object>>();
			}	
			if (option != null) {
				this.doc.add(option);
			}			
		}
	}

	public String getCall_id() {
		return call_id;
	}
	
}

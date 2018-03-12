package com.extract.web;


import java.util.ArrayList;
import java.util.Calendar;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


/*
 * Base class for all object Services
 * This implements the base functionality for SET/GET/DEL/CREATE and objects that inherit must 
 * 1. implement the abstracts (copy from a sample object)
 * 2. implement the JAX-RS interfaces with annotations (copy from a sample object)
 * 3. implement any special APIs or logic needed 
 */
public abstract class RestBase {
	
	public static boolean paramHave(String param) {
		if (param == null || param.isEmpty() || param.equals("null")) {
			return false;
		}
		return true;
	}
	/*
	public static boolean paramGUID(String param) {
		if (ValidUtil.isGUID(param)) {
			return true;
		}	
		return false;
	}*/
	public static boolean paramTrue(String param) {
		if (param != null && param.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}	
	public static boolean paramFalse(String param) {
		if (param != null && param.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	public static boolean paramIsValue(String param, String value) {
		if (param == null && value == null) return true;
		if (value == null) return false;
		if (param != null && param.equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}
	public static boolean paramIs(String param, String value) {
		if (param == null && value == null) return true;
		if (value == null) return false;
		if (param != null && param.equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}
	public static int toInt(String param) {
		if (param != null) {
			return Gtil.toInt(param);
		}
		return 0;
	}
	public static double toDouble(String param) {
		if (param != null) {
			try{
				return Double.parseDouble(param);
			} catch (Throwable t){}
		}
		return 0;
	}	
	
	public static int toMoney_int(String param) {
		if (param != null) {
			return Gtil.toMoney_int(param);
		}
		return 0;
	}
	public static Calendar toDate(String param) {
		if (param != null) {
			return Gtil.loadDate(param);
		}
		return null;
	}
	public static Calendar toTime(String param) {
		if (param != null) {
			return Gtil.loadTimeStamp(param);
		}
		return null;
	}

	public static String get10NumberGUID(String guid) {
		String str = guid.replaceAll("[^\\d.]", "");
		if (str.length() > 10) {
			str = str.substring(0, 10);
		} else if (str.length() < 10) {
			str = new String(new char[10-str.length()]).replace("\0", "3") + str;
		}
		return str;
	}
	
	public static boolean checkAuth(RestResp rr, String type) {
		// FIXME
		return true;
	}
	
	protected int getJInt(JSONObject obj, String name) {
		try {
		return Gtil.toInt(obj.getString(name));
		} catch (Throwable t) {}	
		return -1;
	}
	protected double getJDouble(JSONObject obj, String name) {
		try {
		return Gtil.toDouble(obj.getString(name));
		} catch (Throwable t) {}	
		return -1;
	}
	protected String getJStr(JSONObject obj, String name) {
		try {
		return obj.getString(name);
		} catch (Throwable t) {}	
		return null;
	}
	protected ArrayList<String> getJStrList(JSONObject obj, String name) {
		ArrayList<String> syl = null;
		try {
			JSONArray li = obj.getJSONArray(name);
			for (int i=0;i<li.length();i++) {
				if (syl == null) syl = new ArrayList<String>();
				syl.add(li.getString(i));
			}
			} catch (Throwable t) {}	
		return syl;
	}	
	protected String getJStrList(JSONObject obj, String name, int i) {
		try {
			JSONArray li = obj.getJSONArray(name);
			return li.getString(i);
			} catch (Throwable t) {}	
		return null;
	}

}


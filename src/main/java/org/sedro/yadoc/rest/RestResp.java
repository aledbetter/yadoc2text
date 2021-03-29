 /*************************************************************************
 * Ledbetter CONFIDENTIAL
 * __________________
 * 
 * [2018] - [2021] Aaron Ledbetter
 * All Rights Reserved.
 * 
 * NOTICE: All information contained herein is, and remains
 * the property of Aaron Ledbetter. The intellectual and technical 
 * concepts contained herein are proprietary to Aaron Ledbetter and 
 * may be covered by U.S. and Foreign Patents, patents in process, 
 * and are protected by trade secret or copyright law. 
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Aaron Ledbetter.
 */

package org.sedro.yadoc.rest;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "code", "data"})
public class RestResp {	
	private int code;
	private String data;
	
	public RestResp(UriInfo info, HttpServletRequest hsr, String data) {		
    	this.setCode(200); // start with no problem always  	
    	this.setData(data);
	}

	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return this.data;
	}

	public Response ret() {
		return Response.status(200).entity(this).build(); 
	}
}

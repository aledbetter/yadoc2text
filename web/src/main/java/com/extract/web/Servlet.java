package com.extract.web;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Initialize everything we need in the constructor
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")

	
	// Logger instance named "LoadUtil".
  //  static Logger logger = Logger.getLogger(LoadUtil.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	public Servlet() {
        super();
    	loadService(true);
    	
    //	logger.info("LoadUtil loading Serices Complete");
//    	Security.addProvider(new BouncyCastleProvider());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
	public static void loadService(boolean server) {
		/*
		 * GMT
		 */
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

    	System.out.println("Working Directory = " + System.getProperty("user.dir"));
    	

        System.out.println("LoadService initializing log4j Complete");

    	
    }

}

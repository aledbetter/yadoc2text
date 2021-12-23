package org.sedro.yadoc.rest;



import org.glassfish.jersey.media.multipart.FormDataParam;
import org.sedro.yadoc.parse.YaParser;
import org.sedro.yadoc.parse.YaParserFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;


@Path("/data/")
@Produces(MediaType.APPLICATION_JSON)
public class RestAPI {
	private static int cfCnt = 0;
	
	// CONVERT TO SIMPLE HTML
    @POST
    @Path("/convert")
    @Produces({"text/plain", "text/html", "application/octet-stream"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDocumentPost(@Context UriInfo info,
                                       @Context HttpServletRequest hsr,
                                       @FormDataParam("language") String language,
                                       @FormDataParam("lang_auto") String slang_auto,
                                       @FormDataParam("template") String template,
                                       
                                       @FormDataParam("qqfile") InputStream qquploadedInputStream,
                                       @FormDataParam("qqfile") FormDataContentDisposition qqfileDetail,
                                       @FormDataParam("file") InputStream uploadedInputStream,
                                       @FormDataParam("file") FormDataContentDisposition fileDetail,
                                       @FormDataParam("atok") String access_key,
                                       @CookieParam("atok") String cookie_access_key) {
        if (qquploadedInputStream != null) {
            uploadedInputStream = qquploadedInputStream;
            fileDetail = qqfileDetail;
        }
        if (uploadedInputStream == null) {
            return Response.status(404).build();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        YaParser converter = YaParserFactory.getConverterByFileName(fileDetail.getFileName());
        if (converter == null) {
            System.out.println("ERROR: not converter CONVERT FILE: " + fileDetail.getFileName());
           return Response.status(500).build();
        }
		/* TODO set headers for cross-site scripting	 	
		 * "Access-Control-Allow-Origin", "https://www.testing.com http://localhost:8080"
		 * "Access-Control-Allow-Methods", "POST, OPTIONS"
		 */
        try {
            converter.convertDataHtml(uploadedInputStream, data);
        } catch (Exception e) {
            System.err.println("CONVERT FILE ERROR: " + e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        
        cfCnt++;
		ResponseBuilder response = null;
		try {
			String str = data.toString("UTF-8");		
			StringReader sr = new StringReader(str);
	        response = Response.ok(sr);
	        System.out.println("CONVERT FILE UTF-8["+cfCnt+"] " + fileDetail.getFileName());
		} catch (UnsupportedEncodingException e) {
	        InputStream result = new ByteArrayInputStream(data.toByteArray());
	        response = Response.ok(result);
	        System.out.println("CONVERT FILE byte["+cfCnt+"] " + fileDetail.getFileName());
		}
        return response.type("application/octet-stream").build();
    }

    
    // Extract TEXT
    @POST
    @Path("/extract")
    @Produces({"text/plain", "text/html", "application/octet-stream"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDocumentExtractPost(@Context UriInfo info,
                                       @Context HttpServletRequest hsr,
                                       @FormDataParam("language") String language,
                                       @FormDataParam("lang_auto") String slang_auto,
                                       @FormDataParam("template") String template,
                                       
                                       @FormDataParam("qqfile") InputStream qquploadedInputStream,
                                       @FormDataParam("qqfile") FormDataContentDisposition qqfileDetail,
                                       @FormDataParam("file") InputStream uploadedInputStream,
                                       @FormDataParam("file") FormDataContentDisposition fileDetail,
                                       @FormDataParam("atok") String access_key,
                                       @CookieParam("atok") String cookie_access_key) {
        if (qquploadedInputStream != null) {
            uploadedInputStream = qquploadedInputStream;
            fileDetail = qqfileDetail;
        }
        if (uploadedInputStream == null) {
            return Response.status(404).build();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        YaParser converter = YaParserFactory.getConverterByFileName(fileDetail.getFileName());
        if (converter == null) {
            System.out.println("ERROR: not converter CONVERT FILE: " + fileDetail.getFileName());
           return Response.status(500).build();
        }
		/* TODO set headers for cross-site scripting	 	
		 * "Access-Control-Allow-Origin", "https://www.testing.com http://localhost:8080"
		 * "Access-Control-Allow-Methods", "POST, OPTIONS"
		 */
        try {
            converter.convertDataText(uploadedInputStream, data);
        } catch (Exception e) {
            System.err.println("CONVERT FILE txt ERROR: " + e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        
        cfCnt++;
		ResponseBuilder response = null;
		try {
			String str = data.toString("UTF-8");		
			StringReader sr = new StringReader(str);
	        response = Response.ok(sr);	    
	        System.out.println("EXTRACT FILE UTF-8["+cfCnt+"] " + fileDetail.getFileName());
		} catch (UnsupportedEncodingException e) {
	        InputStream result = new ByteArrayInputStream(data.toByteArray());
	        response = Response.ok(result);
	        System.out.println("EXTRACT FILE byte["+cfCnt+"] " + fileDetail.getFileName());
		}
        return response.type("application/octet-stream").build();
    }
    
	// CONVERT TO SIMPLE HTML
    @POST
    @Path("/json/convert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDocumentPostJSON(@Context UriInfo info,
                                       @Context HttpServletRequest hsr,
                                       @FormDataParam("language") String language,
                                       @FormDataParam("lang_auto") String slang_auto,
                                       @FormDataParam("template") String template,
                                       
                                       @FormDataParam("qqfile") InputStream qquploadedInputStream,
                                       @FormDataParam("qqfile") FormDataContentDisposition qqfileDetail,
                                       @FormDataParam("file") InputStream uploadedInputStream,
                                       @FormDataParam("file") FormDataContentDisposition fileDetail,
                                       @FormDataParam("atok") String access_key,
                                       @CookieParam("atok") String cookie_access_key) {
        if (qquploadedInputStream != null) {
            uploadedInputStream = qquploadedInputStream;
            fileDetail = qqfileDetail;
        }
        if (uploadedInputStream == null) {
            return Response.status(404).build();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        YaParser converter = YaParserFactory.getConverterByFileName(fileDetail.getFileName());
        if (converter == null) {
            System.out.println("ERROR: not converter CONVERT FILE: " + fileDetail.getFileName());
           return Response.status(500).build();
        }
		/* TODO set headers for cross-site scripting	 	
		 * "Access-Control-Allow-Origin", "https://www.testing.com http://localhost:8080"
		 * "Access-Control-Allow-Methods", "POST, OPTIONS"
		 */
        try {
            converter.convertDataHtml(uploadedInputStream, data);
        } catch (Exception e) {
            System.err.println("CONVERT FILE ERROR: " + e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        
        cfCnt++;
		String str = null;		
		try {
			str = data.toString("UTF-8");		
	        System.out.println("JCONVERT FILE UTF-8["+cfCnt+"] " + fileDetail.getFileName());
		} catch (UnsupportedEncodingException e) {
			str = data.toString();
	        System.out.println("JCONVERT FILE byte["+cfCnt+"] " + fileDetail.getFileName());
		}
		
		// make JSON
		RestResp rp = new RestResp(info, hsr, str);
		return rp.ret();
    }

    
    // Extract TEXT
    @POST
    @Path("/json/extract")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDocumentExtractPostJSON(@Context UriInfo info,
                                       @Context HttpServletRequest hsr,
                                       @FormDataParam("language") String language,
                                       @FormDataParam("lang_auto") String slang_auto,
                                       @FormDataParam("template") String template,
                                       
                                       @FormDataParam("qqfile") InputStream qquploadedInputStream,
                                       @FormDataParam("qqfile") FormDataContentDisposition qqfileDetail,
                                       @FormDataParam("file") InputStream uploadedInputStream,
                                       @FormDataParam("file") FormDataContentDisposition fileDetail,
                                       @FormDataParam("atok") String access_key,
                                       @CookieParam("atok") String cookie_access_key) {
        if (qquploadedInputStream != null) {
            uploadedInputStream = qquploadedInputStream;
            fileDetail = qqfileDetail;
        }
        if (uploadedInputStream == null) {
            return Response.status(404).build();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        YaParser converter = YaParserFactory.getConverterByFileName(fileDetail.getFileName());
        if (converter == null) {
            System.out.println("ERROR: not converter CONVERT FILE: " + fileDetail.getFileName());
           return Response.status(500).build();
        }
		/* TODO set headers for cross-site scripting	 	
		 * "Access-Control-Allow-Origin", "https://www.testing.com http://localhost:8080"
		 * "Access-Control-Allow-Methods", "POST, OPTIONS"
		 */
        try {
            converter.convertDataText(uploadedInputStream, data);
        } catch (Exception e) {
            System.err.println("CONVERT FILE txt ERROR: " + e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        cfCnt++;
		String str = null;		
		try {
			str = data.toString("UTF-8");		
	        System.out.println("JEXTRACT FILE UTF-8["+cfCnt+"] " + fileDetail.getFileName());
		} catch (UnsupportedEncodingException e) {
			str = data.toString();
	        System.out.println("JEXTRACT FILE byte["+cfCnt+"] " + fileDetail.getFileName());
		}
		
		// make JSON
		RestResp rp = new RestResp(info, hsr, str);
		return rp.ret();
    }


}

package main.java.com.extract.web;



import org.glassfish.jersey.media.multipart.FormDataParam;

import main.java.com.extract.processor.parse.ConverterHtml;

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


@Path("/data/")
@Produces(MediaType.APPLICATION_JSON)
public class RestAPI {

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
        System.out.println("CONVERT FILE: " + fileDetail.getFileName());

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ConverterHtml converter = ConverterFactory.getConverterByFileName(fileDetail.getFileName());
        if (converter == null) {
            return Response.status(500).build();
        }
		/* TODO set headers for cross-site scripting	 	
		 * "Access-Control-Allow-Origin", "https://www.testing.com http://localhost:8080"
		 * "Access-Control-Allow-Methods", "POST, OPTIONS"
		 */
        try {
            converter.convert(uploadedInputStream, data);
        } catch (Exception e) {
            System.err.println("CONVERT FILE ERROR:");
            e.printStackTrace();
            return Response.status(500).build();
        }

        InputStream result = new ByteArrayInputStream(data.toByteArray());

        ResponseBuilder response = Response.ok(result);
        return response.type("application/octet-stream").build();
    }
    /*

    @POST
    @Path("/tokens")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response tokensUploadPOST(@Context UriInfo urinfo,
                   @Context HttpServletRequest hsr,
                   @FormDataParam("language") String language,
                   @FormDataParam("lang_auto") String slang_auto,
                   @FormDataParam("template") String template,
                   @FormDataParam("pov") String spov,
                   @FormDataParam("lock") String lock,
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
        
		//System.out.println("CONVERT_REST: " + fileDetail.getFileName());
		RestResp rr = new RestResp(urinfo, hsr, null, cookie_access_key, cookie_access_key);
		if (!checkAuth(rr, "user")) return rr.retNoAuth();
		
        String words = RestUtils.readContentConvert(uploadedInputStream, fileDetail.getFileName());
        if (words == null || words.isEmpty() || words.length() < 3) {
			return Response.status(500).build();
		}	
		if (!paramHave(ctx)) ctx = ProcTenant.DEFAULT_TENANT;
        if (paramHave(persona)) persona = null;
        if (paramHave(fsave)) fsave = null;
        ArrayList<String> knowledge = null;
        if (paramHave(sknowlede)) {
        	String kn [] = sknowlede.split(",");
    		knowledge = new ArrayList<>();
    		for (String s:kn) knowledge.add(s);	    
    		if (knowledge.size() < 1) knowledge = null;	       	
        }		
		
		if (RestUtils.haveXSubscription(hsr)) lock = "better";
		if (!paramHave(lock) || !lock.equals("better")) {
			// limit space
			if (words.length() > MAX_LOCK_BYTES) {
				return rr.ret(413); 
			}
		} 

		return resultsProcess(rr, "min", ctx, words, null, depot, docid, template, language, lang_auto, persona, knowledge, fsave, review, 
				 no_pos, no_classifier, no_possessive, no_type, true, no_dir,
				 no_rtype, no_gender, no_quant, no_scope, no_reftype, no_usive, no_doc,
				 true, false, pov, false, false, false);
    }
     */

}

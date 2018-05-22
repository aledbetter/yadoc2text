
package com.extract;

import com.extract.web.RestAPI;

import java.util.logging.Level;
import java.util.logging.Logger;
/*
import java.io.File;
import java.lang.management.ManagementFactory;

//import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AllowSymLinkAliasChecker;
import org.eclipse.jetty.webapp.WebAppContext;
*/

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

//java -jar restprj-1.0-SNAPSHOT.jar
public class StandAlone
{
    public static void main( String[] args ) throws Exception {
        Server server = new Server(8099);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
System.out.println("STARTED WE HAVE");
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
        //        "jersey.config.server.provider.packages",
        //        "com.extract.web"
                "jersey.config.server.provider.classnames",
                RestAPI.class.getCanonicalName()
        );
   
/*
        // Setup JMX
        MBeanContainer mbContainer = new MBeanContainer(
                ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        // The WebAppContext is the entity that controls the environment in
        // which a web application lives and breathes. In this example the
        // context path is being set to "/" so it is suitable for serving root
        // context requests and then we see it setting the location of the war.
        // A whole host of other configurations are available, ranging from
        // configuring to support annotation scanning in the webapp (through
        // PlusConfiguration) to choosing where the webapp will unpack itself.
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        File warFile = new File(
                "../../target/web-convert-0.9.1.war");
        webapp.setWar(warFile.getAbsolutePath());
        webapp.addAliasCheck(new AllowSymLinkAliasChecker());

        // A WebAppContext is a ContextHandler as well so it needs to be set to
        // the server so it is aware of where to send the appropriate requests.
        server.setHandler(webapp);
*/
        try {
            server.start();
            server.dumpStdErr();
            server.join();
        } catch (Exception ex) {
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            server.destroy();
        }
    }
}
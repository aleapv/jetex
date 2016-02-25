package org.eclipse.jetty.server;
 
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
 
public class JettyServer extends AbstractHandler {

    public final static String KEY_STORE_PATH = "C:\\Alex\\jetex\\ssl\\keystore";
    public final static String KEY_STORE_PASSWORD = "OBF:18jj18jj18jj18jj18jj18jj";

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>SSL in Jetty</h1>");
        baseRequest.setHandled(true);
    }

    public static void main( String[] args ) throws Exception {

        File keystoreFile = new File(KEY_STORE_PATH);
        if (!keystoreFile.exists())
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());
 
        Server server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(8443);
        httpConfig.setOutputBufferSize(32768);
 
        ServerConnector http =
            new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setPort(8080);
        http.setIdleTimeout(30000);
 
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(KEY_STORE_PATH);
        sslContextFactory.setKeyStorePassword(KEY_STORE_PASSWORD);
        sslContextFactory.setKeyManagerPassword(KEY_STORE_PASSWORD);
 
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setStsMaxAge(2000);
        src.setStsIncludeSubDomains(true);
        httpsConfig.addCustomizer(src);
 
        ServerConnector https = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
        https.setPort(8443);
        https.setIdleTimeout(500000);
 
        server.setConnectors(new Connector[] { http, https });
        server.setHandler(new JettyServer());
        server.start();
        server.join();
    }
}

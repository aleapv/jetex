package server;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.ProtectionDomain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStart {

    public final static String KEY_STORE_PATH = "C:\\Alex\\portal_sequrity3\\etc\\keystore";
    public final static String KEY_STORE_PASSWORD = "OBF:194s194u194w194y";
    public final static String KEY_STORE_TYPE = "HDImageStore";
    public final static String KEY_STORE_PROVIDER = "JCP";
    public final static String TRUST_STORE_TYPE = "CertStore";
    public final static boolean NEED_CLIENT_AUTH = true;
    public final static boolean WANT_CLIENT_AUTH = false;
    public final static String PROTOCOL = "GostTLS";
    public final static String PROVIDER = "JTLS";
    public final static String[] INCLUDE_PROTOCOLS = {"TLSV"};
    public final static String[] INCLUDE_CIPHER_SUITES = {"TLS_CIPHER_2001", "TLS_CIPHER_2012"};
    public final static boolean USE_CIPHER_SUITES_ORDER = true;

    public static void main( String[] args ) throws Exception {

        File keystoreFile = new File(KEY_STORE_PATH);
        if (!keystoreFile.exists())
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());

        // start.ini settings

        // debug logger settings
        // Log.setLog(new StdErrLog());

        Server server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(8443);
        httpConfig.setOutputBufferSize(32768);

        ServerConnector http =
            new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setPort(8080);
        http.setIdleTimeout(30000);

        // connector and gost tls settings
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(KEY_STORE_PATH);
        sslContextFactory.setKeyStorePassword(KEY_STORE_PASSWORD);
        sslContextFactory.setKeyStoreType(KEY_STORE_TYPE);
        sslContextFactory.setKeyStoreProvider(KEY_STORE_PROVIDER);
        sslContextFactory.setKeyManagerPassword(KEY_STORE_PASSWORD);
        sslContextFactory.setTrustStorePath(KEY_STORE_PATH);
        sslContextFactory.setTrustStorePassword(KEY_STORE_PASSWORD);
        sslContextFactory.setTrustStoreType(TRUST_STORE_TYPE);
        sslContextFactory.setTrustStoreProvider(KEY_STORE_PROVIDER);
        sslContextFactory.setNeedClientAuth(NEED_CLIENT_AUTH);
        sslContextFactory.setWantClientAuth(WANT_CLIENT_AUTH);
        sslContextFactory.setProtocol(PROTOCOL);
        sslContextFactory.setProvider(PROVIDER);
        sslContextFactory.setIncludeProtocols(INCLUDE_PROTOCOLS);
        sslContextFactory.setIncludeCipherSuites(INCLUDE_CIPHER_SUITES);
        sslContextFactory.setUseCipherSuitesOrder(USE_CIPHER_SUITES_ORDER);


        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setStsMaxAge(2000);
        src.setStsIncludeSubDomains(true);
        httpsConfig.addCustomizer(src);

        ServerConnector https = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
        https.setPort(8443);
        https.setIdleTimeout(30000);


        server.setConnectors(new Connector[] { http, https });


        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        ProtectionDomain domain = JettyStart.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        webapp.setServer(server);
        webapp.setWar(location.toExternalForm());
        server.setHandler(webapp);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(Test.class, "/*");

        server.start();
        server.join();
    }
}

package com.embedded;

import java.net.URL;
import java.security.ProtectionDomain;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStarter {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		WebAppContext webapp = new WebAppContext();
		webapp.setDescriptor("WEB-INF/web.xml");
		ProtectionDomain domain = JettyStarter.class.getProtectionDomain();
		URL location = domain.getCodeSource().getLocation();
		webapp.setWar(location.toExternalForm());
		webapp.setContextPath("/promat");

		server.setHandler(webapp);

		server.start();
		server.join();
	}

}

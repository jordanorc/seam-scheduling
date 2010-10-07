/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.security.ProtectionDomain;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class Start {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        connector.setServer(server);
        WebAppContext context = new WebAppContext();

        context.setResourceBase("/home/celestrini/Desktop/seam/seam-scheduling/examples/demo/target/seam-scheduling-demo");
        context.setContextPath("/");
        context.setServer(server);
        server.setHandler(context);
        server.setConnectors(new Connector[]{
                    connector
                });
        server.start();


//        Server server = new Server();
//        SocketConnector connector = new SocketConnector();
//
//        // Set some timeout options to make debugging easier.
//        connector.setMaxIdleTime(1000 * 60 * 60);
//        connector.setSoLingerTime(-1);
//        connector.setPort(8080);
//        server.setConnectors(new Connector[]{connector});
//
//        ContextHandler context = new ContextHandler();
//        context.setServer(server);
//        context.setContextPath("/");
//        context.setResourceBase(".");
//        context.setClassLoader(Thread.currentThread().getContextClassLoader());
//
//        ProtectionDomain protectionDomain = Start.class.getProtectionDomain();
//        URL location = protectionDomain.getCodeSource().getLocation();
//
//
//        server.setHandler(context);
//        try {
//            server.start();
//            System.in.read();
//            server.stop();
//            server.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(100);
//        }
    }
}

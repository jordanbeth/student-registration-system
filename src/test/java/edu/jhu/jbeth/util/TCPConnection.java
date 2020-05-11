package edu.jhu.jbeth.util;

import java.sql.SQLException;

import org.h2.tools.Server;

public class TCPConnection {

    private static Server SERVER;

    private TCPConnection() {

    }

    public static Server start() {
	if (SERVER == null) {
	    try {
		SERVER = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return SERVER;
    }

    public static void stop() {
	if (SERVER != null) {
	    SERVER.stop();
	    SERVER = null;
	}
    }
}

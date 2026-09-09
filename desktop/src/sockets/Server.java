package sockets;

import java.net.*;
import java.io.*;

public class Server {
    public Server() throws IOException {
        Socket server = new ServerSocket(3000).accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);

        String message = in.readLine();
        System.out.println(message);
        server.close();
    }
}

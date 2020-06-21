package com.tearsvalley;

import com.tearsvalley.server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server started");

        Server server = new Server();

        while (!server.waitForThePlayer())
            ;
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
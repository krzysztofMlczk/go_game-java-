package com.tearsvalley;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tearsvalley.client.Client;
import com.tearsvalley.data.Color;
import com.tearsvalley.serializable.InitialMessage;


public class ClientTest {

	ServerSocket serverSocket = null;
	Socket serverClient = null;
	Client client = null;
	Server server;
	
	
	@Before
	public void setUp()
	{
		
	}
	
	@Test
	public void connectionTest() {
		Thread t = new Thread(() -> {
			server = new Server();
			
			server.sendInitialMessage();
			
		});
		
		t.start();
		
		client = new Client();
		
		assertTrue(client.connected);
	}
	
	@After
	public void cleanUp()
	{
		server.close();
		client.close();
	}
	
	class Server {
		
		private ObjectInputStream inputStream = null;
	    private ObjectOutputStream outputStream = null;
		
		public Server() {
	        try {
	            serverSocket = new ServerSocket(5000);
	            serverClient = serverSocket.accept();
	            
	            outputStream = new ObjectOutputStream(serverClient.getOutputStream());
	            inputStream = new ObjectInputStream(serverClient.getInputStream());

	            } catch(IOException ex) {
	                System.out.println(ex);
	            }
	    }
		
		public void sendInitialMessage() {
			try {
				outputStream.writeObject(new InitialMessage(Color.WHITE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void close() {
			try {
				outputStream.close();
				inputStream.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

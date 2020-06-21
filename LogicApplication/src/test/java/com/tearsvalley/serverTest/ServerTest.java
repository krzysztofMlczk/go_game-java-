package com.tearsvalley.serverTest;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tearsvalley.server.Server;

public class ServerTest {
	private Server server = null;
	private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
	
	@Before
	public void setUp()
	{
		server = new Server();
	}
	
	@Test
	public void waitForThePlayerTest()
	{
		Thread t = new Thread(() -> {
			assertTrue(server.waitForThePlayer());
		});
		
		t.start();
		
		try {
				Socket client = new Socket("127.0.0.1", 5000);
	
				outputStream = new ObjectOutputStream(client.getOutputStream());
		        inputStream = new ObjectInputStream(client.getInputStream());
				
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			}
	}
	
	@After
	public void cleanUp()
	{
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

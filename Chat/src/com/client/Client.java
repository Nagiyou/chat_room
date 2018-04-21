package com.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket socket;
	
	OutputStream os;
	PrintWriter pw;
	
	InputStream is;
	BufferedReader br;
	
	ObjectOutputStream oos;
	BufferedInputStream bis;
	
	public Client(){
		try {
			socket = new Socket("127.0.0.1",8000);
			System.out.println("连接服务器成功");
			
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			pw = new PrintWriter(os);
			
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			bis = new BufferedInputStream(is);
			
			
		} catch (UnknownHostException e) {
			System.out.println("at UnknownHostException 您未能连接上主机");
		} catch (IOException e) {
			System.out.println("at IOException 您未能连接上主机");
		}
	}
	
}

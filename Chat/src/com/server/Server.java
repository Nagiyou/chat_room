package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Properties;

import com.information.*;

public class Server {
	private static final int Port = 8000;
	ServerSocket Server = null;
	
	InputStream uis = null;
	OutputStream uos = null;
	
	//在磁盘建立文件，存储用户注册的账号
	public Properties userInformation;
	
	Hashtable<String,Socket> clientConnection = new Hashtable();
	
	public Server(){
		try{
			userInformation = new Properties();

			uis = new FileInputStream("f:/userInfo.properties");
			uos = new FileOutputStream("f:/userInfo.properties", true);
			userInformation.load(uis);
			
			Server =new ServerSocket(Port);
			System.out.println("服务器启动成功");
			
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("服务器启动失败");
		}
	}

	//内部类，登陆并存储在线用户id和socket
	class HandleLogin implements Runnable{
		public void run(){
			Socket client = null;
			try{
				while(true){
					
					//接收socket
					client = Server.accept();
					
					//用于接收传递过来用户对象
					ObjectInputStream ois = null;
					
					//存储用户id和socket
					ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
					Object obj = ois.readObject();
					
					Account account = (Account)obj;
					
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					
					//判断密码是否正确
					if(!userInformation.getProperty(account.getId()).equals(account.getPW())){
						oos.writeObject(false);
						oos.flush();
					}else{
						oos.writeObject(true);
						oos.flush();
						
						clientConnection.put(account.getId(), client);
					
						//新建线程，用于处理当前用户对话请求
						HandleClient hc = new HandleClient(account.getId(),client);
						new Thread(hc).start();
					}
				}
			}catch(Exception e){
				System.out.println("客户端失去连接");
			}
		}
	}
	
	//处理对话
	class HandleClient implements Runnable{
		//发送消息用户
		Socket sendClient;
		
		//接收消息用户
		Socket receiveClient;
		
		String id;
		
		public HandleClient(String id,Socket client){
			this.sendClient = client;
			this.id = id;
		}
		public void run(){
			try{
				//处理用户请求匹配，将在TalkFrame中选择的对话对象id传递给server
					
				InputStream is =sendClient.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				while(true){
					String s = br.readLine();
					String accountId =s.substring(0,s.indexOf(":"));
					
					String message = s.substring(s.indexOf(":")+1);
					
					receiveClient = clientConnection.get(accountId);
					PrintWriter pw = new PrintWriter(receiveClient.getOutputStream());
				
					pw.println(message);
					pw.flush();
				}
			}catch(Exception e){
				System.out.println("与客户端失去连接!");
			}
		}
	}
	public static void main(String[] args){
		
		Server s = new Server();
		
		HandleLogin hl = s.new HandleLogin();
		new Thread(hl).start();
	}
}

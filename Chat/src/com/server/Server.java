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
	
	//�ڴ��̽����ļ����洢�û�ע����˺�
	public Properties userInformation;
	
	Hashtable<String,Socket> clientConnection = new Hashtable();
	
	public Server(){
		try{
			userInformation = new Properties();

			uis = new FileInputStream("f:/userInfo.properties");
			uos = new FileOutputStream("f:/userInfo.properties", true);
			userInformation.load(uis);
			
			Server =new ServerSocket(Port);
			System.out.println("�����������ɹ�");
			
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("����������ʧ��");
		}
	}

	//�ڲ��࣬��½���洢�����û�id��socket
	class HandleLogin implements Runnable{
		public void run(){
			Socket client = null;
			try{
				while(true){
					
					//����socket
					client = Server.accept();
					
					//���ڽ��մ��ݹ����û�����
					ObjectInputStream ois = null;
					
					//�洢�û�id��socket
					ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
					Object obj = ois.readObject();
					
					Account account = (Account)obj;
					
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					
					//�ж������Ƿ���ȷ
					if(!userInformation.getProperty(account.getId()).equals(account.getPW())){
						oos.writeObject(false);
						oos.flush();
					}else{
						oos.writeObject(true);
						oos.flush();
						
						clientConnection.put(account.getId(), client);
					
						//�½��̣߳����ڴ���ǰ�û��Ի�����
						HandleClient hc = new HandleClient(account.getId(),client);
						new Thread(hc).start();
					}
				}
			}catch(Exception e){
				System.out.println("�ͻ���ʧȥ����");
			}
		}
	}
	
	//����Ի�
	class HandleClient implements Runnable{
		//������Ϣ�û�
		Socket sendClient;
		
		//������Ϣ�û�
		Socket receiveClient;
		
		String id;
		
		public HandleClient(String id,Socket client){
			this.sendClient = client;
			this.id = id;
		}
		public void run(){
			try{
				//�����û�����ƥ�䣬����TalkFrame��ѡ��ĶԻ�����id���ݸ�server
					
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
				System.out.println("��ͻ���ʧȥ����!");
			}
		}
	}
	public static void main(String[] args){
		
		Server s = new Server();
		
		HandleLogin hl = s.new HandleLogin();
		new Thread(hl).start();
	}
}

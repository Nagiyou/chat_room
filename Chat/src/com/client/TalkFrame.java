package com.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class TalkFrame extends JFrame {

	private JPanel contentPane;
	private JButton sendButton;
	private JTextArea oldMessageTextArea;
	private JTextArea sendMessageTextArea;
	private Client client;
	private String activeUserId;
	private String recieverId;

	
	public TalkFrame(String activeUserId,Client client) {
		this.activeUserId = activeUserId;
		this.client = client;
		
		setTitle(activeUserId);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 448);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		oldMessageTextArea = new JTextArea();
		oldMessageTextArea.setBounds(10, 40, 415, 268);
		contentPane.add(oldMessageTextArea);
		
		sendMessageTextArea = new JTextArea();
		sendMessageTextArea.setBounds(10, 320, 345, 80);
		contentPane.add(sendMessageTextArea);
		
		showOldMessageThread somt = new showOldMessageThread();
		new Thread(somt).start();
		
		sendButton = new JButton("发送");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String str = sendMessageTextArea.getText();
				if(!str.equals("")){
					String Message = activeUserId+":"+str+"\r\n";
					String sendMessage = recieverId+":"+Message;
					
					client.pw.print(sendMessage);
					client.pw.flush();

					oldMessageTextArea.append(Message);
					sendMessageTextArea.setText("");
				}
			}
		});
		sendButton.setBounds(360, 355, 64, 30);
		contentPane.add(sendButton);
		
		JLabel label = new JLabel("选择对话用户：");
		label.setBounds(22, 5, 84, 29);
		contentPane.add(label);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("");
		comboBox.addItem("cyj");
		comboBox.addItem("jk1403");
		comboBox.addItem("huiwu");
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if(ie.getStateChange()==ItemEvent.SELECTED){
					recieverId = (String)comboBox.getSelectedItem();
				}
			}
		});
		comboBox.setBounds(128, 9, 62, 21);
		contentPane.add(comboBox);
	}
	
	class showOldMessageThread implements Runnable{
		public void run(){
			String message;
			while(true){
				try {
					message = client.br.readLine()+"\r\n";
					oldMessageTextArea.append(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

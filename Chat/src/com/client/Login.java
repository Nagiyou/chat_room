package com.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.information.Account;

@SuppressWarnings("serial")//Ϊʲô�������û�о��棿
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField IDtextField;
	private JPasswordField passwordField;
	private Client client;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Login() {
		setTitle("��½");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel UserID = new JLabel("�˺�");
		UserID.setBounds(75, 62, 35, 21);
		contentPane.add(UserID);
		
		IDtextField = new JTextField();
		IDtextField.setBounds(114, 61, 110, 21);
		contentPane.add(IDtextField);
		IDtextField.setColumns(10);
		
		JLabel UserPW = new JLabel("����");
		UserPW.setBounds(75, 106, 35, 15);
		contentPane.add(UserPW);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(114, 103, 110, 21);
		passwordField.setEchoChar('*');
		contentPane.add(passwordField);
		
		JButton LoginButton = new JButton("��½");
		LoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String accountID = IDtextField.getText();
					String accountPW = new String(passwordField.getPassword());
					
					if(accountID.equals("")){
						JOptionPane.showMessageDialog(null,"�û�������Ϊ��");
					}else if(accountPW.equals("")){
						JOptionPane.showMessageDialog(null,"���벻��Ϊ��");
					}else{
						
						client = new Client();

						Account account = new Account(accountID,accountPW);
						
						
						//���ݴ������˺�
						client.oos.writeObject(account);
						client.oos.flush();
						ObjectInputStream ois = new ObjectInputStream(client.bis);
						Object obj = ois.readObject();
						boolean flag = (boolean) obj;
						
						//������ȷ�͵����û��б��
						if(!flag){
							JOptionPane.showMessageDialog(null, "�û��������벻ƥ��");
						}else{
							new TalkFrame(accountID,client).setVisible(true);
							Login.this.dispose();
						}
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		LoginButton.setBounds(130, 160, 65, 25);
		contentPane.add(LoginButton);
	}
}

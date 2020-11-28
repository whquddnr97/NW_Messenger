package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

public class ClientGUI_Main extends JFrame
{
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtPassword;
	private JTextField idField;
	private JPasswordField passwordField;
	
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private String id = "";
	private String password = "";
	
	public void run() throws IOException
	{
		try
		{
			socket = new Socket(serverAddress, serverPort); // �ҷ��� �����ּ�, port�� ���� ��û
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (in.hasNextLine())
            {
            	String line = in.nextLine();
            	
            	/*CONNECTED�� �����ϴ� ���ڿ��� ������
            	 * ������ ����Ǿ��ٴ� ���̾�α׸� ��� ��
            	 * CONNECTED�� Ŭ���̾�Ʈ�� IP�ּҸ� ���ļ� ���ڿ��� ����*/
            	if (line.startsWith("CONNECTED"))
            	{
            		JOptionPane.showMessageDialog(null, "Connected with server");
            		out.println("CONNECETED " + InetAddress.getLocalHost().getHostAddress());
            	}
            	
            	/*LOGINOK�� �����ϴ� ���ڿ��� ������
            	 * �α��� ���� + Ŭ���̾�Ʈ�� ID�� ���̾�α׷� ���
            	 * �����Ͽ��� �ô� ���̵� �Ǵ� ��й�ȣ�� �ٽ� Ȯ���϶�� ���̾�α׸� ���*/
            	if (line.startsWith("LOGINOK"))
            	{
            		JOptionPane.showMessageDialog(null, "Login Succeced. Welcome " + id);
            	}
            	else if (line.startsWith("LOGINFAIL"))
            	{
            		JOptionPane.showMessageDialog(null, "Login Failed. Check Id or Password ");
            	}
            	
            	/*FINDPWOK�� �����ϴ� ���ڿ��� ������
            	 * �������� �޾ƿ� ���ڿ��� �Ľ��Ͽ� 
            	 * ��й�ȣ ã�� �����̶�� ���ڿ��� ��й�ȣ�� ���̾�α׷� ���
            	 * ���нô� ���̵� �̸����� Ȯ���϶�� ���̾�α׸� ���*/
            	if (line.startsWith("FINDPWOK"))
            	{
            		String[] getPW = line.split(" ");
            		String myPW = getPW[1];
            		try
            		{
						myPW = SimpleCrypto.decrypt(myPW);
					}
            		catch (Exception e)
            		{
						e.printStackTrace();
					}
            		JOptionPane.showMessageDialog(null, "Find Password Succeced. Password: " + myPW);
            	}
            	else if (line.startsWith("FINDPWFAIL"))
            	{
            		JOptionPane.showMessageDialog(null, "Find Password Failed. Check Id or Email ");
            	}
            }
		}
		finally
		{
			setVisible(false);
			dispose();
		}
	}
	
	/*GUI*/
	public ClientGUI_Main(String serverAddress, int serverPort)
	{
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		
		setTitle("Messenger-Client-Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 383, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtId = new JTextField();
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setToolTipText("Enter the ID");
		txtId.setEditable(false);
		txtId.setText("ID:");
		txtId.setBounds(12, 33, 76, 21);
		contentPane.add(txtId);
		txtId.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setToolTipText("Enter the Password");
		txtPassword.setEditable(false);
		txtPassword.setText("Password:");
		txtPassword.setBounds(12, 64, 76, 21);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		idField = new JTextField();
		idField.setToolTipText("Enter the ID");
		idField.setBounds(103, 33, 183, 21);
		contentPane.add(idField);
		idField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Enter the Password");
		passwordField.setEchoChar('*');
		passwordField.setBounds(103, 64, 183, 21);
		contentPane.add(passwordField);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(285, 95, 70, 31);
		contentPane.add(loginButton);
		
		JButton findPasswordButton = new JButton("Forgot your password?");
		findPasswordButton.setBounds(12, 143, 170, 23);
		contentPane.add(findPasswordButton);
		
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(258, 143, 97, 23);
		contentPane.add(registerButton);
		
		/*�α��� ��ư �̺�Ʈ
		 * idField�� passwordField�� �Էµ� ���� �о�ͼ�
		 * LOGIN�̶�� ���ڿ��� �Բ� ������ ����*/
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				id = idField.getText();
				password = String.valueOf(passwordField.getPassword());
				try {
					password = SimpleCrypto.encrypt(password);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				out.println("LOGIN " + id + " " + password);
			}
		});
		
		/*��й�ȣ ã�� ��ư �̺�Ʈ
		 * ��й�ȣ ã�� GUI�� ��� - ClientGUI_FindPassword.java*/
		findPasswordButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClientGUI_FindPassword findPW = new ClientGUI_FindPassword(socket, in, out);
				findPW.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				findPW.setVisible(true);
			}
		});
		
		/*ȸ������ ��ư �̺�Ʈ
		 * ȸ������ GUI�� ��� - ClientGUI_Register.java*/
		registerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClientGUI_Register register = new ClientGUI_Register(socket, in, out);
				register.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				register.setVisible(true);
			}
		});
	}
}

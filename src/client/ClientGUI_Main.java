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
			socket = new Socket(serverAddress, serverPort); // 불러온 서버주소, port로 연결 요청
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (in.hasNextLine())
            {
            	String line = in.nextLine();
            	
            	/*CONNECTED로 시작하는 문자열을 받으면
            	 * 서버와 연결되었다는 다이얼로그를 띄운 뒤
            	 * CONNECTED와 클라이언트의 IP주소를 합쳐서 문자열로 보냄*/
            	if (line.startsWith("CONNECTED"))
            	{
            		JOptionPane.showMessageDialog(null, "Connected with server");
            		out.println("CONNECETED " + InetAddress.getLocalHost().getHostAddress());
            	}
            	
            	/*LOGINOK로 시작하는 문자열을 받으면
            	 * 로그인 성공 + 클라이언트의 ID를 다이얼로그로 띄움
            	 * 실패하였을 시는 아이디 또는 비밀번호를 다시 확인하라는 다이얼로그를 띄움*/
            	if (line.startsWith("LOGINOK"))
            	{
            		JOptionPane.showMessageDialog(null, "Login Succeced. Welcome " + id);
            		ClientGUI_List a = new ClientGUI_List(in, out);
            		a.setVisible(true);
            		setVisible(false);
            	}
            	else if (line.startsWith("LOGINFAIL"))
            	{
            		JOptionPane.showMessageDialog(null, "Login Failed. Check Id or Password ");
            	}
            	
            	/*FINDPWOK로 시작하는 문자열을 받으면
            	 * 서버에서 받아온 문자열을 파싱하여 
            	 * 비밀번호 찾기 성공이라는 문자열과 비밀번호를 다이얼로그로 띄움
            	 * 실패시는 아이디나 이메일을 확인하라는 다이얼로그를 띄움*/
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
		
		/*로그인 버튼 이벤트
		 * idField와 passwordField에 입력된 값을 읽어와서
		 * LOGIN이라는 문자열과 함께 서버로 보냄*/
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
		
		/*비밀번호 찾기 버튼 이벤트
		 * 비밀번호 찾기 GUI를 띄움 - ClientGUI_FindPassword.java*/
		findPasswordButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClientGUI_FindPassword findPW = new ClientGUI_FindPassword(socket, in, out);
				findPW.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				findPW.setVisible(true);
			}
		});
		
		/*회원가입 버튼 이벤트
		 * 회원가입 GUI를 띄움 - ClientGUI_Register.java*/
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

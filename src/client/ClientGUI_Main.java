package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class ClientGUI_Main extends JFrame
{
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtPassword;
	private JTextField idField;
	private JPasswordField passwordField;
	
	private String id;
	private String password;
	
	public ClientGUI_Main(String serverAddress, int serverPort)
	{
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
		
		/*로그인 버튼 이벤트*/
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				id = idField.getText();
				password = String.valueOf(passwordField.getPassword());
				idField.setText("");
				passwordField.setText("");
			}
		});
		
		/*비밀번호 찾기 버튼 이벤트*/
		findPasswordButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClientGUI_FindPassword findPW = new ClientGUI_FindPassword();
				findPW.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				findPW.setVisible(true);
			}
		});
		
		/*회원가입 버튼 이벤트*/
		registerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ClientGUI_Register register = new ClientGUI_Register();
				register.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				register.setVisible(true);
			}
		});
	}
	
	/*ClientMain에서 아이디, 비밀번호 얻기위한 getter*/
	public String getId()
	{
		return id;
	}
	
	public String getPassword()
	{
		return password;
	}
}

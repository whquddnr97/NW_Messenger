package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ClientGUI_FindPassword extends JFrame
{

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtEmail;
	private JTextField idField;
	private JTextField emailField;
	private JButton btnFindPassword;
	
	private String id = "";
	private String email = "";
	
	public ClientGUI_FindPassword()
	{
		setTitle("Find Password");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 328, 169);
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
		
		txtEmail = new JTextField();
		txtEmail.setHorizontalAlignment(SwingConstants.CENTER);
		txtEmail.setToolTipText("Enter the email");
		txtEmail.setEditable(false);
		txtEmail.setText("Email:");
		txtEmail.setBounds(12, 64, 76, 21);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		idField = new JTextField();
		idField.setToolTipText("Enter the ID");
		idField.setBounds(103, 33, 183, 21);
		contentPane.add(idField);
		idField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setToolTipText("Enter the email");
		emailField.setColumns(10);
		emailField.setBounds(103, 64, 183, 21);
		contentPane.add(emailField);
		
		btnFindPassword = new JButton("Find Password");
		btnFindPassword.setBounds(91, 97, 137, 23);
		contentPane.add(btnFindPassword);
		
		/*비밀번호 찾기 버튼 이벤트*/
		btnFindPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				id = idField.getText();
				email = emailField.getText();
				idField.setText("");
				emailField.setText("");
			}
		});
	}
	
	/*ClientMain에서 아이디, 이메일 얻기위한 getter*/
	public String getId()
	{
		return id;
	}
	
	public String getEmail()
	{
		return email;
	}

}

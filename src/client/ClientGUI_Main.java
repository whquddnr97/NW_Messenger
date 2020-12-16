package client;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

/*���� Ŭ���̾�Ʈ class*/
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
	private static String id = "";
	private String password = "";
	
	
	public void run() throws IOException
	{
		try
		{
			socket = new Socket(serverAddress, serverPort); // �ҷ��� �����ּ�, port�� ���� ��û
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
            /*��ü ä�ù��� ���� gui*/
            JFrame frame = new JFrame("Chatter" + id);
    		JTextField textField = new JTextField(50);
    		JTextArea messageArea = new JTextArea(16, 50);
    		JPanel contentPane;
    		frame.getContentPane().add(textField, BorderLayout.SOUTH);
    		frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
    	    frame.pack();
    	    
    	    textField.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    out.println("MESSAGE " + id + " " + textField.getText() + "\n");
                    messageArea.append(textField.getText() + "\n");
                    textField.setText("");
                }
            });
    	    
    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(false);
            
            /*1��1 ä�ù��� �̿��ϱ� ���� gui*/
            JFrame frame1 = new JFrame("Chatter");
    		JTextField textField1 = new JTextField(50);
    		JTextArea messageArea1 = new JTextArea(16, 50);
    		JPanel contentPane1;
    		frame1.getContentPane().add(textField1, BorderLayout.SOUTH);
    		frame1.getContentPane().add(new JScrollPane(messageArea1), BorderLayout.CENTER);
    	    frame1.pack();
    	    
    	    textField1.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	String with = frame1.getName();
                	String[] name = with.split(" ");
                	
                    out.println("CHAT2 " + id + " " + name[1] + " " + textField1.getText());
                    messageArea1.append(textField1.getText() + "\n");
                    textField1.setText("");
                }
            });
    	    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setVisible(false);
            
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
            		String[] getFriend = line.split(" ");
            		ClientGUI_List2 list = new ClientGUI_List2(getFriend, socket, in, out);
            		list.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            		list.setVisible(true);
            		setVisible(false);
            		
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
            	/*FINDPWFAIL�̶�� ���ڿ��� ������,
            	 * ��й�ȣ ã�⿡ ����, ���̵� �̸����� Ȯ���϶�� ���̾�α׸� ���*/
            	else if (line.startsWith("FINDPWFAIL"))
            	{
            		JOptionPane.showMessageDialog(null, "Find Password Failed. Check Id or Email ");
            	}
            	
            	/*SEARCHRESULT��� ���ڿ��� ������, ������ �˻��ߴ����� ����� ���̾�α׿� �����*/
            	if (line.startsWith("SEARCHRESULT"))
            	{
            		String[] who = line.split(" ");
            		String who1 = who[1];
            		JOptionPane.showMessageDialog(null, "Found " + who1);
            	}
            	
            	/*INFOS��� ���ڿ��� ������
            	 * ���� �޽���, �ֱ� ���ӽð�, �� �������� ���θ� ���̾�α׿� �����*/
            	if (line.startsWith("INFOS"))
            	{
            		String[] infos = line.split(" ");
            		String status = infos[1];
            		String time = infos[2] + " " + infos[3];
            		String isOnline = infos[4];
            		JOptionPane.showMessageDialog(null, "�Ѹ��� : " + status + "\n" + "�ֱ� ���ӽð� : " + time + "\n" + "���� : " + isOnline);
            	}
            	
            	/*ENTERALL�̶�� ���ڿ��� ������
            	 * ��ü ä�ù��� ���� gui�� ���*/
            	if (line.startsWith("ENTERALL"))
            	{
            		 frame.setVisible(true);
            	}
            	
            	/*MESSAGEFROM�̶�� ���ڿ��� ������,
            	 * ��ü ä�ù濡�� �������� �� �޽��������� messageArea�� �����*/
            	if (line.startsWith("MESSAGEFROM"))
            	{
            		String[] messages = line.split(" ");
            		String id = messages[1];
            		String message = messages[2];
            		if (id.equals(this.id))
            		{
            			continue;
            		}
            		else
            		{
            			messageArea.append("From " + id + " :" + message + "\n");
            		}
            	}
            	
            	/*OPEN�̶�� ���ڿ��� ������
            	 * ������ ä���� �������� ���� OptionPane�� ����ְ�
            	 * YES�ϰ�� ������ ä���� �����ߴٰ� �˷��ְ� ������ CHATOK��� ���ڿ��� �Բ�
            	 * ������ ���̵� ����
            	 * NO�ϰ�� CHATNO��� ���ڿ��� �Բ� ������ ���̵� ����*/
            	if (line.startsWith("OPEN"))
            	{
            		String[] messages = line.split(" ");
            		String id1 = messages[1];
            		String id2 = messages[2];
            		int result = JOptionPane.showConfirmDialog(null, id1 + "�� ä�� �Ͻðڽ��ϱ�?", "Confirm", JOptionPane.YES_NO_OPTION);
            		
            		if (result == JOptionPane.YES_OPTION)
            		{
            			frame1.setName("With " + id1);
            			frame1.setVisible(true);
            			messageArea1.append("Chat with " + id1 + "\n");
            			out.println("CHATOK " + id1 + " " + id2);
            		}
            		else
            		{
            			out.println("CHATNO "+ id1 + " " + id2);
            		}
            	}
            	
            	/*FOPENF��� ���ڿ��� ������, 1��1 ä���� ���� gui�� ����
            	 * ��븦 ��ٸ��� �ִٴ� �޽����� �����*/
            	if (line.startsWith("FOPENF"))
            	{
            		String[] messages = line.split(" ");
            		String id1 = messages[1];
            		frame1.setName("With " + id1);
            		frame1.setVisible(true);
            		messageArea1.append("Waiting for " + id1 + "\n");
            	}
            	
            	/*CHAT2NO��� �޽����� ������, ��밡 1��1 ä���� �����ߴٰ� ����ְ�
            	 * CHAT2OK��� �޽����� ������, ��밡 ���Դٰ� �����*/
            	if (line.startsWith("CHAT2NO") || line.startsWith("CHAT2OK"))
            	{
            		String[] input = line.split(" ");
            		String id1 = input[1];
            		String id2 = input[2];
            		String stat = input[3];
            		
            		if (line.startsWith("CHAT2NO"))
            		{
            			messageArea1.append(id1 + id2 + " Chat room rejected");
            		}
            		if (line.startsWith("CHAT2OK"))
            		{
            			messageArea1.append(id2 + " Entered" + "\n");
            		}
            	}
            	
            	/*CHAT2WITH�̶�� �޽����� ������, 1��1 ä�ù濡�� �������� �� �޽�������,
            	 * �׸��� �޽����� ������ �����*/
            	if (line.startsWith("CHAT2WITH"))
            	{
            		String[] input = line.split(" ");
            		String from = input[1];
            		String message = input[2];
            		messageArea1.append("From " + from + " : " + message + "\n");
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
	public static String getId()
	{
		return id;
	}
}

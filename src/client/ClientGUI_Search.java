package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/*ģ�� �˻��� �ϱ� ���� class*/
public class ClientGUI_Search extends JFrame
{

	private JPanel contentPane;
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private JTextField textField;
	private String who = "";
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;

	public ClientGUI_Search(Socket socket, Scanner in, PrintWriter out)
	{
		this.socket = socket;
		this.in = in;
		this.out = out;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 10, 294, 32);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("\uAC80\uC0C9");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				out.println("SEARCH " + name);
				}
			}
				);
		btnNewButton.setBounds(325, 14, 97, 28);
		contentPane.add(btnNewButton);
		
		/*ģ�� �߰� ��ư�� ������ ���, ADDFRIEND ���ڿ��� �Բ� ������ �߰��Ұ���
		 * ������ ������*/
		btnNewButton_1 = new JButton("\uCE5C\uAD6C\uB4F1\uB85D");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String who = textField.getText();
				out.println("ADDFRIEND " + "t1 " + who);
				JOptionPane.showMessageDialog(null, "Added " + who);
			}
		});
		
		/*�������� ��ư�� ������ ���, SHOWINFO�� �Բ�
		 * ������ ������ �� ������ ������ ������*/
		btnNewButton_1.setBounds(44, 219, 138, 32);
		contentPane.add(btnNewButton_1);
		btnNewButton_2 = new JButton("\uC815\uBCF4\uBCF4\uAE30");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String who = textField.getText();
				out.println("SHOWINFO " + who);
			}
		});
		btnNewButton_2.setBounds(227, 219, 155, 32);
		contentPane.add(btnNewButton_2);
		
		
		
		
	}
}

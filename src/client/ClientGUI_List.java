package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JTextField;

public class ClientGUI_List extends JFrame
{

	private JPanel contentPane;
	Weather weather = new Weather();
	private JTextField textField;
	Scanner in;
	PrintWriter out;
	String name;

	public ClientGUI_List(Scanner in, PrintWriter out) throws IOException
	{
		this.in = in;
		this.out = out;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 647);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("\uCE5C\uAD6C\uCD94\uAC00");
		btnNewButton.setBounds(0, 0, 94, 55);
		contentPane.add(btnNewButton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(77, 65, 320, 24);
		contentPane.add(textArea);
		
		JLabel lblNewLabel = new JLabel("\uCE5C\uAD6C\uAC80\uC0C9");
		lblNewLabel.setBounds(17, 67, 62, 18);
		contentPane.add(lblNewLabel);
		
		JButton button = new JButton("\uC815\uBCF4\uBCC0\uACBD");
		button.setBounds(96, 0, 94, 55);
		contentPane.add(button);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(411, 0, 21, 600);
		contentPane.add(scrollBar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 119, 388, 396);
		
		contentPane.add(scrollPane);
        		JTree tree = new JTree();
        		tree.setModel(new DefaultTreeModel(
        			new DefaultMutableTreeNode("친구목록") {
        				{
        					DefaultMutableTreeNode node_1;
        					node_1 = new DefaultMutableTreeNode("온라인");
        						node_1.add(new DefaultMutableTreeNode("blue"));
        						node_1.add(new DefaultMutableTreeNode("violet"));
        						node_1.add(new DefaultMutableTreeNode("red"));
        						node_1.add(new DefaultMutableTreeNode("yellow"));
        						node_1.add(new DefaultMutableTreeNode("sadsa"));
        						node_1.add(new DefaultMutableTreeNode("sadas"));
        						node_1.add(new DefaultMutableTreeNode("vcvnsalk"));
        					add(node_1);
        					
        				}
        			}
        		));
        		
        		tree.setFont(new Font("굴림", Font.PLAIN, 15));
        		tree.setBackground(SystemColor.control);
        		scrollPane.setViewportView(tree);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(17, 527, 388, 61);
		textArea_1.append(weather.getInfo());
		contentPane.add(textArea_1);
	}
}

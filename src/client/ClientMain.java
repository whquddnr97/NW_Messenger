package client;

import javax.swing.JFrame;

public class ClientMain
{
	/*���� �޼ҵ�*/
	public static void main(String[] args) throws Exception
	{
		/*���� Ip, Port �о����*/
		ConfigRead readServerInfo = new ConfigRead();
		readServerInfo.readFile();
		String serverAddress = readServerInfo.getIp();
		int serverPort = readServerInfo.getPort();
		
		/*�α���â GUI ����*/
		ClientGUI_Main client = new ClientGUI_Main(serverAddress, serverPort);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
		client.run();
	}
}

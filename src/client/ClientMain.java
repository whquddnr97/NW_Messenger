package client;

import java.awt.EventQueue;
import java.io.IOException;

public class ClientMain
{
	public static void main(String[] args)
	{
		/*���� Ip, Port �о����*/
		ConfigRead readServerInfo = new ConfigRead();
		readServerInfo.readFile();
		String serverAddress = readServerInfo.getIp();
		int serverPort = readServerInfo.getPort();
		
		/*�α���â GUI ����*/
		ClientGUI_Main client = new ClientGUI_Main(serverAddress, serverPort);
		client.setVisible(true);
	}
}

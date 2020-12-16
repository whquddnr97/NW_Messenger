package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigRead
{
	private String ip;
	private String portS;
	private int port;
	
	ConfigRead() // �⺻ ������
	{
		ip = "localhost";
		portS = "59001";
		port = 59001;
	}
	
	public void readFile()
	{
		try
		{
			String filename = "config.txt";
			
			Scanner scanner = new Scanner(new File(filename));
			//ù���� ip�ּ�, �ι�°���� ��Ʈ �ѹ��� ����Ѵ�
			
			ip = scanner.next();
			portS = scanner.next();
			port = Integer.parseInt(portS);

		}
		catch(FileNotFoundException e) // ������ ���� ��� �⺻�� ���
		{
			ip = "localhost";
			portS = "59001";
			port = 59001;
		}
	}
	
	// Client ���� ip�� ����ϱ� ���� getter
	String getIp()
	{
		return ip; 
	}
	
	// Client ���� port�� ����ϱ� ���� getter
	int getPort()
	{
		return port; 
	}

}

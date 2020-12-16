package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain
{
	private static HashMap<String, PrintWriter> participants = new HashMap<>();
	private static HashMap<String, PrintWriter> hello = new HashMap<>(); 
	
	static ServerDB DB = new ServerDB();
	public static void main(String[] args) throws Exception
	{
		/*������ �����Ǹ�, ������ �����Ǿ��ٴ� �޽����� ������ ��� �� ��
		 * ������Ǯ�� �̿��Ͽ� ���� ������ �����
		 * accept �Ǿ��� ��, ������Ǯ�� �̿��Ͽ� Handler�� ����*/
		System.out.println("The chat server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(500);
		try (ServerSocket listener = new ServerSocket(59001))
		{
			while (true)
			{
				pool.execute(new Handler(listener.accept()));
			}
		}
	}

	//�ؽ��ʿ��� value�� key�� ã�� ���� �޼ҵ�
	public static Object getKey(HashMap<String, PrintWriter>in, Object value)
	{
		for (Object o : in.keySet())
		{
			if (in.get(o).equals(value))
			{
				return o;
			}
		}
		return null;
	}
	
	private static class Handler implements Runnable
	{
		private String name;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		public Handler(Socket socket)
		{
			this.socket = socket;
		}

		public void run()
		{
			try
			{
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);
				
				/*Ŭ���̾�Ʈ�� ����Ǹ�, Ŭ���̾�Ʈ�� CONNECTED��� ���ڿ��� ����*/
				out.println("CONNECTED");
				
				while (true)
				{
					String input = in.nextLine();
					
					/*Ŭ���̾�Ʈ�κ��� CONNECTED��� ���ڿ��� ������,
					 * ������ CONNECTED��� ���ڿ��� Ŭ���̾�Ʈ�� �������ּҸ� ���*/
					if (input.startsWith("CONNECETED"))
					{
						System.out.println(input);
					}
					
					/*Ŭ���̾�Ʈ�κ��� �α��� ��û�� ������,
					 * ���� ���ڿ��� �Ľ��Ͽ� ���̵�� ��й�ȣ�� ������ ��
					 * DB�� ���̵�� �н����带 ����
					 * �α��� ������ LOGINOK��, ���н� LOGINFAIL�̶�� ���ڿ���
					 * Ŭ���̾�Ʈ�� ����*/
					if (input.startsWith("LOGIN"))
					{
						System.out.println(input);
						String[] loginArray = input.split(" ");
						String loginId = loginArray[1];
						String loginPassword = loginArray[2];
						boolean check = DB.login(DB, loginId, loginPassword);
						
						if (check)
						{
							String name = DB.findName(DB, loginId);
							out.println("LOGINOK " + name);
							
							//�ؽ����� ����ȭ�ϰ�, �̸��� ��� ��Ʈ�� ����
							synchronized (participants)
							{
								if (name.length() > 0 && !participants.containsKey(name))
								{
									participants.put(name, out);
									break;
								}
							}
						}
						else
						{
							out.println("LOGINFAIL");
						}
					}
					
					/*Ŭ���̾�Ʈ�κ��� ��й�ȣ ã�� ��û�� ������ ��
					 * ���� ���ڿ��� �Ľ��Ͽ� ���̵�� �̸����� ������ ��
					 * DB�� ���̵�� �̸����� ����
					 * ���н� FINDPWFAIL��, ������ FINDPWOK�� ��й�ȣ��
					 * Ŭ���̾�Ʈ�� ����*/
					if (input.startsWith("FINDPW"))
					{
						System.out.println(input);
						String[] loginArray = input.split(" ");
						String findPwId = loginArray[1];
						String findPwEmail = loginArray[2];
						String check = DB.findPw(DB, findPwId, findPwEmail);
						
						if (check.isEmpty())
						{
							out.println("FINDPWFAIL");
						}
						else
						{
							out.println("FINDPWOK " + check);
						}
					}
					
					/*Ŭ���̾�Ʈ�κ��� ȸ������ ��û�� ������ ��
					 * ���� ���ڿ��� DB�� ����*/
					if (input.startsWith("REGISTER"))
					{
						DB.register(DB, input);
						System.out.println(input);
					}
					
					if (input.startsWith("ONLINE"))
					{
						int count = participants.size();
						String countS = String.valueOf(count);
						out.println("ONLINE " + String.valueOf(participants.size()));
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
	}
}
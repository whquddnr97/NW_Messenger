package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain
{
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

	private static class Handler implements Runnable
	{
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
						
						/*�α����� Ȯ�� �Ǹ� �α��� ��¥�� ����
						 * ģ�� ����Ʈ�� DB�� ���� �ҷ��� ��, LOGINOK��� ���ڿ��� �Բ�
						 * ģ�� ����Ʈ�� ������*/
						if (check)
						{
							SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
							Date time = new Date();
							String loginTime = format1.format(time);
							User.add_participants(loginId, out);
							User.add_socket(loginId, socket);
							User.add_loginTime(loginId, loginTime);
							String friendList = DB.findFriend(DB, loginId);
							String[] friend = friendList.split(" ");
							String friendSend = "";
							for (int i = 0; i < friend.length; i++)
							{
								String findFriend = friend[i];
								PrintWriter isOnline = null;
								friendSend += friend[i] + " ";
							}
								out.println("LOGINOK " + friendSend);
							
							
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
					
					/*Ŭ���̾�Ʈ�κ��� ģ�� �˻� ��û�� ���� ��
					 * DB���� �˻� �� ��, ����� ������*/
					if (input.startsWith("SEARCH"))
					{
						String[] inp = input.split(" ");
						String inpName = inp[1];
						String result = DB.Search(DB, inpName);
						out.println("SEARCHRESULT " + result);
					}
					
					/*Ŭ���̾�Ʈ�κ��� ģ�� �߰� ��û�� ���� ��
					 * DB�� �̿��Ͽ� ģ���� �߰���*/
					if (input.startsWith("ADDFRIEND"))
					{
						String[] inp = input.split(" ");
						String inpName = inp[1];
						String addName = inp[2];
						DB.AddFriend(DB, inpName, addName);
					}
					
					/*Ŭ���̾�Ʈ�κ��� ���� �޽��� �Ǵ� �г��� ���� ��û�� ���� ��
					 * DB�� �̿��Ͽ� �г����� �ٲ��ְ�, ���� �޽��� ������ ������ �ݿ���*/
					if (input.startsWith("CHANGEINFO"))
					{
						String[] inp = input.split(" ");
						String id = inp[1];
						String status = inp[2];
						String nickName = inp[3];
						
						User.add_statusMessage(id, status);
						DB.changeNick(DB, id, nickName);
					}
					
					/*Ŭ���̾�Ʈ�κ��� ���� ������ �����޶�� ��û�� ���� ��,
					 * �������� ���� �޽����� �α��� �ð��� �ҷ���*/
					if (input.startsWith("SHOWINFO"))
					{
						String[] inp = input.split(" ");
						String id = inp[1];
						
						String status = User.status_message.get(id);
						String loginTime = User.login_time.get(id);
						boolean isOnline = User.participants.containsKey(id);
						if (isOnline)
						{
							out.println("INFOS " + status + " " + loginTime + " " + "online");
						}
						else
						{
							out.println("INFOS " + status + " " + loginTime + " " + " " + "offline");
						}
					}
					
					/*Ŭ���̾�Ʈ�κ��� ��ü ä�ù� ���� ��û�� ���� ��,
					 * ENTERALL�̶�� ���ڿ��� Ŭ���̾�Ʈ�� ����*/
					if (input.startsWith("ENTERALL"))
					{
						out.println("ENTERALL");
					}
					
					/*Ŭ���̾�Ʈ�� ��ü ä�ù濡 �޽����� ������ ��
					 * MESSAGEFROM ���ڿ��� �Բ� ���� ����� �޽����� �� Ŭ���̾�Ʈ�� ����*/
					if (input.startsWith("MESSAGE"))
					{
						String[] inp = input.split(" ");
						String id = inp[1];
						String message = inp[2];
						for (PrintWriter p : User.participants.values())
						{
							p.println("MESSAGEFROM " + id + " " + message);
						}
					}
					
					/*Ŭ���̾�Ʈ�� 1��1 ä�ù� ���� ��û�� �Ͽ��� ��
					 * ��û�ڿ��Դ� FOPENF��� ���ڿ��� ���̵�,
					 * �ǽ�û�ڿ��Դ� OPEN�̶�� ���ڿ��� ���̵� ������*/
					if (input.startsWith("CHATWITH"))
					{
						String[] inp = input.split(" ");
						String id1 = inp[1];
						String id2 = inp[2];
						PrintWriter id1p = User.participants.get(id1);
						PrintWriter id2p = User.participants.get(id2);
						id1p.println("FOPENF " + id2 + " " + id1);
						id2p.println("OPEN " + id1 + " " + id2);
					}
					
					/*1��1 ä�ù濡 ���� �ǽ�û���� ������ ���� ��
					 * ������ �ź��ϸ� CHAT2NO�� rejected��� ���ڿ���,
					 * ������ ����ϸ� CHAT2OK�� entered��� ���ڿ��� ������*/
					if (input.startsWith("CHATNO") || input.startsWith("CHATOK"))
					{
						String[] ids = input.split(" ");
						String id1 = ids[1];
						String id2 = ids[2];
						PrintWriter id1p = User.participants.get(id1);
						PrintWriter id2p = User.participants.get(id2);
						
						if (input.startsWith("CHATOK"))
						{
							id1p.println("CHAT2OK " + id1 + " " + id2 + " " + "entered");
						}
						if (input.startsWith("CHATNO"))
						{
							id1p.println("CHAT2NO " + id1 + " " + id2 + " " + "rejected");
						}
					}
					
					/*1��1 ä�ù濡�� �޽��� ���� ��û�� ���� ��
					 * �ش��ϴ� ����ڿ��� CHAT2WITH�̶�� ���ڿ��� �Բ�
					 * �޽����� ������*/
					if (input.startsWith("CHAT2"))
					{
						String[] inp = input.split(" ");
						String from = inp[1];
						String to = inp[2];
						String message = inp[3];
						PrintWriter fromp = User.participants.get(inp[1]);
						PrintWriter top = User.participants.get(inp[2]);
						top.println("CHAT2WITH " + from + " " + message);
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
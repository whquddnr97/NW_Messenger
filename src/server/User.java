package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*���� �������� ������ ������ ������� Ŭ����*/
public class User
{
	 public static HashMap<String, PrintWriter> participants = new HashMap<>();
	 public static HashMap<String, Socket> participants_socket = new HashMap<>(); 
	 public static HashMap<String, String> login_time = new HashMap<>(); 
	 public static HashMap<String, String> logout_time = new HashMap<>();
	 public static HashMap<String, String> status_message = new HashMap<>(); 
	 
	 //key�� ���̵�� �ϴ� �ؽ��ʿ� ���� ���¸� �ֱ� ���� �޼ҵ�
	 //��Ʈ��, ����, �α��� �ð�, ���� �޽����� ������
	 public static void add_participants(String id, PrintWriter out)
	 {
		 participants.put(id, out);
	 }
	 
	 public static void add_socket(String id, Socket socket)
	 {
		 participants_socket.put(id, socket);
	 }
	 
	 public static void add_loginTime(String id, String loginTime)
	 {
		 login_time.put(id, loginTime);
	 }
	 
	 public static void add_logoutTime(String id, String logoutTime)
	 {
		 logout_time.put(id, logoutTime);
	 }
	 
	 public static void add_statusMessage(String id, String statusMessage)
	 {
		 status_message.put(id, statusMessage);
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
	
		//������ �¶������� ã�� ���� �޼ҵ�
	public static boolean isOnline(HashMap<String, PrintWriter>map, String id)
	{
		if (map.containsKey(id))
		{
			return true;
		}
		return false;
	}
		
}

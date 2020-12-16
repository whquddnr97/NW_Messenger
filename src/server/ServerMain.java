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
		/*서버가 가동되면, 서버가 가동되었다는 메시지를 서버에 출력 한 뒤
		 * 쓰레드풀을 이용하여 소켓 연결을 대기함
		 * accept 되었을 시, 쓰레드풀을 이용하여 Handler를 실행*/
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

	//해쉬맵에서 value로 key를 찾기 위한 메소드
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
				
				/*클라이언트와 연결되면, 클라이언트로 CONNECTED라는 문자열을 보냄*/
				out.println("CONNECTED");
				
				while (true)
				{
					String input = in.nextLine();
					
					/*클라이언트로부터 CONNECTED라는 문자열을 받으면,
					 * 서버에 CONNECTED라는 문자열과 클라이언트의 아이피주소를 출력*/
					if (input.startsWith("CONNECETED"))
					{
						System.out.println(input);
					}
					
					/*클라이언트로부터 로그인 요청을 받으면,
					 * 받은 문자열을 파싱하여 아이디와 비밀번호를 저장한 뒤
					 * DB로 아이디와 패스워드를 보냄
					 * 로그인 성공시 LOGINOK를, 실패시 LOGINFAIL이라는 문자열을
					 * 클라이언트로 전송*/
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
							
							//해쉬맵을 동기화하고, 이름과 출력 스트림 저장
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
					
					/*클라이언트로부터 비밀번호 찾기 요청이 들어왔을 시
					 * 받은 문자열을 파싱하여 아이디와 이메일을 저장한 뒤
					 * DB로 아이디와 이메일을 보냄
					 * 실패시 FINDPWFAIL을, 성공시 FINDPWOK와 비밀번호를
					 * 클라이언트로 보냄*/
					if (input.startsWith("FINDPW"))
					{
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
					
					/*클라이언트로부터 회원가입 요청이 들어왔을 시
					 * 받은 문자열을 DB로 보냄*/
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

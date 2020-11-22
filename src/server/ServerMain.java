package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain
{
	//�ӼӸ� ����� �����ϱ� ���� �̸��� key, ��� ��Ʈ���� value�� �ϴ� �ؽ����� ����
	private static HashMap<String, PrintWriter> participants = new HashMap<>(); 
	
	public static void main(String[] args) throws Exception
	{
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
				
				out.println("CONNECTED");
				
				while (true)
				{
					String input = in.nextLine();
					if (input.startsWith("CONNECETED"))
					{
						System.out.println(input);
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
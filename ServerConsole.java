import java.io.IOException;
import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole;
	
	public ServerConsole(int port) {
		server = new EchoServer(port, this);
		try {
			server.listen();
		} catch (Exception e) {
			System.out.println("Unable to listen to connections");
		}
		fromConsole = new Scanner(System.in);
	}
	
	public void accept() {
		try {
			String message;
			
			while(true) {
				message = fromConsole.nextLine();
				System.out.println(">"+message);
				server.sendToAllClients("SERVER MSG> "+message);
			}
			
		} catch (Exception e) {
			System.out.println("Unexpected error while reading from console!");
		}
	}
	
	public static void main(String[] args) {
		
		int port;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch (IndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		} catch (NumberFormatException ne) {
			port = DEFAULT_PORT;
		}
		
		ServerConsole serverUI = new ServerConsole(port);
		serverUI.accept();
	}

	@Override
	public void display(String message) {
		System.out.println("> " + message);
	}

}

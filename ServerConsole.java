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
				if (message.startsWith("#")) {
		        	String command = message.substring(1);
		        	switch (command) {
		        	case "quit":
		        		System.exit(0);
		        	case "stop":
		        		server.stopListening();
		        		break;
		        	case "close":
		        		server.close();
		        		break;
		        	case "start":
		        		if (!server.isListening()) {
		        			server.listen();
		        		} else {
		        			display("Server is already started");
		        		}
		        		break;
		        	case "getport":
		        		display("Port: " + server.getPort());
		        		break;
		        	default:
		        		if (command.startsWith("setport")) {
		        			if (server.isListening()) {
		        				display("Cannot set port while listening");
		        			} else {
		        				try {
		        					int port = Integer.parseInt(command.substring(8).trim());
									server.setPort(port);
									display("Port set to: " + port);
								} catch (NumberFormatException e) {
									display("Invalid port parameter");
								}
		        			}
		        			break;
		        		}
		        		display("Unknown command");
		        	}
		        } else {
		        	display(message);
		        	server.sendToAllClients("SERVER MESSAGE> "+message);
		        }
				
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

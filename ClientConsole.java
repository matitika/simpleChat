// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF {
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String id, String host, int port) {
    try {
      client= new ChatClient(id, host, port, this);
      
      
    } 
    catch(IOException exception) {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() {
    try {

      String message;

      while (true) {
        message = fromConsole.nextLine();
        if (message.startsWith("#")) {
        	String command = message.substring(1);
        	switch (command) {
        	case "quit":
        		client.quit();
        		if (!client.isConnected()) {
        			System.exit(0);
        		}
        	case "logoff":
        		client.closeConnection();
        		break;
        	case "login":
        		if(client.isConnected()) {
        			display("Client is already connected");
        		} else {
        			client.openConnection();
        		}
        		break;
        	case "gethost":
        		display("Host: " + client.getHost());
        		break;
        	case "getport":
        		display("Port: " + client.getPort());
        		break;
        	default:
        		if (command.startsWith("sethost")) {
        			if (client.isConnected()) {
        				display("Cannot set host while connected");
        			} else {
        				String host = command.substring(8).trim();
        				client.setHost(host);
        				display("Host set to: " + host);
        			}
        			break;
        		} else if (command.startsWith("setport")) {
        			if (client.isConnected()) {
        				display("Cannot set port while connected");
        			} else {
        				try {
        					int port = Integer.parseInt(command.substring(8).trim());
							client.setPort(port);
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
        	client.handleMessageFromClientUI(message);
        }
        
      }
    } 
    catch (Exception ex) {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    if(message.startsWith("SERVER MESSAGE>")) {
    	System.out.println(message);
    } else {
    	System.out.println("> " + message);
    }
  	
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The login id.
   */
  public static void main(String[] args) {
    
  	String id = "";
	  String host = "";
    int port;
    

    try {
    	id = args[0];
    	if (id.isBlank()) {
    		System.out.println("ERROR - No login ID specified. Connection aborted");
    		System.exit(0);
    	}
    } catch(ArrayIndexOutOfBoundsException e) {
    	System.out.println("ERROR - No login ID specified. Connection aborted");
    	System.exit(0);
    }
    
    
    try {
      host = args[1];
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e) {
      host = "localhost";
      port = DEFAULT_PORT;
    }  catch (NumberFormatException ne) {
    	host = "localhost";
    	port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(id, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class

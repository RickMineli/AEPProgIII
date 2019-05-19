package AEP;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientAEP {
	public static void main(String[] args) {
		ClientAEP c = new ClientAEP();
		c.start();
	}
	
	private void start() {
		try(Socket server = new Socket("localhost",9092)) {
			
			Scanner console =  new  Scanner(System.in);
			Scanner fromServer =  new  Scanner(server.getInputStream());
			PrintWriter toServer = new PrintWriter(server.getOutputStream());
			String serverResponse = "";
			System.out.println("? for help");
			System.out.println("Please use command set to set a path first.");
			do {
				System.out.println("Send a command to server: ");
				String command = console.nextLine();
				String command2[] = command.split(" ");
				String fileExtension[] = command.split(".");
				toServer.println(command);
				toServer.flush();
				
				while(!serverResponse.equals("$")) {
					if (command2[0].equals("sendFile")) {
					String filePath = (command2[command2.length-1]);
					toServer.println(fileExtension[fileExtension.length-1]);
					sendFile(filePath, server);
					}else{
						serverResponse = fromServer.nextLine();
						if (!serverResponse.equals("$")) {
							System.out.println(serverResponse);
						}	
					}
				}
				serverResponse = "";
			} while (true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendFile(String filePath, Socket server) throws IOException {
		DataOutputStream dos = new DataOutputStream(server.getOutputStream());
		FileInputStream fis = new FileInputStream(filePath);
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		fis.close();
		dos.close();	
	}
	
	
}
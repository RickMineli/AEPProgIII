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
		while (true) {
			String command = "";
			String command2[];
			Scanner console = new Scanner(System.in);
			System.out.println("Please connect to an localhost using: \n connect <port>  \n Avaliable ports: 9092");
			command = console.nextLine();
			command2 = command.split(" ");
			String serverResponse = "";
			try (Socket server = new Socket("localhost", Integer.parseInt(command2[1]))) {
				Scanner fromServer = new Scanner(server.getInputStream());
				PrintWriter toServer = new PrintWriter(server.getOutputStream(), true);				
				String fileExtension[];
				System.out.println("Please use command set to set a path first.");
				while (!command.equals("exit")) {
					System.out.println("Type your command: ");
					command = console.nextLine();
					command2 = command.split(" ");
					toServer.println(command);
//
//					if (command2[0].equals("sendFile")) {							
//						fileExtension = command.split("\\.");
//						String filePath = (command2[command2.length - 1]);
//						toServer.println(fileExtension[fileExtension.length - 1]);
//						sendFile(filePath, server);
//						command2[0] = "";
//					}
					while (!serverResponse.equals("$")) {
						serverResponse = fromServer.nextLine();
						if (!serverResponse.equals("$")) {
							System.out.println(serverResponse);
						}
					}

					serverResponse = "";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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

package AEP;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerAEP {
	private static final int PORT = 9092;
	
	public static void main(String[] args) {
		ServerAEP s = new ServerAEP();
		s.start();
	}
	
	public void start() {
		System.out.println("Initializing server...");
		System.out.println("Listening on port " + PORT);
		try (ServerSocket socket = new ServerSocket(PORT)){
			
			Socket client = socket.accept();
			Scanner fromClient = new Scanner(client.getInputStream());
			PrintWriter toClient = new PrintWriter(client.getOutputStream());
			String clientCommand = "";
			String path = "";
			do {
				clientCommand = fromClient.nextLine();
				String[] clientCommand2 = clientCommand.split(" ");
				switch (clientCommand2[0]) {
					case "?":
						toClient.println("?                    --> Show all commands \n"
									+ "path --> Show current path \n"
									+ "set <path> --> Set directory path \n"
									+ "showFiles --> List files \n"
									+ "showAll --> List subdirectories \n"
									+ "createDir <name> --> Create a new directory \n"
									+ "deleteDir <name> --> Delete an empty directory \n"
									+ "sendFile <fileDirectory> --> Create a new file"
									+ "exit --> Exit"
									+ "\n"
									+ "$");
						break;
					case "path":
						toClient.println("Path seted to " + path + ".");
						toClient.println("$");
						break;
					case "set":
						path = clientCommand2[clientCommand2.length-1];
						toClient.println("Path seted to " + path + ".");
						toClient.println("$");
						break;
					case "showFiles":
						showDirectories(toClient, path);
						toClient.println("$");
						break;
					case "showAll":
						readDirectories(toClient, path);
						toClient.println("$");
						break;
					case "createDir":
					createDir(toClient, path, clientCommand2);
					toClient.println("$");
						break;
					case "deleteDir":
					deleteDir(toClient, path, clientCommand2);
					toClient.println("$");
						break;
					case "sendFile":
					String fileExtension = fromClient.nextLine();
					saveFile(client, path, fileExtension);						
					toClient.println("$");
						break;
					default:	
						toClient.println("$");
						break;
				}
				

				toClient.flush();
			} while (!clientCommand.equals("exit"));


		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server terminated.");
	}

	private void deleteDir(PrintWriter toClient, String path, String[] clientCommand2) {
		File root = new File (path +"/"+ clientCommand2[clientCommand2.length-1]);
		if(root.isDirectory()){
			if(root.list().length>0){
				toClient.println("Directory is not empty!");
				toClient.println("$");
			}else{	
				root.delete();
			}
		}
		toClient.println("$");
	}

	private void createDir(PrintWriter toClient, String path, String[] clientCommand2) {
		File root = new File (path +"/"+ clientCommand2[clientCommand2.length-1]);
		root.mkdirs();
		toClient.println("$");
	}

	public void readDirectories(PrintWriter toClient, String path) {
		try {
			File root = new File(path);
			
			showSubDirectories(root,"",toClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showSubDirectories(File input, String indentation,PrintWriter toClient) {
		toClient.println(indentation + input.getAbsolutePath());	
		if(input.isDirectory()) {
			for(File file : input.listFiles()) 
				showSubDirectories(file, indentation + " ",toClient);
		}
	}
	
	private void showDirectories(PrintWriter toClient, String path) {
		File root = new File(path);
		for(File file : root.listFiles()) {
			toClient.println(file.getAbsolutePath());
		}
	}
	private void saveFile(Socket clientSock, String path, String fileExtension) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream(path + "/NewFile." + fileExtension);
		byte[] buffer = new byte[4096];
		
		int filesize = 15123; 
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		dis.close();
	}

}

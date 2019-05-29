package AEP2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class App {
	public static void main(String[] args) {
		long inicio = System.currentTimeMillis();
		File diretorio = new File("C:/Windows/System32");
			for (File file : diretorio.listFiles()) {
				if (file.getName().endsWith("dll")) {
					try {
						InputStream in = new BufferedInputStream(new FileInputStream(file.getPath()),40096);
						OutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("D:/dll/" + file.getName() +".gz" )),40096);
						int dado = in.read();
						while (dado != -1) {
							out.write(dado);
							dado = in.read();
						}
						out.close();
						in.close();
						System.out.println("Tempo total: " + (System.currentTimeMillis()-inicio));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
				
				
				
				
		
	}
}

/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.fileserver;
/** @author G3OVA */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private static final int PORT = 1234; // Puerto para escuchar conexiones

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor esperando conexiones en el puerto " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                     FileOutputStream fos = new FileOutputStream("archivo_recibido")) {

                    System.out.println("Conexion establecida: " + clientSocket.getInetAddress());

                    long fileSize = dis.readLong();
                    byte[] buffer = new byte[4096];

                    int read = 0;
                    long totalRead = 0;
                    while (totalRead < fileSize && (read = dis.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                        totalRead += read;
                    }

                    System.out.println("Archivo recibido con éxito.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
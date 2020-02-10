/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.servidor.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Sophia
 */
public class Servidor {
    
    private final List<PrintWriter> printWriters = new ArrayList<>();
    
    private void enviarMensagens(String mensagem) {
        for (PrintWriter printWriter : printWriters) {
            printWriter.println(mensagem);
            printWriter.flush();
        }
    }
    
    public Servidor() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(3000);
            while (true) {
                Socket socket = serverSocket.accept();
                printWriters.add(new PrintWriter(socket.getOutputStream()));
                new Thread(new ThreadCliente(socket)).start();
            }
        } catch (IOException ex) {}
    }
    
    private class ThreadCliente implements Runnable {
        
        private Scanner scanner;
        
        public ThreadCliente(Socket socket) {
            try {
                scanner = new Scanner(socket.getInputStream());
            } catch (IOException ex) {}
        }
        
        @Override
        public void run() {
            String mensagem;
            try {
            while ((mensagem = scanner.nextLine()) != null) {
                enviarMensagens(mensagem);
            }
            } catch (NoSuchElementException n) {}
        }
    }
}
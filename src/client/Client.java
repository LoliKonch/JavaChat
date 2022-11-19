package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String age;
    private String phoneNumber;

    public Client(Socket socket, String username) {

        try {

            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void startClient() {
        listenForMessage();
        sendMessage();
    }

    public void sendMessage() {

        try {

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {

                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            System.out.println("Ошибка отправки сообщения");
        } finally {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void listenForMessage() {
        // создание и запуск нового потока для получения сообщений
        new Thread(new Runnable() { // реализация анонимного класса
            @Override
            public void run() {

                String messageFromChat;

                while (socket.isConnected()) {
                    try {

                        messageFromChat = bufferedReader.readLine();
                        System.out.println(messageFromChat);

                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }

            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {

            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String username = null;
        boolean flag = true;

        while (flag) {
            System.out.println("Введите свой ник для чата: ");
            username = scanner.nextLine();

            if (username != null && !username.equals("")) {
                flag = false;
            }
        }


        Socket socket = new Socket("localhost", 9090);
        Client client = new Client(socket, username);
        client.startClient();

    }
}
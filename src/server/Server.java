package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Класс сервера со свойством <b>serverSocket</b>.
 *
 * @author Kirill Chezlov
 * @version 0.1
 */
public class Server {

    private ServerSocket serverSocket;

    /**
     * Конструктор класса server.Server.
     * @param serverSocket
     */
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Метод <b>startServer</b> запускает сервер и ожидает подключения к себе.
     * После этого создается и запускается новый поток для работы с
     * новоподключившимся клиентом.
     */
    public void startServer() {

        System.out.println("Сервер запущен...");

        try {

            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("Клиент " + socket.getInetAddress() + ":" +
                        socket.getPort() + " подключился к серверу");

                Thread newClientThread = new Thread(new ClientHandler(socket));
                newClientThread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeServer() {

        try {

            if (serverSocket != null) {
                serverSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9090);
        Server server = new Server(serverSocket);
        server.startServer();

    }
}

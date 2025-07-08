import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainProgram {
    private ServerSocket serverSocket;
    private int port;
    private Socket playerOne;
    private Socket playerTwo;
    private int connectionCount = 0;
    public MainProgram(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(this.port);


        new Thread(() -> {
            try {
                acceptConnections();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    private void acceptConnections() throws IOException {
        if (connectionCount == 2) return;
        this.connectionCount ++;

        while (true){
            Socket socket  = serverSocket.accept();

            new Thread(() -> {
                try {
                    recieveMessages(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    /**
     * Method to listen for messages from the players
     */
    private void recieveMessages(Socket socket) throws IOException {
        System.out.println("CONNECTED");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        while (true){
            String resp = in.readLine();
//            System.out.println(resp);
//
            handleMessages(resp);
        }
    }

    /**
     * Method to handle messages that we get from the clients
     *
     * @param message
     */
    private void handleMessages(String message){
        System.out.println("received a message " + message);
        switch (message){
            case "d":
                break;
            case "Hello":
                System.out.println("MADE IT");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello world");

        MainProgram mp = new MainProgram(7000);
        Player player1 = new Player("JEFF", 7000, "localhost");
        Player player2 = new Player("HAM", 7000, "localhost");
    }
}

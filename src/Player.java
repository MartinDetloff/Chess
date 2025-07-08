import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    private String name;
    private int port;
    private String host;
    private boolean isFirst;
    private boolean isMyTurn = false;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public Player(String name, int port, String host) throws IOException {
        this.name = name;
        this.port = port;
        this.host = host;
        this.isFirst = true;
        connect2Server();
        sendMessage();
    }

    private void connect2Server() throws IOException {
        this.client = new Socket("localhost",7000);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void sendMessage(){
        out.println("Hello");
    }




}

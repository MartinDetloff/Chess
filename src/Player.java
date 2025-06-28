public class Player {

    private String name;
    private int port;
    private String host;
    private boolean isFirst;
    private boolean isMyTurn = false;

    public Player(String name, int port, String host){
        this.name = name;
        this.port = port;
        this.host = host;
        this.isFirst = true;
    }


}

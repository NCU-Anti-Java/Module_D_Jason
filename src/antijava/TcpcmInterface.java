package antijava;

import java.net.InetAddress;

public interface TcpcmInterface {
    boolean connectServer(InetAddress serverip);
    void inputMoves(int MoveCode);
}

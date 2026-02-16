import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

final class DataConnectionManager {
    private final InetAddress localAddress;
    private ServerSocket passiveServer;
    private String activeHost;
    private int activePort;

    DataConnectionManager(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    synchronized String openPassive() throws IOException {
        closePassive();
        passiveServer = new ServerSocket(0, 1, localAddress);
        int p = passiveServer.getLocalPort();
        byte[] addrBytes = localAddress.getAddress();
        int e = (p >> 8) & 0xFF;
        int f = p & 0xFF;
        return String.format("%d,%d,%d,%d,%d,%d",
                addrBytes[0] & 0xFF,
                addrBytes[1] & 0xFF,
                addrBytes[2] & 0xFF,
                addrBytes[3] & 0xFF,
                e, f);
    }

    synchronized void setActiveEndpoint(String host, int port) {
        activeHost = host;
        activePort = port;
    }

    synchronized Socket openDataSocket() throws IOException {
        if (passiveServer != null) {
            return passiveServer.accept();
        }
        if (activeHost != null && activePort > 0) {
            return new Socket(activeHost, activePort);
        }
        return null;
    }

    synchronized void closePassive() {
        if (passiveServer != null) {
            try {
                passiveServer.close();
            } catch (IOException ex) {
            }
            passiveServer = null;
        }
    }

    synchronized void clearActive() {
        activeHost = null;
        activePort = -1;
    }

    synchronized void closeAll() {
        closePassive();
        clearActive();
    }
}

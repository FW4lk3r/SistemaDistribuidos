import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;

public interface StorageInterface  extends Remote {

    void ReceivedFiles(String filename, byte[] array, int size) throws IOException;
    void addFile(String path) throws IOException, NotBoundException;
}

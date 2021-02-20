import java.io.IOException;
import java.rmi.Remote;

public interface StorageInterface  extends Remote {

    void ReceivedFiles(String filename, byte[] array, int size) throws IOException;

}

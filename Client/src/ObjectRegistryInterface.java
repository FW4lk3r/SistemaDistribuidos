import com.sun.org.apache.xpath.internal.operations.String;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectRegistryInterface extends Remote {

    String getObject(java.lang.String s);

    void addObject(java.lang.String objectId, String ServerAddress) throws RemoteException;

    int getNumServers(String serverType) throws RemoteException;
}

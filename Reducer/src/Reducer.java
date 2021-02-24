import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Reducer extends UnicastRemoteObject implements ReducerInterface {

    private Integer port;
    private static LinkedHashMap<String, ArrayList<ResourceInfo>> data = new LinkedHashMap<>();

    protected Reducer(Integer port) throws RemoteException {
        this.port = port;
    }

    public void getDataFromReducers(LinkedHashMap<String, ArrayList<ResourceInfo>> data){
        this.data = data;
    }
}

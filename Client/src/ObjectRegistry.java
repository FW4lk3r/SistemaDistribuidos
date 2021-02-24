import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ObjectRegistry extends UnicastRemoteObject implements ObjectRegistryInterface {
    private static final long serialVersionUID = 1l;
    static Map<Integer, String> hashMap = new TreeMap<>();
    static int length = 2;

    protected ObjectRegistry() throws RemoteException {
    }

    /**
     * @description Add a server address to the hashmap Map.
     * @param objectId String
     * @param ServerAddress String
     * @throws RemoteException RemoteException
     */
    public void addObject(String objectId, String ServerAddress) throws RemoteException{
        hashMap.put(Integer.parseInt(objectId), ServerAddress);
    }

    /**
     * @description Return the RMI address given yours ID.
     * @param objectId String
     * @return
     */
    public String getObject(String objectId){
        return hashMap.get(Integer.parseInt(objectId));
    }

    /***
     * @description Return the number of servers of a type ex. mapper, reducer
     * @param serverType String
     * @throws RemoteException
     */
    public int getNumServers(String serverType) throws RemoteException{

        int numberOfServers = 0;

        ArrayList<Integer> keys = new ArrayList<>(hashMap.keySet());

        for(int i = keys.size()-1; i >= 0; i--){
            if(hashMap.get(keys.get(i)).contains(serverType)){
                numberOfServers++;
            }
        }

        return numberOfServers;
    }

}
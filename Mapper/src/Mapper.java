import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    private ArrayList<ResourceInfo> Mapper = new ArrayList<>();
    private static ObjectRegistryInterface objRegInt;
    private Integer port;

    protected Mapper(Integer port) throws RemoteException
    {
        this.port = port;
    }

    public void ReceiveStorageFromMaster(ArrayList<ResourceInfo> storageData) {
        System.out.println("Mapper(" + this.port + ") has receive the data.");
        Mapper = storageData;
    }

    public void sendReducer(ArrayList<Integer> data, String registryAddress) throws RemoteException, NotBoundException, MalformedURLException {
        objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

        String portReducer = objRegInt.getObject("2030");
        ReducerInterface reducerInterface = (ReducerInterface) Naming.lookup(portReducer);

        //reducerInterface.receiveFromStorage(data);
    }

    /**
     * Compute statistics for each combination
     * @param len number of combinations
     * @param fileCount number of files (i.e., runs)
     */
    public Set<Set<ResourceInfo>> combinations(int len, int fileCount) {

            ArrayList<ResourceInfo> resources = new ArrayList<>(Mapper);
            System.out.println("Number of resources " + resources.size());
            Set<Set<ResourceInfo>> combinations = Sets.combinations(ImmutableSet.copyOf(resources), len);

            return combinations;
    }
}

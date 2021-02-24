import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    private static ArrayList<ResourceInfo> timerHarMap = new ArrayList<>();
    private Integer port;

    protected Mapper(Integer port) throws RemoteException
    {
        this.port = port;
    }

    public void ReceiveStorageFromMaster(ArrayList<ResourceInfo> storageData) {
        System.out.println("Mapper(" + this.port + ") has receive the data.");
        timerHarMap = storageData;
    }

    /*
    private Set<Set<String>> getCombinations(int len) {
        ArrayList<String> resources= new ArrayList<>(timerHarMap.keySet());

        System.out.println("Number of resources: " + resources.size());
        Set<Set<String>> combinations = Sets.combinations(ImmutableSet.copyOf(resources), len);

        return combinations;
    }*/
}

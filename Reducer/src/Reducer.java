import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Reducer extends UnicastRemoteObject implements ReducerInterface {
    private Integer port;
    Set<Set<String>> dataSet;
    StorageInterface storageInt;
    private static LinkedHashMap<String, ArrayList<ResourceInfo>> data = new LinkedHashMap<>();
    private ArrayList<ProcessCombinationModel> combinationStatistics = new ArrayList<>();

    protected Reducer(Integer port) throws RemoteException {
        this.port = port;
    }

    public void getDataToReducers(LinkedHashMap<String, ArrayList<ResourceInfo>> data){
        this.data = data;
    }

    public void receiveFromStorage(Set<Set<String>> data) throws RemoteException, NotBoundException, MalformedURLException {
        this.dataSet = data;
        processCombinationReducers(dataSet.size());
        sendProcessCombination("rmi://localhost:2023/registry");
    }

    private void sendProcessCombination(String registryAddress) throws RemoteException, MalformedURLException, NotBoundException {
        ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);

        String storageAddress = objRegInt.getObject("2022");

        storageInt = (StorageInterface) Naming.lookup(storageAddress);
        storageInt.ReceivedCombinations(combinationStatistics);
        System.out.println("Data processed and sent to the client.");
    }

    public void processCombinationReducers(Integer fileCount) {
        Set r;
        StringBuffer line = new StringBuffer();
        Iterator combIterator = dataSet.iterator();
        System.out.println("Number of combinations " + dataSet.size());
        int i = 0;
        while (combIterator.hasNext()) {
            r = (Set) combIterator.next();

            line.setLength(0);
            Iterator lineIterator = r.iterator();
            while (lineIterator.hasNext()) {
                if (line.length() > 0) line.append(",");
                line.append(lineIterator.next().toString());
            }

            ProcessCombinationModel combinationInfo = new ProcessCombinationModel();
            combinationInfo.combination = line.toString();
            calculateStatistics(combinationInfo, fileCount);
            if (i++ % 100000 == 0) System.out.println("Comb " + i);
        }
    }


    /**
     * Auxiliary method that calculates statistics for each resource
     * @param combinationInfo single combination of resources
     * @param fileCount number of runs
     */
    public void calculateStatistics(ProcessCombinationModel combinationInfo, int fileCount){
        boolean resourceFound=false;

        String[] resources = combinationInfo.combination.split(","); // resources of each combination
        for (int i =0; i < fileCount; i++) { //controlo por run
            for(String combinationResource : resources){
                resourceFound=false;
                for(ResourceInfo comb : data.get(combinationResource))
                    if(comb.harRun == i) {
                        resourceFound = true;
                        combinationInfo.resourceLength += comb.resourceLength;
                        break;
                    }
                if(! resourceFound){ break;}
            }
            if(resourceFound){
                combinationInfo.numberOfRuns++;
            }
        }

        combinationInfo.percentage = (float) combinationInfo.numberOfRuns/fileCount;

        if(combinationInfo.percentage > 0.5) {
            System.out.print("Combination + probability");
            for(String s: resources) System.out.print(System.identityHashCode(s) + "  ");
            System.out.print(combinationInfo.percentage + "\n");

            this.combinationStatistics.add(combinationInfo);
            System.out.println("Comb valida. Percentagem: " + combinationInfo.percentage);
        }
    }
}

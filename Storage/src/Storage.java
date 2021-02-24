import harreader.HarReader;
import harreader.HarReaderException;
import harreader.model.Har;
import harreader.model.HarEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Storage extends UnicastRemoteObject implements StorageInterface {

    private static LinkedHashMap<Integer, ArrayList<ResourceInfo>> MapperResource = new LinkedHashMap<>();
    private static LinkedHashMap<Integer, ArrayList<ResourceInfo>> ReducerCombinations = new LinkedHashMap<>();
    private static LinkedHashMap<Integer, ArrayList<ResourceInfo>> ResourceInfo = new LinkedHashMap<>();
    private static ArrayList<ProcessCombinationModel> CombinationModel = new ArrayList<>();

    private static ArrayList<ResourceInfo> DataClient = new ArrayList<>();

    private String storageReceivedPath = ".\\Storage\\ReceivedFile\\";

    protected Storage() throws RemoteException {

    }

    @Override
    public void ReceivedFilesClient(String filename, byte[] array, int size, int totalItems) throws IOException, HarReaderException {

        new File(storageReceivedPath).mkdirs();

        File file = new File(storageReceivedPath + filename);

        file.createNewFile();

        FileOutputStream out = new FileOutputStream(file, true);

        out.write(array, 0, size);
        out.flush();
        out.close();

        FillResourcesMap(storageReceivedPath, filename.substring(0, filename.lastIndexOf("_")), ResourceInfo);

        //DivideResources(totalItems);
    }

    /**
     * @description Add the file to the repository
     * @param path
     */
    public void addFile(String path, String storageAddress) throws IOException, NotBoundException {

        System.out.println("Processing the har files.");
        File filepath = new File(path);
        java.lang.String[] contents = filepath.list();
        StorageInterface storage = (StorageInterface) Naming.lookup(storageAddress);

        try {
            for (int i = 0; i < contents.length; i++) {
                File file = new File(path + contents[i]);

                FileInputStream in = new FileInputStream(file);

                byte[] array = new byte[1024 * 1024];
                int size = in.read(array);
                storage.ReceivedFilesClient(contents[i], array, size, contents.length);
                System.out.println(contents[i]);
            }
        } catch (RemoteException e){
            System.out.println(e.getMessage());
        }

        catch(Exception e) { e.printStackTrace(); }

    }

    /**
     * @description Divide the Array List in N Mappers
     *
     */
    public void DivideResources() {

        System.out.println("Starting the division of resources.");

        ArrayList<ResourceInfo> ResourcesOriginals = new ArrayList<>();;
        ArrayList<ResourceInfo> ResourcesCopy = ResourcesOriginals;

        //Getting Collection of keys from HashMap
        Set<ArrayList<ResourceInfo>> test = new HashSet<>(ResourceInfo.values());

        List<ArrayList<ResourceInfo>> listTest = test.stream().collect(Collectors.toList());

        for(int i = 0; i < listTest.size(); i++){
            System.out.println(listTest.get(i));
            //ResourcesCopy.add();
        }
        //ResourcesCopy = listTest;
    }

    @Override
    public ArrayList<ResourceInfo> returnDataStorage() throws RemoteException {
        DivideResources();
        return DataClient;
    }

    public void ReceivedCombinations(ArrayList<ProcessCombinationModel> combinationStatistics){
        CombinationModel = combinationStatistics;
    }

    public void FillResourcesMap(String path, String fileName, LinkedHashMap<Integer, ArrayList<ResourceInfo>> ResourceInfo) throws HarReaderException {
        int[] count = new int[]{0};
        int fileCount = 0;
        try {
            HarReader harReader = new HarReader();
            File file = new File(path + fileName + ".har");
            while (file.exists()){
                Har otherHar = harReader.readFromFile(file);
                for (HarEntry otherEntry : otherHar.getLog().getEntries()) {
                    if (!otherEntry.getResponse().getHeaders().get(0).getValue().contains("no-cache")) {
                        ResourceInfo resourceInfo = new ResourceInfo();
                        resourceInfo.resourceTime = (float) otherEntry.getTime();
                        resourceInfo.resourceType = otherEntry.get_resourceType();
                        resourceInfo.cachedResource = otherEntry.getResponse().getHeaders().get(0).getValue();
                        resourceInfo.resourceLength = otherEntry.getResponse().getBodySize();
                        resourceInfo.harRun = fileCount;

                        if (ResourceInfo.containsKey(otherEntry.getRequest().getUrl())) {
                            ArrayList<ResourceInfo> list = ResourceInfo.get(otherEntry.getRequest().getUrl());
                            AtomicBoolean repeatedCall = new AtomicBoolean(false);
                            list.forEach(value -> {
                                if (value.resourceTime.equals(resourceInfo.resourceTime)) {
                                    repeatedCall.set(true);
                                    return;
                                }
                            });
                            if (!repeatedCall.get())
                                ResourceInfo.get(otherEntry.getRequest().getUrl()).add(resourceInfo);
                        } else {
                            ArrayList<ResourceInfo> l = new ArrayList<>();
                            l.add(resourceInfo);
                            ResourceInfo.put(Integer.parseInt(otherEntry.getRequest().getUrl()), l);
                        }

                    }
                }
                file = new File(path + fileName + "_" + ++fileCount +".har");
            }
        } catch (Exception ex) {
            // e.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

}

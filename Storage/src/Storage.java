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

public class Storage extends UnicastRemoteObject implements StorageInterface {

    private static ArrayList<Integer> MapperResource = new ArrayList<>();
    private static ArrayList<ResourceInfo> ResourceInfoCopy = new ArrayList<>();
    private static LinkedHashMap<Integer, ArrayList<ResourceInfo>> ReducerCombinations = new LinkedHashMap<>();
    private static LinkedHashMap<String, ArrayList<ResourceInfo>> ResourceInfoData = new LinkedHashMap<>();
    private static ArrayList<ProcessCombinationModel> CombinationModel = new ArrayList<>();

    private static ArrayList<Integer> DataClient = new ArrayList<>();

    private String storageReceivedPath = ".\\Storage\\ReceivedFile\\";

    protected Storage() throws RemoteException {

    }

    @Override
    public void ReceivedFilesClient(String filename, byte[] array, int size, int totalItems) throws IOException, HarReaderException {

        new File(storageReceivedPath).mkdirs();

        File file = new File(storageReceivedPath + filename);

        //System.out.println("File name " + file.getName());

        file.createNewFile();

        FileOutputStream out = new FileOutputStream(file, true);

        out.write(array, 0, size);
        out.flush();
        out.close();

        FillResourcesMap(storageReceivedPath, filename.substring(0, filename.lastIndexOf("_")));

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
     * @return
     */
    public ArrayList<Integer> DivideResources() {

        System.out.println("Starting the division of resources.");
        System.out.println("Resource Info size:" + ResourceInfoData.size());

        /*Integer sizeResourceInfo = ResourceInfoData.size();
        Integer numberItem = sizeResourceInfo / 2;
        Boolean isPar =  (sizeResourceInfo % 2) != 0;

        Integer count = 0;

        for(int i = 0; i < sizeResourceInfo; i++){

            for(int j = 0; j < numberItem; j++) {
                System.out.println("Set data to the mapper:" + i);
                MapperResource.put(i, ResourceInfoData.get(count));
                count++;
            }

        }
        if(!isPar) {
            MapperResource.put((Integer) MapperResource.keySet().toArray()[1], ResourceInfoData.get(sizeResourceInfo));
        }*/

        /*ArrayList<ResourceInfo> ResourcesOriginals = new ArrayList<>();
        ArrayList<ResourceInfo> ResourcesCopy = ResourcesOriginals;

        //Getting Collection of keys from HashMap
        Set<ArrayList<ResourceInfo>> test = new HashSet<>(ResourceInfoData.values());

        List<ArrayList<ResourceInfo>> listTest = test.stream().collect(Collectors.toList());


        for (Map.Entry me : ResourceInfoData.entrySet()) {
            System.out.println("Key: "+me.getKey() + " & Value: " + me.getValue());
            ResourcesCopy.addAll((Collection<? extends ResourceInfo>) me.getValue());
        }*/

        ArrayList<Integer> resourcePorMapper = new ArrayList<>();
        int numMappers= 2;
        int numResources = ResourceInfoData.size();

        int resourcesPerMapper = (numResources / numMappers);
        int remainingResources = (numResources % 2);

        for (int i = 1; i <= numMappers; i++)
        {
            int extra = (i <= remainingResources) ? 1:0;
            resourcePorMapper.add((resourcesPerMapper + extra));
        }

        for(int i=0;i<resourcePorMapper.size();i++){
            System.out.println("\n <<-- Mapper " + i + " with "+ resourcePorMapper.get(i) +" resources");
        }

        MapperResource = resourcePorMapper;

        return MapperResource;
    }

    @Override
    public ArrayList<Integer> returnDataStorage() throws RemoteException {
        DataClient = DivideResources();
        return DataClient;
    }

    public void ReceivedCombinations(ArrayList<ProcessCombinationModel> combinationStatistics) throws RemoteException{
        CombinationModel = combinationStatistics;
    }

    public LinkedList<ProcessCombinationModel> getCombinationResults() {
        return null;
    }

    public void FillResourcesMap(String path, String fileName) throws HarReaderException {
        int[] count = new int[]{0};
        int fileCount = 0;
        try {
            HarReader harReader = new HarReader();
            File file = new File(path + fileName + ".har");
            while (file.exists()){
                //System.out.println("Enter in the while");
                Har otherHar = harReader.readFromFile(file);
                for (HarEntry otherEntry : otherHar.getLog().getEntries()) {
                    if (!otherEntry.getResponse().getHeaders().get(0).getValue().contains("no-cache")) {
                        ResourceInfo resourceInfo = new ResourceInfo();
                        resourceInfo.resourceTime = (float) otherEntry.getTime();
                        resourceInfo.resourceType = otherEntry.get_resourceType();
                        resourceInfo.cachedResource = otherEntry.getResponse().getHeaders().get(0).getValue();
                        resourceInfo.resourceLength = otherEntry.getResponse().getBodySize();
                        resourceInfo.harRun = fileCount;

                        if (ResourceInfoData.containsKey(otherEntry.getRequest().getUrl())) {
                            ArrayList<ResourceInfo> list = ResourceInfoData.get(otherEntry.getRequest().getUrl());
                            AtomicBoolean repeatedCall = new AtomicBoolean(false);
                            list.forEach(value -> {
                                if (value.resourceTime.equals(resourceInfo.resourceTime)) {
                                    repeatedCall.set(true);
                                    return;
                                }
                            });
                            if (!repeatedCall.get())
                                ResourceInfoData.get(otherEntry.getRequest().getUrl()).add(resourceInfo);
                        } else {
                            ArrayList<ResourceInfo> l = new ArrayList<>();
                            l.add(resourceInfo);
                            //System.out.println("Entry: " + otherEntry.getRequest().getUrl());
                            ResourceInfoData.put(otherEntry.getRequest().getUrl(), l);
                            ResourceInfoCopy.add(resourceInfo);
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

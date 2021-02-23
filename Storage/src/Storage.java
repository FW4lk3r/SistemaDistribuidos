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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Storage extends UnicastRemoteObject implements StorageInterface {

    private static LinkedHashMap<String, ArrayList<ResourceInfo>> clientData = new LinkedHashMap<>();
    private String storageReceivedPath = ".\\Storage\\ReceivedFile\\";

    protected Storage() throws RemoteException {

    }

    public  LinkedHashMap<String, ArrayList<ResourceInfo>> clientDataMap(){
        return clientData;
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

        LoadToMemory(totalItems, storageReceivedPath);

        DivideResources(totalItems);
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
     * @param length total of files
     */
    public void DivideResources(Integer length) {

        HashMap<Integer, ArrayList<ResourceInfo>> halfHashMap = new HashMap<>();
        HashMap<Integer, ArrayList<ResourceInfo>> halfHashMap2 = new HashMap<>();

        for(int i = 0; i < clientData.size(); i++) {
            ( i < (clientData.size()/length) ? halfHashMap:halfHashMap2)
                    .put(Integer.parseInt(clientData.keySet().toString()), clientData.get(i));

            System.out.println("ID of Resources:" + Integer.parseInt(clientData.keySet().toString()));
        }
    }

    private void LoadToMemory(int length, String receivedFilePath) throws HarReaderException {
        String fileName = "www_nytimes_com";
        File filepath = new File(receivedFilePath);
        String content[] = filepath.list();

        if(length == 76)
        {
            FillResourcesMap(receivedFilePath, fileName, clientData);
            System.out.println("Data loaded to memory by Storage(FilleResourcesMap)");
        }
    }

    public void FillResourcesMap(String path, String fileName, LinkedHashMap<String, ArrayList<ResourceInfo>> timeHarMap) throws HarReaderException {
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

                        if (timeHarMap.containsKey(otherEntry.getRequest().getUrl())) {
                            ArrayList<ResourceInfo> list = timeHarMap.get(otherEntry.getRequest().getUrl());
                            AtomicBoolean repeatedCall = new AtomicBoolean(false);
                            list.forEach(value -> {
                                if (value.resourceTime.equals(resourceInfo.resourceTime)) {
                                    repeatedCall.set(true);
                                    return;
                                }
                            });
                            if (!repeatedCall.get())
                                timeHarMap.get(otherEntry.getRequest().getUrl()).add(resourceInfo);
                        } else {
                            ArrayList<ResourceInfo> l = new ArrayList<>();
                            l.add(resourceInfo);
                            timeHarMap.put(otherEntry.getRequest().getUrl(), l);
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

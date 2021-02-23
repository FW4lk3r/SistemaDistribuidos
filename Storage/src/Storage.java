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
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Storage extends UnicastRemoteObject implements StorageInterface {

    protected Storage() throws RemoteException {

    }

    @Override
    public void ReceivedFiles(String filename, byte[] array, int size) throws IOException {

        String filepath = ".\\Storage\\ReceivedFile\\";

        new File(filepath).mkdirs();

        File file = new File(filepath + filename);

        file.createNewFile();

        FileOutputStream out = new FileOutputStream(file, true);

        out.write(array, 0, size);
        out.flush();
        out.close();
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
                storage.ReceivedFiles(contents[i], array, size);
                System.out.println(contents[i]);
            }
        } catch (RemoteException e){
            System.out.println(e.getMessage());
        }

        catch(Exception e) { e.printStackTrace(); }

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

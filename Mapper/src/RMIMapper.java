import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIMapper {

    public static void main(RMIClient.TYPECLASS mapperTypeClass, String registryAddress, Integer dynamicPort) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(dynamicPort);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Mapper mapper = new Mapper(dynamicPort);
            r.rebind(mapperTypeClass.toString(), mapper);

            ObjectRegistryInterface objRegInt = (ObjectRegistryInterface) Naming.lookup(registryAddress);
            objRegInt.addObject(String.valueOf(dynamicPort), "rmi://localhost:" + dynamicPort + "/" + mapperTypeClass);

            System.out.println(mapperTypeClass.toString().toUpperCase() + " ready with port:" + dynamicPort);
        }catch(Exception e) {
            System.out.println(mapperTypeClass.toString().toUpperCase() + " main " + e.getMessage());
        }
    }
}

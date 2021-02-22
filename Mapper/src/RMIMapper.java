import com.sun.org.apache.xpath.internal.operations.String;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIMapper {

    public static void main(String[] strings) {
        Registry r = null;

        try{
            r = LocateRegistry.createRegistry(2023);
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            Mapper mapper = new Mapper();
            r.rebind("mapper", mapper);

            System.out.println("Mapper ready");
        }catch(Exception e) {
            System.out.println("Mapper main " + e.getMessage());
        }
    }
}

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Mapper extends UnicastRemoteObject implements MapperInterface {

    protected Mapper() throws RemoteException
    {

    }

    /**
     * @description Add to the hashmap the respective Id
     * and Object divided by the total of mappers
     * @param length
     */
    public void FillDataTableOfFiles(int length) {
        //TODO: add object divided with respective id in the hashmap


        //TODO: Call the Reducer
    }

    public void DivideObjectsFilesFromTotalMappers() {

    }
}

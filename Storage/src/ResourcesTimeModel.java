import java.util.ArrayList;

public class ResourcesTimeModel {
    public String resourceType;
    public String firstRsrc;
    public String secondRsrc;
    ArrayList<Float> diffResourceTimes = new ArrayList<Float>();
    public float diffResourceTime;
    public float diffResourceTime2;
    public double median;
    public float percentil_cinco;
    public float percentil_noventaCinco;
    public float probability;

    public ResourcesTimeModel() {
    }
}

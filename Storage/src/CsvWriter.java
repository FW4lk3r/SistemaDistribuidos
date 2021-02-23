import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class CsvWriter {
    private String klmResult;

    public CsvWriter(){

    }

    public void SaveKLMString(String klmResult){
        this.klmResult = klmResult;
    }

    public void SaveStatistics (ArrayList<OperatorsStatistics> operatorsStatistics, float wOperator, String fileName){
        try {
            double totaltime = 0.0;
            // PrintWriter writer = new PrintWriter(new File("/Users/ruipedroduarte/IdeaProjects/webbench/src//main/javascript/files/DataToCharts.csv"));
            PrintWriter writer = new PrintWriter(new File("/Users/cunha/Downloads/webbench/src/main/java/files/" + fileName + "DataToCharts.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("Operator");
            sb.append(';');
            sb.append("Number of Interactions");
            sb.append(';');
            sb.append("Percentage");
            sb.append(';');
            sb.append("TimePerOperator");
            sb.append('\n');
            for(int i=0; i <operatorsStatistics.size(); i++) {
                sb.append(operatorsStatistics.get(i).operator + ";");
                sb.append(operatorsStatistics.get(i).count + ";");
                //sb.append(operatorsStatistics.get(i).percentage + ";");
                sb.append((double) Math.round(operatorsStatistics.get(i).percentage*100)/100 + ";");
                sb.append((double) Math.round(operatorsStatistics.get(i).timePerOperator*100)/100);
                sb.append('\n');
                totaltime += operatorsStatistics.get(i).timePerOperator;
            }
            // Change this code later!!
            sb.append("W" + ";");
            sb.append(0 + ";");
            sb.append(0.0 + ";");
            sb.append((double) Math.round(wOperator*100)/100);
            sb.append('\n');
            //----------------------------------
            sb.append((double) Math.round((wOperator + totaltime)*100)/100);
            sb.append('\n');
            sb.append(klmResult);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void SaveDiffResourcesTimes (ArrayList<ResourcesTimeModel> resourcesTimeModels, String fileName, String basePath){
        try {
            PrintWriter writer = new PrintWriter(new File(basePath + fileName + "SistemaDistribuidos2021.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("First Resource");
            sb.append(';');
            sb.append("Second Resource");
            sb.append(';');
            sb.append("Resource type");
            sb.append(';');
           // sb.append("Time difference between them");
           // sb.append(';');
           // sb.append("Second Time difference between them");
           // sb.append(';');
            sb.append("Median between them");
            sb.append(';');
            sb.append("Percentile 5 between them");
            sb.append(';');
            sb.append("Percentile 95 between them");
            sb.append(';');
            sb.append("Resource Probability");
            sb.append('\n');
            resourcesTimeModels.forEach(resource -> {
                sb.append(resource.firstRsrc + ";");
                sb.append(resource.secondRsrc + ";");
                sb.append(resource.resourceType + ";");
                // sb.append(resource.diffResourceTime + ";");
                // sb.append(resource.diffResourceTime2 + ";");
                sb.append(Math.round(resource.median) + ";");
                sb.append(Math.round(resource.percentil_cinco) + ";");
                sb.append(Math.round(resource.percentil_noventaCinco) + ";");
                sb.append(resource.probability);
                sb.append('\n');
            });
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void SaveResourcesCombinationsProbabilities (LinkedList<ProcessCombinationModel> resourcesCombinationList, String fileName){
        try {
            PrintWriter writer = new PrintWriter(new File("/Users/cunha/Downloads/webbench/src/main/java/files/" + fileName + "ResourcesCombinationProb.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("Combination");
            sb.append(';');
            sb.append("Probability");
            sb.append(';');
            sb.append("CombinationLength");
            sb.append('\n');
            for(ProcessCombinationModel combinationInfo : resourcesCombinationList){
                sb.append(combinationInfo.combination + ";");
                sb.append((double) Math.round(combinationInfo.percentage) + ";");
                sb.append(combinationInfo.resourceLength + ";");
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}

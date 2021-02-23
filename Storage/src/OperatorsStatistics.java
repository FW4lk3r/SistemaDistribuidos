public class OperatorsStatistics {
    public String operator;
    public int count;
    public float percentage;
    public float timePerOperator;

    public OperatorsStatistics(String operator, int count, float percentage, float timePerOperator) {
        this.operator = operator;
        this.count = count;
        this.percentage = percentage;
        this.timePerOperator = timePerOperator;
    }

    public OperatorsStatistics() {

    }
}

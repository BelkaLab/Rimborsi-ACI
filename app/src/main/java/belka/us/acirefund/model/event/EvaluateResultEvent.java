package belka.us.acirefund.model.event;

/**
 * Created by fabriziorizzonelli on 06/10/2016.
 */
public class EvaluateResultEvent {
    private double evaluation;
    private double kmCost;

    public EvaluateResultEvent(double evaluation, double kmCost) {
        this.evaluation = evaluation;
        this.kmCost = kmCost;
    }

    public double getEvaluation() { return evaluation; }

    public double getKmCost() { return kmCost; }
}

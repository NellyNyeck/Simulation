package environment;

public class CEdge implements IEdge {

    private double weight;

    public CEdge(){
        weight=1;
    }
    @Override
    public double weight() {
        return weight;
    }
}

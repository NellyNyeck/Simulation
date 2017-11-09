package environment;

public class CTransformer<CEdge, Double> {
    public Object transform(IEdge iEdge) {
        return iEdge.weight();
    }
}

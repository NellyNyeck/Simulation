package environment;

public class CTransformer implements ITransformer {
    @Override
    public Object transform(IEdge iEdge) {
        return iEdge.weight();
    }
}

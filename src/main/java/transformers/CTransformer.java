package transformers;

import environment.CEdge;

public class CTransformer implements ITransformer<CEdge, Double>
{
    @Override
    public Double transform(CEdge edge) {
        return edge.weight();
    }
}

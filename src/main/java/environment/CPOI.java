package environment;

import java.util.Collection;

public class CPOI implements IPOI, INode {

    private CNode node;

    private Collection<ILabel> labels;

    public CPOI(CNode n){
        node=n;
    }

    @Override
    public Collection<ILabel> labels() {
        return labels;
    }

    @Override
    public Object id() {
        return node;
    }
}

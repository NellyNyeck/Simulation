package environment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Point of interest class
 */

public class CPOI implements INode<CNode>
{

    private CNode m_node;

    private Collection<ILabel> m_labels = new ArrayList<>();

    public CPOI( final CNode p_node )
    {
        m_node = p_node;
    }

    public Collection<ILabel> labels()
    {
        return m_labels;
    }

    @Override
    public CNode id()
    {
        return m_node;
    }

    @Override
    public Double xcoord()
    {
        return m_node.xcoord();
    }

    @Override
    public Double ycoord()
    {
        return m_node.ycoord();
    }
}

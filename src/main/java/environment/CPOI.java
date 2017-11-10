package environment;

import java.util.Collection;

/**
 * The Point of interest class
 */

public class CPOI implements IPOI, INode
{

    private INode m_node;

    private Collection<ILabel> m_labels;

    public CPOI( final INode n )
    {
        m_node = n;
    }

    @Override
    public Collection<ILabel> labels()
    {
        return m_labels;
    }

    @Override
    public Object id()
    {
        return m_node;
    }
}

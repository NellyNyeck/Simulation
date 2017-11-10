package environment;

import java.util.Collection;

/**
 * The Point of interest class
 */

public class CPOI implements IPOI
{

    private INode<?> m_node;

    private Collection<ILabel> m_labels;

    public CPOI( final INode<?> n )
    {
        m_node = n;
    }

    @Override
    public Collection<ILabel> labels()
    {
        return m_labels;
    }
    
    public INode<?> id()
    {
        return m_node;
    }
}

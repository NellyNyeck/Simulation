package environment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Point of interest class
 */

public class CPOI implements IPOI
{

    private CNode m_node;

    private Collection<ILabel> m_labels = new ArrayList<>();

    public CPOI( final CNode n )
    {
        m_node = n;
    }

    @Override
    public Collection<ILabel> labels()
    {
        return m_labels;
    }

    public CNode id()
    {
        return m_node;
    }
}

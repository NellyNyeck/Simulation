package environment;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The Point of interest class
 */

public class CPOI implements INode<String>
{

    private CNode m_node;

    private Collection<ILabel> m_labels = new ArrayList<>();

    public CPOI( final CNode p_node )
    {
        m_node = p_node;
    }

    public void description( final Collection<ILabel> p_labels )
    {
        m_labels = p_labels;
    }

    public Collection<ILabel> labels()
    {
        return m_labels;
    }

    @Override
    public String name()
    {
        return m_node.name();
    }

    public Double firstCoord()
    {
        return m_node.firstCoord();
    }

    public Double secondCoord()
    {
        return m_node.secondCoord();
    }
}

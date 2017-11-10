package environment;

/**
 * The node class
 */
public class CNode implements INode<Integer>
{

    private Integer m_id;

    public CNode( final int i )
    {
        m_id = i;
    }

    @Override
    public Integer id()
    {
        return m_id;
    }

}

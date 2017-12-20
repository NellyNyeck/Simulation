package environment;




/**
 * The node class
 */
public class CNode implements INode<String>
{

    private String m_name;
    private Double m_1coord;
    private Double m_2coord;

    /**
     * Constructor
     */
    public CNode(  )
    {

    }

    public Double firstCoord()
    {
        return m_1coord;
    }

    public Double secondCoord()
    {
        return m_2coord;
    }

    @Override
    public String name()
    {
        return m_name;
    }

}

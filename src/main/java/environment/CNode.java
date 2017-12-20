package environment;



import org.json.simple.JSONObject;


/**
 * The node class
 */
public class CNode implements INode<String>
{

    private String m_name;
    private Double m_fcoord;
    private Double m_scoord;


    /**
     * Constructor
     */
    public CNode( final JSONObject p_json )
    {
        m_name = (String) p_json.get( "name" );
        final JSONObject l_object = (JSONObject) p_json.get( "coordinates" );
        m_fcoord = (Double) l_object.get( "first coordinate" );
        m_scoord = (Double) l_object.get( "second coordinate" );

    }

    public Double firstCoord()
    {
        return m_fcoord;
    }

    public Double secondCoord()
    {
        return m_scoord;
    }

    @Override
    public String name()
    {
        return m_name;
    }


}

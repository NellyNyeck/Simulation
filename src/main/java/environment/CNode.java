package environment;



import org.json.simple.JSONObject;


/**
 * The node class
 */
public class CNode implements INode<String>
{

    private String m_name;
    private Long m_fcoord;
    private Long m_scoord;


    /**
     * Constructor
     */
    public CNode( final JSONObject p_json )
    {
        m_name = (String) p_json.get( "name" );
        final JSONObject l_object = (JSONObject) p_json.get( "coordinates" );
        m_fcoord = (Long) l_object.get( "first coordinate" );
        m_scoord = (Long) l_object.get( "second coordinate" );

    }

    public Long firstCoord()
    {
        return m_fcoord;
    }

    public Long secondCoord()
    {
        return m_scoord;
    }

    @Override
    public String name()
    {
        return m_name;
    }


}

package environment;

import org.json.simple.JSONObject;


/**
 * The node class
 */
public class CNode implements INode<String>
{

    private String m_id;
    private Double m_1coord;
    private Double m_2coord;

    /**
     * Constructor
     * @param p_json given json object to build the node
     */
    public CNode( final JSONObject p_json )
    {
        m_id = (String) p_json.get( "name" );
        final JSONObject l_object = (JSONObject) p_json.get( "coordinates" );
        m_1coord = (Double) l_object.get( "first coordinate" );
        m_2coord = (Double) l_object.get( "second coordinate" );

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
    public String id()
    {
        return m_id;
    }

}

package environment;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * The node class
 */
public class CNode implements INode<Integer>
{

    private Integer m_id;
    private Double m_xcoord;
    private Double m_ycoord;

    /**
     * Constructor
     * @param p_json given json object to build the node
     */
    public CNode( final JSONObject p_json )
    {
        try
        {
            m_id = p_json.getInt( "id" );
            m_xcoord = p_json.getDouble( "x" );
            m_ycoord = p_json.getDouble( "y" );

        }
        catch ( final JSONException l_err )
        {
            l_err.printStackTrace();
        }

    }

    public Double xcoord()
    {
        return m_xcoord;
    }

    public Double ycoord()
    {
        return m_ycoord;
    }

    @Override
    public Integer id()
    {
        return m_id;
    }

}

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

    public CNode( JSONObject p_jsonObject )
    {
        try
        {
            m_id = p_jsonObject.getInt( "id" );
            m_xcoord = p_jsonObject.getDouble( "x" );
            m_ycoord = p_jsonObject.getDouble( "y" );

        }
        catch ( JSONException p_e )
        {
            p_e.printStackTrace();
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

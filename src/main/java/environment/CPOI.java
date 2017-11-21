package environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Point of interest class
 */

public class CPOI implements INode<String>
{

    private String m_id;
    private Double m_xcoord;
    private Double m_ycoord;

    private Collection<ILabel> m_labels = new ArrayList<>();

    public CPOI( final JSONObject p_object )
    {
        try
        {
            m_id  = p_object.getString( "id" );
            m_xcoord = p_object.getDouble( "x" );
            m_ycoord = p_object.getDouble( "y" );
        }
        catch ( JSONException p_e )
        {
            p_e.printStackTrace();
        }

    }

    public Collection<ILabel> labels()
    {
        return m_labels;
    }

    @Override
    public String id()
    {
        return m_id;
    }

    @Override
    public Double xcoord()
    {
        return m_xcoord;
    }

    @Override
    public Double ycoord()
    {
        return m_ycoord;
    }
}

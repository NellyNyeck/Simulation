package objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * the provider class
 */
public class CProvider implements IProvider
{

    private String m_name;
    private String m_colour;
    private String m_depot;
    private HashMap<String, CPOD> m_pods;
    private Double m_maxout;
    private HashMap<String, ArrayList<Number>> m_funct;


    public CProvider( final JSONObject p_obj)
    {
        m_name = (String) p_obj.get( "name" );
        m_colour = (String) p_obj.get( "colour" );
        m_depot = (String) p_obj.get( "depot" );
        final JSONArray l_pods = (JSONArray) p_obj.get( "pods" );
        m_pods = new HashMap<>(  );
        for ( int i = 0; i < l_pods.size(); i++ )
        {
            final CPOD l_pod = new CPOD( (JSONObject) l_pods.get( i ) );
            m_pods.put( l_pod.id(), l_pod );
        }
        m_maxout = (Double) p_obj.get( "maximum outgoing pods/time unit" );
        final JSONObject l_function = (JSONObject) p_obj.get( "client selection function" );
        m_funct = new HashMap<>(  );

        FIX THE M FUNCTT!!!
    }


    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public String colour()
    {
        return m_colour;
    }

    @Override
    public String depot()
    {
        return m_depot;
    }

    @Override
    public HashMap<String, CPOD> pods()
    {
        return m_pods;
    }

    @Override
    public Number maxOutgoing()
    {
        return m_maxout;
    }

    @Override
    public HashMap<String, ArrayList<Number>> funct()
    {
        return m_funct;
    }
}

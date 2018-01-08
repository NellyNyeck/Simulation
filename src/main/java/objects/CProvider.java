package objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    private String m_funct;
    private HashMap<String, Double> m_params;

    /**
     * constructor
     * @param p_obj the given jso object
     */
    public CProvider( final JSONObject p_obj )
    {
        m_name = (String) p_obj.get( "name" );
        m_colour = (String) p_obj.get( "colour" );
        m_depot = (String) p_obj.get( "depot id" );
        final JSONArray l_pods = (JSONArray) p_obj.get( "pods" );
        m_pods = new HashMap<>(  );
        for ( int i = 0; i < l_pods.size(); i++ )
        {
            final CPOD l_pod = new CPOD( (JSONObject) l_pods.get( i ) );
            m_pods.put( l_pod.id(), l_pod );
        }
        m_maxout = (Double) p_obj.get( "maximum outgoing pods/time unit" );
        final JSONObject l_function = (JSONObject) p_obj.get( "client selection function" );
        m_funct = (String) l_function.get( "name" );
        final JSONObject l_params = (JSONObject) l_function.get( "parameters" );
        m_params = new HashMap<>();
        switch ( m_funct.toLowerCase() )
        {
            case "even":
                m_params.put( "p", (Double) l_params.get( "p" ) );
                break;
            case "normal":
                m_params.put( "sigma", (Double) l_params.get( "sigma" ) );
                m_params.put( "mu",  (Double) l_params.get( "mu" ) );
                break;
            default:
                break;
        }
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
    public String funct()
    {
        return m_funct;
    }

    @Override
    public HashMap<String, Double> params()
    {
        return m_params;
    }

}

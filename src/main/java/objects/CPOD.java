package objects;

import org.json.simple.JSONObject;


/**
 * implementation of the Ipod interface
 */
public class CPOD implements IPOD<String>
{

    private String m_id;
    private Long m_capacity;
    private String m_provider;
    private String m_strategy;

    /**
     * the constructor
     * @param p_json the input json object
     */
    public CPOD( final JSONObject p_json )
    {
        m_id = (String) p_json.get( "name" );
        m_strategy = (String) p_json.get( "strategy" );
        m_capacity = (Long) p_json.get( "capacity" );
        m_provider = (String) p_json.get( "provider" );
    }

    /**
     * the get id function
     * @return the objects id
     */
    @Override
    public String id()
    {
        return m_id;
    }

    @Override
    public Long capacity()
    {
        return m_capacity;
    }

    @Override
    public String provider()
    {
        return m_provider;
    }

    @Override
    public String strategy()
    {
        return m_strategy;
    }
}

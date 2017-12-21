package exec;

import org.json.simple.JSONObject;


/**
 * class that holds the simulation specifications
 */
public class CSpecs implements ISpecs
{
    private String m_coordtype;
    private Long m_clientnb;
    private String m_timeunit;
    private String m_speedunit;
    private String m_length;

    /**
     * constructor
     * @param p_json objecct that holds the goods
     */
    public CSpecs( final JSONObject p_json )
    {
        m_length = (String) p_json.get( "length unit" );
        m_speedunit = (String) p_json.get( "speed unit" );
        m_timeunit = (String) p_json.get( "time unit" );
        m_clientnb = (Long) p_json.get( "number of waiting clients" );
        m_coordtype = (String) p_json.get( "coordinate  type" );
    }

    @Override
    public String coordType()
    {
        return m_coordtype;
    }

    @Override
    public Long clientNb()
    {
        return m_clientnb;
    }

    @Override
    public String timeUnit()
    {
        return m_timeunit;
    }

    @Override
    public String speedUnit()
    {
        return m_speedunit;
    }

    @Override
    public String lengthUnit()
    {
        return m_length;
    }
}

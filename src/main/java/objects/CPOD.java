package objects;

/**
 * implementation of the Ipod interface
 */
public class CPOD implements IPOD<Integer>
{

    private Integer m_id;
    private Double m_capacity;
    private String m_provider;
    private String m_strategy;


    public CPOD( )
    {

    }

    /**
     * the get id function
     * @return the objects id
     */
    @Override
    public Integer id()
    {
        return m_id;
    }

    @Override
    public Number capacity()
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

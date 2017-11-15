package objects;

/**
 * implementation of the Ipod interface
 */
public class CPOD implements IPOD<Integer>
{

    private Integer m_id;

    /**
     * constructor with given id
     * @param p_id the integer given as id
     */
    public CPOD( final Integer p_id )
    {
        m_id = p_id;
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
}

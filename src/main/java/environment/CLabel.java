package environment;

/**
 * label class
 */
public class CLabel implements ILabel
{

    private String m_name;
    private Double m_waittime;

    /**
     * constructor
     * @param p_string the name
     * @param p_time the time
     */
    public CLabel( final String p_string, final Double p_time )
    {
        m_name = p_string;
        m_waittime = p_time;
    }

    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public Double waintingtime()
    {
        return m_waittime;
    }
}

package exec;

/**
 * class that holds the simulation specifications
 */
public class CSpecs implements ISpecs
{
    private String m_coordtype;
    private Number m_clientnb;
    private String m_timeunit;
    private String m_speedunit;
    private String m_length;

    @Override
    public String coordType()
    {
        return m_coordtype;
    }

    @Override
    public Number clientNb()
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

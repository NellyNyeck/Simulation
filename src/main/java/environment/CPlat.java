package environment;

/**
 * the Platooning edge class.
 * implements the Iedge inerface, but does not hold the weight of the edge
 * but the counter for how many vehicles use it
 */
public class CPlat implements IEdge
{

    private String m_about;

    private int m_counter;

    /**
     * constructor for Pedge
     * @param p_edge the given edge which it replicates
     */
    public CPlat( final CEdge p_edge )
    {
        m_about = p_edge.about();
        m_counter = 1;
    }

    public int getCounter()
    {
        return m_counter;
    }

    public String getAbout()
    {
        return m_about;
    }

    public void add()
    {
        m_counter++;
    }

}

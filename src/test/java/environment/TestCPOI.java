package environment;

import org.junit.Before;
import org.junit.Test;

/**
 * the test class for CPOI
 */
public class TestCPOI
{
    private CPOI m_poi;
    private CNode m_node;

    /**
     * the initialize function
     */
    @Before
    public void initialize()
    {
        m_node = new CNode( 14 );
        m_poi=new CPOI(m_node);
    }

    @Test
    public void constructor()
    {
        if ( m_poi.id().equals( m_node ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    @Test
    public void id()
    {
        if ( m_poi.id().id() == 14 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    @Test
    public void label()
    {
        if ( m_poi.labels().isEmpty() )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }
}

package environment;

import org.junit.Before;
import org.junit.Test;

/**
 * the test class for CPOI
 */
public class TestCPOI
{
    private CPOI m_poi;

    /**
     * the initialize function
     */
    @Before
    public void initialize()
    {
        final CNode l_node = new CNode( 14 );
        m_poi = new CPOI( l_node );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constructor()
    {
        final CNode l_node = new CNode( 14 );
        m_poi = new CPOI( l_node );
        if ( m_poi.id().equals( l_node ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * test the id return function
     */
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

    /**
     * test the label return function
     */
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

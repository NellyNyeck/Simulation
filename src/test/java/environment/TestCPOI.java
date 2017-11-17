package environment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

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
        m_poi = new CPOI( m_node );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constructor()
    {
        assumeNotNull( m_poi );
        assumeNotNull( m_node );
        assertTrue( m_poi.id().equals( m_node ) );
    }

    /**
     * testing the constructor again
     */
    @Test
    public void constr()
    {
        final CNode l_node = new CNode( 8 );
        final CPOI l_cpoi = new CPOI( l_node );
        assertTrue( l_cpoi != null );
        assertTrue( l_cpoi.id() == l_node );
    }

    /**
     * test the id return function
     */
    @Test
    public void id()
    {
        assumeNotNull( m_poi );
        assertTrue( m_poi.id().id() == 14 );
    }

    /**
     * test the label return function
     */
    @Test
    public void label()
    {
        assumeNotNull( m_poi );
        assertTrue( m_poi.labels().isEmpty() );
    }
}

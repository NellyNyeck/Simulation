package environment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

/**
 * test class for CEdge
 */
public final class TestCEdge
{

    private CEdge m_edge;

    /**
     * initializing edge
     */
    @Before
    public void initialize()
    {
        m_edge = new CEdge( "a b" );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr( )
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.weight() == 1 );
        assertTrue( m_edge.visited() == 0 );
        assertTrue( m_edge.about().equals( "a b" ) );
    }

    /**
     * testing the return weight function
     */
    @Test
    public void weight( )
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.weight() == 1 );
    }

    /**
     * testing the return visits function
     */
    @Test
    public void visited( )
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.visited() == 0 );
    }

    /**
     * testing the return info function
     */
    @Test
    public void about( )
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.about().contentEquals( "a b" ) );
    }

    /**
     * testing the reset weights and visited function
     */
    @Test
    public void reset()
    {
        assumeNotNull( m_edge );
        m_edge.add();
        m_edge.add();
        m_edge.reset();
        assertTrue( m_edge.weight() == 1 );
        assertTrue( m_edge.visited() == 0 );
    }

    /**
     * testing the incrementing visited function
     */
    @Test
    public void add()
    {
        assumeNotNull( m_edge );
        m_edge.add();
        assertTrue( m_edge.visited() == 1 );
    }
}

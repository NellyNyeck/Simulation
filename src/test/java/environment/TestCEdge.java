package environment;

import org.junit.Before;
import org.junit.Test;

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
        if ( ( m_edge.weight() == 1 ) && ( m_edge.visited() == 0 ) && ( m_edge.about().equals( "a b" ) ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the return weight function
     */
    @Test
    public void weight( )
    {
        if ( m_edge.weight( ) == 1 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the return visits function
     */
    @Test
    public void visited( )
    {
        if ( m_edge.visited() == 0 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the return info function
     */
    @Test
    public void about( )
    {
        if ( m_edge.about().contentEquals( "a b" ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the reset weights and visited function
     */
    @Test
    public void reset()
    {
        m_edge.reset();
        if ( ( m_edge.weight() == 1 ) && ( m_edge.visited() == 0 ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the incrementing visited function
     */
    @Test
    public void add()
    {
        m_edge.add();
        if ( m_edge.visited() == 1 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * the main function
     */
    public void main()
    {
        initialize();
        constr();
        about();
        visited();
        weight();
        add();
        reset();
    }


}

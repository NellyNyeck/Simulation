package environment;


import org.junit.Before;
import org.junit.Test;

/**
 * test class for CNode
 */
public class TestCNode
{
    private CNode m_node;

    @Before
    public void initialize( )
    {
        m_node = new CNode( 14 );
    }

    /**
     * testing the constructor and the return id function
     */
    @Test
    public void constructor()
    {
        if ( m_node.id( ) == 14 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }
}

package environment;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

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
        assumeNotNull( m_node );
        assertTrue( m_node.id( ) == 14 );
    }
}

package environment;

import org.junit.Before;
import org.junit.Test;

/**
 * test class
 */
public final class TestCEdge {

    private CEdge t_edge;

    @Before
    public void initialize()
    {
        t_edge = new CEdge( "a b" );
    }

    @Test
    public void test_constr()
    {
        if ( ( t_edge.weight() == 1 ) && ( t_edge.visited() == 0 ) && ( t_edge.about().equals("a b")) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }


}

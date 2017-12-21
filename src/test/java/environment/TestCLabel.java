package environment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 *
 * test class for label
 */
public class TestCLabel
{
    private CLabel m_label;

    /**
     * init
     */
    @Before
    public void init()
    {
        m_label = new CLabel( "client", 10.00 );
    }

    /**
     * test constr
     */
    @Test
    public void constr()
    {
        assertTrue( m_label != null );
    }

    /**
     * test name
     */
    @Test
    public void name()
    {
        assumeNotNull( m_label );
        assertTrue( m_label.name().contentEquals( "client" ) );
    }

    /**
     * test wait
     */
    @Test
    public void time()
    {
        assumeNotNull( m_label );
        assertTrue( m_label.waintingtime().equals( 10.00 ) );
    }
}

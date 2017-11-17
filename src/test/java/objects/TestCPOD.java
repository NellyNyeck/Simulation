package objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the CPOD test class
 */
public class TestCPOD
{
    private CPOD m_pod;

    /**
     * initializing the pod
     */
    @Before
    public void initialize()
    {
        m_pod = new CPOD( 14 );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constructor()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod != null );
    }

    /**
     * testing the id return function
     */
    @Test
    public void id()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod.id() == 14 );
    }
}

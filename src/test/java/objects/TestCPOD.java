package objects;

import org.junit.Before;
import org.junit.Test;

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
        if ( m_pod != null )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testiny the id return function
     */
    @Test
    public void id()
    {
        if ( m_pod.id() == 14 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }
}

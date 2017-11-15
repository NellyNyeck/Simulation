package output;

import org.junit.Before;

/**
 * The test class for the cswwriter
 */
public class TestCSVWriter
{

    private CSVWriter m_write;

    @Before
    public void init()
    {
        m_write = new CSVWriter( "somefile.txt" );
    }

}

package output;

import environment.CEdge;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assume.assumeNotNull;


/**
 * The test class for the cswwriter
 */
public class TestCSVWriter
{

    private CSVWriter m_write;

    /**
     * the initialization
     */
    @Before
    public void init()
    {
        m_write = new CSVWriter( "somefile.txt" );
    }

    /**
     * testing with file
     */
    @Test
    public void wfil()
    {
        assumeNotNull( m_write );
        m_write = new CSVWriter( "somefile.txt" );
    }

    /**
     * testing without file
     */
    @Test
    public  void wofile()
    {
        assumeNotNull( m_write );
        m_write = new CSVWriter( "" );
    }

    /**
     * testing the write to file function
     */
    @Test
    public void write()
    {
        assumeNotNull( m_write );
        final ArrayList<CEdge> l_list = new ArrayList<>();
        final CEdge l_ed = new CEdge( "a b" );
        l_ed.add();
        l_ed.add();
        final CEdge l_ed2 = new CEdge( "b a" );
        l_ed2.add();
        l_ed2.add();
        l_ed2.add();
        l_list.add( l_ed );
        l_list.add( l_ed2 );
        m_write.writeCsvFile( l_list );
    }

    /**
     * testing new line
     */
    @Test
    public void newline()
    {
        assumeNotNull( m_write );
        m_write.writeNewLine();
    }

    /**
     * testing file closure
     */
    @Test
    public void close()
    {
        assumeNotNull( m_write );
        m_write.done();
    }
}

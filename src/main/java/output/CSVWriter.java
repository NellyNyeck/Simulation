package output;

import environment.CEdge;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * the csw writer class which is app redundant so .... that's nice
 */
public class CSVWriter
{

    private static final String HEADER = "edge,count";
    private static final String COMMA = ",";
    private static final String NEWLINE = "\n";
    private Writer m_filewriter;

    /**
     * The constructor of the class which creates a file
     * @param p_filename the name of the file
     */
    public CSVWriter( final String p_filename )
    {
        try
        {
            m_filewriter = new OutputStreamWriter( new FileOutputStream( p_filename ), "UTF-8" );
            m_filewriter.append( HEADER );
            m_filewriter.append( NEWLINE );
            m_filewriter.flush();
        }
        catch ( final UnsupportedEncodingException l_err )
        {
            l_err.printStackTrace();
        }
        catch ( final FileNotFoundException l_err )
        {
            l_err.printStackTrace();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
    }

    /**
     * writing the edges in the file
     * @param p_plat list of the edges where platoning is possible
     */
    public void writeCsvFile( final ArrayList<CEdge> p_plat )
    {
        try
        {
            for ( final CEdge l_pl : p_plat )
            {
                if ( l_pl.visited() > 1 )
                {
                    m_filewriter.append( l_pl.name() );
                    m_filewriter.append( COMMA );
                    m_filewriter.append( String.valueOf( l_pl.visited() ) );
                    m_filewriter.append( NEWLINE );
                    m_filewriter.flush();
                }
            }
        }
        catch ( final Exception l_err )
        {
            System.out.println( "Error in CsvFileWriter !!!" );
            l_err.printStackTrace();
        }
    }

    /**
     * adding a placeholder line between each run
     */
    public void writeNewLine()
    {
        try
        {
            m_filewriter.append( "x x" );
            m_filewriter.append( COMMA );
            m_filewriter.append( "0" );
            m_filewriter.append( NEWLINE );
            m_filewriter.flush();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
    }

    /**
     *closing the file
     * */
    public void done()
    {
        try
        {
            m_filewriter.close();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
    }
}

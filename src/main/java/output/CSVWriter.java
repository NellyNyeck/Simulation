package output;

import environment.CEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * the csw writer class which is app redundant so .... that's nice
 */
public class CSVWriter
{

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static FileWriter s_filewriter;

    private static final String FILE_HEADER = "edge,count";

    /**
     * the intialization of the filewriter
     * @param p_filename name of the file
     */
    public void start( final String p_filename )
    {
        try
        {
            s_filewriter = new FileWriter( p_filename, true );
            s_filewriter.append( FILE_HEADER.toString() );
            s_filewriter.append( NEW_LINE_SEPARATOR );
            s_filewriter.flush();
        }
        catch ( final Exception l_err )
        {
            System.out.println( "Error in CsvFileWriter !!!" );
            l_err.printStackTrace();
        }

    }

    /**
     * writing the edges in the file
     * @param p_plat list of the edges where platoning is possible
     */
    public static void writeCsvFile( final ArrayList<CEdge> p_plat )
    {
        try
        {
            for ( final CEdge l_pl : p_plat )
            {
                if ( l_pl.visited() > 1 )
                {
                    s_filewriter.append( l_pl.about() );
                    s_filewriter.append( COMMA_DELIMITER );
                    s_filewriter.append( String.valueOf( l_pl.visited() ) );
                    s_filewriter.append( NEW_LINE_SEPARATOR );
                    s_filewriter.flush();
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
    public static void writeNewLine()
    {
        try
        {
            s_filewriter.append( "x x" );
            s_filewriter.append( COMMA_DELIMITER );
            s_filewriter.append( "0" );
            s_filewriter.append( NEW_LINE_SEPARATOR );
            s_filewriter.flush();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
    }

    /**
     *closing the file
     * */
    public static void done()
    {
        try
        {
            s_filewriter.close();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
    }
}

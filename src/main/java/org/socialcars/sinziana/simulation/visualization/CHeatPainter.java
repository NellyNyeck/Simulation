package org.socialcars.sinziana.simulation.visualization;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * the heatpainter class
 */
public class CHeatPainter  implements Painter<JXMapViewer>
{

    private List<List<GeoPosition>> m_routes;
    private HashMap<GeoPosition, Integer> m_values;
    private boolean m_antialias = true;
    private HashMap<GeoPosition, Color> m_heat;

    /**
     * ctor
     * @param p_tracks the list of routes
     */
    public CHeatPainter( final List<List<GeoPosition>> p_tracks ) throws IOException
    {
        m_routes = p_tracks;
        m_values = new HashMap<>();
        p_tracks.stream()
            .forEach( l -> l.stream().forEach( i -> m_values.put( i, m_values.getOrDefault( i, 0 ) + 1 ) ) );
        m_heat = new HashMap<>();
        final Integer l_max = m_values.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
        m_values.entrySet().forEach( p -> m_heat.put( p.getKey(), EColorMap.PLASMA.apply( p.getValue(), l_max ) ) );
        writeValues( m_values );
    }

    public void writeValues( HashMap<GeoPosition, Integer> p_values ) throws IOException
    {
        FileWriter writer = new FileWriter("test.csv");
        writer.append( "geoposition" );
        writer.append( "," );
        writer.append( "visited" );
        writer.append( "/n" );
        writer.flush();
        Set<GeoPosition> l_keys = p_values.keySet();
        l_keys.forEach( g -> {
            try {
                writer.append( g.toString() );
                writer.append(",");
                writer.append( p_values.get( g ).toString() );
                writer.append("/n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );
        writer.close();
    }

    @Override
    public void paint( Graphics2D p_graphics, final JXMapViewer p_viewer, final int p_wigth, final int p_height )
    {
        p_graphics = (Graphics2D) p_graphics.create();

        // convert from viewport to world bitmap
        final Rectangle l_rect = p_viewer.getViewportBounds();
        p_graphics.translate( -l_rect.x, -l_rect.y );

        if ( m_antialias )
            p_graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        p_graphics.setStroke( new BasicStroke( 3 ) );
        drawHeat( p_graphics, p_viewer );

        p_graphics.dispose();

    }

    private void drawHeat( final Graphics2D p_graphics, final JXMapViewer p_map )
    {
        m_routes.stream().forEach( i ->
        {
            int l_lastx = 0;
            int l_lasty = 0;
            boolean first = true;


            for ( final GeoPosition l_gp : i )
            {
                final Point2D l_pt = p_map.getTileFactory().geoToPixel( l_gp, p_map.getZoom() );
                if ( first ) first = false;
                else
                {
                    p_graphics.setColor( m_heat.get( l_gp ) );
                    p_graphics.drawLine( l_lastx, l_lasty, (int) l_pt.getX(), (int) l_pt.getY() );
                }
                l_lastx = (int) l_pt.getX();
                l_lasty = (int) l_pt.getY();
            }
        } );
    }
}

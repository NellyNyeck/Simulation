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
import java.util.ArrayList;
import java.util.List;

/**
 * route painter class
 */
public class CRoutePainter implements Painter<JXMapViewer>
{
    private Color m_color = Color.RED;
    private boolean m_antialias = true;

    private List<GeoPosition> m_track;

    /**
     * ctor
     * @param p_track the track
     */
    public CRoutePainter( final List<GeoPosition> p_track )
    {
        m_track = new ArrayList<GeoPosition>( p_track );
    }

    @Override
    public void paint( final Graphics2D p_gr, final JXMapViewer p_map, final int p_width, final int p_height )
    {
        final Graphics2D l_gr = (Graphics2D) p_gr.create();

        // convert from viewport to world bitmap
        final Rectangle l_rect = p_map.getViewportBounds();
        l_gr.translate( -l_rect.x, -l_rect.y );

        if ( m_antialias )
            l_gr.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        // do the drawing
        l_gr.setColor( Color.BLACK );
        l_gr.setStroke( new BasicStroke( 2 ) );

        drawRoute( l_gr, p_map );

        // do the drawing again
        l_gr.setColor( m_color );
        l_gr.setStroke( new BasicStroke( 2 ) );


        drawRoute( l_gr, p_map );

        l_gr.dispose();
    }

    /**
     * @param p_gr the graphics object
     * @param p_map the map
     */
    private void drawRoute( final Graphics2D p_gr, final JXMapViewer p_map )
    {
        int l_lastx = 0;
        int l_lasty = 0;

        boolean first = true;

        for ( final GeoPosition l_gp : m_track )
        {
            // convert geo-coordinate to world bitmap pixel
            final Point2D l_pt = p_map.getTileFactory().geoToPixel( l_gp, p_map.getZoom() );

            if ( first )
            {
                first = false;
            }
            else
            {
                p_gr.drawLine( l_lastx, l_lasty, (int) l_pt.getX(), (int) l_pt.getY() );
            }

            l_lastx = (int) l_pt.getX();
            l_lasty = (int) l_pt.getY();
        }
    }
}

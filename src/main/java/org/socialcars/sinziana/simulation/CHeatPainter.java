package org.socialcars.sinziana.simulation;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CHeatPainter  implements Painter<JXMapViewer> {

    private List<List<GeoPosition>> m_routes;
    private HashMap<GeoPosition, Integer> m_values;
    private boolean antiAlias = true;
    private HashMap<GeoPosition, Color> m_heat;

    public CHeatPainter( List<List<GeoPosition>> tracks )
    {
        m_routes = tracks;
        m_values = new HashMap<>();
        tracks.stream()
            .forEach( l -> l.stream().forEach( i -> m_values.put(i, m_values.getOrDefault( i, 0 ) + 1) ) );
        m_heat = new HashMap<>();
        final Integer l_max = m_values.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
        m_values.entrySet().forEach( p -> m_heat.put( p.getKey(), EColorMap.VIDRIS.apply( p.getValue(), l_max ) ) );
    }

    @Override
    public void paint(Graphics2D graphics2D, JXMapViewer jxMapViewer, int i, int i1)
    {
        graphics2D = (Graphics2D) graphics2D.create();

        // convert from viewport to world bitmap
        Rectangle rect =jxMapViewer.getViewportBounds();
        graphics2D.translate(-rect.x, -rect.y);

        if (antiAlias)
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHeat( graphics2D, jxMapViewer );

        graphics2D.dispose();

    }

    private void drawHeat(Graphics2D g, JXMapViewer map)
    {
        m_routes.stream().forEach( i ->
        {
            int lastX = 0;
            int lastY = 0;

            boolean first = true;

            for (GeoPosition gp : i )
            {
                // convert geo-coordinate to world bitmap pixel
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

                if (first)
                {
                    first = false;
                }
                else
                {
                    g.setColor(m_heat.get(gp));
                    g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
                }
                lastX = (int) pt.getX();
                lastY = (int) pt.getY();
            }

        });
    }
}

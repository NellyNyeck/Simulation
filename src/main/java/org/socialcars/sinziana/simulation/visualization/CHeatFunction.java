package org.socialcars.sinziana.simulation.visualization;

import com.google.common.base.Function;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.Paint;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * heatmap with function
 */
public class CHeatFunction implements Function<IEdge, Paint>
{
    private Map<IEdge, Color> m_coding;

    /**
     * ctor
     * @param p_countingmap the map
     */
    public CHeatFunction( final Map<IEdge, Integer> p_countingmap )
    {
        final Integer l_max = p_countingmap.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
        m_coding = p_countingmap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, i -> EColorMap.VIDRIS.apply( i.getValue(), l_max ) ) );
    }

    @Nullable
    @Override
    public Paint apply( @Nullable final IEdge p_edge )
    {
        return m_coding.get( p_edge );
    }
}

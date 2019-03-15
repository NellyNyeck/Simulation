package org.socialcars.sinziana.simulation.visualization;

import com.google.common.base.Function;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;

import javax.annotation.Nullable;
import java.awt.*;
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
//<<<<<<< HEAD
        m_coding = p_countingmap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, i -> EColorMap.PLASMA.apply( i.getValue(), l_max ) ) );
//=======
        //m_coding = p_countingmap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, i -> EColorMap.INFERNO.apply( i.getValue(), l_max ) ) );
//>>>>>>> dc91cfacf50f48ac87624c039a7312516f9c7fe5
    }

    @Nullable
    @Override
    public Paint apply( @Nullable final IEdge p_edge )
    {
        return m_coding.get( p_edge );
    }
}

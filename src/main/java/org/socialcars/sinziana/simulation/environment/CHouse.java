package org.socialcars.sinziana.simulation.environment;


import org.socialcars.sinziana.simulation.data.input.CCoordinates;
import org.socialcars.sinziana.simulation.data.input.CStart;

/**
 * the house class for delivery points
 */
public class CHouse implements INode<String>
{

    private CStart m_location;

    // TODO: 01.02.18  add list of whatever client class you make

    @Override
    public String name()
    {
        return m_location.getName();
    }

    @Override
    public CCoordinates coord()
    {
        return m_location.getCoordinates();
    }
}

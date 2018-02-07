package org.socialcars.sinziana.simulation.environment.osm;

import org.jxmapviewer.JXMapViewer;
import org.socialcars.sinziana.simulation.elements.IElement;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;


public class COSMEnvironment implements IEnvironment<JXMapViewer>
{
    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final INode... p_via
    )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final Stream<INode> p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final String... p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final Stream<String> p_via )
    {
        return null;
    }

    @Override
    public String randomnodebyname()
    {
        return null;
    }

    @Override
    public INode randomnode()
    {
        return null;
    }

    @Override
    public IEnvironment initialize( final IElement... p_element )
    {
        return null;
    }

    @Override
    public JXMapViewer panel( final Dimension p_dimension )
    {
        return null;
    }

}

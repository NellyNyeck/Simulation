package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinatespojo;

/**
 * the interface for the node object
 */
public interface INode
{
    String name();

    /**
     * coordinate
     * @return coordinate
     *
     * @todo Du solltest nicht den Pojo liefern, der Pojo ist _nur_ dafür da, das Objekt zu bauen, danach
     * wird der Pojo entfernt, also das hier zu etwas besserem ersetzen
     */
    CCoordinatespojo coord();

}

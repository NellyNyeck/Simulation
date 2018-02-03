package org.socialcars.sinziana.simulation.elements;

/**
 * the interface for the pod LJobject
 *
 * @todo "get" entfernen
 */
public interface IPod extends IMovable
{
    /**
     * capacity
     * @return capacity
     * @todo wieso muss die capacity nach außen sichtbar sein?
     */
    Number getCapacity();

    /**
     * provider
     * @return provider
     *
     * @todo wieso liefert das getProvider einen String und keinen IProvider
     * @todo wieso muss der Provider nach außen sichtbar sein?
     */
    String getProvider();

}

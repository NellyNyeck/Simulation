package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.INode;

import java.util.Collection;

/**
 * the interface for movable agents
 *
 * @todo bitte "set" und "get" entfernen das macht man heute nicht mehr
 */
public interface IMovable extends IElement
{

    Number getSpeed();

    /**
     * location
     *
     * @return location
     * @todo wieso ist die locattion eine Nummer? Ist das nicht nach Deiner Definition eine Node?
     * @todo das ist z.B. inconsistent zu IStatic, denn da liefert getLocation eine INode
     */
    Number getLocation();

    /**
     * speed
     * @param p_speed speed
     * @todo Wieso kann man von außen die Geschwindigkeit eines Moveable-Agenten setzen? Die Polizei kann doch
     * auch nicht auf das Gaspedal eines anderen Autos treten. Das hier ist ein Fehler in Deinem Design
     */
    void setSpeed( Number p_speed );

    /**
     * location
     *
     * @param p_location location
     * @todo wieso kann die Location von außen gesetzt werden? Das ist ebenfalls wie bei setSpeed ein Designfehler,
     * die Location wo der Agent startet und endet und über welche Zwischenziele er fahren muss kennt der Agent selbst
     */
    void setLocation( Number p_location );

    /**
     * start
     * @return start
     *
     * @todo Wieso muss der Startpunkt nach außen gegeben werden, wer muss wissen, wo der Agent started
     */
    INode getStart();

    /**
     * finish
     * @return finish
     *
     * @todo Wieso muss der Endpunkt nach außen gegeben werden, wer muss wissen, was das Ziel des Agenten ist?
     */
    INode getFinish();

    /**
     * middle
     * @return middle
     *
     * @todo Was ist "middle", benutze verständliche (!) Namen, die jeder verstehen kann!
     * @todo Wer muss die Zwischenziele des Agenten von außen wissen?
     */
    Collection<? extends INode> getMiddle();

    /**
     * accel
     * @return accel
     * @todo benutze lesbare (!) Namen
     * @todo wer muss wissen, wie schnell der Agent maximal fahren kann?
     */
    Number getMaxAccel();

    /**
     * deccel
     * @return deccel
     * @todo benutze lesbare (!) Namen
     * @todo wer muss wissen, wie schnell der Agent maximal bremsen kann?
     */
    Number getMaxDeccel();

    /**
     * maxspeed
     * @return maxpseed
     * @todo wer muss wissen, was die maximale Geschwindigkeit des Agenten ist?
     */
    Number getMaxSpeed();
}

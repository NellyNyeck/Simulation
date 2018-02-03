package org.socialcars.sinziana.simulation.elements;


import org.socialcars.sinziana.simulation.environment.INode;

import java.util.Collection;

/**
 * the provider interface
 */
public interface IProvider extends IStatic
{
    /**
     * color
     * @return color
     *
     * @todo wieso muss ein Color nach außengegeben werden
     * @todo Wieso benutzt Du nicht den Java Typ "Color", sondern einen String
     */
    String colour();

    /**
     * depots
     * @return sepots
     * @todo wieso müssen die Depots von außen sichtbar sein?
     * @todo Wie verhinderst Du, dass in die Collection neue Deopts hinzugefügt werden bzw Deopts entfernt werden
     */
    Collection<? extends INode> depots();

    /**
     * pods
     * @return pods
     * @todo Wieso müssen die Pods des Providers nach außen sichtbar sein
     * @todo Wie verhinderst Du, dass Pods aus der Collection entfernt werden bze hinzugeführt werden
     * @todo Ist es nicht so, dass ein Pod zum Depot gehört und nicht zum Provider? Wieso hat also der Provider eine Liste von Pods?
     */
    Collection<? extends IPod> pods();

    /**
     * maxclients
     * @return maxclients
     *
     * @todo wieso muss der Provider die Anzahl der Clients nach außen geben? Warum ist das nach außen überhaupt sichtbar? Wenn ich DHL Frage, wieviel Clients habt ihr, dann sagen die mir das sicherlich nicht
     * @todo Wieso ist das überhaupt notwendig? Die Clientanzahl die die Anzahl der Empfänger der Pakete, also ist das überhaupt sinnvoll / notwendig?
     */
    Number maxClients();

    /**
     * podtime
     * @return podtime
     *
     * @todo was heisst das? Das ist nicht ordentlich beannt? Was soll das bedeuten?
     * @todo Wieso muss der Provider so etwas haben? Was ist der inhaltliche Sinn davon?
     */
    Number maxPodsTime();

    //TODO: 01.02.18 function class and obvs parameter need to be written but WHERE????
}

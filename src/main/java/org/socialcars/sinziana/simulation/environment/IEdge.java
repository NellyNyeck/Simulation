package org.socialcars.sinziana.simulation.environment;

/**
 * the street interface
 * @todo Im Moment ist eine Edge kein Agent, wenn Du aber z.B. Lanes ab einer gewissen Zeit mal sperren willst,
 * z.B. Fußgängerzonen, die morgens beliefert werden können und ab 9 Uhr nicht mehr, wäre es dann ggf sinnvoll
 * hier einen Agenten zu verwenden, der ab 9 Uhr dann das Gewicht der Edge auf unendlich setzt
 */
public interface IEdge
{
    /**
     * name
     * @return name
     *
     * @todo überlege Dir, ob nicht "id()" als Methoden name besser wäre und das dann einheitlich für alle Objekte
     */
    String name();

    /**
     * from
     * @return from
     * @todo wieso ist der Return-Typ ein String und keine INode
     */
    String from();

    /**
     * to
     * @return to
     *
     * @todo wieso ist der Return-Type ein String und keine INode
     */
    String to();

    /**
     * weight
     * @return weight
     *
     * @todo wie ist diese Methode mit den Dijekstra-Algorithmus im Environment-Graph verbunden?
     */
    Number weight();

    //TODO: 02.02.18 do the function
    Object function();

}

package org.socialcars.sinziana.simulation.environment.jung;

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
     * @return id
     */
    String id();

    /**
     * from
     * @return from
     */
    INode from();

    /**
     * to
     * @return to
     */
    INode to();

    /**
     * weight
     * @return weight
     */
    Number weight();

    /**
     * length
     * @return length
     */
    int length();

    /**
     * orientation
     * @return orientation
     */
    String orientation();

}

package org.socialcars.sinziana.simulation.elements;

import java.util.concurrent.Callable;

/**
 * Dein oberstes Interface, von dem alle Simulationselemente abgeleitet werden müssen
 * @todo bitte so umbauen, dass das Interface zu den LightJason Agenten passt
 * @todo bitte mal generell das "get" weg lassen, das macht man heute nicht mehr, also anstatt "getName" nur "name"
 * und dann lowercase
 */
public interface IElement extends Callable<IElement>
{
    /**
     * name
     * @return name
     * ist das ein sinnvoller datentyp für den agenten? Ist der Name eindeutig während die Simulation läuft?
     * Wenn generierst Du den Namen des Agenten und wie garantierst Du, dass er ggf "eindeutig ist" ?
     *
     */
    String getName();

    /**
     * filename
     *
     * @return frilename
     * @todo du das hier, wozu brauchst ein agent seinen filename? Wie wird ein LightJason Agent erzeugt
     * bitte mal das Tutorial durcharbeite _und verstehen_ https://lightjason.github.io/tutorials/agentspeak-in-fifteen-minutes/
     */
    String getFilename();

    /**
     * agentype
     * @return agtent type
     *
     * @todo brauchst du das hier? siehe "instanceof"
     */
    String getAgentType();

    /**
     * checks if the agent is terminated
     *
     * @return terminated
     * @todo bitte überlegen, ob das mit Deiner Ausführung inhaltlich stimmt, das ist hier nur ein Vorschlag
     */
    boolean terminate();
}

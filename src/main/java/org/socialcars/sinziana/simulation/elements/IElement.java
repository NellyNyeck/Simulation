package org.socialcars.sinziana.simulation.elements;

import java.util.concurrent.Callable;

/**
 * Dein oberstes Interface, von dem alle Simulationselemente abgeleitet werden müssen
 * @todo bitte so umbauen, dass das Interface zu den LightJason Agenten passt
 */
public interface IElement extends Callable<IElement>
{

    /**
     * checks if the agent is terminated
     *
     * @return terminated
     * @todo bitte überlegen, ob das mit Deiner Ausführung inhaltlich stimmt, das ist hier nur ein Vorschlag
     */
    boolean terminate();
}

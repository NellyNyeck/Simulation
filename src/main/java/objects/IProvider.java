package objects;

import java.util.HashMap;


/**
 * the interface for the providers
 */
public interface IProvider
{
    String name();

    String colour();

    String depot();

    HashMap<String, CPOD> pods();

    Number maxOutgoing();

    String funct();

    HashMap<String, Double> params();
    //HashMap<String, Number> params();
    //params need to be figured out
}

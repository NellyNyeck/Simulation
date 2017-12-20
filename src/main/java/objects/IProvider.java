package objects;

import java.util.ArrayList;
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

    HashMap<String, ArrayList<Number>> funct();

}

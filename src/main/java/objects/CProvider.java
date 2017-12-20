package objects;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * the provider class
 */
public class CProvider implements IProvider
{

    private String m_name;
    private String m_colour;
    private String m_depot;
    private HashMap<String, CPOD> m_pods;
    private Double m_maxout;
    private HashMap<String, ArrayList<Number>> m_funct;


    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public String colour()
    {
        return m_colour;
    }

    @Override
    public String depot()
    {
        return m_depot;
    }

    @Override
    public HashMap<String, CPOD> pods()
    {
        return m_pods;
    }

    @Override
    public Number maxOutgoing()
    {
        return m_maxout;
    }

    @Override
    public HashMap<String, ArrayList<Number>> funct()
    {
        return m_funct;
    }
}

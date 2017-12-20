package output;

import environment.CEdge;
import environment.CNode;
import exec.CSpecs;
import objects.CProvider;

import java.util.HashMap;


/**
 * the class for the input json file
 */
public class CInput
{

    private CSpecs m_specs;

    /**
     * the class responsible for the environment part
     */
    public class CEnvironment
    {
        private HashMap<String, CNode> m_nodes;

        private HashMap<String, CEdge> m_edges;
    }

    /**
     * the class responsible for the providers
     */
    public class CProviders
    {
        private HashMap<String, CProvider> m_providers;
    }

    /**
     * the class holding all the simulation specifications
     */


}

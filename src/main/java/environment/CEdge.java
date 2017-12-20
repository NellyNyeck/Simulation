/*
* @cond LICENSE
* ######################################################################################
* # LGPL License                                                                       #
* #                                                                                    #
* # This program is free software: you can redistribute it and/or modify               #
* # it under the terms of the GNU Lesser General Public License as                     #
* # published by the Free Software Foundation, either version 3 of the                 #
* # License, or (at your option) any later version.                                    #
* #                                                                                    #
* # This program is distributed in the hope that it will be useful,                    #
* # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
* # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
* # GNU Lesser General Public License for more details.                                #
* #                                                                                    #
* # You should have received a copy of the GNU Lesser General Public License           #
* # along with this program. If not, see http://www.gnu.org/licenses/                  #
* ######################################################################################
* @endcond
*/

package environment;



import java.util.ArrayList;
import java.util.HashMap;


/**
 * CEdge defines the edges of the graph
 * */
public class CEdge implements IEdge
{
    private String m_name;
    private String m_from;
    private String m_to;
    private double m_weight;
    private int m_visited;
    private HashMap<String, ArrayList<Double>> m_funct;

    /**
     * constructor
     * */
    public CEdge( )
    {

    }

    public HashMap<String, ArrayList<Double>> function()
    {
        return m_funct;
    }

    public Double weight()
    {
        return m_weight;
    }

    public int visited()
    {
        return m_visited;
    }

    public String name()
    {
        return m_name;
    }

    public String from()
    {
        return m_from;
    }

    public String to()
    {
        return m_to;
    }

    public void reset()
    {
        m_visited = 0;
    }

    public void add()
    {
        m_visited++;
    }
}

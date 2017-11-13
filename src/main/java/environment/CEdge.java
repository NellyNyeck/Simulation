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

/**
 * CEdge defines the edges of the graph
 * */
public class CEdge implements IEdge
{
    private double m_weight;

    private final String m_about;

    private int m_visited;

    /**
     * contructor giving the edge it's starting and ending node
     * */
    public CEdge( final String s )
    {
        m_about = s;
        m_weight = 1;
        m_visited = 0;
    }

    public double weight()
    {
        return m_weight;
    }

    @Override
    public int visited()
    {
        return m_visited;
    }

    public String about()
    {
        return m_about;
    }

    /**
     * resets the edges visited coundter and weight
     */
    public void reset()
    {
        m_weight = 1;
        m_visited = 0;
    }

    public void add()
    {
        m_visited++;
    }
}

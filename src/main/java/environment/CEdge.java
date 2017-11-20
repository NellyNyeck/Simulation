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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * CEdge defines the edges of the graph
 * */
public class CEdge implements IEdge
{
    private String m_about;
    private double m_length;
    private double m_weight;
    private int m_visited;

    /**
     * constructor
     * */
    public CEdge( final JSONObject p_json )
    {
        try
        {
            m_about = p_json.getString( "from" ) + " " + p_json.getString( "to" );
            m_length = p_json.getDouble( "length" );
            m_weight = p_json.getDouble( "weight" );
            m_visited = 0;
        }
        catch ( final JSONException l_err )
        {
            l_err.printStackTrace();
        }

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

    public double length()
    {
        return m_length;
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

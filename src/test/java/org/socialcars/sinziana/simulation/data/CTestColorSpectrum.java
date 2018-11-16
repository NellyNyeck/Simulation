package org.socialcars.sinziana.simulation.data;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


public class CTestColorSpectrum
{
    private static void getYtoR()
    {
        final int red = 255;
        final int blue = 0;
        AtomicInteger green = new AtomicInteger( 255 );
        IntStream.range( 0, 64 ).forEach( i ->
        {
            System.out.println( "new Color( " + red + ", " + green.get() + ", " + blue + " )," );
            green.getAndDecrement();
            green.getAndDecrement();
            green.getAndDecrement();
            green.getAndDecrement();
        } );
    }

    private static void getGtoY ()
    {
        AtomicInteger red = new AtomicInteger();
        int blue = 0;
        int green = 255;
        IntStream.range( 0, 64 ).forEach( i ->
        {
            System.out.println( "new Color( " + red.get() + ", " + green + ", " + blue + " )," );
            red.getAndAdd( 4 );
        } );
    }

    private static void getCtoG()
    {
        int red = 0;
        int green = 255;
        AtomicInteger blue = new AtomicInteger( 255 );
        IntStream.range( 0, 64 ).forEach( i ->
        {
            System.out.println( "new Color( " + red + ", " + green + ", " + blue.get() + " )," );
            blue.getAndDecrement();
            blue.getAndDecrement();
            blue.getAndDecrement();
            blue.getAndDecrement();
        } );
    }

    private static void getBtoC()
    {
        int red = 0;
        final int blue = 255;
        AtomicInteger green = new AtomicInteger();
        IntStream.range( 0, 64 ).forEach( i ->
        {
            System.out.println( "new Color( " + red + ", " + green.get() + ", " + blue + " )," );
            green.getAndAdd( 4 );
        } );
    }

    public static void main( final String[] p_args )
    {
        getBtoC();
        getCtoG();
        getGtoY();
        getYtoR();
    }

}

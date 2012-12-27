package com.enonic.cms.plugin.example.function;

import java.security.SecureRandom;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;

/**
 * This class illustrates a simple function library example. The library implements a set of simple
 * methods to illustrate various input and output types.  To configure this plugin place the
 * <b>example-plugin.xml</b> file into <b>CMS_HOME/plugins</b> directory with the jar file(s).
 */
public final class FunctionLibraryExample
{
    /**
     * Random generator.
     */
    private final Random random = new SecureRandom();

    /**
     * Returns a random number.
     */
    public int getRandomNumber()
    {
        return this.random.nextInt();
    }

    /**
     * Return the random numbers.
     */
    public int[] getRandomNumbers( int count )
    {
        int[] result = new int[count];
        for ( int i = 0; i < result.length; i++ )
        {
            result[i] = getRandomNumber();
        }

        return result;
    }

    /**
     * Return the random numbers document.
     */
    public Document getRandomNumbersDocument( int count )
    {
        int[] result = getRandomNumbers( count );
        Element root = new Element( "numbers" );
        for ( int number : result )
        {
            root.addContent( new Element( "number" ).setText( String.valueOf( number ) ) );
        }

        return new Document( root );
    }
}

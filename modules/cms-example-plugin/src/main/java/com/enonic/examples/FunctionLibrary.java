package com.enonic.examples;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class FunctionLibrary
{

    public static void main( String... args )
        throws Exception
    {

        Document res = getSomeXML();
        XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() );
        out.output( res, System.out );

    }

    public static Document getSomeXML()
        throws Exception
    {

        final String message = "<echo>Hello World!</echo>";
        SAXBuilder sb = new SAXBuilder();
        Document xml = sb.build( new StringReader( message ) );

        return xml;
    }

}

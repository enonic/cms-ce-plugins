package com.enonic.cms.api.example.client.local;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.LocalClient;
import com.enonic.cms.api.plugin.ext.http.HttpController;

/**
 * Jan 28, 2010
 */
public class MinimalLocalClientExample
    extends HttpController
{
    private int siteKey;

    public void setSiteKey( int siteKey )
    {
        this.siteKey = siteKey;
    }

    @Override
    public void handleRequest( HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {

        LocalClient client = ClientFactory.getLocalClient();
        PrintWriter out = response.getWriter();
        out.println( "cms.properties" );
        out.println( "-----------------------------" );
        printCmsProperties( client, out );
        out.println( "-----------------------------" );
        out.println( "cms.site.properties" );
        out.println( "-----------------------------" );
        printSiteProperties( client, out, siteKey );
        out.println( "-----------------------------" );

    }

    private void printCmsProperties( LocalClient client, PrintWriter out )
    {
        Map<String, String> cmsProperties = client.getConfiguration();
        printProperties( cmsProperties, out );
    }

    private void printSiteProperties( LocalClient client, PrintWriter out, int siteKey )
    {
        Map<String, String> siteProperties = client.getSiteConfiguration( siteKey );
        printProperties( siteProperties, out );
    }

    private void printProperties( Map<String, String> properties, PrintWriter out )
    {
        for ( Map.Entry prop : properties.entrySet() )
        {
            out.println( prop.getKey() + " = " + prop.getValue() );
        }
    }
}
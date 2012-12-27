package com.enonic.cms.plugin.example.http;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.api.plugin.ext.http.HttpController;

/**
 * This class illustrates a simple http controller example. The controller servers a simple site on a
 * predefined path. To configure this plugin place the <b>example-plugin.xml</b> file into
 * <b>CMS_HOME/plugins</b> directory with the jar file(s).
 */
public final class HttpControllerExample
    extends HttpController
{
    /**
     * Service the request.
     */
    public void handleRequest( HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        PrintWriter out = response.getWriter();
        out.println( "Hello World!" );
        out.close();
    }
}

package com.enonic.cms.plugin.example.http;

import javax.servlet.http.HttpServletRequest;

import com.enonic.cms.api.plugin.ext.http.HttpResponseFilter;

/**
 * This class illustrates a simple http response filter example. This filter adds the remote
 * address of the user to a predefined tag in the response. To configure this plugin place the
 * <b>example-plugin.xml</b> file into <b>CMS_HOME/plugins</b> directory with the jar file(s).
 */
public final class HttpResponseFilterExample
    extends HttpResponseFilter
{
    /**
     * Tag to replace.
     */
    private final static String TAG_TO_REPLACE = "##ip##";

    /**
     * Filters the textural response.
     */
    public String filterResponse( HttpServletRequest request, String response, String contentType )
        throws Exception
    {
        String remoteAddr = request.getRemoteAddr();
        return response.replaceAll( TAG_TO_REPLACE, remoteAddr );
    }
}

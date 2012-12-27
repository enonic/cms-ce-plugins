package com.enonic.cms.plugin.example.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.api.plugin.ext.http.HttpInterceptor;

/**
 * This class illustrates a simple http interceptor example. The interceptor times the request and
 * logs it into system out. To configure this plugin place the <b>example-plugin.xml</b> file into
 * <b>CMS_HOME/plugins</b> directory with the jar file(s).
 */
public final class HttpInterceptorExample
    extends HttpInterceptor
{
    /**
     * Executes before the actual resource being called.
     */
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response )
    {
        request.setAttribute( "startTime", System.currentTimeMillis() );
        return true;
    }

    /**
     * Executes after the actual resource being called.
     */
    public void postHandle( HttpServletRequest request, HttpServletResponse response )
    {
        long startTime = (Long) request.getAttribute( "startTime" );
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println( "Reqest [" + request.getRequestURL().toString() + "] took [" + totalTime + "] ms" );
    }
}

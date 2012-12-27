package com.enonic.cms.plugin.example.http;

import javax.servlet.http.HttpServletRequest;

import com.enonic.cms.api.plugin.ext.http.HttpAutoLogin;

/**
 * This class illustrates a simple http auto login example. It returns the name of who should be
 * logged in when first entering the path where it resides.
 */
public final class HttpAutoLoginExample
    extends HttpAutoLogin
{
    /**
     * Return the qualified user name of who should be logged in.
     */
    public String getAuthenticatedUser( HttpServletRequest request )
        throws Exception
    {
        return "myuserstore\\someuser";
    }
}

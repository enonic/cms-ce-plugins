package com.enonic.examples;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.plugin.ext.http.HttpController;


public class ExamplePlugin
    extends HttpController
{

    private final static Logger LOG = Logger.getLogger( ExamplePlugin.class.getName() );

    private final static String CREATE_URL_PATTERN = "/create";

    private final static String DELETE_URL_PATTERN = "/delete";

    private final static String UPDATE_URL_PATTERN = "/update";

    private Client client = null;

    @Override
    public void handleRequest( HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {

        try
        {

            String urlPattern = request.getRequestURI();
            // this.getUrlPattern();
            if ( client != null )
            {
                client.login( "admin", "password" ); // unless delete won't work :)

                if ( urlPattern.endsWith( CREATE_URL_PATTERN ) )
                {
                    LOG.info( "Create 100 dummy content in dummy category." );
                    final CreateContent cc = new CreateContent();
                    cc.execute( client );
                }
                else if ( urlPattern.endsWith( DELETE_URL_PATTERN ) )
                {
                    LOG.info( "Delete 90 dummy content." );
                    final DeleteContent dc = new DeleteContent();
                    dc.execute( client );
                }
                else if ( urlPattern.endsWith( UPDATE_URL_PATTERN ) )
                {
                    LOG.info( "Update rest of dummy content." );
                    final UpdateContent uc = new UpdateContent();
                    uc.execute( client );
                }
            }
            else
            {
                LOG.info( "Sorry, no Client present ..." );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }

    }

    public void setClient( Client client )
    {
        this.client = client;
    }

}

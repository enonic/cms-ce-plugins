package com.enonic.cms.api.example.client.remote;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.RemoteClient;
import com.enonic.cms.api.client.model.GetMenuParams;

/**
 * This example illustrates the use of binrpc remote client.
 */
public final class RemoteClientSimpleUsageExample
{
    /**
     * Execute the example.
     */
    public static void main( String[] args )
        throws Exception
    {
        RemoteClient client = ClientFactory.getRemoteClient( "http://localhost:8080/rpc/bin" );
        System.out.println( "Connected to [" + client.getServiceUrl() + "]" );

        client.login( "admin", "password" );
        GetMenuParams params = new GetMenuParams();
        params.menuKey = 0;
        params.levels = 2;
        Document result = client.getMenu( params );
        client.logout();

        XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() );
        out.output( result, System.out );
    }
}

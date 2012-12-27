package com.enonic.examples;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.ClientFactory;

public class ExampleApp
{

    static final String remoteURL = "http://localhost:8080/cms/rpc/bin"; // 4.5.4 ent

    public static void main( String... args )
        throws Exception
    {

        final Client client = ClientFactory.getRemoteClient( remoteURL );
        client.login( "admin", "password" );

        System.out.println( "Create 100 dummy content in dummy category." );
        final CreateContent cc = new CreateContent();
        cc.execute( client );

        System.out.println( "Delete 90 dummy content." );
        final DeleteContent dc = new DeleteContent();
        dc.execute( client );

        System.out.println( "Update rest of dummy content." );
        final UpdateContent uc = new UpdateContent();
        uc.execute( client );

        System.out.println( "Finished." );
    }

}

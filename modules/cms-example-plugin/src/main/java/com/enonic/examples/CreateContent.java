package com.enonic.examples;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.model.CreateContentParams;
import com.enonic.cms.api.client.model.content.ContentDataInput;
import com.enonic.cms.api.client.model.content.TextInput;

public class CreateContent
{

    public void execute( Client client )
        throws Exception
    {
        // create 100 content item
        for ( int i = 0; i < 100; i++ )
        {

            final TextInput ti = new TextInput( "mytext", "This is my text (" + i + ")" );
            final ContentDataInput cdi = new ContentDataInput( "dummy" );
            cdi.add( ti );

            final CreateContentParams ccp = new CreateContentParams();
            ccp.categoryKey = 0; // this has to be replaced with the actual content type id!
            ccp.contentData = cdi;

            client.createContent( ccp );
        }
    }

}

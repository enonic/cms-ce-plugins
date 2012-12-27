package com.enonic.examples;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.model.DeleteContentParams;
import com.enonic.cms.api.client.model.GetContentByCategoryParams;

public class DeleteContent
{

    public void execute( Client client )
        throws Exception
    {
        this.deleteContent( this.getContentByCategory( client ), client );
    }

    private void deleteContent( Document contentXml, Client client )
        throws Exception
    {

        final List<?> contentList = contentXml.getRootElement().getChildren( "content" );
        final DeleteContentParams dcp = new DeleteContentParams();

        for ( Object obj : contentList )
        {
            final Element e = (Element) obj;
            dcp.contentKey = Integer.parseInt( e.getAttributeValue( "key" ) );
            client.deleteContent( dcp );
        }

    }

    private Document getContentByCategory( Client client )
        throws Exception
    {
        final GetContentByCategoryParams gcbcp = new GetContentByCategoryParams();

        gcbcp.categoryKeys = new int[]{0};
        gcbcp.includeData = false;
        gcbcp.count = 90; // we only delete 90 elements ...
        gcbcp.includeOfflineContent = true;
        gcbcp.orderBy = "key ASC";

        return client.getContentByCategory( gcbcp );

    }

}

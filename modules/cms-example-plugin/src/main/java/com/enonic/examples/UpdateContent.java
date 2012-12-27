package com.enonic.examples;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.model.GetContentByCategoryParams;
import com.enonic.cms.api.client.model.UpdateContentParams;
import com.enonic.cms.api.client.model.content.BinaryInput;
import com.enonic.cms.api.client.model.content.ContentDataInput;
import com.enonic.cms.api.client.model.content.TextInput;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class UpdateContent
{

    public void execute( Client client )
        throws Exception
    {
        this.updateContent( this.getContentByCategory( client ), client );
    }

    private void updateContent( Document contentXml, Client client )
        throws Exception
    {

        final List<?> elements = contentXml.getRootElement().getChildren( "content" );

        for ( Object o : elements )
        {

            final Element e = (Element) o;
            final String myText = e.getChild( "contentdata" ).getChildText( "mytext" ) + " UPDATED";
            final int myKey = Integer.parseInt( e.getAttributeValue( "key" ) );

            final ContentDataInput cdi = new ContentDataInput( "dummy" );
            cdi.add( new TextInput( "mytext", myText ) );
            cdi.add( new BinaryInput( "myimage", loadImage( "smiley.png" ), "smiley.png" ) );

            final UpdateContentParams ucp = new UpdateContentParams();
            ucp.contentKey = myKey;
            ucp.contentData = cdi;

            client.updateContent( ucp );

        }

    }

    private Document getContentByCategory( Client client )
        throws Exception
    {
        final GetContentByCategoryParams gcbcp = new GetContentByCategoryParams();

        gcbcp.categoryKeys = new int[]{0}; // // this has to be replaced with the actual content type id!
        gcbcp.includeData = true;
        gcbcp.count = 100; // add maximum 100 items
        gcbcp.includeOfflineContent = true;

        return client.getContentByCategory( gcbcp );

    }

    private byte[] loadImage( String imageName )
        throws Exception
    {

        final BufferedImage bufferedImage = ImageIO.read( this.getClass().getResourceAsStream( "/smiley.png" ) );
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "png", baos );
        return baos.toByteArray();

    }
}

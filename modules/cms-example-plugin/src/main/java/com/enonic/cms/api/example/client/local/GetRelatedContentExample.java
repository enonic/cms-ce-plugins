package com.enonic.cms.api.example.client.local;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.LocalClient;
import com.enonic.cms.api.client.model.GetRelatedContentsParams;
import com.enonic.cms.api.plugin.ext.http.HttpController;

/**
 * Jan 28, 2010
 */
public final class GetRelatedContentExample
    extends HttpController
{
    @Override
    public void handleRequest( HttpServletRequest req, HttpServletResponse res )
        throws Exception
    {
        LocalClient client = ClientFactory.getLocalClient();

        Integer contentKey = getParameterAsInteger( "content-key", req );
        Integer relation = getParameterAsInteger( "relation", 1, req );
        Integer childrenLevel = getParameterAsInteger( "children-level", 10, req );
        Integer parentLevel = getParameterAsInteger( "parent-level", 0, req );
        String query = req.getParameter( "query" );
        String orderby = req.getParameter( "orderby" );
        Integer index = getParameterAsInteger( "index", 0, req );
        Integer count = getParameterAsInteger( "count", 100, req );

        GetRelatedContentsParams params = new GetRelatedContentsParams();
        params.contentKeys = new int[]{contentKey};
        params.relation = relation;
        params.query = query;
        params.orderBy = orderby;
        params.index = index;
        params.count = count;
        params.childrenLevel = childrenLevel;
        params.parentLevel = parentLevel;
        params.includeData = true;

        Document resultDoc = client.getRelatedContent( params );

        Element rootEl = new Element( "root" );
        rootEl.addContent( createCallArgumentInfo( params ) );
        rootEl.addContent( resultDoc.getRootElement().detach() );
        String documentAsString = prettyPrintDocument( new Document( rootEl ) );

        res.setContentType( "text/xml" );
        res.getWriter().print( documentAsString );
    }

    private Integer getParameterAsInteger( String name, Integer defaultValue, HttpServletRequest req )
    {
        Integer value = getParameterAsInteger( name, req );
        if ( value != null )
        {
            return value;
        }
        return defaultValue;
    }

    private Integer getParameterAsInteger( String name, HttpServletRequest req )
    {
        try
        {
            return Integer.valueOf( req.getParameter( name ) );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    private Element createCallArgumentInfo( GetRelatedContentsParams params )
    {
        Element el = new Element( "you-called-getRelatedContent-with-the-following-arguments" );
        el.addContent( new Element( "content-keys" ).setText( String.valueOf( params.contentKeys[0] ) ) );
        el.addContent( new Element( "relation" ).setText( String.valueOf( params.relation ) ) );
        el.addContent( new Element( "query" ).setText( params.query ) );
        el.addContent( new Element( "ordery-by" ).setText( params.orderBy ) );
        el.addContent( new Element( "index" ).setText( String.valueOf( params.index ) ) );
        el.addContent( new Element( "count" ).setText( String.valueOf( params.index ) ) );
        el.addContent( new Element( "children-level" ).setText( String.valueOf( params.childrenLevel ) ) );
        el.addContent( new Element( "parent-level" ).setText( String.valueOf( params.parentLevel ) ) );
        el.addContent( new Element( "include-data" ).setText( String.valueOf( params.includeData ) ) );
        return el;
    }

    public String prettyPrintDocument( Document doc )
    {
        StringWriter sw = new StringWriter();
        XMLOutputter outputter = new XMLOutputter( Format.getPrettyFormat() );
        try
        {
            outputter.output( doc, sw );
            return sw.getBuffer().toString();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Failed to print document", e );
        }
    }
}
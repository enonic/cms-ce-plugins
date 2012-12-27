package com.enonic.cms.api.example.client.local;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.LocalClient;
import com.enonic.cms.api.client.model.ImportContentsParams;
import com.enonic.cms.api.plugin.ext.http.HttpController;

/**
 * This example requires the following conten type:
 * <contenttype>
 * <config>
 * <form>
 * <title name="title"/>
 * <block name="HTTP Request">
 * <input name="title" required="true" type="text">
 * <display>Title</display>
 * <xpath>contentdata/title</xpath>
 * </input>
 * <input name="request-time" type="text">
 * <display>Request time</display>
 * <xpath>contentdata/request-time</xpath>
 * </input>
 * <input name="request-uri" type="text">
 * <display>Request URI</display>
 * <xpath>contentdata/request-uri</xpath>
 * </input>
 * <input name="request-url" type="text">
 * <display>Request URL</display>
 * <xpath>contentdata/request-url</xpath>
 * </input>
 * <input name="request-contextpath" type="text">
 * <display>Request Context Path</display>
 * <xpath>contentdata/request-contextpath</xpath>
 * </input>
 * <input name="request-locale" type="text">
 * <display>Request Locale</display>
 * <xpath>contentdata/request-locale</xpath>
 * </input>
 * <input name="request-remotehost" type="text">
 * <display>Request Remote Host</display>
 * <xpath>contentdata/request-remotehost</xpath>
 * </input>
 * </block>
 * </form>
 * <imports>
 * <import base="http-request" mode="xml" name="single-http-request-import" status="2">
 * <mapping dest="title" src="@title"/>
 * <mapping dest="request-time" src="@time"/>
 * <mapping dest="request-uri" src="@uri"/>
 * <mapping dest="request-url" src="@url"/>
 * <mapping dest="request-locale" src="@locale"/>
 * <mapping dest="request-remotehost" src="@remote-host"/>
 * </import>
 * </imports>
 * </config>
 * <indexparameters/>
 * </contenttype>
 */
public final class MinimalCustomContentImportExample
    extends HttpController
{
    @Override
    public void handleRequest( HttpServletRequest req, HttpServletResponse res )
        throws Exception
    {

        final String httpRequestAsXmlString = wrapWithElement( "http-requests", createHttpRequestXml( req ) );

        LocalClient client = ClientFactory.getLocalClient();
        Integer categoryToImportTo = getParameterAsInteger( "category-to-import-to", req );

        ImportContentsParams importContentsParams = new ImportContentsParams();
        importContentsParams.categoryKey = categoryToImportTo;
        importContentsParams.importName = "single-http-request-import";
        importContentsParams.data = httpRequestAsXmlString;
        importContentsParams.publishFrom = new Date();

        client.importContents( importContentsParams );

        final PrintWriter printWriter = res.getWriter();
        printWriter.println( "Imported content, check categoy: " + categoryToImportTo );
        printWriter.println( "The XML sent to importContent was:" );
        printWriter.println( httpRequestAsXmlString );
    }

    private String createHttpRequestXml( HttpServletRequest req )
    {
        String requestURI = req.getRequestURI();
        String time = new Date( System.currentTimeMillis() ).toString();
        String remoteHost = req.getRemoteHost();
        String title = remoteHost + " @ " + time;
        StringBuffer url = req.getRequestURL();
        Locale locale = req.getLocale();

        StringBuffer xml = new StringBuffer();
        xml.append( "<http-request\n" );
        xml.append( " title=\"" ).append( title ).append( "\"\n" );
        xml.append( " time=\"" ).append( time ).append( "\"\n" );
        xml.append( " uri=\"" ).append( requestURI ).append( "\"\n" );
        xml.append( " url=\"" ).append( url ).append( "\"\n" );
        xml.append( " locale=\"" ).append( locale ).append( "\"\n" );
        xml.append( " remote-host=\"" ).append( remoteHost ).append( "\"" );
        xml.append( " >\n" );
        xml.append( "</http-request>" );
        return xml.toString();
    }

    private String wrapWithElement( String wrapperElementName, String xmlToWrap )
    {
        return "<" + wrapperElementName + ">\n" + xmlToWrap + "\n</" + wrapperElementName + ">";
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
}

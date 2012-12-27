package com.enonic.cms.plugin.gsasearch;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.model.GetContentParams;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class GSASearchFunctionLibrary
{

    private final static Integer DEFAULT_CONNECTION_TIMEOUT = 2000;  // 2 seconds.

    private Integer timeoutMs = -1;

    // Disse verdiene fungerer lokalt med tunnel mot Google Mini: ssh -L 8999:10.0.20.91:80 developer@beast.enonic.com

    private String protocol = "http";

    private Integer portNumber = 80;

    private String host = "localhost";

    private String userAgent = "Mozilla/5.0 (X11; U; Linux i686; en-GB; rv:1.9.1) Enonic CMS/4.5.1 Firefox/3.6.13";

    private String siteFilter = "*";

    private String clientFilter = "*";

    private static URLCodec URL_CODEC = new URLCodec( "UTF-8" );

    private boolean includeAttachmentContentData = false;

    /**
     * Search the Google GSA API with all default values
     *
     * @param q      The query to specify the search.
     * @param site   The Google Mini defined site name.
     * @param client The Google Mini defined client name.
     * @return A JDom Document with the result.
     * @throws GSAException           If theres a configuration problem with this plugin.
     * @throws java.io.IOException    If there are problems communicating with the Google Mini server.
     * @throws org.apache.commons.codec.EncoderException
     *                                If theres a problem converting any of the inputdata to UTF-8.
     * @throws org.jdom.JDOMException If there are problems parsing the XML returned from Google Mini.
     */
    public Document count( String q, String site, String client )
        throws JDOMException, IOException, EncoderException, GSAException
    {
        return count( q, site, client, null, null, null, null, null, null, null, null, null, "1", "p", null, null );
    }

    public Document count( String q, String site, String client, String as_dt, String as_sitesearch, String as_epq, String as_ft,
                           String as_filetype, String as_lq, String as_occt, String as_oq, String as_q, String filter, String access,
                           String sort, String lr )
        throws GSAException, EncoderException, IOException, JDOMException
    {
        verifyParameters( site, client );
        String file =
            createFileURLPart( q, site, client, as_dt, as_sitesearch, as_epq, as_ft, as_filetype, as_lq, as_occt, as_oq, as_q, filter, 0, 1,
                               3, access, sort, lr );

        URL url = new URL( protocol, host, portNumber, file );

        String xmlData = getStringResultFromURL( url );

        Document googleResultDoc = parseXmlResult( xmlData );

        Element resElem = googleResultDoc.getRootElement().getChild( "RES" );
        if ( resElem == null )
        {
            Element countElement = new Element( "M" ).setText( "0" );
            resElem = new Element( "RES" ).addContent( countElement );
            googleResultDoc.getRootElement().addContent( resElem );
        }
        else
        {
            resElem.removeChildren( "R" );
        }

        Element countResultElement = new Element( "gsa-count" );
        countResultElement.addContent( googleResultDoc.getRootElement().detach() );
        return new Document( countResultElement );
    }

    /**
     * Search the Google GSA API with all default values
     *
     * @param q             The query to specify the search.
     * @param site          The Google Mini defined site name.
     * @param client        The Google Mini defined client name.
     * @param start         The starting index of the result set.
     * @param num           The number of search hits to include in each result set.
     * @param query         A query to limit which content from the search result that is enriched with all content data from Enonic CMS.
     * @param childrenLevel How many levels of children to fetch of the found content.
     * @param parentLevel   How many levels of parents to fetch of the found content.
     * @return A JDom Document with the result.
     * @throws GSAException           If theres a configuration problem with this plugin.
     * @throws org.apache.commons.codec.EncoderException
     *                                If theres a problem converting any of the inputdata to UTF-8.
     * @throws org.jdom.JDOMException If there are problems parsing the XML returned from Google Mini.
     * @throws java.io.IOException    If there are problems communicating with the Google Mini server.
     */
    public Document search( String q, String site, String client, int start, int num, String query, int childrenLevel, int parentLevel )
        throws GSAException, EncoderException, JDOMException, IOException
    {
        return search( q, site, client, null, null, null, null, null, null, null, null, null, "1", start, num, 3, "p", null, null, query,
                       childrenLevel, parentLevel );
    }

    public Document search( String q, String site, String client, String as_dt, String as_sitesearch, String as_epq, String as_ft,
                            String as_filetype, String as_lq, String as_occt, String as_oq, String as_q, String filter, int start, int num,
                            int numgm, String access, String sort, String lr, String query, int childrenLevel, int parentLevel )
        throws GSAException, EncoderException, IOException, JDOMException
    {
        Document googleResultDoc;
        Document contentDoc = null;
        Document facetDoc = null;
        Client ecmsClient = ClientFactory.getLocalClient();
        verifyParameters( site, client );
        String file =
            createFileURLPart( q, site, client, as_dt, as_sitesearch, as_epq, as_ft, as_filetype, as_lq, as_occt, as_oq, as_q, filter,
                               start, num, numgm, access, sort, lr );

        URL url = new URL( protocol, host, portNumber, file );

        String xmlData = getStringResultFromURL( url );

        googleResultDoc = parseXmlResult( xmlData );

        int[] contentKeys = getContentKeysFromXML( googleResultDoc );
        if ( contentKeys != null && contentKeys.length > 0 )
        {
            GetContentParams contentParams = new GetContentParams();
            contentParams.childrenLevel = childrenLevel;
            contentParams.parentLevel = parentLevel;
            contentParams.query = query;
            contentParams.contentKeys = contentKeys;
            contentParams.includeData = true;
            contentDoc = ecmsClient.getContent( contentParams );
        }

        return mergeResultDocuments( googleResultDoc, contentDoc, facetDoc );
    }

    private void verifyParameters( String site, String client )
        throws GSAException
    {
        if ( siteFilter != null && !siteFilter.equals( "" ) && !siteFilter.equals( "*" ) )
        {
            if ( !site.startsWith( siteFilter ) )
            {
                throw new GSAException(
                    "Illegal site value (" + site + ").  The site configuration requires site to start with : " + siteFilter );
            }
        }
        if ( clientFilter != null && !clientFilter.equals( "" ) && !siteFilter.equals( "*" ) )
        {
            if ( !client.startsWith( clientFilter ) )
            {
                throw new GSAException(
                    "Illegal client value (" + client + ").  The client configuration requires client to start with : " + clientFilter );
            }
        }
    }

    private String createFileURLPart( String q, String site, String client, String as_dt, String as_sitesearch, String as_epq, String as_ft,
                                      String as_filetype, String as_lq, String as_occt, String as_oq, String as_q, String filter, int start,
                                      int num, int numgm, String access, String sort, String lr )
        throws EncoderException
    {
        StringBuffer file = new StringBuffer( 1000 );
        file.append( "/search" );
        file.append( "?ie=UTF8&oe=utf8&show_spelling=1&output=xml_no_dtd&getfields=*" );
        file.append( "&q=" ).append( q == null ? "" : URL_CODEC.encode( q ) );
        file.append( "&site=" ).append( URL_CODEC.encode( site ) );
        file.append( "&client=" ).append( URL_CODEC.encode( client ) );
        if ( as_dt != null && !as_dt.equals( "" ) )
        {
            file.append( "&as_dt=" ).append( URL_CODEC.encode( as_dt ) );
        }
        if ( as_sitesearch != null && !as_sitesearch.equals( "" ) )
        {
            file.append( "&as_sitesearch=" ).append( URL_CODEC.encode( as_sitesearch ) );
        }
        if ( as_epq != null && !as_epq.equals( "" ) )
        {
            file.append( "&as_epq=" ).append( URL_CODEC.encode( as_epq ) );
        }
        if ( as_ft != null && !as_ft.equals( "" ) )
        {
            file.append( "&as_ft=" ).append( URL_CODEC.encode( as_ft ) );
        }
        if ( as_filetype != null && !as_filetype.equals( "" ) )
        {
            file.append( "&as_filetype=" ).append( URL_CODEC.encode( as_filetype ) );
        }
        if ( as_lq != null && !as_lq.equals( "" ) )
        {
            file.append( "&as_lq=" ).append( URL_CODEC.encode( as_lq ) );
        }
        if ( as_occt != null && !as_occt.equals( "" ) )
        {
            file.append( "&as_occt=" ).append( URL_CODEC.encode( as_occt ) );
        }
        if ( as_oq != null && !as_oq.equals( "" ) )
        {
            file.append( "&as_oq=" ).append( URL_CODEC.encode( as_oq ) );
        }
        if ( as_q != null && !as_q.equals( "" ) )
        {
            file.append( "&as_q=" ).append( URL_CODEC.encode( as_q ) );
        }
        if ( filter != null && !filter.equals( "" ))
        {
            file.append( "&filter=" ).append( URL_CODEC.encode( filter ) );
        }
        file.append( "&start=" ).append( start );
        file.append( "&num=" ).append( num );
        file.append( "&numgm=" ).append( numgm );
        if ( access != null && !access.equals( "" ) )
        {
            file.append( "&access=" ).append( URL_CODEC.encode( access ) );
        }
        if ( sort != null && !sort.equals( "" ) )
        {
            file.append( "&sort=" ).append( URL_CODEC.encode( sort ) );
        }
        if ( lr != null && !lr.equals( "" ) )
        {
            file.append( "&lr=" ).append( URL_CODEC.encode( lr ) );
        }
        return file.toString();
    }

    private Document mergeResultDocuments( Document googleResultDoc, Document contentDoc, Document facetDoc )
    {
        Element resultElement = new Element( "gsa-search" );
        if ( googleResultDoc != null )
        {
            resultElement.addContent( googleResultDoc.getRootElement().detach() );
        }
        if ( contentDoc != null )
        {
            resultElement.addContent( contentDoc.getRootElement().detach() );
        }
        if ( facetDoc != null )
        {
            resultElement.addContent( facetDoc.getRootElement().detach() );
        }
        return new Document( resultElement );
    }

    private Document parseXmlResult( String xmlData )
        throws JDOMException, IOException
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build( new StringReader( xmlData ) );
    }

    private int[] getContentKeysFromXML( Document doc )
    {
        ArrayList<Integer> contentKeys = new ArrayList<Integer>();
        Element res = doc.getRootElement().getChild( "RES" );
        if ( res == null )
        {
            return null;
        }
        List<Element> rs = res.getChildren( "R" );
        for ( Element r : rs )
        {
            List<Element> metaNodes = r.getChildren( "MT" );
            Element keyNode = null;
            for ( Element metaNode : metaNodes )
            {
                Attribute nameAttr = metaNode.getAttribute( "N" );
                if ( nameAttr != null )
                {
                    if ( nameAttr.getValue().equals( "dcterms.identifier" ) )
                    {
                        keyNode = metaNode;
                    }
                }
            }

            if ( keyNode != null )
            {
                Attribute vAttr = keyNode.getAttribute( "V" );
                if ( vAttr != null && !vAttr.getValue().equals("") )
                {
                    Integer contentKey = Integer.parseInt( vAttr.getValue() );
                    contentKeys.add( contentKey );
                }
            }
            else if ( includeAttachmentContentData )
            {
                String url = r.getChild( "U" ).getText();
                Integer contentKey = extractAttachmentContentKey( url );
                if ( contentKey != null )
                {
                    contentKeys.add( contentKey );
                }
            }
        }

        int[] contentKeyArray = new int[contentKeys.size()];
        for ( int cnt = 0; cnt < contentKeys.size(); cnt++ )
        {
            contentKeyArray[cnt] = contentKeys.get( cnt );
        }
        return contentKeyArray;
    }

    static Integer extractAttachmentContentKey( String url )
    {
        int attachmentIndex = url.indexOf( "_attachment" );
        if ( attachmentIndex > 0 )
        {
            int attachmentKeyIndex = attachmentIndex + 12;
            int nextSlashIndex = url.indexOf( '/', attachmentKeyIndex );
            int nextQuestionMarkIndex = url.indexOf( '?', attachmentKeyIndex );
            int attachmentKeyEndIndex;

            if ( nextSlashIndex < 0 )
            {
                attachmentKeyEndIndex = nextQuestionMarkIndex;
            }
            else if ( nextQuestionMarkIndex < 0 )
            {
                attachmentKeyEndIndex = nextSlashIndex;
            }
            else
            {
                attachmentKeyEndIndex = nextSlashIndex < nextQuestionMarkIndex ? nextSlashIndex : nextQuestionMarkIndex;
            }

            if ( attachmentKeyEndIndex > 0 )
            {
                String attachmentKeyString = url.substring( attachmentKeyIndex, attachmentKeyEndIndex );
                try
                {
                    return Integer.parseInt( attachmentKeyString );
                }
                catch ( NumberFormatException e )
                {
                    // Just fall through and return null at the end of the method if the number is not parsable.
                }
            }
        }
        return null;
    }

    private String getStringResultFromURL( URL url )
        throws IOException
    {
        URLConnection urlConn = url.openConnection();
        urlConn.setConnectTimeout( timeoutMs > 0 ? timeoutMs : DEFAULT_CONNECTION_TIMEOUT );
        urlConn.setRequestProperty( "User-Agent", userAgent );

        InputStream result = urlConn.getInputStream();

        BufferedReader reader = new BufferedReader( new InputStreamReader( result, "UTF-8" ) );
        StringBuffer xml = new StringBuffer();
        for ( String line = reader.readLine(); line != null; line = reader.readLine() )
        {
            xml.append( line );
        }
        return xml.toString();
    }

    public void setProtocol( String p )
    {
        protocol = p;
    }

    public void setTimeoutMs( Integer ms )
    {
        timeoutMs = ms;
    }

    public void setPortNumber( Integer port )
    {
        portNumber = port;
    }

    public void setHost( String h )
    {
        host = h;
    }

    public void setUserAgent( String agent )
    {
        userAgent = agent;
    }

    public void setSiteFilter( String filter )
    {
        if ( !filter.endsWith( "*" ) )
        {
            throw new IllegalArgumentException( "Illegal site filter: " + filter + ".  A site filter must end with a '*' character." );
        }
        siteFilter = filter.substring( 0, filter.length() - 1 );
        if ( siteFilter.contains( "*" ) )
        {
            throw new IllegalArgumentException(
                "Illegal site filter: " + filter + ".  A site filter may not contain '*' characters other than at the end." );
        }
    }

    public void setClientFilter( String filter )
    {
        if ( !filter.endsWith( "*" ) )
        {
            throw new IllegalArgumentException( "Illegal site filter: " + filter + ".  A site filter must end with a '*' character." );
        }
        clientFilter = filter.substring( 0, filter.length() - 1 );
        if ( clientFilter.contains( "*" ) )
        {
            throw new IllegalArgumentException(
                "Illegal site filter: " + filter + ".  A site filter may not contain '*' characters other than at the end." );
        }
    }

    public void setIncludeAttachmentContentData( String includeAttachmentContentData )
    {
        if ( includeAttachmentContentData.equalsIgnoreCase( "true" ) )
        {
            this.includeAttachmentContentData = true;
        }
    }
}

package com.enonic.cms.api.example.client.local;


import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.LocalClient;
import com.enonic.cms.api.client.model.ImportContentsParams;
import com.enonic.cms.api.plugin.ext.http.HttpController;

/**
 * Jan 28, 2010
 */
public final class DatesCustomContentImportExamle
    extends HttpController
{

    @Override
    public void handleRequest( HttpServletRequest req, HttpServletResponse res )
        throws Exception
    {

        Integer categoryToImportTo = getParameterAsInteger( "category-to-import-to", req );

        DateTime startDate = getParameterAsDateTime( "start-date", req );
        DateTime endDate = getParameterAsDateTime( "end-date", req );
        String reference = req.getParameter( "reference" );

        final String datesAsXmlString = createDatesXml( startDate, endDate, reference );

        LocalClient client = ClientFactory.getLocalClient();

        ImportContentsParams importContentsParams = new ImportContentsParams();
        importContentsParams.categoryKey = categoryToImportTo;
        importContentsParams.importName = "dates-import";
        importContentsParams.data = datesAsXmlString;
        importContentsParams.publishFrom = new Date();

        client.importContents( importContentsParams );

        final PrintWriter printWriter = res.getWriter();
        printWriter.println( "Imported content, check categoy: " + categoryToImportTo );
    }

    private String createDatesXml( DateTime startDate, DateTime endDate, String reference )
    {
        StringBuffer xml = new StringBuffer();
        xml.append( "<dates>" );
        appendDateXml( startDate, reference, xml );
        for ( int dayCount = 1; ; dayCount++ )
        {
            DateTime nextDate = startDate.plusDays( dayCount );
            appendDateXml( nextDate, reference, xml );
            if ( nextDate.equals( endDate ) )
            {
                break;
            }
        }
        xml.append( "</dates>" );
        return xml.toString();
    }

    private void appendDateXml( DateTime date, String reference, StringBuffer xml )
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern( "yyyy-MM-dd" );

        xml.append( "<date>" );
        xml.append( "<name>" ).append( formatter.print( date ) ).append( "</name>" );
        xml.append( "<year>" ).append( date.getYear() ).append( "</year>" );
        xml.append( "<month>" ).append( date.getMonthOfYear() ).append( "</month>" );
        xml.append( "<day-of-month>" ).append( date.getDayOfMonth() ).append( "</day-of-month>" );
        xml.append( "<day-of-week>" ).append( date.getDayOfWeek() ).append( "</day-of-week>" );
        xml.append( "<day-of-year>" ).append( date.getDayOfWeek() ).append( "</day-of-year>" );
        xml.append( "<reference>" ).append( reference != null ? reference : "" ).append( "</reference>" );
        xml.append( "</date>" );
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

    private DateTime getParameterAsDateTime( String name, HttpServletRequest req )
    {
        final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern( "yyyy-MM-dd" );
        return timeFormatter.parseDateTime( req.getParameter( name ) );
    }
}

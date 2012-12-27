package com.enonic.cms.plugin.example.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.enonic.cms.api.plugin.ext.TaskHandler;

public class TaskPluginExample
    extends TaskHandler
{

    private static final DateFormat df = new SimpleDateFormat( "MMM dd, yyyy hh:mm:ss" );

    public void execute( Properties props )
    {
        System.out.println( df.format( new Date( System.currentTimeMillis() ) ) + ": Testing plugin TaskPluginExample with properties :" );
        for ( Map.Entry<Object, Object> entry : props.entrySet() )
        {
            System.out.println( " - Key: " + entry.getKey().toString() + " - Value : " + entry.getValue().toString() );
        }
    }

}

package com.enonic.cms.plugin.gsasearch;

public class GSAException
    extends Exception
{
    public GSAException()
    {
        super();
    }

    public GSAException( String message )
    {
        super( message );
    }

    public GSAException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public GSAException( Throwable cause )
    {
        super( cause );
    }
}

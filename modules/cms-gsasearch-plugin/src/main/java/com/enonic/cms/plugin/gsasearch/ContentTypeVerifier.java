package com.enonic.cms.plugin.gsasearch;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to check for valid content type names, after it has been initialized with the valid ones.
 */
public class ContentTypeVerifier
{
    private Set<String> ctyNames = new HashSet<String>();

    private boolean allOk;

    /**
     * Initialize this class with a string array of valid content type names to use this class as a filter.  If this class is initialized
     * with and empty array or null, the filter is turned off and all names are considered acceptable.
     *
     * @param cty An array of valid content type names.
     */
    public ContentTypeVerifier( String[] cty )
    {
        if ( cty == null || cty.length == 0 )
        {
            allOk = true;
        }
        else
        {
            allOk = false;
            for ( String c : cty )
            {
                ctyNames.add( c.toLowerCase().trim() );
            }
        }
    }

    /**
     * Verify that a content type name is valid for the installation.  The rules are:
     * - A name is not valid if it is null or empty.
     * - All names are valid if no filter have been set, that is if this class have been initialized with an empty or null array of valid content type names.
     * - Otherwise, a content type name must match a name in the array that initialized this class, in order to be valid.
     *
     * @param name The name that needs to be verified.
     * @return <code>true</code> if the <code>name</code> is okay, <code>false</code> otherwise.
     */
    public boolean verifyCtyName( String name )
    {
        if ( name == null || name.equals( "" ) )
        {
            return false;
        }
        if ( allOk )
        {
            return true;
        }
        return ctyNames.contains( name.toLowerCase().trim() );
    }

    /**
     * @return <code>true</code> if the filter is empty, so that all names are okay.  <code>false</code> otherwise.
     */
    public boolean isAllOk()
    {
        return allOk;
    }
}

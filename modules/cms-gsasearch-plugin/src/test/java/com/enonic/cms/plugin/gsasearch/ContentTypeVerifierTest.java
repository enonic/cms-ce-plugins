package com.enonic.cms.plugin.gsasearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContentTypeVerifierTest
{
    @Test
    public void testNullInitializedVerifier()
    {
        ContentTypeVerifier verifier = new ContentTypeVerifier( null );
        assertTrue( verifier.verifyCtyName( "article" ) );
        assertTrue( verifier.verifyCtyName( "Community-Blog" ) );
        assertFalse( verifier.verifyCtyName( "" ) );
        assertFalse( verifier.verifyCtyName( null ) );
        assertTrue( verifier.verifyCtyName( "123" ) );
        assertTrue( verifier.verifyCtyName( "_#,.@@$%&" ) );
    }

    @Test
    public void testContentTypeVerifier()
    {
        String[] ctyNames = new String[]{"article", "Community-Blog"};
        ContentTypeVerifier verifier = new ContentTypeVerifier( ctyNames );
        assertTrue( verifier.verifyCtyName( "article" ) );
        assertTrue( verifier.verifyCtyName( "Community-Blog" ) );
        assertFalse( verifier.verifyCtyName( "" ) );
        assertFalse( verifier.verifyCtyName( null ) );
        assertFalse( verifier.verifyCtyName( "123" ) );
        assertFalse( verifier.verifyCtyName( "_#,.@@$%&" ) );

    }
}

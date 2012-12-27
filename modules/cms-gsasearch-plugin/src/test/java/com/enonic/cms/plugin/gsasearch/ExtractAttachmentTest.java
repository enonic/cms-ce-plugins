package com.enonic.cms.plugin.gsasearch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtractAttachmentTest
{

    @Test
    public void extractAttachmentTest()
    {
        String url1 = "http://www.kommunikasjon.no/_attachment/3869/binary/380?download=true";
        String url2 = "http://www.kommunikasjon.no/Foreningen/Om+oss/_attachment/4824?_ts=11ff633ec5f&download=true";
        Integer cKey1 = GSASearchFunctionLibrary.extractAttachmentContentKey( url1 );
        Integer cKey2 = GSASearchFunctionLibrary.extractAttachmentContentKey( url2 );
        assertEquals( 3869, cKey1.longValue() );
        assertEquals( 4824, cKey2.longValue() );
    }
}

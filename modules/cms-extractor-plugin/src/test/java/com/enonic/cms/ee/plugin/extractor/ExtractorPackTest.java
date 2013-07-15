package com.enonic.cms.ee.plugin.extractor;

import java.io.InputStream;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExtractorPackTest
{
    @Test
    public void testDocxFile()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( "small.docx" );
        final ExtractorPack extractorPack = new ExtractorPack();
        final String extracted = extractorPack.extractText("application/vnd.openxmlformats-officedocument.wordprocessingml.document", inputStream, "UTF-8" );

        assertEquals( "Small docx file", extracted.trim() );
    }
}

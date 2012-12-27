package com.enonic.cms.plugin.example.function;

import org.jdom.Document;

import com.enonic.cms.api.client.Client;
import com.enonic.cms.api.client.ClientFactory;
import com.enonic.cms.api.client.model.GetContentByCategoryParams;
import com.enonic.cms.api.client.model.GetContentByQueryParams;
import com.enonic.cms.api.client.model.GetContentBySectionParams;
import com.enonic.cms.api.client.model.GetContentParams;
import com.enonic.cms.api.client.model.GetContentVersionsParams;
import com.enonic.cms.api.client.model.GetRelatedContentsParams;


/**
 * This class is a function library example that demonstrating simple use of the Client API to retreive data.
 */
public final class FunctionLibraryUsingClientExample
{
    public Document getContent( int contentKey )
    {
        Client client = ClientFactory.getLocalClient();
        GetContentParams params = new GetContentParams();
        params.childrenLevel = 10;
        params.parentLevel = 10;
        params.contentKeys = new int[]{contentKey};
        params.includeUserRights = true;
        params.includeData = true;
        return client.getContent( params );
    }

    public Document getRelatedContent( int contentKey, int relation, int childrenLevel, int parentLevel )
    {
        Client client = ClientFactory.getLocalClient();
        GetRelatedContentsParams params = new GetRelatedContentsParams();
        params.contentKeys = new int[]{contentKey};
        params.relation = relation;
        params.childrenLevel = childrenLevel;
        params.parentLevel = parentLevel;
        params.includeUserRights = true;
        params.includeData = true;
        return client.getRelatedContent( params );
    }

    public Document getContentByCategory( int categoryKey )
    {
        Client client = ClientFactory.getLocalClient();
        GetContentByCategoryParams params = new GetContentByCategoryParams();
        params.childrenLevel = 10;
        params.parentLevel = 10;
        params.categoryKeys = new int[]{categoryKey};
        params.includeUserRights = true;
        params.includeData = true;
        return client.getContentByCategory( params );
    }

    public Document getContentBySection( int menuItemKey )
    {
        Client client = ClientFactory.getLocalClient();
        GetContentBySectionParams params = new GetContentBySectionParams();
        params.childrenLevel = 10;
        params.parentLevel = 10;
        params.menuItemKeys = new int[]{menuItemKey};
        params.includeUserRights = true;
        params.includeData = true;
        return client.getContentBySection( params );
    }

    public Document getContentByQuery( String query )
    {
        Client client = ClientFactory.getLocalClient();
        GetContentByQueryParams params = new GetContentByQueryParams();
        params.childrenLevel = 10;
        params.parentLevel = 10;
        params.query = query;
        params.includeUserRights = true;
        params.includeData = true;
        return client.getContentByQuery( params );
    }

    public Document getContentVersions( int[] contentVersionKeys, boolean requireOnline )
    {
        Client client = ClientFactory.getLocalClient();
        GetContentVersionsParams params = new GetContentVersionsParams();
        params.contentVersionKeys = contentVersionKeys;
        params.childrenLevel = 10;
        params.contentRequiredToBeOnline = requireOnline;
        return client.getContentVersions( params );
    }
}

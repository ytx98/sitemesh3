package com.opensymphony.sitemesh3.html.rules.decorator;

import com.opensymphony.sitemesh3.tagprocessor.BasicBlockRule;
import com.opensymphony.sitemesh3.tagprocessor.Tag;
import com.opensymphony.sitemesh3.SiteMeshContext;
import com.opensymphony.sitemesh3.ContentProperty;

import java.io.IOException;

/**
 * Replaces tags that look like {@code <sitemesh:write property='foo'/>} with the
 * {@link ContentProperty} being merged into the current document. The body contents of the tag will be
 * discarded.
 *
 * @author Joe Walnes
 * @see SiteMeshContext#getContentToMerge()
 */
public class SiteMeshWriteRule extends BasicBlockRule {

    private final SiteMeshContext siteMeshContext;

    public SiteMeshWriteRule(SiteMeshContext siteMeshContext) {
        this.siteMeshContext = siteMeshContext;
    }

    @Override
    protected Object processStart(Tag tag) throws IOException {
        String propertyName = tag.getAttributeValue("property", true);
        ContentProperty contentToMerge = siteMeshContext.getContentToMerge();
        if (contentToMerge != null) {
            ContentProperty property = contentToMerge.getChild(propertyName);
            if (property.hasValue()) {
                property.writeValueTo(tagProcessorContext.currentBuffer());
            }
        }
        tagProcessorContext.pushBuffer();
        return null;
    }

    @Override
    protected void processEnd(Tag tag, Object data) throws IOException {
        tagProcessorContext.popBuffer();
    }
}
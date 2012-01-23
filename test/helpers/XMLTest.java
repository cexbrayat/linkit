package helpers;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import play.test.UnitTest;

/**
 * Unit tests for {@link XML}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class XMLTest extends UnitTest {
    
    @Test public void getFirstNodeValue() {
        final String xml = "<person><id>ABCD</id><first-name>Cyril</first-name><last-name>Lacôte</last-name></person>";
        final Document dom = XML.getDocument(xml);
        assertXmlValue(XML.getFirstNodeValue(dom, "id"));
        assertXmlValue(XML.getFirstNodeValue(dom, "first-name"));
        assertXmlValue(XML.getFirstNodeValue(dom, "last-name"));
        assertNull(XML.getFirstNodeValue(dom, "headline"));
        assertNull(XML.getFirstNodeValue(dom, "summary"));
    }
    
    private void assertXmlValue(final String value) {
        assertNotNull(value);
        assertTrue(StringUtils.isNotBlank(value));
    }
}

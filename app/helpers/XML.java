package helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * XML tools
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class XML {
    
    private XML() {};
    
    public static Document getDocument(final String data) {
        return play.libs.XML.getDocument(data);
    }
    
    public static String getFirstNodeValue(Document dom, String tag) {
        String value = null;
        Node firstNode = dom.getElementsByTagName(tag).item(0);
        if (firstNode != null) {
            value = firstNode.getChildNodes().item(0).getNodeValue();
        }
        return value;
    }
}

package example.kozaczekapp.Service;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import example.kozaczekapp.KozaczekItems.Article;

import example.kozaczekapp.KozaczekItems.Image;
/**
 * implementation class used to parse response from Kozaczek.pl
 */
public class KozaczekParser {
    private static final String ITEM_TAG_NAME = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PUBLISH_DATE = "pubDate";
    private static final String ENCLOSURE = "enclosure";
    private static final String LINK_GUIDE = "link";
    private static final String ENCODING_STANDARD = "ISO-8859-2";
    private static final String UNUSED_LINE = "#text";

    /**
     * Method parse response to arrayList of ArticlesItems
     * @param response HttpResponse.
     * @return array List of Article objects.
     */
    public ArrayList<Article> parse(HttpResponse response) {
        NodeList nodeList = null;
        Document doc;
        ArrayList<Article> arrayOfArticles = new ArrayList<>();
        try {
            doc = buildDocumentFromInputStream(response);
            nodeList = doc.getElementsByTagName(ITEM_TAG_NAME);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        if(nodeList !=null){
            for(int i=0;i<nodeList.getLength();i++){
                arrayOfArticles.add(parseNodeToArticle(nodeList.item(i)));
            }
        }
        return arrayOfArticles;
    }
    private Document buildDocumentFromInputStream(HttpResponse response)throws IOException,
                                                                        ParserConfigurationException,
                                                                            SAXException {
        HttpEntity r_entity = response.getEntity();
        String xmlString = EntityUtils.toString(r_entity, ENCODING_STANDARD);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlString));
        return db.parse(inStream);
    }

    private Article parseNodeToArticle(Node node){
        String title="",description="",pubDate = "",link="";
        Image image = null;
        int i = 0;
        while(i != node.getChildNodes().getLength()) {
            Node nodeChild =  node.getChildNodes().item(i);
            String text =nodeChild.getNodeName();
            if(text.equals(UNUSED_LINE)){
                i++;
                continue;
            }
            if (text.equals(TITLE)){
                title = nodeChild.getChildNodes().item(0).getNodeValue();
            }
            if(text.equals(DESCRIPTION)){
                description = nodeChild.getChildNodes().item(0).getNodeValue();
            }
            if(PUBLISH_DATE.equals(text)){
                pubDate = nodeChild.getChildNodes().item(0).getNodeValue();
            }
            if(ENCLOSURE.equals(text)){
                image = createImageFromNodeAttributes(node.getChildNodes().item(i));
            }
            if(LINK_GUIDE.equals(text)){
                link = nodeChild.getChildNodes().item(0).getNodeValue();
            }
            i++;
        }
        return new Article(pubDate,image,link,title,description);
    }

    private Image createImageFromNodeAttributes(Node node){
        String linkToImage = node.getAttributes().item(1).getNodeValue();
        String imageSize =  node.getAttributes().item(2).getNodeValue();
        return new Image(linkToImage,imageSize);
    }

}
package timotei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//Loads data from internet and returns it to databaseController.
public class XMLLoader {

    //Loads, prepares and parses data.
    public ArrayList<String> loadPostInfo(){
        String text = loadData( "http://smartpost.ee/fi_apt.xml" );
        Document doc = prepDoc( text );
        return parsePost( doc );
    }

    
    private String loadData( String adress ){
        String text = "";
        try{
            URL url = new URL( adress );
            BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream()));
            String line;
            while(( line = reader.readLine()) != null ){
                text += line + "\n";
            }
        } catch( Exception ex ){
            System.out.println("Virhe XML:n lataamisessa!");
        }
        return text;
    }
    
    
    private Document prepDoc( String text ){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse( new InputSource( new StringReader( text )));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
    
    
    private ArrayList<String> parsePost( Document doc ){
        ArrayList<String> text = new ArrayList();
        NodeList nodes = doc.getElementsByTagName( "place" );
        for( int i = 0; i < nodes.getLength(); i++ ){
            Node node = nodes.item( i );
            Element element = (Element) node;
            String line = element.getElementsByTagName("code").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("city").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("address").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("availability").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("postoffice").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("lat").item(0).getTextContent();
            line += ";" + element.getElementsByTagName("lng").item(0).getTextContent();
            text.add( line );
        }
        return text;
    }

}
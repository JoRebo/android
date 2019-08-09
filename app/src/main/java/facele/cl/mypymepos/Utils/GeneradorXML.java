package facele.cl.mypymepos.Utils;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class GeneradorXML {

    public String obtenerInputEmisionCrear(String descripcion, String monto) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // elemento raiz
            Document doc = docBuilder.newDocument();
            Element iec = doc.createElement("InputEmisionCrear");
            doc.appendChild(iec);

            // atributo version a raiz
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://facele.cl/docele/dto/v10/POS");
            iec.setAttributeNode(attr);

            // TipoDTE
            Element eDescripcion = doc.createElement("descripcion");
            eDescripcion.appendChild(doc.createTextNode(descripcion));
            iec.appendChild(eDescripcion);

            // TipoDTE
            Element eMonto = doc.createElement("montoBruto");
            eMonto.appendChild(doc.createTextNode(monto));
            iec.appendChild(eMonto);

            return getStringXML(doc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStringXML(Document doc) throws TransformerException {
        doc.setXmlStandalone(true);
        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(writer));

        Log.d("texto", writer.getBuffer().toString().replaceAll("\n|\r", "").replace("", ""));

        return writer.getBuffer().toString().replaceAll("\n|\r", "").replace("", "");
    }
}

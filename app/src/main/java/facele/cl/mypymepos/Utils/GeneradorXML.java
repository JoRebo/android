package facele.cl.mypymepos.Utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

    public Bitmap getTedFromBytes(String base64Xml, int width, int height, int columns, int errorlevel) {
        String decoded = decodificarDeBase64(base64Xml);
        String[] split1 = decoded.split("<TED");
        String[] split2 = split1[1].split("</TED>");
        String ted = split2[0];
        ted = "<TED" + ted + "</TED>";
        ted = ted.replaceAll("\\s", "").replaceAll("\n", "");
        Log.d("ted", ted);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Map<EncodeHintType, ?> hints = new HashMap<EncodeHintType, Object>() {{
                put(EncodeHintType.ERROR_CORRECTION, errorlevel);
                put(EncodeHintType.PDF417_COMPACTION, Compaction.TEXT);
                put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(columns, columns, 1, 100));
            }};
            BitMatrix bitMatrix = multiFormatWriter.encode(ted, BarcodeFormat.PDF_417, width, height, hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getTedFromBytes(String base64Xml) {
        String decoded = decodificarDeBase64(base64Xml);
        String[] split1 = decoded.split("<TED");
        String[] split2 = split1[1].split("</TED>");
        String ted = split2[0];
        ted = "<TED" + ted + "</TED>";
        ted = ted.replaceAll("\\s", "").replaceAll("\n", "");
        Log.d("ted", ted);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Map<EncodeHintType, ?> hints = new HashMap<EncodeHintType, Object>() {{
                put(EncodeHintType.ERROR_CORRECTION, 5);
                put(EncodeHintType.PDF417_COMPACTION, Compaction.TEXT);
                put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(15, 15, 1, 100));
            }};
            BitMatrix bitMatrix = multiFormatWriter.encode(ted, BarcodeFormat.PDF_417, 2, 2, hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String decodificarDeBase64(String stringCondificado) {
        try {
            return new String(Base64.decode(stringCondificado.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}

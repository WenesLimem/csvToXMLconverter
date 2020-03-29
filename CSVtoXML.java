
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;


public class CSVtoXML {
    private String csvSplitter = ",";
    private Document output;
    private Element rtElmt;
    private BufferedReader reader;
    private LinkedList<String> tags;
    private String csvName;


    CSVtoXML() {
        output = null;
        rtElmt = null;
        reader = null;
        csvName = null;
    }


    public void convertToXml(String inputFileName){

        //Check if the given file is csv .
        try {
            String ext = inputFileName.substring(inputFileName.lastIndexOf('.',inputFileName.length()));
            if (ext.contentEquals("csv")){
                System.out.println("CSV file is needed.");
                throw  new FileNotFoundException();
            }
            InputStream is = getClass().getResourceAsStream("/"+inputFileName);
            if(is == null){
                throw new FileNotFoundException();
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String csvHeaders = reader.readLine();
            if (csvHeaders!= null){
                addLine(csvHeaders);
            }else {
                System.out.println("Error input file is empty.");
                throw new Exception();
            }



        }
        catch (Exception Fe){
            Fe.getMessage();
        }
        finally {
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Success : File is converted !");
    }

    private void addLine (String csvLine){
        try {
            output = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            rtElmt = output.createElement("Cities");
            output.appendChild(rtElmt);
            tags = new LinkedList<String >(Arrays.asList(csvLine.split(csvSplitter)));
            String csvEntry;

            while ((csvEntry=reader.readLine())!=null){
                addElements(csvEntry);
            }
            generateXml(new File("generatedOuptut"));
        }catch(Exception e){
            e.getMessage();
        }
    }

    private void addElements(String csvEntry){
        LinkedList<String> entryNode = new LinkedList<String>(Arrays.asList(csvEntry.split(csvSplitter)));
         Element entry = output.createElement("City");
         Comment comment = output.createComment("test--coomment");
         entry.appendChild(comment);
         rtElmt.appendChild(entry);

        int index = 0;
        for (String tag:tags) {
            Element elmt = output.createElement(tag);
            String value;
            if(index>=entryNode.size()){
                value="n/a";
                System.out.println("Warning: Input file "+csvName);
                System.out.println(tags);
                System.out.println(csvEntry);
                System.out.println("Warning: No value for header "+tag+". Assigned NA");
            }else {
                value = entryNode.get(index);
            }
            elmt.appendChild(output.createTextNode(value));
            entry.appendChild(elmt);
            index++;
        }
        while( index < entryNode.size() ){
            System.out.println("Input file is ki zok 3amtek !");
            index ++;
        }
    }

    private void generateXml(File file){

        DOMSource dom = new DOMSource(output);
        StreamResult result = new StreamResult(new File(String.valueOf(file)));
        try {
            output.setXmlStandalone(true);
            output.setXmlVersion("1.1");
            TransformerFactory.newInstance().newTransformer().transform(dom,result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

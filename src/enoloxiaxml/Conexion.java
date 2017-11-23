
package enoloxiaxml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class Conexion {
    Connection conn;
    String ruta = "/home/oracle/NetBeansProjects/enoloxiaXML/analise.xml";
    XMLInputFactory fic = XMLInputFactory.newInstance();
    XMLStreamReader sr;
    PreparedStatement ps;
    String datos[] = new String[4];
    //ResulSet rs;
    public void conexion() {

        String driver = "jdbc:oracle:thin:";
        String host = "localhost.localdomain"; // tambien puede ser una ip como "192.168.1.14"
        String porto = "1521";
        String sid = "orcl";
        String usuario = "hr";
        String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;
        try {
            //para conectar co native protocal all java driver: creamos un obxecto Connection usando o metodo getConnection da clase  DriverManager
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión realizada correctamente ");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void leerXML(){
        
        try {
            sr = fic.createXMLStreamReader(new FileReader(ruta));
          
            while(sr.hasNext()){
                   int eventType = sr.next();
                switch (eventType) {
                    
                  case XMLStreamReader.START_DOCUMENT:
                        break;
                  case XMLStreamReader.START_ELEMENT:
                        System.out.println(sr.getLocalName());
                        
                   if(sr.getAttributeCount() >0) {
                   System.out.println(sr.getAttributeName(0) + " = " + sr.getAttributeValue(0));
                  
                        }
                   if(sr.getLocalName()=="tipo"){
                      datos[0]=sr.getElementText();
                   }
                   if(sr.getLocalName()=="acidez"){
                      datos[1]=sr.getElementText();
                       
                   }
                   if(sr.getLocalName()=="cantidade"){
                      datos[2] = sr.getElementText();
                   }
                  if(sr.getLocalName()=="dni"){
                      datos[3] = sr.getElementText();
                  }
                   break;
                  case XMLStreamReader.CHARACTERS:
                      System.out.println(sr.getText());
                   break;
                  case XMLStreamReader.END_ELEMENT:
                    if(sr.getLocalName()=="analise"){
                 ps = conn.prepareStatement("SELECT nomeu,acidezmin,acidezmax from uvas where tipo = '"+datos[0]+"'");
                        
                        
                    }  
                    
                }
            }
            
        } catch (XMLStreamException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
    }
    
    
    
    public void consultasDB(){
        
      
        
    }
    
    
    
}

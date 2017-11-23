
package enoloxiaxml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    PreparedStatement ps1;
    String datos[] = new String[4];
    String insert[] = new String[3];
    ResultSet rs;
    String codigo[] = new String[1];
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
                      
                        
                   if(sr.getAttributeCount() >0) {
                  
                  codigo[0]=sr.getAttributeValue(0);
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
                 ps1 = conn.prepareStatement("insert into xerado(num,nomeuva,tratacidez,total)values(?,?,?,?)");
                 rs = ps.executeQuery();
                 rs.next();
                 
                 insert[0]=rs.getString("nomeu");
                 insert[1]=rs.getString("acidezmin");
                 insert[2]=rs.getString("acidezmax");
                 ps1.setString(1,codigo[0]);
                 ps1.setString(2,insert[0]);
                 if(Integer.parseInt(datos[1])<Integer.parseInt(insert[1])){
                 ps1.setString(3,"Subir acidez");
                    }else if(Integer.parseInt(datos[1])>Integer.parseInt(insert[2])){  
                 ps1.setString(3,"Bajar acidez");
                }else{
                 ps1.setString(3,"Equilibrada");
                    }
                 int cuenta = Integer.parseInt(datos[2])*15;
                 ps1.setInt(4,cuenta);
                 ps1.executeUpdate();
                        
                   
                    }
                 break;
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

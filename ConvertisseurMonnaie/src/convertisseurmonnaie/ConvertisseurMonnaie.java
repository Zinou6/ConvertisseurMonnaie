package convertisseurmonnaie;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.border.Border;
import com.itextpdf.text.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.text.DateFormatter;


/**
 *
 * @author HP
 */
public class ConvertisseurMonnaie {

    static JLabel p3l1, p3l2, p3l3, p3l4, p3l5, p3l6;
    
    static String DBurl = "jdbc:mysql://localhost:3300/conversion_db?zeroDateTimeBehavior=convertToNull";
    static Connection conn = null;
    
    static org.jsoup.nodes.Document doc = null;
    
    static double LeCours = 0;
    static String DateCours = "";
    
    public static void main(String[] args) throws IOException, SQLException {
        
        
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String todayString  = today.format(formatter);
        
            
        try {
            connection();

            if(conn != null){
                JFrame f = new JFrame("CONVERTISSEUR DE DEVISE");
                f.setSize(680,400);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLocationRelativeTo(null);

                JLabel l1, l2;
                l1 = new JLabel("Type :");
                l2 = new JLabel("DZD");

                String[] items = {"DZD - EUR","EUR - DZD"};
                JComboBox<String> c = new JComboBox<>(items);

                JTextField t1;
                t1 = new JTextField("0.00");

                JButton b1,b2,b3;
                b1 = new JButton("Convertir");
                b2 = new JButton("Affiche Historique");
                b3 = new JButton("Affiche Cours");
                
                JPanel panel2 = new JPanel(new GridBagLayout());
                Border border = BorderFactory.createTitledBorder("Conversion");
                panel2.setBorder(border);
                
                JLabel p2l1, p2l2, p2l3, p2l4, p2l5, p2l6;
                p2l1 = new JLabel("Type : ");
                p2l2 = new JLabel("Date : ");
                p2l3 = new JLabel("Montant : ");
                p2l4 = new JLabel("Cours : ");
                p2l5 = new JLabel("Date de Cours : ");
                p2l6 = new JLabel("Montant Convertir : ");
                
                JPanel panel3 = new JPanel(new GridBagLayout());
                Border border2 = BorderFactory.createTitledBorder("Derniere conversion");
                panel3.setBorder(border2);
                
                p3l1 = new JLabel("Type : ");
                p3l2 = new JLabel("Date : ");
                p3l3 = new JLabel("Montant : ");
                p3l4 = new JLabel("Cours : ");
                p3l5 = new JLabel("Date de Cours : ");
                p3l6 = new JLabel("Montant Convertir : ");
                lastConv();
                
                JPanel panel4 = new JPanel(new GridBagLayout());
                Border border3 = BorderFactory.createTitledBorder("Dernier cours");
                panel4.setBorder(border3);
                networkCoursORlastCours();
                JLabel p4l1 = new JLabel("Cours : "+LeCours);
                JLabel p4l2 = new JLabel("Date cours : "+DateCours);
                
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(l1, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(c, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 3;
                gbc.gridheight = 2;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(t1, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 3;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(l2, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(b1, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(b2, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(b3, gbc);
                
                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(panel2, gbc);
                
                GridBagConstraints gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 1;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l1, gbcp2);
                
                gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 2;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l2, gbcp2);
                
                gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 3;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l3, gbcp2);
                
                gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 4;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l4, gbcp2);
                
                gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 5;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l5, gbcp2);
                
                gbcp2 = new GridBagConstraints();
                gbcp2.gridx = 0;
                gbcp2.gridy = 6;
                gbcp2.anchor = GridBagConstraints.WEST;
                gbcp2.insets = new Insets(5,5,5,5);
                panel2.add(p2l6, gbcp2);
                
                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(panel3, gbc);
                
                GridBagConstraints gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 1;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l1, gbcp3);
                
                gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 2;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l2, gbcp3);
                
                gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 3;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l3, gbcp3);
                
                gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 4;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l4, gbcp3);
                
                gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 5;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l5, gbcp3);
                
                gbcp3 = new GridBagConstraints();
                gbcp3.gridx = 0;
                gbcp3.gridy = 6;
                gbcp3.anchor = GridBagConstraints.WEST;
                gbcp3.insets = new Insets(5,5,5,5);
                panel3.add(p3l6, gbcp3);
                
                gbc = new GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5,5,5,5);
                panel.add(panel4, gbc);
                
                GridBagConstraints gbcp4 = new GridBagConstraints();
                gbcp4.gridx = 0;
                gbcp4.gridy = 0;
                gbcp4.anchor = GridBagConstraints.WEST;
                gbcp4.insets = new Insets(5,5,5,5);
                panel4.add(p4l1, gbcp4);
                
                gbcp4 = new GridBagConstraints();
                gbcp4.gridx = 0;
                gbcp4.gridy = 1;
                gbcp4.anchor = GridBagConstraints.WEST;
                gbcp4.insets = new Insets(5,5,5,5);
                panel4.add(p4l2, gbcp4);
                
                c.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        if (c.getSelectedItem().equals("DZD - EUR")){
                            l2.setText("DZD");
                        }else{
                            l2.setText("EUR");
                        }
                    }
                });
                
                b1.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String TypeConversion;
                        String DateConversion;
                        double MontantAConvertir;
                        double MontantConvertir = 0;
                        TypeConversion = (String) c.getSelectedItem();
                        DateConversion = todayString;
                        String MontantAConvertirSTR = t1.getText();
                        MontantAConvertir = Double.parseDouble(MontantAConvertirSTR);
                        
                        try {
                            networkCoursORlastCours();
                            p4l1.setText("Cours : "+LeCours);
                            p4l2.setText("Date cours : "+DateCours);
                
                        } catch (SQLException ex) {
                            Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if(TypeConversion.equals("DZD - EUR")){
                            MontantConvertir = MontantAConvertir*LeCours;
                        }else{
                            if(MontantAConvertir == 0){
                                MontantConvertir = 0;
                            }else{
                                MontantConvertir = MontantAConvertir/LeCours;
                            }
                        }
                        p2l1.setText("Type : "+TypeConversion);
                        p2l2.setText("Date : "+DateConversion);
                        p2l3.setText("Montant : "+MontantAConvertir);
                        p2l4.setText("Cours : "+LeCours);
                        p2l5.setText("Date Cours : "+DateCours);
                        p2l6.setText("Montant Convertir : "+String.format("%.2f", MontantConvertir));
                       
                        String requete = "INSERT INTO conversion (Type,Date_conversion,Montant_a_converti,Lecours,Date_cours,Montant_converti)VALUES (?,?,?,?,?,?)";
                        try (Connection conn = DriverManager.getConnection(DBurl,"root","");PreparedStatement pstmt = conn.prepareStatement(requete)){
                            pstmt.setString(1, TypeConversion);
                            pstmt.setString(2, DateConversion);
                            pstmt.setDouble(3, MontantAConvertir);
                            pstmt.setDouble(4, LeCours);
                            pstmt.setString(5, DateCours);
                            pstmt.setDouble(6, MontantConvertir);
                            pstmt.executeUpdate();
                        } catch (SQLException ex) {
                            Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        try {
                            lastConv();
                        } catch (SQLException ex) {
                            Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                b2.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame fr = new JFrame("Historique");
                        JLabel lb = new JLabel("De : ");
                        
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormatter formatter = new DateFormatter(format);
                        
                        JFormattedTextField tx = new JFormattedTextField(formatter);
                        tx.setValue(new java.util.Date());
                        tx.setPreferredSize(new Dimension(100,20));
                        
                        JLabel lb2 = new JLabel("A : ");
                        
                        JFormattedTextField tx2 = new JFormattedTextField(formatter);
                        tx2.setValue(new java.util.Date());
                        tx2.setPreferredSize(new Dimension(100,20));
                        
                        JButton bt = new JButton("Valider");
                        bt.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e){
                                try {
                                    historique(tx.getText(), tx2.getText());
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (DocumentException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        
                        fr.setLayout(new FlowLayout());
                        fr.add(lb);
                        fr.add(tx);
                        fr.add(lb2);
                        fr.add(tx2);
                        fr.add(bt);
                        fr.setSize(320,100);
                        fr.setLocationRelativeTo(null);
                        fr.setVisible(true);
                    }
                });
                
                b3.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame fr = new JFrame("Cours");
                        JLabel lb = new JLabel("Entre la date : ");
                        JTextField tx = new JTextField();
                        tx.setPreferredSize(new Dimension(100,20));
                        JButton bt = new JButton("Valider");
                        bt.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e){
                                try {
                                    cours(tx.getText());
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (DocumentException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        
                        fr.setLayout(new FlowLayout());
                        fr.add(lb);
                        fr.add(tx);
                        fr.add(bt);
                        fr.setSize(260,100);
                        fr.setLocationRelativeTo(null);
                        fr.setVisible(true);
                    }
                });

                f.setContentPane(panel);
                f.setVisible(true);
            
            }
            
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            
        }
    }
    
    public static void connectionNetwork() throws IOException{
        String urlCurrency = "https://www.google.com/search?q=dzd+to+euro&oq=dzd+to+euro&aqs=chrome..69i57j0i512j0i22i30l5j69i60.14419j1j7&sourceid=chrome&ie=UTF-8";
        doc = Jsoup.connect(urlCurrency).get();
    }
    
    public static void connection () {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DBurl,"root","");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "JDBC driver not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public static void networkCoursORlastCours() throws SQLException{
        try {
            connectionNetwork();
            String exchangeRateStr = doc.select("#knowledge-currency__updatable-data-column > div.b1hJbf > div.dDoNo.ikb4Bb.gsrt > span.DFlfde.SwHCTb").text();
            String exchangeRateStr2 = exchangeRateStr.replace(",", ".");
            String exchangeDate    = doc.select("#knowledge-currency__updatable-data-column > div.k0Rg6d.hqAUc > span").text();
            
            LeCours = Double.parseDouble(exchangeRateStr2);
            DateCours = exchangeDate;
        } catch (IOException ex) {
            connection();
            if(conn != null) {
                String req = "SELECT Lecours,Date_cours FROM conversion ORDER BY N_conversion DESC LIMIT 1";
                PreparedStatement pstmt = conn.prepareStatement(req);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()){
                    LeCours = rs.getDouble("Lecours");
                    DateCours = rs.getString("Date_cours");
                }
            }
        }
    }
    
    public static void lastConv() throws SQLException{
        connection();
        if(conn != null){
            String req = "SELECT * FROM conversion ORDER BY N_conversion DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(req);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                p3l1.setText("Type : "+rs.getString("Type"));
                p3l2.setText("Date : "+rs.getString("Date_conversion"));
                p3l3.setText("Montant : "+rs.getDouble("Montant_a_converti"));
                p3l4.setText("Cours : "+rs.getDouble("Lecours"));
                p3l5.setText("Date Cours : "+rs.getString("Date_cours"));
                p3l6.setText("Montant Convertir : "+String.format("%.2f",rs.getDouble("Montant_converti")));
            }
        }
    }
    
    public static void historique(String Date, String Date2) throws SQLException, FileNotFoundException, DocumentException, IOException{
        connection();
        if(conn != null){
            String req = "SELECT * FROM conversion WHERE STR_TO_DATE(Date_conversion, '%d-%m-%Y')  BETWEEN STR_TO_DATE(?, '%d-%m-%Y') AND STR_TO_DATE(?, '%d-%m-%Y')";
            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setString(1, Date);
            pstmt.setString(2, Date2);
            ResultSet rs = pstmt.executeQuery();
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
            document.open();
            
            while (rs.next()){
                String Type = rs.getString("Type");
                String DateCon = rs.getString("Date_conversion");
                Double Mont = rs.getDouble("Montant_a_converti");
                Double cours = rs.getDouble("Lecours");
                String DateeCours = rs.getString("Date_cours");
                Double MontCon = rs.getDouble("Montant_converti");
                
                com.itextpdf.text.Paragraph paragraph = new com.itextpdf.text.Paragraph("Type "+Type+"\n"+"Date "+DateCon+"\n"+"Montant "+Mont+"\n"+"Cours "+cours+"\n"+"Date Cours "+DateeCours+"\n"+"Montant a convertir "+MontCon+"\n"+"=====================================");
                document.add(paragraph);
                
            }
            
            document.close();
                
            File file = new File("output.pdf");
            Desktop.getDesktop().open(file);
        }
    }
    
    public static void cours (String Date) throws SQLException, FileNotFoundException, DocumentException, IOException{
        connection();
        if(conn != null){
            String req = "SELECT Lecours,Date_cours FROM conversion WHERE Date_cours = ?";
            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setString(1, Date);
            ResultSet rs = pstmt.executeQuery();
            
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream("outputCours.pdf"));
            document.open();
            
            while (rs.next()){
                Double cours = rs.getDouble("Lecours");
                String Datecours = rs.getString("Date_cours");
                
                com.itextpdf.text.Paragraph paragraph = new com.itextpdf.text.Paragraph("Cours "+cours+"\n"+"Date Cours "+Datecours+"\n"+"=====================================");
                document.add(paragraph);
                
            }
            
            document.close();
                
            File file = new File("outputCours.pdf");
            Desktop.getDesktop().open(file);
        
        }
    }
    
}

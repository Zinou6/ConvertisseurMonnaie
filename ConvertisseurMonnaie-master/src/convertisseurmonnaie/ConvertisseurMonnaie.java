package convertisseurmonnaie;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.ZoneId;
/**
 *
 * @author HP
 */
public class ConvertisseurMonnaie {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("CONVERTISSEUR DE DEVISE");
        String[] items = {"dinar-euro","euro-dinar"};
        JComboBox<String> c = new JComboBox<>(items);
        JLabel l1, l2;
        JTextField t1, t2;
        JButton b1,b2,b3;
        Connection conn = null;
        LocalDate today = LocalDate.now();
        double coursToday = 0.0069;
        
        c.setBounds(150, 5, 100, 30);
        
        l1 = new JLabel("Dinar :");
        l1.setBounds(20,40,60,30);
        l2 = new JLabel("Euro");
        l2.setBounds(170, 40, 60, 30);
        
        t1 = new JTextField("0");
        t1.setBounds(80, 40, 50, 30);
        t2 = new JTextField("0");
        t2.setBounds(240,40,50,30);
        
        b1 = new JButton("Convertir"); //Conversion Monnaie
        b1.setBounds(120, 150, 100, 30);
        
        
        b2 = new JButton("Affiche Historique"); //Affiche Historique
        b2.setBounds(10, 150, 100, 30);
        
        b3 = new JButton("Affiche Cours"); //Affiche Cours
        b3.setBounds(230, 150, 100, 30);
        
        f.add(c);
        f.add(l1);
        f.add(l2);
        f.add(t1);
        f.add(t2);
        f.add(b1);
        f.add(b2);
        f.add(b3);
        
        f.setLayout(null);
        f.setSize(400,300);
        f.setVisible(true);
        
        
        try {
            // TODO code application logic here
            Class.forName("com.mysql.jdbc.Driver");
            String DBurl = "jdbc:mysql://localhost:3300/conversion_db?zeroDateTimeBehavior=convertToNull";
            try {
                conn = DriverManager.getConnection(DBurl,"root","");
                System.out.println("Connector Loaded Successfully");
                
                b1.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!t1.getText().equals("")){
                            Double Result = Double.parseDouble(t1.getText()) * coursToday;
                            t2.setText("" +Result);
                            String requete = "INSERT INTO conversion (Type,Date_conversion,Montant_a_converti,Lecours,Date_cours,Montant_converti)VALUES (?,?,?,?,?,?)";
                            try (Connection conn = DriverManager.getConnection(DBurl,"root","");PreparedStatement pstmt = conn.prepareStatement(requete)){
                                
                                pstmt.setString(1, (String) c.getSelectedItem());
                                pstmt.setDate(2, (Date) Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                pstmt.setDouble(3, Double.parseDouble(t1.getText()));
                                pstmt.setDouble(4, coursToday);
                                pstmt.setDate(5, (Date) Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                pstmt.setDouble(6, Double.parseDouble(t2.getText()));
                            } catch (SQLException ex) {
                                Logger.getLogger(ConvertisseurMonnaie.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            if(!t2.getText().equals("")){

                            }
                        }
                    }
                });
                
                b2.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //afficher l'Historique
                    }
                });
                
                b3.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //afficher le cours
                    }
                });

                
            } catch (SQLException ex) {
                System.out.println("Connector Not Loaded");
                System.out.println(ex.getMessage());
            }
            
                    } catch (ClassNotFoundException ex) {
            
        }
    }
    
}

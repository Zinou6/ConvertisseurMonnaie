package convertisseurmonnaie;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        
        try {
            // TODO code application logic here
            Class.forName("com.mysql.jdbc.Driver");
            String DBurl = "jdbc:mysql://localhost:3300/conversion_db?zeroDateTimeBehavior=convertToNull";
            try {
                conn = DriverManager.getConnection(DBurl,"root","");
                System.out.println("Connector Loaded Successfully");
            } catch (SQLException ex) {
                System.out.println("Connector Not Loaded");
                System.out.println(ex.getMessage());
            }
            
                    } catch (ClassNotFoundException ex) {
            
        }
        
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
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("HAH");
            }
        });
        
        b2 = new JButton("Affiche Historique"); //Affiche Historique
        b2.setBounds(10, 150, 100, 30);
        b2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("HOH");
            }
        });
        
        b3 = new JButton("Affiche Cours"); //Affiche Cours
        b3.setBounds(230, 150, 100, 30);
        b3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("HIH");
            }
        });
        
        
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
        
    }
    
}

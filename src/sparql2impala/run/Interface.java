package sparql2impala.run;


import java.awt.Component;  
import java.awt.Container;  
import java.awt.FlowLayout;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.awt.BorderLayout;  
  
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JOptionPane;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;  
import javax.swing.ScrollPaneConstants;
  
  
public class Interface extends JFrame{  
  
    private JTextArea campo;  
    private JTextField path;  
    private String arquivo = null;  
    private JScrollPane scroll;  
          
    public static void main(String args[]){  
        Interface janela = new Interface();  
        //janela.setSize(375, 450);  
        janela.setResizable(true);  
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
      
    public Interface(){  
          
        //super("SimasPad"<img src="http://javafree.uol.com.br/forum/images/smiles/icon_wink.gif">;  
          
        Container tela = getContentPane();  
        BorderLayout layout = new BorderLayout();  
        tela.setLayout(layout);  
          
        JLabel rotulo = new JLabel("Digite seu Texto:");  
        JLabel rotulo2 = new JLabel("Path:");  
        JButton salva = new JButton("Salvar");  
       // salva.addActionListener(this);    
          
        path = new JTextField(15);  
        campo = new JTextArea(20, 32);  
        campo.setLineWrap(true);  
        scroll = new JScrollPane(campo);  
          
        JPanel pSuperior = new JPanel();  
        pSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));  
        pSuperior.add(rotulo);  
          
        JPanel pCentro = new JPanel();  
        pCentro.setLayout(new FlowLayout(FlowLayout.LEFT, 10,5));  
        pCentro.add(scroll);  
             
        JPanel pInferior = new JPanel();  
        pInferior.setLayout(new FlowLayout(FlowLayout.CENTER));  
        pInferior.add(rotulo2);  
        pInferior.add(path);  
        pInferior.add(salva);  
        
        JPanel panel3 = new JPanel();
        pInferior.setLayout(new FlowLayout(FlowLayout.RIGHT));  
        JTextArea textArea1 = new JTextArea(2,2);
        JScrollPane scrollPane = new JScrollPane(textArea1);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel3.add(scrollPane);
  
        tela.add(BorderLayout.BEFORE_FIRST_LINE, panel3);
        tela.add(BorderLayout.NORTH, pSuperior);  
        tela.add(BorderLayout.CENTER, pCentro);   
        tela.add(BorderLayout.SOUTH, pInferior);  
          
        pack();  
        setVisible(true);   
    }  
    
}
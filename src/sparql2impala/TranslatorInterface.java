package sparql2impala;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import sparql2impala.ImpalaJDBCConnection;
import sparql2impala.op.ImpalaOp;
import sparql2impala.op.ImpalaProject;
import sparql2impala.sparql.AlgebraTransformer;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.function.library.substring;
import com.hp.hpl.jena.sparql.algebra.op.OpProject;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * Main Class of the ImpalaSPARQL translator.
 * This class generates the Algebra Tree for a SPARQL query,
 * performs some optimizations, translates the Algebra tree into an
 * ImpalaOp tree, initializes the translation and collects the final output.
 */
public class TranslatorInterface extends JFrame implements ActionListener{

    private ImpalaOp impalaOpRoot;
    private String SqlQueryAux = "";
    
    //Variaveis interface
    JLabel lblPrefix, lblSparql, lblAlgebra, lblSql, lblTime;
	JTextArea textArea1, textArea2, textArea3;
	JScrollPane scrollPane;
	JButton btnTrad, btnExec;
	JTextField textfiel1;
	JTextPane textPane;
	//JRadioButton um, dois, tres, snappy, gzip;


    //Cria os componentes da Interface
    public TranslatorInterface(){
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	//setLayout(null);
      	
    	//Label Prefixos
    	JPanel panelPrefix = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblPrefix = new JLabel();
        lblPrefix.setText("<html>DEFAULT PREFIXES:<br> PREFIX PingER-ont: &#60http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#><br>" +
        		"PREFIX MGC: &#60http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#><br>" +
        		"PREFIX qb: &#60http://purl.org/linked-data/cube#><br>" +
    			"PREFIX time: &#60http://www.w3.org/2006/time#><br>" +
    			"PREFIX gn-ont: &#60http://www.geonames.org/ontology#><br>" +
    			"PREFIX MU: &#60http://www.fp7-moment.eu/MomentUnits.owl#><br>" +
    			"PREFIX MD: &#60http://www.fp7-moment.eu/MomentDataV2.owl#><br>" +
    			"PREFIX rdf: &#60http://www.w3.org/1999/02/22-rdf-syntax-ns#></html>");
        lblPrefix.setForeground(Color.BLUE);
        lblPrefix.setBounds(10,0,2000,130);
        lblPrefix.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrefix.add(lblPrefix);
        panel.add(panelPrefix);
        
     /*   JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        um = new JRadioButton("100M", true);
        dois = new JRadioButton("200M", false);
        tres = new JRadioButton("300M", false);
        snappy = new JRadioButton("Snappy", true);
         gzip = new JRadioButton("Gzip", false);
        JLabel grupo = new JLabel("Conjunto de dados:");
        panelButton.add(grupo);
        panelButton.add(um);
        panelButton.add(dois);
        panelButton.add(tres);
        panelButton.add(snappy);
        panelButton.add(gzip);
        panel.add(panelButton);*/
     
       //Label SPARQL Query
        JPanel panelSparql = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblSparql = new JLabel("SPARQL Query: ");
        lblSparql.setBounds(10,130,100,20);
        panelSparql.add(lblSparql);
        panel.add(panelSparql);
        
       //TextArea SPARQL Query
        textArea1 = new JTextArea(50,50);
        scrollPane = new JScrollPane(textArea1);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10, 160, 1100, 170);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
		panel.add(scrollPane);
        
        //Botao Traduzir
		JPanel panelTrad = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTrad = new JButton("Translate");
        btnTrad.setBounds(1010,340,100,30);
        btnTrad.setToolTipText("Translate");
        btnTrad.setForeground(Color.RED);
        btnTrad.addActionListener(this);
        panelTrad.add(btnTrad);
        panel.add(panelTrad);
        
        //Label SPARQL Algebra Query
        JPanel panelAlgebra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblAlgebra = new JLabel("SPARQL Algebra Structure: ");
        lblAlgebra.setBounds(10,370,300,20);
        panelAlgebra.add(lblAlgebra);
        panel.add(panelAlgebra);
        
        //TextArea SPARQL Algebra
        textArea2 = new JTextArea(50,500);
        scrollPane = new JScrollPane(textArea2);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10, 400, 1100, 170);
        textArea2.setEditable(false);
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        panel.add(scrollPane);
        
        //Label SQL Query
        JPanel panelSql = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblSql = new JLabel("SQL Query: ");
        lblSql.setBounds(10,580,300,20);
        panelSql.add(lblSql);
        panel.add(panelSql);
        
        //TextArea SQL Query
        textArea3 = new JTextArea(50,500);
        scrollPane = new JScrollPane(textArea3);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10, 610, 1100, 170);
        textArea3.setEditable(false); //MUDAR!!!
        textArea3.setLineWrap(true);
        textArea3.setWrapStyleWord(true);
        panel.add(scrollPane);
        
        //Botao Executar
        JPanel panelExec = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExec = new JButton("Execute");
        btnExec.setBounds(1010,790,100,30);
        btnExec.setToolTipText("Execute");
        btnExec.setForeground(Color.RED);
        btnExec.addActionListener(this);
        panelExec.add(btnExec);
        panel.add(panelExec);
        
        getContentPane().add(panel);
        
    
    }
    
  //funcao que limpa o programa e deixa ele como executado da primeira vez
    private void limpaDados(){
      textArea1.setText("");
      textArea2.setText("");
      textArea3.setText("");
      textPane.setText("");
    }
    
  //Funcao responsavel pelos cliques
    public void actionPerformed(ActionEvent acesso){
      String  InputQuery;
    
      
      //se o acesso for via botao traduzir
      if(acesso.getSource() == btnTrad){
    	
    	//limpa os dados antes
      	textArea2.setText("");
        textArea3.setText("");
        
        //chama a funcao que ira traduzir a query inserida no textArea1
    	InputQuery = textArea1.getText();
        translateQuery(InputQuery);
   
        //Muda o foco do componente
        textArea1.grabFocus();
   
      }
      
      //se o acesso for via botao executar
      if(acesso.getSource() == btnExec){
    	 
    	 //cria 2 paineis para exibir o resultado da consulta e o tempo de execução 
    	 JPanel mainPanel = new JPanel();
    	 mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    	 mainPanel.setSize(1700, 800);
    	 JPanel intermediatePanel = new JPanel();
    	 intermediatePanel.setLayout(new BoxLayout(intermediatePanel, BoxLayout.X_AXIS));
    	
        
    	//Cria um TextPane para inserir o resultado da consulta
        textPane = new JTextPane();
		textPane.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(textPane); 
		scrollPane.setPreferredSize( new Dimension( 1700, 800 ) );
        
        //Exibe o tempo de execução da consulta
        lblSparql = new JLabel("Execution Time (seconds): ");
        textfiel1 = new JTextField();
        intermediatePanel.add(lblSparql);
        intermediatePanel.add(textfiel1);
        mainPanel.add(scrollPane);
        mainPanel.add(intermediatePanel);

    	
		//Executa o SQL Script gerada no Impala atraves de conexao JDBC
		//O resultado da execucao e inserido no TextPane
    	textPane.setText(ImpalaJDBCConnection.ExecuteQuery(textArea3.getText()));
    	
    	//Recebe o tempo de execução da query
    	textfiel1.setText(ImpalaJDBCConnection.getTimeExecution());
		
		
        JOptionPane.showMessageDialog(null, mainPanel, "Query Result",  
                                               JOptionPane.YES_NO_OPTION, new ImageIcon((new ImageIcon("/home/geral/Pictures/impala-logo.png").getImage()).getScaledInstance(70, 90, java.awt.Image.SCALE_SMOOTH)));
        
    	SqlQueryAux = "";
    	limpaDados();
    	textArea1.grabFocus();
      }
      
    }
    
    /**
     * Translates the SPARQL query into a sql script for use with Impala.
     */
    public void translateQuery(String InputQuery) {
    	
    	String sqlScript = "";
    	
        //Parse input query
    	InputQuery = "PREFIX PingER-ont: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#> \n" +
    			"PREFIX MGC: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#> \n" +
    			"PREFIX qb: <http://purl.org/linked-data/cube#> \n" +
    			"PREFIX time: <http://www.w3.org/2006/time#> \n" +
    			"PREFIX gn-ont: <http://www.geonames.org/ontology#> \n" +
    			"PREFIX MU: <http://www.fp7-moment.eu/MomentUnits.owl#> \n" +
    			"PREFIX MD: <http://www.fp7-moment.eu/MomentDataV2.owl#> \n" +
    			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" + InputQuery;
        Query query = QueryFactory.create(InputQuery);
        System.out.println(query);
        
        //Get prefixes defined in the query
        PrefixMapping prefixes = query.getPrefixMapping();

        //Generate Algebra Tree of the SPARQL query 
        Op opRoot = Algebra.compile(query);

        //Output original Algebra Tree to textArea2
        textArea2.setText(opRoot.toString(prefixes));

        //Transform SPARQL Algebra Tree in ImpalaOp Tree - Nao mostrado na interface
        AlgebraTransformer transformer = new AlgebraTransformer(prefixes);
        impalaOpRoot = transformer.transform(opRoot);
        //ImpalaOpPrettyPrinter.print(logWriter, impalaOpRoot);
        

        // Walk through ImpalaOp Tree and generate translation
    
        // Translate the query
        ImpalaOpTranslator translator = new ImpalaOpTranslator();
        sqlScript += translator.translate(impalaOpRoot, Tags.expandPrefixes);
        
        System.out.println("\n\n\n\nRESULTADO:"+sqlScript);
        
        //Imprime a query traduzida para SQL em textArea3
        textArea3.setText(sqlScript);
        SqlQueryAux = sqlScript;
        
    }
    

}
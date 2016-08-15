package sparql2impala;


import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import sparql2impala.op.ImpalaOp;
import sparql2impala.sparql.AlgebraTransformer;
import sparql2impala.sparql.BGPOptimizerNoStats;
import sparql2impala.sparql.TransformFilterVarEquality;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transformer;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterConjunction;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterDisjunction;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterEquality;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterPlacement;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformJoinStrategy;
import com.hp.hpl.jena.sparql.core.QueryEngineFactoryWrapper;



public class Translator  {

    String inputFile;
    String outputFile;
    private String sqlScript;
    private ImpalaOp impalaOpRoot;
    

    private static final Logger logger = Logger.getLogger(Translator.class);



    public Translator(String _inputFile, String _outputFile) {
    	sqlScript = "";
        inputFile = _inputFile;
        outputFile = _outputFile;
    }



    public void setDelimiter(String _delimiter) {
        Tags.delimiter = _delimiter;
    }


    public void setExpandMode(boolean value) {
        Tags.expandPrefixes = value;
    }


    public void setOptimizer(boolean value) {
        Tags.optimizer = value;
    }


    public void setJoinOptimizer(boolean value) {
        Tags.joinOptimizer = value;
    }


    public void setFilterOptimizer(boolean value) {
        Tags.filterOptimizer = value;
    }


    public void setBGPOptimizer(boolean value) {
        Tags.bgpOptimizer = value;
    }



    public void translateQuery() {
        //Parse input query
        Query query = QueryFactory.read("file:"+inputFile);
        System.out.println(query);
        
        //Get prefixes defined in the query
        PrefixMapping prefixes = query.getPrefixMapping();
       

        //Generate translation logfile
        PrintWriter logWriter;
        try {
            logWriter = new PrintWriter(outputFile + ".log");
        } catch (FileNotFoundException ex) {
            logger.warn("Cannot open translation logfile, using stdout instead!", ex);
            logWriter = new PrintWriter(System.out);
        }

        //Output original query to log
        logWriter.println("SPARQL Input Query:");
        logWriter.println("###################");
        logWriter.println(query);
        logWriter.println();
        //Print Algebra Using SSE, true -> optimiert
        //PrintUtils.printOp(query, true);

        //Generate Algebra Tree of the SPARQL query
        Op opRoot = Algebra.compile(query);

        //Output original Algebra Tree to log
        logWriter.println("Algebra Tree of Query:");
        logWriter.println("######################");
        logWriter.println(opRoot.toString(prefixes));
        logWriter.println();

 

        //Transform SPARQL Algebra Tree in ImpalaOp Tree
        AlgebraTransformer transformer = new AlgebraTransformer(prefixes);
        impalaOpRoot = transformer.transform(opRoot);

        // Print ImpalaOp Tree to log
        logWriter.println("ImpalaOp Tree:");
        logWriter.println("###########");
        ImpalaOpPrettyPrinter.print(logWriter, impalaOpRoot);
        //close log file
        logWriter.close();

        // Walk through ImpalaOp Tree and generate translation
       
        // Translate the query
        ImpalaOpTranslator translator = new ImpalaOpTranslator();
        sqlScript += translator.translate(impalaOpRoot, Tags.expandPrefixes);
        
        // Print resulting SQL script program to output file
        PrintWriter sqlWriter;
        try {
        	sqlWriter = new PrintWriter(outputFile+".sql");
        	sqlWriter.print(sqlScript);
        	sqlWriter.close();
        } catch (Exception ex) {
            logger.fatal("Cannot open output file!", ex);
            System.exit(-1);
        }
    }

}

package sparql2impala;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import sparql2impala.sparql.AlgebraTransformer;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpAsQuery;
import com.hp.hpl.jena.sparql.algebra.Transformer;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterConjunction;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterDisjunction;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterEquality;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformFilterPlacement;
import com.hp.hpl.jena.sparql.algebra.optimize.TransformJoinStrategy;
import com.hp.hpl.jena.sparql.core.QueryEngineFactoryWrapper;

import sparql2impala.op.ImpalaOp;
import sparql2impala.sparql.AlgebraTransformer;
import sparql2impala.sparql.BGPOptimizerNoStats;
import sparql2impala.sparql.TransformFilterVarEquality;

public class teste {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*String outputFile = "teste";
		ImpalaOp impalaOpRoot;
		
		Query query = QueryFactory.read("C:/Users/Raphaela/Desktop/teste-sparql-input.txt");
		//query.setPrefix(arg0, arg1) - DEFINIR PREFIXOS FIXOS!
		Op opRoot = Algebra.compile(query);
		//Print SPARQL algebra tree
		System.out.println(opRoot.toString());
		
		
		//reverte de algebra para SPARQL
		//System.out.println(OpAsQuery.asQuery(opRoot));
		
		PrefixMapping prefixes = query.getPrefixMapping();
		
		AlgebraTransformer transformer = new AlgebraTransformer(prefixes);
        impalaOpRoot = transformer.transform(opRoot);

        // Print ImpalaOp Tree 
        PrintWriter out = new PrintWriter(System.out);
        out.println("ImpalaOp Tree:");
        ImpalaOpPrettyPrinter.print(out, impalaOpRoot);
        out.flush();*/
        
	//String oi = "oi";
	//String tese = "tese";	
    //String imprime = String.format("%20s", tese);   
    //String imprime2 = String.format("%20s\n", oi);
	//System.out.print(imprime);
	//System.out.print("%20soi");
	
	String resultado = "";
    resultado = resultado + String.format("%20s", "oi1");
    resultado = resultado + String.format("%20s", "oi2");
    resultado = resultado + String.format("%20s", "oi3");
    resultado = resultado + "\n";
    resultado = resultado + String.format("%20s", "oi4");
    resultado = resultado + String.format("%20s", "oi5");
    resultado = resultado + String.format("%20s", "oi6");
    System.out.print(resultado);
       
    
    
	}

}

package sparql2impala.op;

import java.util.Set;

import java.util.*;

import sparql2impala.ImpalaOpVisitor;
import sparql2impala.Tags;
import sparql2impala.sql.SQLStatement;
import sparql2impala.sql.Schema;
import sparql2impala.sql.Select;

import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpProject;
import com.hp.hpl.jena.sparql.core.Var;


public class ImpalaProject extends ImpalaOp1 {

	private static OpProject opProject;

	public ImpalaProject(OpProject _opProject, ImpalaOp _subOp,
			PrefixMapping _prefixes) {
		super(_subOp, _prefixes);
		opProject = _opProject;
		resultName = Tags.PROJECT;
	}


	@Override
	public SQLStatement translate(String _resultName, SQLStatement child) {
		// update schema
		resultName = _resultName;
		
		if(child.getStatementName().equalsIgnoreCase("AGREG")){
			resultSchema = subOp.getSchema();
		}
		else{
			resultSchema = Schema.shiftToParent(subOp.getSchema(), subOp.getResultName());
		}
	    
		// translates to select object
		Select projection = new Select(this.getResultName());
		
		
		// Add selectors (subset of result schema)
		boolean first = true;
		for (Var var : this.opProject.getVars()) {
			//System.out.println(var.getName());
			//resultSchema.get(var.getName());
			
			
			projection.addSelector(var.getName(), resultSchema.get(var.getName()));
			first = false;
		}
		
		
		// set from
		System.out.println(child.toNamedString());
		projection.setFrom(child.toNamedString());
		
		//set order
		projection.addOrder(child.getOrder());
		//System.out.println(child.getOrder());
		
		//set limit
		projection.addLimit(child.getLimit());
		
		//set offset
		projection.addOffset(child.getOffset());

		
		return projection;
	}

	//retorna as vari�veis do project!!!
	public static OpProject getOpProject(){
		return opProject;
	}
	
	@Override
	public void visit(ImpalaOpVisitor impalaOpVisitor) {
		impalaOpVisitor.visit(this);
	}
	
	

}

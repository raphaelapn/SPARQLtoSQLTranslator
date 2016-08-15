package sparql2impala.op;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;

import sparql2impala.ImpalaOpVisitor;
import sparql2impala.Tags;
import sparql2impala.sparql.ExprTranslator;
import sparql2impala.sql.SQLStatement;
import sparql2impala.op.ImpalaGroup;

import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.op.OpFilter;
import com.hp.hpl.jena.sparql.expr.Expr;


public class ImpalaFilter extends ImpalaOp1 {

	private final OpFilter opFilter;

	public ImpalaFilter(OpFilter _opFilter, ImpalaOp _subOp,
			PrefixMapping _prefixes) {
		super(_subOp, _prefixes);
		opFilter = _opFilter;
		resultName = Tags.FILTER;
	}

	public SQLStatement translate(String _resultName, SQLStatement child) {
		resultName = subOp.getResultName();
		this.resultSchema = subOp.getSchema();
		
		Iterator<Expr> iterator = opFilter.getExprs().iterator();
		Expr current = iterator.next();
		
		ExprTranslator translator = new ExprTranslator(prefixes);
		String condition = translator.translate(current,
				expandPrefixes,resultSchema);
		//child.updateSelection(resultSchema);
		if(!condition.equals("")){
			//caso filter seja Having
			if(condition.startsWith("(.")){
				String[] parts = condition.split(" ");
				String key = parts[0].replace("(", "");
				//Substituir o alias no Having
				child.addHavingConjunction(condition.replace(key, ImpalaGroup.getOperator(key)));
			}
			//caso filter seja Where
			else{
				child.addWhereConjunction(condition); 
			}	
		}
		while (iterator.hasNext()) {
			translator = new ExprTranslator(prefixes);
			current = iterator.next();
			condition = translator.translate(current,
					expandPrefixes,resultSchema);
			//child.updateSelection(resultSchema); // consider schema changes 

			if(!condition.equals("")){
				//caso filter seja Having
				if(condition.startsWith("(.")){
					String[] parts = condition.split(" ");
					String key = parts[0].replace("(", "");
					//Substituir o alias no Having
					child.addHavingConjunction(condition.replace(key, ImpalaGroup.getOperator(key)));
				}
				//caso filter seja Where
				else{
					child.addWhereConjunction(condition); 
				}
			}
		}
		
		

		return child;
	}



	@Override
	public void visit(ImpalaOpVisitor impalaOpVisitor) {
		impalaOpVisitor.visit(this);
	}

}

package sparql2impala.op;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.op.OpExtend;
import com.hp.hpl.jena.sparql.core.Var;

import sparql2impala.ImpalaOpVisitor;
import sparql2impala.Tags;
import sparql2impala.sql.SQLStatement;
import sparql2impala.sql.Schema;

public class ImpalaExtend extends ImpalaOp1 {
	
	private final OpExtend opExtend;


	public ImpalaExtend(OpExtend _opExtend, ImpalaOp _subOp, PrefixMapping _prefixes) {
		//subOp o que foi retirado da pilha
		super(_subOp, _prefixes);
		opExtend = _opExtend;
		resultName = Tags.EXTEND;
	}

	@Override
	public void visit(ImpalaOpVisitor impalaOpVisitor) {
		// TODO Auto-generated method stub
		impalaOpVisitor.visit(this);
	
	}

	@Override
	public SQLStatement translate(String name, SQLStatement child) {
		// TODO Auto-generated method stub
		resultName = name;
		resultSchema = Schema.shiftToParent(subOp.getSchema(), subOp.getResultName());
		
		Map<String,String[]> select = child.getSelectors();
		
		List<Var> lista = this.opExtend.getVarExprList().getVars();
		
		for(Var var : lista){
			//pega a variável
			String variavel = var.getName();
			//System.out.println(var.getName());
			//pega o alias
			String alias = this.opExtend.getVarExprList().getExpr(var).toString();
			//System.out.println(this.opExtend.getVarExprList().getExpr(var).toString());
			
			String[] agregacao = select.get(alias);
			select.remove(alias);
			select.put(variavel, agregacao);
			
			//System.out.println(agregacao[0]);
			
			//Adiciona os seletores atualizados no resultSchema
			
			String[] varRS = resultSchema.get(alias);
			varRS[1] = varRS[1].replace(alias, variavel);
			
			resultSchema.put(variavel, varRS);
			resultSchema.remove(alias);
			
		}
		
		return child;
	}

}

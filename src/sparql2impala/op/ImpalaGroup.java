package sparql2impala.op;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sparql2impala.ImpalaOpVisitor;
import sparql2impala.Tags;
import sparql2impala.sql.SQLStatement;
import sparql2impala.sql.Schema;
import sparql2impala.sql.Select;

import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.op.OpGroup;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.ExprAggregator;


public class ImpalaGroup extends ImpalaOp1 {
	
	private final OpGroup opGroup;
	//Cria um HashMap para armazenar os alias dos operadores de agregação
	public static Map<String,String> alias = new HashMap<String,String>(50);

	public ImpalaGroup(OpGroup _opGroup, ImpalaOp _subOp,
			PrefixMapping _prefixes) {
		super(_subOp, _prefixes);
		opGroup = _opGroup;
		resultName = Tags.GROUP;

	}
	
	//retorna o operador de agregação para o having
	public static String getOperator(String key){
		return alias.get(key);
	}

	@Override
	public void visit(ImpalaOpVisitor impalaOpVisitor) {
		// TODO Auto-generated method stub
		impalaOpVisitor.visit(this);
	}

	
	/**gerar um select para o group by **/ 
	@Override
	public SQLStatement translate(String name, SQLStatement child) {
		resultName = name;
		resultSchema = Schema.shiftToParent(subOp.getSchema(), subOp.getResultName());
		
		//Set Select
		Select group = new Select(this.getResultName());
		
		//Adiciona ao Selector as variáveis de agregação
		 List<Var> lista = this.opGroup.getGroupVars().getVars();
		 
		 for(Var var : lista){
				
			 	group.addSelector(var.getName(), resultSchema.get(var.getName()));
				//System.out.println(resultSchema.get(var.getName()));
		}
			
		 
		//Adiciona ao Selector os operadores de agregação 
		for(ExprAggregator agg : this.opGroup.getAggregators()){

			//pegar a variável da agregação em formato de array String
			String[] variavel = resultSchema.get(agg.getAggregator().getExpr().getVarName());
			
			//substitui a variável de agregação dentro do operador de agregação
			String[] agregacao = new String[1];
			agregacao[0] = agg.asSparqlExpr().replace(agg.getAggregator().getExpr().toString(), variavel[0]+"."+variavel[1]);
			
			group.addSelector(agg.getAggVar().toString(), agregacao);

			//CRIAR UM HASHMAP, CUJA CHAVE É A VAR E O VALUE É A EXPRESSÃO
			alias.put(agg.getAggVar().toString().replace("?", ""), agregacao[0]);
			//System.out.println(alias.get(agg.getAggVar().toString().replace("?", "")));
			
			//Adiciona os novos seletores no resulSchema
			String[] varRS = new String[1];
			varRS[0] = agg.getAggVar().toString();
			resultSchema.put(agg.getAggVar().toString(), varRS);
			
		}

		
		//criar um from que vem do child
		group.setFrom(child.toNamedString());
		
		//criar um group by
		for(Var var : lista){
			group.addGroupBy(var.getName());
		}

		
		return group;
	}

}

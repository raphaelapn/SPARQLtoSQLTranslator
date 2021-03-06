package sparql2impala.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sparql2impala.ImpalaOpVisitor;
import sparql2impala.Tags;
import sparql2impala.sql.Join;
import sparql2impala.sql.JoinType;
import sparql2impala.sql.JoinUtil;
import sparql2impala.sql.SQLStatement;
import sparql2impala.sql.Schema;
import sparql2impala.sql.TripleGroup;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;


public class ImpalaBGP extends ImpalaOp0 {

	private final OpBGP opBGP;
	private int tableNumber = 0;

	public ImpalaBGP(OpBGP _opBGP, PrefixMapping _prefixes) {
		super(_prefixes);
		opBGP = _opBGP;
		resultName = Tags.BGP;
	}

	public SQLStatement translate(String _resultName) {
		resultName = _resultName;
		
		List<Triple> triples = opBGP.getPattern().getList();

		HashMap<Node, TripleGroup> tripleGroups = new HashMap<Node, TripleGroup>();


		if (expandPrefixes) {
			prefixes = PrefixMapping.Factory.create();
		}


		for (Triple triple : triples) {
			Node key = null;
			boolean fromTripletable = false;
			//se o predicado for uma variável
			if (triple.getPredicate().isVariable()) {
				key = triple.getPredicate();
				fromTripletable = true;
			} 
			//caso o predicado não seja variavel, a chave do Hash Map será o subject
			else {
				key = triple.getSubject();
				fromTripletable = false;
			}

			//se o HashMap ainda não conter o tipo de Sujeito, cria uma nova entrada no HashMap
			if (!tripleGroups.containsKey(key)) {
				//cada grupo cria um BGP1_tableNumber
				tripleGroups.put(key, new TripleGroup(resultName + "_"
						+ tableNumber++, prefixes, fromTripletable));
			}
			//adiciona a tripla ao grupo correspondente dela
			tripleGroups.get(key).add(triple);
		}

		boolean onlyTripleStore = false;

		TripleGroup group = null;
		ArrayList<TripleGroup> groups = new ArrayList<TripleGroup>();
		groups.addAll(tripleGroups.values());
		group = groups.get(0);
		groups.remove(0);


		// joins are necessary
		if (groups.size() > 0) {
			ArrayList<String> onConditions = new ArrayList<String>();
			ArrayList<SQLStatement> rights = new ArrayList<SQLStatement>();
			// Find join partner with most shared vars.
			Map<String, String[]> group_shifted = Schema.shiftToParent(group.getMappings(), group.getName());
			while (groups.size() > 0) {
				int index = findBestJoin(group_shifted, groups);
				TripleGroup right = groups.get(index);
				Map<String, String[]> right_shifted = Schema.shiftToParent(right.getMappings(), right.getName());
				onConditions.add(JoinUtil.generateConjunction(JoinUtil
						.getOnConditions(group_shifted, right_shifted)));
				group_shifted.putAll(right_shifted);
				rights.add(right.translate());
				groups.remove(index);

			}
			this.resultSchema = group_shifted;
			Join join= new Join(getResultName(), group.translate(), rights,
					onConditions, JoinType.natural);
			return join;
		} 
		// no join needed
		this.resultName = group.getName();
		SQLStatement res = group.translate();
		this.resultSchema = group.getMappings();
		return res;
	}


	public int findBestJoin(Map<String, String[]> group_shifted, ArrayList<TripleGroup> groups) {
		int best = -1;
		int index = 0;
		for (int i = 0; i < groups.size(); i++) {
			int sharedVars = JoinUtil.getSharedVars(group_shifted, groups.get(i).getMappings()).size();
			if(sharedVars > best){
				best = sharedVars;
				index = i;
			}
		}
		return index;
	}

	@Override
	public void visit(ImpalaOpVisitor impalaOpVisitor) {
		impalaOpVisitor.visit(this);
	}

}

package sparql2impala;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.jena.atlas.lib.NotImplemented;

import sparql2impala.op.ImpalaBGP;
import sparql2impala.op.ImpalaDistinct;
import sparql2impala.op.ImpalaFilter;
import sparql2impala.op.ImpalaJoin;
import sparql2impala.op.ImpalaLeftJoin;
import sparql2impala.op.ImpalaOp;
import sparql2impala.op.ImpalaOrder;
import sparql2impala.op.ImpalaProject;
import sparql2impala.op.ImpalaReduced;
import sparql2impala.op.ImpalaSequence;
import sparql2impala.op.ImpalaSlice;
import sparql2impala.op.ImpalaUnion;
import sparql2impala.op.ImpalaGroup;
import sparql2impala.op.ImpalaExtend;
import sparql2impala.sql.Join;
import sparql2impala.sql.JoinType;
import sparql2impala.sql.JoinUtil;
import sparql2impala.sql.SQLStatement;
import sparql2impala.sql.Schema;


public class ImpalaOpTranslator extends ImpalaOpVisitorBase {


	private boolean expandPrefixes;

	private int countBGP, countJoin, countLeftJoin, countUnion, countSequence,
			countFilter;

	private Stack<SQLStatement> stack = new Stack<SQLStatement>();


	public ImpalaOpTranslator() {
		countBGP = 0;
		countJoin = 0;
		countLeftJoin = 0;
		countUnion = 0;
		countSequence = 0;
		countFilter = 0;
	}


	public String translate(ImpalaOp op, boolean _expandPrefixes) {
		expandPrefixes = _expandPrefixes;

		ImpalaOpWalker.walkBottomUp(this, op);

		//String raw = Tags.QUERY_PREFIX + stack.pop().toString() + " ; "+ Tags.QUERY_SUFFIX;
		String raw = stack.pop().toString() ;
		//return clean(raw);
		return raw;
	}

	private String clean(String raw) {
		StringBuilder sb = new StringBuilder();
		Stack<Character> chars = new Stack<Character>();
		for (Character c : raw.toCharArray()) {
			if (c.equals('\'')) {
				if (!chars.isEmpty() && chars.peek().equals('\'')) {
					chars.pop();
				} else {
					chars.push('\'');
				}
			} else if (!chars.isEmpty() && c.equals('"')) {
				if (chars.peek().equals('"')) {
					chars.pop();
				} else {
					chars.push('"');
				}
			}
			if (c.equals(':')) {
				if (chars.isEmpty()) {
					sb.append('_');
				} else {
					sb.append(':');
				}
			} 
			if (c.equals('-')) {
				if (chars.isEmpty()) {
					sb.append('-');
				} else {
					sb.append('_');
				}
			}
			else {
				sb.append(c);
			}

		}
		return sb.toString();
	}

	/**
	 * Translates a BGP 
	 */
	@Override
	public void visit(ImpalaBGP bgp) {
		countBGP++;
		bgp.setExpandMode(expandPrefixes);
		stack.push(bgp.translate(Tags.BGP + countBGP));
	}

	/**
	 * Translates a FILTER 
	 */
	@Override
	public void visit(ImpalaFilter filter) {
		countFilter++;
		stack.push(filter.translate(Tags.FILTER + countFilter, stack.pop()));
	}

	/**
	 * Translates a JOIN
	 */
	@Override
	public void visit(ImpalaJoin join) {
		countJoin++;
		SQLStatement right = stack.pop();
		SQLStatement left = stack.pop();
		stack.push(join.translate(Tags.JOIN + countJoin, left, right));
	}

	/**
	 * Translates a sequence of JOINs 
	 */
	@Override
	public void visit(ImpalaSequence sequence) {
		Stack<SQLStatement> children_rev = new Stack<SQLStatement>();
		for (int i = 0; i < sequence.size(); i++) {
			children_rev.push(stack.pop());
		}
		List<SQLStatement> children = new ArrayList<SQLStatement>();
		for (int i = 0; i < sequence.size(); i++) {
			children.add(children_rev.pop());
		}

		countJoin++;
		sequence.setResultName(Tags.SEQUENCE + countJoin);

		Map<String, String[]> filterSchema = new HashMap<String, String[]>();
		String tablename = sequence.getResultName();
		List<String> onStrings = new ArrayList<String>();
		for (int i = 1; i < children.size(); i++) {
			Map<String, String[]> leftschema = Schema.shiftToParent(sequence
					.getElements().get(i - 1).getSchema(), sequence
					.getElements().get(i - 1).getResultName());
			Map<String, String[]> rightschema = Schema.shiftToParent(sequence
					.getElements().get(i).getSchema(), sequence.getElements()
					.get(i).getResultName());
			filterSchema.putAll(leftschema);
			filterSchema.putAll(rightschema);
			onStrings.add(JoinUtil.generateConjunction(JoinUtil
					.getOnConditions(leftschema, rightschema)));
		}

		sequence.setSchema(filterSchema);
		Join join = new Join(tablename, children.get(0), children.subList(1,
				children.size()), onStrings, JoinType.natural);

		stack.push(join);
	}

	/**
	 * Translates a LEFTJOIN 
	 */
	@Override
	public void visit(ImpalaLeftJoin impalaLeftJoin) {

		SQLStatement right = stack.pop();
		SQLStatement left = stack.pop();
		stack.push(impalaLeftJoin.translate(Tags.LEFT_JOIN + countLeftJoin++,
				left, right));
	}


	/**
	 * Translates a UNION 
	 */
	@Override
	public void visit(ImpalaUnion union) {
		countUnion++;
		SQLStatement right = stack.pop();
		SQLStatement left = stack.pop();
		SQLStatement s = union.translate(Tags.UNION + countUnion, left, right);
		stack.push(s);
	}

	/**
	 * Translates a PROJECT 
	 */
	@Override
	public void visit(ImpalaProject impalaProject) {
		SQLStatement projection = impalaProject.translate(Tags.PROJECT,
				stack.pop());
		stack.push(projection);
	}

	/**Translate a GROUP BY**/
	@Override
	public void visit(ImpalaGroup impalaGroup) {
		SQLStatement group = impalaGroup.translate(Tags.GROUP,
				stack.pop());
		stack.push(group);
	}
	
	/**Translate an Extend**/
	@Override
	public void visit(ImpalaExtend impalaExtend) {
		SQLStatement extend = impalaExtend.translate(Tags.EXTEND,
				stack.pop());
		stack.push(extend);
	}
	
	
	/**
	 * Translates a DISTINCT 
	 */
	@Override
	public void visit(ImpalaDistinct distinct) {
		SQLStatement sql = stack.pop();
		sql.setDistinct(true);
		stack.push(sql);
	}

	/**
	 * Translates a REDUCE 
	 */
	@Override
	public void visit(ImpalaReduced impalaReduced) {
		throw new NotImplemented();
		// String[] s = impalaReduced.translate(Tags.REDUCED);
		// prependToScript(s[0]);
		// appendToScript(s[1]);
	}

	/**
	 * Translates an ORDER 
	 */
	@Override
	public void visit(ImpalaOrder impalaOrder) {
		SQLStatement s = impalaOrder.translate(Tags.ORDER, stack.pop());
		stack.push(s);

	}

	/**
	 * Translates a SLICE 
	 */
	@Override
	public void visit(ImpalaSlice slice) {
		SQLStatement child = stack.pop();
		stack.push(slice.translate(child.getName(), child));
	}

}

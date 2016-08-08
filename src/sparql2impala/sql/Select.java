package sparql2impala.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Select extends SQLStatement {

	
	//Utilizou-se LinkedHashMap para garantir que os seletores sejam apresentados na ordem estabelecida na consulta SPARQL
	//LinkedHashMap defines the iteration ordering, which is normally the order in which keys were inserted into the map
	LinkedHashMap<String, String[]> selection = new LinkedHashMap<String, String[]>();

	public Select(String tablename) {
		super(tablename);
	}

	private int limit = -1;
	private int offset = -1;
	
	
	public void addSelector(String alias, String[] selector) {
		selection.put(alias, selector);
	}

	protected String order = "";

	protected String from = "";

	public void appendToFrom(String s) {
		from += s;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	protected String where = "";

	public void addWhereConjunction(String condition) {
		if (where.equals("")) {
			where += condition;
		} else {
			where += " AND " + condition;
		}
	}

	protected String having = "";

	public void addHavingConjunction(String condition) {
		if (having.equals("")) {
			having += condition;
		} else {
			having += " AND " + condition;
		}
	}
	
	protected String group_by = "";

	public void addGroupBy(String vars) {
		if (group_by.equals("")) {
			group_by += vars;
		} else {
			group_by += ", " + vars;
		}
	}
	
	public void addOrder(String by) {
		this.order = by;
	}

	public String toString() {
		//System.out.println(statementName);
		StringBuilder sb = new StringBuilder("");
		if(this.statementName.matches("PROJECTION")){
			sb.append("SELECT ");
		}
		else{
			sb.append("(SELECT ");
		}
		if(isDistinct)
			sb.append(" DISTINCT ");
		if (selection.size() == 0) {
			sb.append("* ");
		} else {
			boolean first = true;
			for (String key : selection.keySet()) {
				String[] selector = selection.get(key);
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				if (selector.length > 1) {
					sb.append(selector[0]+"." + selector[1]+  " AS " + "\"" + key + "\" ");
				} else {
					sb.append(selector[0]+  " AS " + "\"" + key + "\" ");
				}
				//System.out.println(sb);
			}
		}
		sb.append("\n FROM ");
		// pretty formating
		if (from.length() > 10)
			sb.append("\n");
		sb.append(from);
		if (!this.where.equals("")) {
			sb.append(" \n WHERE ");
			// pretty formating
			if (where.length() > 10)
				sb.append("\n");
			sb.append(where);
		}
		if (!this.group_by.equals("")) {
			sb.append(" \n GROUP BY ");
			sb.append(group_by);
		}
		if (!this.having.equals("")) {
			sb.append(" \n HAVING ");
			// pretty formating
			if (where.length() > 10)
				sb.append("\n");
			sb.append(having);
		}
		//Fazer com que o Order By so saia no Projection
		if (!this.order.equals("") && this.statementName.matches("PROJECTION")) {
			sb.append("\n ORDER BY ");
			sb.append(order);
		}
		//Fazer com que o Limit so saia no Projection
		if(this.limit != -1 && this.statementName.matches("PROJECTION")){
			sb.append("\n LIMIT ");
			sb.append(this.limit);
		}
		//Fazer com que o Offset saia no Projection, depois do Limit
		if(this.offset != -1 && this.statementName.matches("PROJECTION")){
			sb.append("\n OFFSET ");
			sb.append(this.offset);
		}
		if(!this.statementName.matches("PROJECTION")){
			sb.append(")");
		}
		return sb.toString();
	}

	@Override
	public HashMap<String, String[]> getSelectors() {
		return this.selection;
	}

	@Override
	public void updateSelection(Map<String, String[]> resultSchema) {
		for (String key : resultSchema.keySet()) {
			if (this.selection.containsKey(key)) {
				String[] entry;
				if(selection.get(key).length >1){
					entry = new String[]{selection.get(key)[0], resultSchema.get(key)[1]};
				} else{
					entry = new String[]{ resultSchema.get(key)[0]};
				}
				this.selection.put(key, entry);
			}
		}

	}

	@Override
	public void removeNullFilters() {
		String[] filters =  where.split(" AND ");
		for(int i = 0; i < filters.length; i++){
			if(filters[i].toLowerCase().contains("is not null")){
				filters[i] = "";
			}
		}
		this.where = "";
		for(String filter : filters){
			if(!filter.equals(""))
			this.addWhereConjunction(filter);
		}
		
	}

	public void setName(String string) {
		this.statementName = string;
		
	}

	
	public String getName(){
		return this.statementName;
	}

	@Override
	public void addLimit(int i) {
		this.limit = i;
		
	}

	@Override
	public Integer getLimit() {
		return this.limit;
		
	}
	
	@Override
	public boolean addOffset(int i) {
		if(!this.order.equals("")){
			this.offset = i;
			return true;
		} 
		return false;
	}

	@Override
	public String getOrder() {
		return this.order;
	}

	@Override
	public Integer getOffset() {
		return this.offset;
	}
}

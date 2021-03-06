package sparql2impala.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class SQLStatement {

	protected String statementName;
	protected boolean isDistinct = false;

	public String getName() {
		return statementName;
	}

	public SQLStatement(String name) {
		this.statementName = name;
	}
	
	public String getStatementName(){
		return this.statementName;
	}

	public abstract void addWhereConjunction(String where);
	
	public abstract void addHavingConjunction(String having);

	public abstract void addOrder(String byColumn);
	
	public abstract String getOrder();
	
	public abstract Integer getLimit();
	
	public abstract Integer getOffset();

	public abstract String toString();


	public String toNamedString() {
		return this.toString() + " " + statementName;
	}

	public abstract void addSelector(String alias, String[] selector);

	public abstract HashMap<String, String[]> getSelectors();

	public abstract void updateSelection(Map<String, String[]> resultSchema);

	
	public abstract void addLimit(int i);
	public abstract boolean addOffset(int i);
	
	public void setDistinct(boolean b) {
		this.isDistinct = b;
	}

	public abstract void removeNullFilters();
}

package sparql2impala;

import java.util.HashMap;


public final class Tags {

	//Constantes
	public static final String IMPALA_TABLENAME = "bigtable_parquet";
	public static final String IMPALA_TABLENAME_TRIPLESTORE = "triplestore_parquet";
	public static final String IMPALA_BGP = "ImpalaBGP";
	public static final String IMPALA_FILTER = "ImpalaFilter";
	public static final String IMPALA_JOIN = "ImpalaJoin";
	public static final String IMPALA_SEQUENCE = "ImpalaSequenceJoin";
	public static final String IMPALA_LEFTJOIN = "ImpalaLeftJoin";
	public static final String IMPALA_CONDITIONAL = "ImpalaConditional";
	public static final String IMPALA_UNION = "ImpalaUnion";
	public static final String IMPALA_PROJECT = "ImpalaProject";
	public static final String IMPALA_DISTINCT = "ImpalaDistinct";
	public static final String IMPALA_ORDER = "ImpalaOrder";
	public static final String IMPALA_SLICE = "ImpalaSlice";
	public static final String IMPALA_REDUCED = "ImpalaReduced";

	public static final String BGP = "BGP";
	public static final String FILTER = "FILTER";
	public static final String JOIN = "JOIN";
	public static final String SEQUENCE = "SEQUENCE_JOIN";
	public static final String LEFT_JOIN = "OPTIONAL";
	public static final String CONDITIONAL = "OPTIONAL";
	public static final String UNION = "UNION";
	public static final String PROJECT = "PROJECTION";
	public static final String DISTINCT = "SM_Distinct";
	public static final String ORDER = "SM_Order";
	public static final String SLICE = "SM_Slice";
	public static final String REDUCED = "SM_Reduced";
	public static final String GROUP = "AGREG";
	public static final String EXTEND = "EXTEND";

	public static final String GREATER_THAN = " > ";
	public static final String GREATER_THAN_OR_EQUAL = " >= ";
	public static final String LESS_THAN = " < ";
	public static final String LESS_THAN_OR_EQUAL = " <= ";
	public static final String EQUALS = " = ";
	public static final String NOT_EQUALS = " != ";
	public static final String LOGICAL_AND = " AND ";
	public static final String LOGICAL_OR = " OR ";
	public static final String LOGICAL_NOT = "NOT ";
	public static final String BOUND = " is not NULL";
	public static final String NOT_BOUND = " is NULL";

	public static final String NO_VAR = "#noVar";
	public static final String NO_SUPPORT = "#noSupport";

	public static final String QUERY_PREFIX = "DROP TABLE IF EXISTS result;\n Create Table result as ";
	public static final String QUERY_SUFFIX = "\nPROFILE; ";

	public static final String OFFSETCHAR = "\t";
	public static final String SUBJECT_COLUMN_NAME = "subject";
	public static final String PREDICATE_COLUMN_NAME_TRIPLESTORE = "predicate";
	public static final String OBJECT_COLUMN_NAME_TRIPLESTORE = "object";

	public static final int LIMIT_LARGE_NUMBER = 100000000;
	public static final String ADD = "+";
	public static final String SUBTRACT = "-";
	public static final String LIKE = " LIKE ";
	public static final String LANG_MATCHES = " LIKE ";

	
	public static String delimiter = " ";
	public static String defaultReducer = "%default reducerNum '1';";
	public static String udf = "ImpalaSPARQL_udf.jar";
	public static String indata = "indata";
	public static String rdfLoader = "impalasparql.rdfLoader.ExNTriplesLoader";
	public static String resultWriter = "ImpalaStorage";
	public static int globalOffsetTabs = 0;

	public static boolean expandPrefixes = false;
	public static boolean optimizer = true;
	public static boolean joinOptimizer = false;
	public static boolean filterOptimizer = true;
	public static boolean bgpOptimizer = true;


	public static HashMap<String, String> restrictedNames = new HashMap<String, String>();
	static {
		restrictedNames.put("comment", "comme");
		restrictedNames.put("date", "dat");
		restrictedNames.put("?comment", "comme");
		restrictedNames.put("?date", "dat");
	}


	private Tags() {
	}

}

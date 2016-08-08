package sparql2impala.sql;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.algebra.op.OpProject;
import com.hp.hpl.jena.sparql.util.FmtUtils;

public class SearchTable {
/*	
	public static String setTipoTabela(String tipo){
		if(tipo.equals("snappy")){
			return "snappy";
		}
		else{
			return "snappy";
		}
		
	}
	public static String setTamanho(String tamanho){
		if(tamanho.equals("100M")){
			return "100M";
		}
		else if(tamanho.equals("200M")){
			return "200M";
		}
		else{
			return "300M";
		}
		
	}*/

	public static String scanTable(Node predicate){
		
		String table;
		String string = FmtUtils.stringForNode(predicate);
		
		if(string.equalsIgnoreCase("<http://purl.org/linked-data/cube#dataSet>") ||
			string.equalsIgnoreCase("<http://purl.org/linked-data/cube#measureType>") ||
			string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#DateTime>") || 
			string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasDestinationNode>") || 
			string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasSourceNode>") || 
			string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasValue>")){
			return table = "parquet_snappy_compression.qb_Observation300M_snappy"; 
		}
		else if(string.equalsIgnoreCase("<http://www.geonames.org/ontology#name>") ||
			string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#continentCode>")){
			return table = "parquet_snappy_compression.MGC_Continent_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www.geonames.org/ontology#population>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#areaInSqKm>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#currency>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#languages>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#countryCode>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#capitalName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#ContinentName>")){
				return table = "parquet_snappy_compression.MGC_Country_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#population>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#name>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#postalcode>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#GeoState>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#GeoCountry>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#GeoGMTOffset>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#GeoContinent>") ||
				string.equalsIgnoreCase("<http://www.geonames.org/ontology#parentCountry>") ||
				string.equalsIgnoreCase("<http://www.geonames.org/ontology#parentADM1>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#isInCountry>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#isInState>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#isInContinent>")){
				return table = "parquet_snappy_compression.MGC_Town_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeIP>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeFullName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeNickName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeSiteName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasNodeURL>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasLocationDescription>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasProjectType>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasTraceServer>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasGMTOffset>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasPingServer>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasDataServer>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasAppUser>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#PingERLat>")||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#PingERLong>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#isInTown>")){
				return table = "parquet_snappy_compression.MGC_NodeInformation_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#displayValue>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#defaultUnit>")){
				return table = "parquet_snappy_compression.qb_measureType_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www.fp7-moment.eu/MomentUnits.owl#hasSymbol>")){
				return table = "parquet_snappy_compression.MU_unitMeasure_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www.fp7-moment.eu/MomentDataV2.owl#PacketSizeValue>")){
				return table = "parquet_snappy_compression.MD_PacketSize_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www.w3.org/2006/time#unitType>") ||
				string.equalsIgnoreCase("<http://www.w3.org/2006/time#year>") ||
				string.equalsIgnoreCase("<http://www.w3.org/2006/time#month>") ||
				string.equalsIgnoreCase("<http://www.w3.org/2006/time#day>") ||
				string.equalsIgnoreCase("<http://www.w3.org/2006/time#dayOfYear>") ||
				string.equalsIgnoreCase("<http://www.w3.org/2006/time#dayOfWeek>")){
				return table = "parquet_snappy_compression.PingER_ont_DateTime_snappy";
		}
		else if(string.equalsIgnoreCase("<http://purl.org/linked-data/cube#dimension>") ||
				string.equalsIgnoreCase("<http://purl.org/linked-data/cube#measure>") ||
				string.equalsIgnoreCase("<http://purl.org/linked-data/cube#attribute>")){
				return table = "parquet_snappy_compression.qb_DataStructureDefinition_snappy";
		}
		else if(string.equalsIgnoreCase("<http://purl.org/linked-data/cube#structure>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/pinger.owl#hasDefaultPacketSize>")){
				return table = "parquet_snappy_compression.qb_DataSet_snappy";
		}
		else if(string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolType>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolNumberOfStudents>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolNumberOfUgradStudents>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolNumberOfGradStudents>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolEndowment>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolFacultySize>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolName>") ||
				string.equalsIgnoreCase("<http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#SchoolPingerName>")){
				return table = "parquet_snappy_compression.MGC_School_snappy";
		}
		else{return table = "XX";}


	}
}

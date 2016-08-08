package sparql2impala;

//java.sql packages are required
import java.sql.*;
//java.math packages are required
import java.math.*;

import com.hp.hpl.jena.sparql.algebra.op.OpProject;
import com.hp.hpl.jena.sparql.core.Var;

import java.util.List;

import sparql2impala.op.ImpalaProject;


class ImpalaJDBCConnection{
	//Define a string as the fully qualified class name (FQCN) of
	//the desired JDBC driver
	static String JDBCDriver = "com.cloudera.impala.jdbc3.Driver";
	//Define a string as the connection URL
	static String ConnectionURL = "jdbc:impala://localhost:21050";

	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	
	static double msElapsedTime;
	
	
	public static String getTimeExecution(){
		return Double.toString(msElapsedTime);
	}
	
	public static String ExecuteQuery(String sqlScript){
		
		OpProject opProject = ImpalaProject.getOpProject();
		//Define the SQL statement for a query
		//String query = "SELECT gn_ont_name FROM parquet_gzip_compression.mgc_continent_gzip LIMIT 20";
		String resultado = "<html><table><tr>";
		
		try {
			//Register the driver using the class name
			Class.forName(JDBCDriver);
			//Establish a connection using the connection URL
			con = DriverManager.getConnection(ConnectionURL);
			//Create a Statement object for sending SQL
			//statements to the database
			stmt = con.createStatement();
			
			long start = System.currentTimeMillis();
			//stmt = con.createStatement();
			//Execute the SQL statement
			rs = stmt.executeQuery(sqlScript);
			long finish = System.currentTimeMillis();
			msElapsedTime = ((finish - start)*0.001);
			System.out.println(msElapsedTime);
			
		
			//Cria o cabeçalho do resultado da query
	        String[] arrayTitle = new String[opProject.getVars().size()];
			int i=0;
	        for(Var var : opProject.getVars()){
	      	  arrayTitle[i] = var.toString(); 
	      	  resultado = resultado +  "<td>" + arrayTitle[i] + "</td>";
	      	  i++;
	        }
			resultado = resultado + "</tr>";
			
			
			while(rs.next()) {
				
				String[] array = new String[opProject.getVars().size()];
				int j=0;
				resultado = resultado + "<tr>";
				//Recupera valores da linha onde o cursor esta atualmente usando os nomes das colunas recuperados
				for(Var var : opProject.getVars()){
		        	  array[j] = rs.getString(var.toString().replace("?", "")); 
		        	  if(array[j]==null){
		        		  resultado = resultado + "<td>" + array[j] + "</td>";
		        	  }
		        	  else{
		        		  resultado = resultado + "<td>" + array[j].replace("<", "&#60") + "</td>";  
		        	  }
		        	  j++;
		        }
				resultado = resultado + "</tr>";
			}
			resultado = resultado + "</table></html>";
			
			
			
		} catch (SQLException se) {
			//Handle errors encountered during interaction
			//with the data source
			se.printStackTrace();
		} catch (Exception e) {
			//Handle other errors
			e.printStackTrace();
		} finally {
			//Perform clean up
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se1) {
				// Log this
			}
			try {
				if (stmt!=null) {
					stmt.close();
				}
			} catch (SQLException se2) {
				// Log this
			}
			try {
				if (con!=null) {
					con.close();
				}
			} catch (SQLException se3) {
				// Log this
				se3.printStackTrace();
			} // End try
		} // End try
		
		//Retorna o resultado da query formatado
		return resultado;
		
	} // End main
}

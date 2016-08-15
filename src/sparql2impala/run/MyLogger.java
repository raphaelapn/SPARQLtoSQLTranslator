package sparql2impala.run;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class MyLogger {
	
	long startTime;
	
	FileWriter outFile;
	PrintWriter out;
	

	public MyLogger(String logFile){
		startTime = System.currentTimeMillis();
		try {
			outFile = new FileWriter(logFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out = new PrintWriter(outFile);

	}


	public long getRuntime(){
		return System.currentTimeMillis() - startTime;
	}
	

	
	public void processOptions(){
		
	}
	public void logNewSeparator(){
		out.println(MyLoggerHelper.iterationSeparator);
	}
	
	public void log(String s){
		out.println(MyLoggerHelper.wrapInFrame(s));
	}


	public void logRuntime() {
		logNewSeparator();
		out.println(MyLoggerHelper.wrapInFrame("Total runtime: " + getRuntime()/1000 + "s"));
		logNewSeparator();
		
	}


	public void flushLog() {
		out.flush();
		
	}

	public void closeLog() {
		out.close();
		
	}

}

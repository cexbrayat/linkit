package controllers;

import java.io.File;

import play.Play;
import play.modules.cobertura.CoberturaPlugin;
import play.mvc.Controller;

public class Cobertura  extends Controller{
	
	public static boolean reportExist = false;
	
	public static void index(){
		
		//Check if previous report exist
		if(!reportExist){
		
			String coverageTestPath = Play.applicationPath + CoberturaPlugin.separator + "test-result" 
				+ CoberturaPlugin.separator + "code-coverage" + CoberturaPlugin.separator + "index.html";
			
			File f = new File(coverageTestPath);
			
			if(f.exists())
				reportExist = true;
		}
		
		boolean reportExistTmp = reportExist; // use local variable because global variable is not report on view ?
		render(reportExistTmp);
	}
	
	public static void generateReport(){
		CoberturaPlugin.forceReportWriting();
		Cobertura.index();
	}
	
	public static void clear(){
		CoberturaPlugin.forceInit();
		Cobertura.generateReport();
	}
	
	public static void cleanReport(){
		Cobertura.index();
	}
}

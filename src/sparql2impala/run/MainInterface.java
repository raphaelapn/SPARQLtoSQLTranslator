package sparql2impala.run;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import sparql2impala.TranslatorInterface;

import javax.swing.*;
import java.awt.*;


public class MainInterface {


	public static void main(String[] args) {
		
		TranslatorInterface translator = new TranslatorInterface();
		
		translator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		translator.getContentPane().setBackground(Color.WHITE);
		translator.setTitle("Tradutor Linguagem SPARQL para SQL");
		translator.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		translator.setResizable(true);
		translator.setVisible(true);
		translator.setLocationRelativeTo(null);
	//	translator.pack();
        
		
	
	}

}
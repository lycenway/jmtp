/*
 * Copyright 2007 Pieter De Rycke
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

package jmtp.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 *
 * @author Pieter De Rycke
 */
public class PropertyKeyReader {
    
    private static final String HEADER_FILE = 
            "C:\\Documents and Settings\\Pieter De Rycke\\Bureaublad\\Include\\PortableDevice.h";
    
    private static final boolean PRINT_COMMENTS = false;
    
    private StringBuilder buffer;
    
    public PropertyKeyReader(File header) {
    	buffer = new StringBuilder();
    	process(header);
    }
    
    private void process(File header) {
    	BufferedReader reader = null;
    	try {
    		try {
    			reader = 
    				new BufferedReader(new InputStreamReader(new FileInputStream(header)));
    			
    			String line = reader.readLine();
    			while(line != null) {
    				if(line.startsWith("DEFINE_PROPERTYKEY")) {
                        String[] defineParts = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim().split(",");
                        
                        String name = defineParts[0];
                        String[] arguments = new String[defineParts.length - 1];
                        for(int i = 1; i < defineParts.length; i++)
                            arguments[i - 1] = defineParts[i];
                        
                        processPropertyKey(name, arguments);
    				}
    				else if(line.startsWith("DEFINE_GUID")) {
                        String[] defineParts = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim().split(",");
                        
                        String name = defineParts[0];
                        String[] arguments = new String[defineParts.length - 1];
                        for(int i = 1; i < defineParts.length; i++)
                            arguments[i - 1] = defineParts[i];
                        
                        processGuid(name, arguments);
                    }
    				else if(line.startsWith("#define")) {
    					String[] parts = line.trim().split(" ");
    					
    					if(parts[2].startsWith("L\"") && parts[2].endsWith("\"")) {
    						processString(parts[1], parts[2].substring(2, parts[2].length() - 2));
    					}    					
    				}
    				
    				line = reader.readLine();
    			}
    		}
    		finally {
    			if(reader != null)
    				reader.close();
    		}
    	}
    	catch(IOException e) {}
    }
    
    private void processPropertyKey(String name, String[] arguments) {
        buffer.append("\tstatic final PropertyKey " + name.trim()
                            + " = new PropertyKey(new Guid(" + arguments[0].trim() + "l, " + arguments[1].trim()
                            + ", " + arguments[2].trim() + ", new short[]{" + arguments[3].trim() 
                            + ", " + arguments[4].trim() + ", " + arguments[5].trim() + ", " 
                            + arguments[6].trim() + ", " + arguments[7].trim() + ", " 
                            + arguments[8].trim() + ", " + arguments[9].trim() 
                            + ", " + arguments[10].trim() + "}), " + arguments[11].trim() + ");" + "\n");
    }
    
    private void processGuid(String name, String[] arguments) {
    	buffer.append("\tstatic final Guid " + name.trim()
                + " = new Guid(" + arguments[0].trim() + "l, " + arguments[1].trim()
                            + ", " + arguments[2].trim() + ", new short[]{" + arguments[3].trim() 
                            + ", " + arguments[4].trim() + ", " + arguments[5].trim() + ", " 
                            + arguments[6].trim() + ", " + arguments[7].trim() + ", " 
                            + arguments[8].trim() + ", " + arguments[9].trim() 
                            + ", " + arguments[10].trim() + "});" + "\n");
    }
    
    private void processString(String name, String value) {
    	buffer.append("\tstatic final String " + name + " = \"" + value + "\";\n");
    }
    
    public void save(File outputDirectory, String packageName, String className) {
    	OutputStreamWriter writer = null;
    	try {
    		try {
    			File sourceFile = new File(outputDirectory.getAbsoluteFile(), className + ".java");
    	    	sourceFile.createNewFile();
    	    	
    			writer = new OutputStreamWriter(new FileOutputStream(sourceFile));
    			if(packageName != null) {
    				writer.write("package " + packageName + ";\n");
    				writer.write("\n");
    	        }
    	        
    			writer.write("import be.derycke.pieter.com.Guid;\n");
    	        writer.write("\n");
    	        writer.write("public class " + className + " {\n");
    	        
    			writer.write(buffer.toString());
    			
    			writer.write("}");
    			writer.flush();
    		}
    		finally {
    			if(writer != null)
    				writer.close();
    		}
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) {
    	PropertyKeyReader reader = new PropertyKeyReader(new File(HEADER_FILE));
    	reader.save(new File("src\\jmtp"), "jmtp", "Win32WPDDefines");
    }
    
    /*
    public static void main(String[] args) {
        try {
            BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(new FileInputStream(HEADER_FILE)));
            
            processDocumentStart("jmtp", "Win32WPDDefines");
            
            String section = null;
            String comment = null;
            String line = reader.readLine();
            while(line != null) {
                if(line.startsWith("DEFINE_PROPERTYKEY")) {
                    String[] defineParts = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim().split(",");
                    
                    String name = defineParts[0];
                    String[] arguments = new String[defineParts.length - 1];
                    for(int i = 1; i < defineParts.length; i++)
                        arguments[i - 1] = defineParts[i];
                    
                    processPropertyKey(name, arguments, comment);
                }
                else if(line.startsWith("DEFINE_GUID")) {
                    String[] defineParts = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim().split(",");
                    
                    String name = defineParts[0];
                    String[] arguments = new String[defineParts.length - 1];
                    for(int i = 1; i < defineParts.length; i++)
                        arguments[i - 1] = defineParts[i];
                    
                    processGuid(name, arguments);
                }
                else if(line.startsWith("//")) {
                    String newCommentLine = line.substring(2).trim();
                    if(!newCommentLine.equals("")) {
                        if(comment == null)
                            comment = newCommentLine;
                        else
                            comment += "\n" + newCommentLine;                                
                    }

                }
                else if(line.startsWith("/*")) {
                    section = "";
                }
                else if(line.startsWith("*")) {
                    if(line.endsWith("*///")) {
                        /*processNewSection(section);
                    }
                    else {
                        if(section == null)
                            section = "";
                        else
                            section += "\n";
                        section += line.replaceFirst("\\**", "").trim();
                    }
                        
                }
                else {
                }
                
                //een gewone comment is afgelopen
                if(!line.startsWith("//")) {
                    comment = null;
                }
                
                //een section comment is afgelopen
                if(!line.startsWith("*")) {
                    section = null;
                }
                
                line = reader.readLine();
            }
            
            System.out.println("}");
            
            //bestand afgelopen
            
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/
    
    private static void processDocumentStart(String packageName, String className) {
        if(packageName != null) {
            System.out.println("package " + packageName + ";");
            System.out.println("");
        }
        
        System.out.println("import be.derycke.pieter.com.Guid;");
        System.out.println("");
        System.out.println("public class " + className + " {");
    }
    
    private static void processNewSection(String comment) {
        //System.out.println("\t/*");
        //System.out.println("\t* "+ comment);
        //System.out.println("\t*/");
    }
    
    private static void processPropertyKey(String name, String[] arguments, String comment) {
        System.out.println("\tstatic final PropertyKey " + name.trim()
                            + " = new PropertyKey(new Guid(" + arguments[0].trim() + "l, " + arguments[1].trim()
                            + ", " + arguments[2].trim() + ", new short[]{" + arguments[3].trim() 
                            + ", " + arguments[4].trim() + ", " + arguments[5].trim() + ", " 
                            + arguments[6].trim() + ", " + arguments[7].trim() + ", " 
                            + arguments[8].trim() + ", " + arguments[9].trim() 
                            + ", " + arguments[10].trim() + "}), " + arguments[11].trim() + ");");
    }
    
    private static void processGuid(String name, String[] arguments, String dfs) {
        System.out.println("\tstatic final Guid " + name.trim()
                + " = new Guid(" + arguments[0].trim() + "l, " + arguments[1].trim()
                            + ", " + arguments[2].trim() + ", new short[]{" + arguments[3].trim() 
                            + ", " + arguments[4].trim() + ", " + arguments[5].trim() + ", " 
                            + arguments[6].trim() + ", " + arguments[7].trim() + ", " 
                            + arguments[8].trim() + ", " + arguments[9].trim() 
                            + ", " + arguments[10].trim() + "});");
    }

}

package com.Mina.FileNet.util;

import javax.mail.*;
import javax.mail.internet.*;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class SendMail extends javax.mail.Authenticator
{


//	private static final String SMTP_AUTH_USER = "SODIC\\ECM";
//	private static final String SMTP_AUTH_PWD = "Ec1234567";
//	private static final String from = "ECM@Sodic.com";
//	private static final String MailServer = "SODIC";
//	private static final String TarekMail ="tbarakat@sodic.com";
//	String baseP8URL = "http://172.16.10.38:9080/WorkplaceXT/";
	
	private static final String SMTP_AUTH_USER = "prosoftmailjavatest@gmail.com";
	private static final String SMTP_AUTH_PWD  = "Fn1234567";
	private static final String from = "prosoftmailjavatest@gmail.com";
	private static final String MailServer="Gmail";
	String baseP8URL = "http://50.0.0.43:9080/WorkplaceXT/";
	private static String logFile="";
    
	Date today = Calendar.getInstance().getTime();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
	private ArrayList<String> PropertiesDisplayNames=new ArrayList<String>(Arrays.<String>asList("Ball In Court" ,"City Villa Category" ,"City Villa Project" ,"Close-Out Category" ,"Consultant Closed Date" ,"Consultant Due Date" ,"Contract Agreement Category" ,"Due Date" ,"Facility Management Category" ,"Letter No" ,"Location" ,"MIR No" ,"NCR Category" ,"NCR Rev" ,"No Of Drawings" ,"Permits Category" ,"Report Category" ,"RFI Category" ,"RFI Rev" ,"SODIC Letter No" ,"SODIC Transmittal No" ,"Spec Dev Description" ,"Status" ,"Submittal Latest Rev" ,"Tender Document Category" ,"To Company" ,"TOC No" ,"Type Code" ,"Attention" ,"By" ,"Company" ,"Company Category" ,"Contract Document Category" ,"Date" ,"Defect Liability Period" ,"Drawing Latest Rev" ,"Drawing Rev" ,"End Defect Liability Period Date" ,"Info" ,"Insurance & Bonds Category" ,"IR Latest Rev" ,"Issue Date" ,"Library Category" ,"Material / Sample Category" ,"Multi Action" ,"Overdue" ,"Prequalification & Short List Category" ,"Procedure & Forms Category" ,"Project" ,"Project Group" ,"Reference" ,"Remarks" ,"RFI No" ,"Scope of Work" ,"SI Latest Rev" ,"SI No" ,"SI Rev" ,"Single Action" ,"SODIC S/N" ,"Spec Dev No" ,"Spec Sec No" ,"Statutory Authorities Category" ,"Subject*" ,"Submittal Category" ,"Submittal Rev" ,"Tender Submission Category" ,"Transmittal No" ,"Variation Category" ,"Action By" ,"Consultant" ,"Consultant Overdue" ,"Contract No" ,"Contractor Due Date" ,"Contractor Overdue" ,"Contractor Respond Date" ,"Description","Department" ,"Development" ,"Directed By" ,"Drawing No" ,"Drawing Type" ,"Incoming Date" ,"Incoming Ref No" ,"Initial & Acquisition Stage Category" ,"IR No" ,"IR Rev" ,"Item No" ,"Master Plan Category" ,"NCR Latest Rev" ,"NCR No" ,"Outgoing Date" ,"Outgoing Ref No" ,"Phase" ,"Prepared By" ,"Received Date" ,"Return Date" ,"RFI Latest Rev" ,"Signed By" ,"SODIC Memo No" ,"SODIC MOM No" ,"Spec Rev" ,"Spec Sec Name" ,"Sub Sec No" ,"Transmittal Rev" ,"Type" ,"CC*" ,"Others" ,"Submittal No","Description*","Description"));
	
	public void postMail( String[] recipients, String subject, String body,String[] properties,String[] propertiesValues,String contentUrl,String propertiesUrl,String AttachmentURL,Id docID,ObjectStore os) throws MessagingException
	  {
		  if(subject.compareTo("")==0)
	    {
	    	try {
	    		GetLogFilePath();
	    		writeToFile(logFile,true,"----------------------- SendMailUsingAuthentication Subject Or Description is empty  ------------------");
	    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    	}
	    }
	    else
	    {
	    	try {
	        	GetLogFilePath();
	        	writeToFile(logFile,true,"----------------------- SendMailUsingAuthentication.PostMail Triggered ------------------");
	            boolean debug = true;

	            Properties props = GetMailProperties(MailServer);
	    		
	            Session session = Session.getInstance(props, this);
	            session.setDebug(debug);

	            MimeMessage msg = new MimeMessage(session);
	          
	            InternetAddress addressFrom = new InternetAddress(from);
	            msg.setFrom(addressFrom);
	            InternetAddress[] addressTo = new InternetAddress[recipients.length];
	            for (int i = 0; i < recipients.length; i++) {
	                addressTo[i] = new InternetAddress(recipients[i],true);
	            }
	            msg.addRecipients(Message.RecipientType.TO, addressTo);

	            msg.setSubject(subject, "UTF-8");
	            String html=null;
	            if(AttachmentURL!=null)
	            {
	            	if(contentUrl!="")
	            		html = body + " \n<a href='"+contentUrl+"'>Document Link</a> "+", \n<a href='"+propertiesUrl+"'>Properties Link</a>"+", \n<a href='"+AttachmentURL+"'>Attachment Link</a> <br> <br>";
	            	else
	            		html = body + " \n<a href='"+propertiesUrl+"'>Properties Link</a>"+", \n<a href='"+AttachmentURL+"'>Attachment Link</a> <br> <br>";
	            }
	            else
	            {
	            	
	            	if(contentUrl!="")
	            		html = body + " \n<a href='"+contentUrl+"'>Document Link</a> "+", \n<a href='"+propertiesUrl+"'>Properties Link</a> <br> <br>";
	            	else
	            		html = body + " \n<a href='"+propertiesUrl+"'>Properties Link</a> <br> <br>";
	            }
//	              html = body + " \n<a href='"+contentUrl+"'>Document Link</a> "+", \n<a href='"+propertiesUrl+"'>Properties Link</a> <br> <br>";
	            if(properties!=null){
	            	for(int i=0;i<properties.length;i++){
	                	if(propertiesValues[i].length()>0){
	                		html = html + properties[i] +": " + propertiesValues[i] + "<br>";
	                	}
	                }
	            	
	            }

	    	    msg.setText(html, "UTF-8", "html");

	    	    try {
					Transport.send(msg);
					
				} catch (Exception e) {
					writeToFile(logFile, true, " Mail not sent SMTP ERROR"+e.getMessage());
					e.printStackTrace();
					Document doc=Factory.Document.fetchInstance(os, docID, null);
					if (doc.getProperties().getStringListValue("FileNetCommand") == null)
						doc.getProperties().putValue("FileNetCommand",Factory.StringList.createList());
					doc.getProperties().getStringListValue("FileNetCommand").add("SMTP Error");
					doc.save(RefreshMode.NO_REFRESH);
				}
	            
	        } 
	        catch (Throwable e) 
	        {
	        	 try{	
	        		 e.printStackTrace();
	    				writeToFile(logFile,true,formatter.format(today)+"---Exception--------SendMailUsingAuthentication.postMail------------");
	    				PrintWriter out = new PrintWriter(new FileWriter(logFile,true), true);
	    				e.printStackTrace(out);
	    				out.close();
	    				}catch(Exception t){
	    					
	    				}
	        }
	    	
	    }
	    
	  }
	
	public String GetCreatorMail(ObjectStore os,Document Doc)
	  {
		  String creatormail=Doc.get_Creator().toString().toLowerCase()+"@sodic.com";
		  return creatormail;
	  }

	public String GetDocPropertiesInHTML(ObjectStore os,Id id)
	  {
		  String HTML="";
		  SimpleDateFormat formatter2 = new SimpleDateFormat("MMMM d, yyyy");
		  Document doc = Factory.Document.fetchInstance(os,id, null);
	      ClassDefinition cls = Factory.ClassDefinition.fetchInstance(os, doc.getClassName(), null); // args[0] = class definition id
		    PropertyDefinitionList propList = cls.get_PropertyDefinitions();
		    Iterator propIterator = propList.iterator();
		    Iterator propsIterator = propList.iterator();
		    ArrayList<String>Properites=new ArrayList<String>();
		    while(propsIterator.hasNext())
		    {
		    	PropertyDefinition property=(PropertyDefinition)propsIterator.next();
		    	Properites.add(property.get_SymbolicName());
		    	
//		    	property.set_ChoiceList(null);
		    }
		    PropertyDefinition propDef = (PropertyDefinition) propIterator.next();
		    ArrayList propNames = new ArrayList();
		    ArrayList propValues = new ArrayList();
		    int infoSize=0;
		    int actionBySize=0;
		    String attention = "";
		    do{ 
		    	System.out.println(propDef.get_SymbolicName());
		    	if(doc.getProperties().getObjectValue(propDef.get_SymbolicName())!=null && PropertiesDisplayNames.contains(propDef.get_DisplayName().toString())){
		    		propNames.add(propDef.get_DisplayName());
		    		
		    		if(propDef.get_SymbolicName().compareTo("Development") == 0)
		    		{
		    			String str = doc.getProperties().getStringValue(propDef.get_SymbolicName());
		    			propValues.add(str.substring(str.indexOf("_")+1));
		    		}
		    		else if(propDef.get_SymbolicName().compareTo("Company") == 0)
		    		{
		    			if(cls.get_DisplayName().startsWith("13")||cls.get_DisplayName().startsWith("43")||cls.get_DisplayName().startsWith("44")||cls.get_DisplayName().startsWith("45")||cls.get_DisplayName().startsWith("46"))
		    			{
		    				if(doc.getProperties().getStringValue(propDef.get_SymbolicName())!=null&&doc.getProperties().getStringValue(propDef.get_SymbolicName()).length()>0)
		    				{
		    					String str = doc.getProperties().getStringValue(propDef.get_SymbolicName());
		    					propValues.add(str.substring(str.indexOf("_")+1));
		    				}
			    			else
			    			{
			    				propNames.remove(propDef.get_DisplayName());
			    			}
		    			}
		    			else
		    			{
	  					String str = doc.getProperties().getStringValue(propDef.get_SymbolicName());
	  					propValues.add(str.substring(str.indexOf("_")+1));
		    			}	
		    		}
		    		else if(propDef.get_SymbolicName().compareTo("Project") == 0|| propDef.get_SymbolicName().compareTo("ProjectGroup") == 0){
//		    			String project = doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(0).toString();
		    			
		    			if(cls.get_DisplayName().startsWith("13")||cls.get_DisplayName().startsWith("43")||cls.get_DisplayName().startsWith("44")||cls.get_DisplayName().startsWith("45")||cls.get_DisplayName().startsWith("46"))
		    			{
		    				if(doc.getProperties().getStringListValue(propDef.get_SymbolicName())!=null&&doc.getProperties().getStringListValue(propDef.get_SymbolicName()).size()>0)
			    				propValues.add(getPropertyFromCopiedDocs(os, id, propDef.get_SymbolicName()));
			    			else
			    			{
			    				propNames.remove(propDef.get_DisplayName());
			    			}
		    			}
		    			else
		    			{
		    				propValues.add(getPropertyFromCopiedDocs(os, id, propDef.get_SymbolicName()));
		    			}
		    			
		    		}else if(propDef.get_SymbolicName().contains("Date")||propDef.get_DataType().equals(TypeID.DATE)){
		    			propValues.add(formatter2.format(doc.getProperties().getDateTimeValue(propDef.get_SymbolicName())));
		    		}else if(propDef.get_SymbolicName().compareTo("Attention")==0 || propDef.get_SymbolicName().compareTo("DirectedBy")==0 || propDef.get_SymbolicName().compareTo("By")==0){
		    			String str = doc.getProperties().getStringValue(propDef.get_SymbolicName());
		    			propValues.add(str.substring(str.lastIndexOf("_")+1));
		    			if(propDef.get_SymbolicName().compareTo("Attention")==0)
		    				attention = str.substring(str.lastIndexOf("_")+1);
		    		}else if(propDef.get_Cardinality().toString().compareTo("LIST")==0||
		    				propDef.get_SymbolicName().compareTo("CC") == 0 ||
		    				propDef.get_SymbolicName().compareTo("Info") == 0 ||
		    				propDef.get_SymbolicName().compareTo("ToCompany") == 0 ||
		    				propDef.get_SymbolicName().compareTo("ActionBy") == 0 ||
		    				propDef.get_SymbolicName().compareTo("SignedBy") == 0||
		    				propDef.get_SymbolicName().compareTo("TypeCode") == 0 ||
		    				propDef.get_SymbolicName().compareTo("MultiAction") == 0 ){
		    			int size = doc.getProperties().getStringListValue(propDef.get_SymbolicName()).size();
		    			String str = "";
		    		        if(size>0){
		    		        	for(int i=0;i<size;i++){
		    		        		if(propDef.get_SymbolicName().compareTo("Info")==0 || propDef.get_SymbolicName().compareTo("ActionBy")==0 || propDef.get_SymbolicName().compareTo("SignedBy")==0){
		    		        			if(propDef.get_SymbolicName().compareTo("Info")==0){
		    		        				infoSize = size;
		    		        			}else if(propDef.get_SymbolicName().compareTo("ActionBy")==0){
		    		        				actionBySize = size;
		    		        			}
		    		        			str = str.concat(doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(i).toString().substring(doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(i).toString().lastIndexOf("_")+1)).concat(";");
		    		        		}else if(propDef.get_SymbolicName().compareTo("ToCompany")==0){
		    		        				str = str.concat(doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(i).toString().substring(doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(i).toString().indexOf("_")+1)).concat(";");
		    		        		}else{
		    		        				str = str.concat(doc.getProperties().getStringListValue(propDef.get_SymbolicName()).get(i).toString()).concat(";");
		    		        		}
		    		        	}
		    		        }
		    		        propValues.add(str);
		    		}
		    		else{//for each propvalues to exclude (SodicLetterNo , SodicMemoNo, SODICTransmittalNo)
		    			propValues.add(doc.getProperties().getObjectValue(propDef.get_SymbolicName()));
		    		}
		    	}
		    	propDef = (PropertyDefinition) propIterator.next();
		    }while(propIterator.hasNext());
		    if(!propNames.contains("Info"))
		    {
		    	String str1="";
		    	int size = doc.getProperties().getStringListValue("Info").size();
		    	for(int i=0;i<size;i++)
		    	{
		    		
		    		str1 =  str1.concat(doc.getProperties().getStringListValue("Info").get(i).toString().substring(doc.getProperties().getStringListValue("Info").get(i).toString().lastIndexOf("_")+1)).concat(";");
		    		
		    	}
		    	if(size>0)
		    	{
		    		propValues.add(str1);
		    		propNames.add("Info");
		    	}
		    }
		    String[] propNamesArray = new String[propNames.size()];
		    for (int i = 0; i < propNamesArray.length; i++) {
				propNamesArray[i] = propNames.get(i).toString();
			}
		    
		    String[] propValuesArray = new String[propValues.size()];
		    for (int i = 0; i < propValuesArray.length; i++) {
		    	propValuesArray[i] = propValues.get(i).toString();
		    	//System.out.println(propValuesArray[i]);
			}
		  
		    
		    
		    if(propNamesArray!=null){
	        	for(int i=0;i<propNamesArray.length;i++){
	            	if(propValuesArray[i].length()>0){
	            		HTML += propNamesArray[i] +": " + propValuesArray[i] + "<br>";
	            	}
	            }
	        	
	        }
		  
		  
		  return " <br> <br>".concat(HTML);
	  }

	public java.util.Properties GetMailProperties(String MailServer) throws IOException
	  {
		  java.util.Properties props = new java.util.Properties();
		    if (MailServer.compareTo("SODIC") == 0)
		    {
		      props.put("mail.smtp.host", "172.16.9.40");
		      props.put("mail.smtp.auth", "true");
		      props.setProperty("charset", "utf-8");
		      props.put("mail.smtp.port", "587");
		    }
		    else
		    {
		      props.put("mail.smtp.host", "smtp.gmail.com");
		      props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.port", "587");
		      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		    }
		    return props;
	  }
  
	public static void writeToFile(String file_path , boolean append_value,String textLine ) throws IOException 
	{
		FileWriter write = new FileWriter( file_path , append_value);
		PrintWriter print_line = new PrintWriter( write );
		String[] textArray = {textLine};
		print_line.printf( "%s" + "%n" , textArray);
		print_line.close();
	}

	public void GetLogFilePath() throws IOException
	{
		SimpleDateFormat Logformatter = new SimpleDateFormat("yyyy-MM-dd");
		String FileName=Logformatter.format(today).concat(".txt");
		String ClogFile = "C:/IBM/Development/Log"; //OnVmware
		String DlogFile = "D:/IBM/Development/Log";  //On Site
		File f = new File(DlogFile);
		if(f.exists() && f.isDirectory()){ 
			File f2=new File(DlogFile.concat("/").concat(FileName));
			if(f2.exists() && !f2.isDirectory())
			{
				logFile=DlogFile.concat("/").concat(FileName);
			}
			else
			{
				f2.createNewFile();
				logFile=DlogFile.concat("/").concat(FileName);
			}
		}
		else{
			File f2=new File(ClogFile.concat("/").concat(FileName));
			if(f2.exists() && !f2.isDirectory())
			{
				logFile=ClogFile.concat("/").concat(FileName);
			}
			else
			{
				f2.createNewFile();
				logFile=ClogFile.concat("/").concat(FileName);
			}
		}
	}	

	public PasswordAuthentication getPasswordAuthentication()
	{
		String username = SMTP_AUTH_USER;
		String password = SMTP_AUTH_PWD;
		return new PasswordAuthentication(username, password);
	}
	

	  private String getPropertyFromCopiedDocs(ObjectStore os, Id id,String PropetySymName)
	{
		String STR="";
		Document doc = Factory.Document.fetchInstance(os,id, null);
		if(PropetySymName.contains("ProjectGroup")&&doc.getProperties().getStringListValue("ProjectGroup")!=null&&doc.getProperties().getStringListValue("ProjectGroup").size()>0)
		{
			STR= doc.getProperties().getStringListValue("ProjectGroup").get(0).toString().substring(doc.getProperties().getStringListValue("ProjectGroup").get(0).toString().indexOf('_')+1);
		}
		else
		{
			STR=doc.getProperties().getStringListValue("Project").get(0).toString().substring(doc.getProperties().getStringListValue("Project").get(0).toString().indexOf('_')+1);
		}
		STR+=" ; ";
		int FilenetSize= doc.getProperties().getStringListValue("FileNetCommand").size();
		String[] FilenetCommands = new String[FilenetSize];
		
	    for( int i=0; i < FilenetSize; i++ ) {
	    	FilenetCommands[i] = doc.getProperties().getStringListValue(("FileNetCommand")).get(i).toString();
	    }
	    
	    for (int i = 0; i < FilenetCommands.length; i++) {
			if(FilenetCommands[i].contains("Copy To:"))
			{
				String CopyDocId=FilenetCommands[i].substring(FilenetCommands[i].indexOf(':')+1);
				Document CopyDoc = Factory.Document.fetchInstance(os,CopyDocId, null);
				String project="";
				if(PropetySymName.contains("ProjectGroup"))
				{
					project= CopyDoc.getProperties().getStringListValue("ProjectGroup").get(0).toString().substring(doc.getProperties().getStringListValue("ProjectGroup").get(0).toString().indexOf('_')+1);
				}
				else
				{
					project=CopyDoc.getProperties().getStringListValue("Project").get(0).toString().substring(doc.getProperties().getStringListValue("Project").get(0).toString().indexOf('_')+1);
				}
				STR+=project+" ; ";
			}
		}
		return STR;
	}

}



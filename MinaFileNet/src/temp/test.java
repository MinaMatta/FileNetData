package temp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;

import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.filenet.api.admin.Choice;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.CmChangePreprocessorDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.admin.SubscribableClassDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.CmChangePreprocessorDefinitionList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.EngineCollection;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.MarkingList;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.events.CmChangePreprocessorAction;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Marking;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;

public class test {

	public static final String URI ="http://172.16.10.65:9080/wsi/FNCEWS40MTOM/";					//On SODIC Site
	public static final String USERID = "filenet";													//On SODIC Site
	public static final String PASSWORD = "Fn1234567";												//On SODIC Site
//	public static final String projectMarkingSetId = "{86C2ACF3-2695-4C17-89F0-F10C5DF85475}"; 		//On SODIC Site
//	public static final String companyChoiceListId = "{EB509EF4-EA3A-4378-B019-32F585B2E029}";		
//	public static  final String URI ="http://50.0.0.43:9080/wsi/FNCEWS40MTOM/";						//On SODIC Lab
//	public static  final String USERID = "fnadmin3";												//On SODIC Lab
//	public static  final String PASSWORD = "P@ssw0rd";												//On SODIC Lab
	public static  final String projectMarkingSetId = "{1BE033CF-05D1-4BFC-A4B3-8EE49AD7C6C5}";		//On SODIC Lab
	public static String FolderCustomizerPath="C:\\Users\\mina\\Desktop\\Folder Customizer";        //On SODIC Site
	public static String CustomizeColumnsFileName=" Columns.xml";
//	public static final String URI ="http://fn52.test.com:9080/wsi/FNCEWS40MTOM/";					//On VMWare
//	public static final String USERID = "admin";													//On VMWare
//	public static final String PASSWORD = "P@ssw0rd";												//On VMWare
//	public static final String projectMarkingSetId = "{2B6E95FC-19DF-4239-8D98-83E40683139B}";		//On VMWare
	 public static String logFile = "D:/IBM/Development/Automation2/log.txt";						//On VMware
//	 public static final String logFile = "D:/IBM/Development/Automation2/log.txt";					//On Site
	 public static Date today = Calendar.getInstance().getTime();
	 static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");   
	 public static ArrayList<String> ContactCopies=new ArrayList<String>();
	 public static ArrayList<String> Properites=new ArrayList<String>();
	 public final static String DRIVER_NAME ="sun.jdbc.odbc.JdbcOdbcDriver";
//	 public final static String DATABASE_URL1 = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=%s;READONLY=TRUE";
	 public static String[] UsersProperties={"Attention","Info","ActionBy","DirectedBy","By","SignedBy","PreparedBy"};
	 public static String[] Classes={"OutgoingLetter","IncomingLetter","OutgoingTransmittal","IncomingTransmittal","Submittal","MOM","InternalMemo","GeneralInternalMemo","GeneralIncomingLetter","GeneralOutgoingLetter","GeneralIncomingInvitation","GeneralOutgoingInvitation"};
	 public static ArrayList<String> ClassesUsers;
	 public static ArrayList<String> PropertiesUsers;
	 public static String[] Cols={"Contract","Development","Project","Company"};
	 public static ArrayList<Row> mails;
	 public static final String developmentGoupsChoiceListId = "{A5BD8039-7DAF-4160-9CC4-00ABF2506459}";
	 public static final String UsersChoiceListId="{3FED5983-25D6-48BD-8FB1-B0143402191F}";
	 public static String developmentClId="{4DCFACA3-AD90-4CC1-8AC6-485E86C2C8C5}";            	//On VMware
	 public static String[] ColumnNames ={"Application_name","Application","Barcode","Employee_No","Employee_Name","Sector","InfoFort","Film","Film_Doc_No","Path"};

	 	public static String FoldersTXT="C:/Users/mina/Desktop/Folder Customizer/Folders.txt";   //On Site
		public static String Path="C:/Users/mina/Desktop/Folder Customizer/";                    //On Site
		public static String EntryTemplateFileName=" Entry Templates.xml";
		public static final String DesignPath="D:/Design AAIB.xls";
		public static UserContext uc2;
		public static ObjectStore os; 
//		public static final String USERID = UploaderScreen.username.getText().toString();
//		public static final String PASSWORD = UploaderScreen.Password.getText().toString();
		//AAIB Site
//		public static final String URI ="http://172.20.4.121:9080/wsi/FNCEWS40MTOM/";
		public static final String DATABASE_URL_Write = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=";
		public static final String DATABASE_URL = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=%s;READONLY=TRUE";
		private static final String UnderPath = "/General";
	public static void main(String[] args) throws Exception 
	{
		
		try {
			Connection conn = Factory.Connection.getConnection(URI);
			Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
			UserContext uc = UserContext.get();
			uc.pushSubject(subj);
			Domain dom = Factory.Domain.getInstance(conn, null);
			ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"UMC", null);
			AssociateCPtoDocumentClass(os, "{90720D22-5963-4CA5-8FF8-750029100850}", "{5FF8DCC9-8DA7-4050-AADD-95AA829104CC}");
//			Folder Zone=Factory.Folder.fetchInstance(os, "/Allegria/TH", null);
//			FolderSet fs=Zone.get_SubFolders();
//			Iterator it=fs.iterator();
//			while (it.hasNext()) {
//				Folder f = (Folder) it.next();
//				DeleteAnnotationsFromFolder(f);
//				System.out.println(f.get_FolderName());
//				f.refresh();
//				AssociateEntryTemplate(os, f, new File("C:\\Users\\mina\\Desktop\\Entry Templates\\PlotFolder-ET.xml"));
//				System.out.println(f.get_FolderName().concat(" Done."));
//			}
//			
		} catch (Exception e) {
			String out="";
			for (int j = 0; j < e.getStackTrace().length; j++) {
				out+=e.getStackTrace()[j].toString().concat("  \n");
			}
			System.out.println(out);
		}
//		String select = "d.Id,d.LastViolationNo,d.FolderName";		
//		String where = "d.LastViolationNo IS NULL ";
//		String from = "PlotNoFolder";
//		IndependentObjectSet folders=searchForFolders(os, select, from, where, null, 0);
//		if(folders.isEmpty())
//			{
//				System.out.println("No Data Found");
//			}
//			else
//			{
//				
//				int index=0;
//				for( Iterator j = folders.iterator(); j.hasNext(); )
//				{	
//					Folder f = Factory.Folder.createInstance(os, null);
//					f = (Folder) j.next();
//					f=Factory.Folder.fetchInstance(os, f.get_Id(), null);
////					UpdateFolderColumnsAndTemplates(os, f);
//					f.getProperties().putObjectValue("LastViolationNo", 0);
//					f.save(RefreshMode.REFRESH);
//					DeleteAnnotationsFromFolder(f);
//					AssociateEntryTemplate(os, f, new File("C:/Users/mina/Desktop/PLOT Templates.xml"));
//					System.out.println(f.get_Id().toString()+"=-=-=Folder Number : "+String.valueOf(index+1)+" Folder Name : "+f.get_Name());
//					index++;
//					}
//					System.out.println("Total Folders "+index);
//				}
	
//		String[] FolderTypes=GetFolderNames();
//		for (int i = 0; i < FolderTypes.length; i++) 
//		{
//			String FolderName=FolderTypes[i];
//			System.out.println("####Folder Type = "+FolderName+"  ### Under Path  "+ UnderPath);
//			String select = "d.Id,d.This,d.FolderName";		
//			
//			String where = "d.This INSUBFOLDER '"+UnderPath+"' AND d.FolderName = '"+FolderName+"'";
//			String from = "Folder";
//			IndependentObjectSet folders=searchForFolders(os, select, from, where, null, 0);
//			if(folders.isEmpty())
//				{
//					System.out.println("No Data Found");
//				}
//				else
//				{
//					
//					int index=0;
//					for( Iterator j = folders.iterator(); j.hasNext(); )
//					{	
//						Folder f = Factory.Folder.createInstance(os, null);
//						f = (Folder) j.next();
//						f=Factory.Folder.fetchInstance(os, f.get_Id(), null);
//						UpdateFolderColumnsAndTemplates(os, f);
//						System.out.println(f.get_Id().toString()+"=-=-=Folder Number : "+String.valueOf(index+1)+" Folder Name : "+f.get_Name());
//						index++;
//						}
//						System.out.println("Total Folders "+index);
//					}
//		}
		
//		String className = "General Incoming Letter";
////		String from = "GeneralOutgoingInvitation";
////		String className = "General Outgoing Invitation";
//		IndependentObjectSet DocumentsSet=searchForDocuments(os, select, from, where, null, 0);
//		if(DocumentsSet.isEmpty())
//		{
//			System.out.println("No Data Found");
//		}
//		else
//		{
//			
//			int index=0;
//			for( Iterator i = DocumentsSet.iterator(); i.hasNext(); )
//			{	
//				Document doc = Factory.Document.createInstance(os, null);
//				doc = (Document) i.next();
//				doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
//				System.out.println(doc.get_Id().toString()+"=-=-=Document Number : "+String.valueOf(index+1));
//				String GenPath="/General/".concat(className);
//	        	Date date=doc.getProperties().getDateTimeValue("ReceivedDate");
//				SimpleDateFormat Yearformat = new SimpleDateFormat("yyyy");
//				SimpleDateFormat Monthformat= new SimpleDateFormat("MM");
//				String year=Yearformat.format(date);
//				String month=Monthformat.format(date);
//				GenPath=GenPath.concat("/").concat(year).concat("/").concat(month);
//				if (isFolderExist(os, "/General") == false)
//					addFolder(os, "General");
//				if (isFolderExist(os, "/General/".concat(className)) == false)
//					addSubFolder(os, "/General", className);
//				if (isFolderExist(os, "/General/".concat(className).concat("/").concat(year)) == false)
//					addSubFolder(os, "/General/".concat(className), year);
//				if (isFolderExist(os, "/General/".concat(className).concat("/").concat(year).concat("/").concat(month)) == false)
//				{
//					addSubFolder(os, "/General/".concat(className).concat("/").concat(year), month);
//					CustomizeColumns(os, Factory.Folder.fetchInstance(os, "/General/".concat(className).concat("/").concat(year).concat("/").concat(month), null), new File(FolderCustomizerPath.concat(className).concat(CustomizeColumnsFileName)));
//				}
//				unfileFromAllFolders(os, doc.get_Id());
//				fillInFolder(os, doc.get_Id(),GenPath);
//				
//				index++;
//			}
//			System.out.println("Total Document "+index);
//		}
		
// 
//		IndependentObjectSet DocumentsSet=GetAllDocumentsUnderPath(os, "/Six October City");
//		if(DocumentsSet.isEmpty())
//		{
//			System.out.println("No Data Found");
//		}
//		else
//		{
//			Document doc = Factory.Document.createInstance(os, null);
//			int index=0;
//			for( Iterator i = DocumentsSet.iterator(); i.hasNext(); )
//			{	
//				doc = (Document) i.next();
//				doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
//				System.out.println(doc.get_Id().toString()+"=-=-=Document Number : "+String.valueOf(index+1));
//				
//				ReplaceProjectinDocument(doc, "SOC0_SOC 31 Acres Ph.0-block 0","SO_Six October City");
//				index++;
//			}
//			System.out.println("Total Document "+index);
//		}
	}
	
	public static boolean fillInFolder(ObjectStore os, Id docId, String folderPath){
		Document myDoc = Factory.Document.fetchInstance(os, docId, null);
		Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
		try {
			DynamicReferentialContainmentRelationship drcr = (DynamicReferentialContainmentRelationship) parentFolder.file(myDoc, AutoUniqueName.AUTO_UNIQUE, myDoc.get_Name(),DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
			drcr.save(RefreshMode.REFRESH);
			return true;
		}catch(Exception e){
			try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
			return false;
		}
	}
	public static void unfileFromAllFolders (ObjectStore os, Id docId){
		try{
			Document myDoc = Factory.Document.fetchInstance(os, docId, null);
			FolderSet folders = myDoc.get_FoldersFiledIn();
			Iterator iterator = folders.iterator();
			while(iterator.hasNext()){
				unfillFromFolder(os,docId,((Folder)iterator.next()).get_PathName());
				}
		}catch(Exception e){
			try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
		}
	}
	public static boolean unfillFromFolder (ObjectStore os, Id docId, String folderPath){
		Document myDoc = Factory.Document.fetchInstance(os, docId, null);
	    Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
	    try {
	    	ReferentialContainmentRelationship drcr = 
		           (ReferentialContainmentRelationship) parentFolder.unfile(myDoc);
		        drcr.save(RefreshMode.REFRESH);   
	        return true;
	     }catch(Exception e){
	     	try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
	    	 return false;
	     }
		
	}
	public static void DeleteFolderContents(ObjectStore os,Folder folder) throws IOException
	{
		try 
		{
			FolderSet Folders=folder.get_SubFolders();
			Iterator SubFolders=Folders.iterator();
			while(SubFolders.hasNext()) 
			{
				Folder SubFolder =(Folder) SubFolders.next();
				DeleteFolderContents(os, SubFolder);
		    }
			DocumentSet myDocs = folder.get_ContainedDocuments();
			Iterator myDocsIterator=myDocs.iterator();
			while(myDocsIterator.hasNext()) 
			{
				Document Doc =(Document) myDocsIterator.next();
				Doc.delete();
				Doc.save(RefreshMode.NO_REFRESH);
		    }
			folder.delete();
			folder.save(RefreshMode.NO_REFRESH);
		} 
		catch (Exception e) 
		{
			System.out.println("Error While Deleting Request Folder "+e.getMessage());
		}
	}
	public static CustomObject getCCObject(ObjectStore o,String ContractNo,String ContractFolderID)throws Exception{

		String select = " d.ContractNo, d.FoldersIDList,d.Id ";
		String from = "CustomContract";
		String where = "d.ContractNo = '"+ContractNo+"' AND '"+ContractFolderID+"' in d.FoldersIDList";
//		writeToFile(logFile, true, "Select ".concat(select).concat(" from ").concat(from).concat(" where ").concat(where));
		IndependentObjectSet set = searchForDocuments(o,select,from,where,null,0);
		com.filenet.api.core.CustomObject resDoc=null;
		if(set.isEmpty()){
		}else{
			for( Iterator i = set.iterator(); i.hasNext(); )
			{
				resDoc = (com.filenet.api.core.CustomObject)i.next();
				resDoc=Factory.CustomObject.fetchInstance(o, resDoc.get_Id(), null);
				break;
			}
		}
		return resDoc;
	}
	/*	Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		ClassDefinition cls = Factory.ClassDefinition.fetchInstance(os, "", null); // args[0] = class definition id
	    PropertyDefinitionList propList = cls.get_PropertyDefinitions();
	    Iterator propIterator = propList.iterator();
	    Iterator propsIterator = propList.iterator();
	    Properites=new ArrayList<String>();
	    while(propsIterator.hasNext())
	    {
	    	PropertyDefinition property=(PropertyDefinition)propsIterator.next();
	    	Properites.add(property.get_SymbolicName());
//	    	property.set_ChoiceList(null);
	    }
	    PropertyDefinition propDef = (PropertyDefinition) propIterator.next();
	    ArrayList propNames = new ArrayList();
	    ArrayList propValues = new ArrayList();
	}
		
		String FilenetFolderPath="/‪Westown‬/‪SW - Residential‬/‪Banks‬/‪Arab Investment Bank‬/‪Pre Contract‬/‪Incoming Letter‬";
		IndependentObjectSet set=GetAllDocumentsUnderPath(os, FilenetFolderPath, "Document");
		Document doc=null;
		int index=0;
		if(set.isEmpty())
	 	{
			System.out.println("No Data !!!");
		}
		else
		{
			for ( Iterator i = set.iterator(); i.hasNext(); ) 
			{
				doc=(Document)i.next();
//				doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
//				System.out.println(doc.getProperties().getStringValue("DocumentTitle").toString()+"\t"+doc.get_Id().toString());
				index++;
			}
		}
		System.out.println(index);
		uc.popSubject();
		int index=0;
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		try
		{		
			Domain dom = Factory.Domain.fetchInstance(conn, null, null);
			ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
			final com.filenet.api.core.CustomObject sodicLastMail = Factory.CustomObject.getInstance(os, "SODICLastMail","/Administration/SODIC Last Mail");
			sodicLastMail.lock(20, null);
			
			sodicLastMail.save(RefreshMode.REFRESH);
			System.out.println(sodicLastMail.isLocked());
			String select = "d.Id,d.SODICSN";
			String from = "IncomingLetter";
			String where = "'WTR-INFR_WTR Infrastructure' IN d.Project AND d.ContractNo = 'WST-03-CC-P1' AND d.Company = 'HAC_Hassan Allam Co.' ";
			IndependentObjectSet DocumentsSet=searchForDocuments(os, select, from, where, "d.SODICSN", 55);
			if(DocumentsSet.isEmpty()){
				System.out.println("No Data Found");
			}else{
				Document doc = Factory.Document.createInstance(os, null);
				for( Iterator i = DocumentsSet.iterator(); i.hasNext(); )
				{	
					
					doc = (Document) i.next();
					doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
					System.out.println(doc.getProperties().getObjectValue("SODICSN").toString());
//					doc.getProperties().putObjectValue("Company", "MSD_Misr Sons of Development (Hassan Allam)");
//					doc.save(RefreshMode.NO_REFRESH);
					index++;
				}
				System.out.println(index);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			uc.popSubject();
			System.out.println("Connection Closed");
		}
		/*Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		String select = "d.Id,d.Company";
		String from = "ContractFolder";
		String where = "d.CompanyCategory IS NULL";
		IndependentObjectSet set = searchForFolders(os,select,from,where,null,0);
		Folder f=null;
		int index=0;
		if(set.isEmpty()==true)
	 	{
			System.out.println("No Data !!!");
		}
		else
		{
			for ( Iterator i = set.iterator(); i.hasNext(); ) 
			{
				f=(Folder)i.next();
				f=Factory.Folder.fetchInstance(os, f.get_Id(), null);
				System.out.println(f.get_FolderName()+"\t"+f.get_Id());
				String Category=getGroupName(os,new Id(companyChoiceListId),f.getProperties().getStringValue("Company"));
				if(Category!=null&&Category!="")
				{
					System.out.println(f.get_FolderName()+"\t"+Category+"\t"+f.get_Id());
					f.getProperties().putObjectValue("CompanyCategory",Category);
					f.save(RefreshMode.NO_REFRESH);
				}
				
				index++;
			}
		}  */
	
	public static void ChangeEntryTemplateandColumnsonFolderByName(ObjectStore os,String FolderName,String RootPath,File EntryXML,File ColumnsXML) throws Exception
	{
		String select = "d.Id";
		String from = "Folder";
		String where = "d.FolderName = '"+FolderName +"' AND d.This INSUBFOLDER '"+RootPath+"'";
		IndependentObjectSet set = searchForFolders(os,select,from,where,null,0);
		Folder f=null;
		if(set.isEmpty()==true)
	 	{
			System.out.println("No Data !!!");
		}
		else
		{
			for ( Iterator i = set.iterator(); i.hasNext(); ) 
			{
				f=(Folder)i.next();
				f=Factory.Folder.fetchInstance(os, f.get_Id(), null);
				System.out.println(f.get_FolderName()+"\t"+f.get_Id().toString());
				DeleteAnnotationsFromFolder(f);
				AssociateEntryTemplate(os, f, EntryXML);
				CustomizeColumns(os, f, ColumnsXML);
			}
		}
	}
	////////////////////done
	public static ArrayList<Row> getData(File file) throws SQLException {
			

			ArrayList<Row> list = new ArrayList<Row>();
			try
		  {
				String filename="Sheet1";
				String filepath=String.format(DATABASE_URL, file.getAbsolutePath());
				Class.forName(DRIVER_NAME);
				java.sql.Connection conn=DriverManager.getConnection(filepath);
		    Statement stment = conn.createStatement();
		    String qry = "SELECT * FROM ["+filename+"$]";
		    ResultSet rs = stment.executeQuery(qry);
		    ResultSetMetaData rsmd = rs.getMetaData();
		    System.out.println(rsmd.getColumnCount());
		    
//		    while(rs.next())
//		    {
//		    	Row r=new Row();
//		    	r.DisplayName=rs.getString("Display Name");
//		    	r.Value=rs.getString("E-mail Address");
//		    	r.mail=rs.getString("E-mail");
//				list.add(r);
//		    }
		    rs.close();
		  }
		  catch(Exception err)
		  {
		    System.out.println(err);
		  }
			finally {
		    return list;
		   }
		 }
	
	public static String GetCreatorMail(ObjectStore os,Document Doc)
	{
		String creatormail=Doc.get_Creator().toString().toLowerCase()+"@sodic.com";
		return creatormail;
	}
	
	public static void GetAllSystemUsers()
	{
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		try
		{		 

		 		System.out.println("Connection Opened2");
		 		Domain dom = Factory.Domain.fetchInstance(conn, null, null);
		 		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		 		String select="d.LastModifier";
		 		String from= "Document";
		 		String where="d.LastModifier IS NOT NULL AND ([LastModifier] <> 'filenet') AND NOT(IsClass(d, PreferencesDocument))";

		 		IndependentObjectSet set = searchForDocuments2(os,select,from,where,"LastModifier",0);
		 		
		 		HSSFWorkbook wb = new HSSFWorkbook();
				HSSFCreationHelper createhelper = wb.getCreationHelper();
				HSSFSheet sheet = wb.createSheet("Sheet1");
				Row row = null;
				Cell cell = null;
				Cell cell2 = null;
		 		
		 		
				int index=0;
		 		if(!set.isEmpty()){
		 			for( Iterator k = set.iterator(); k.hasNext(); )
					{
		 				
						Document resDoc = (Document)k.next();
						//resDoc=Factory.Document.fetchInstance(os, resDoc.get_Id(), null);
						System.out.print(resDoc.getProperties().getStringValue("LastModifier")+";"+resDoc.getClassName());
						//System.out.print(","+resDoc.getClassName());
						System.out.println();
						
						
						row = sheet.createRow(index);
						cell = row.createCell(0);
						cell.setCellValue(resDoc.getProperties().getStringValue("LastModifier"));
						cell2 = row.createCell(1);
						cell2.setCellValue(resDoc.getClassName());
						index++;
				    }
		 		}
		 		try {
					File file=new File("C:\\Users\\Mina.PROSOFT.000\\Desktop\\New folder\\users.xls");
					FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
					wb.write(out);
					out.close();
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
		}catch(Exception e){
		 		 System.out.println(e.getMessage());
		 }
		 finally
		 {
		 		 uc.popSubject();
		 		 System.out.println("Connection Closed");
		 }		
	}
	
	public static IndependentObjectSet searchForDocuments2 (ObjectStore o, String select,String from,String where,String orderBy ,int maxRecords){
		try{
			SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList(select);
		    sqlObject.setDistinct();
		    sqlObject.setFromClauseInitialValue(from, "d", true);
		    sqlObject.setWhereClause(where);
		    if(orderBy != null) sqlObject.setOrderByClause(orderBy);
		    if(maxRecords != 0) sqlObject.setMaxRecords(maxRecords);

		    SearchScope search = new SearchScope(o);

		    // Set the page size (Long) to use for a page of query result data. This value is passed 
		    // in the pageSize parameter. If null, this defaults to the value of 
		    // ServerCacheConfiguration.QueryPageDefaultSize.
		    Integer myPageSize = new Integer(100);

		    // Specify a property filter to use for the filter parameter, if needed. 
		    // This can be null if you are not filtering properties.
		    PropertyFilter myFilter = new PropertyFilter();
		    int myFilterLevel = 1;
		    myFilter.setMaxRecursion(myFilterLevel);
		    myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 

		    // Set the (Boolean) value for the continuable parameter. This indicates 
		    // whether to iterate requests for subsequent pages of result data when the end of the 
		    // first page of results is reached. If null or false, only a single page of results is 
		    // returned.
		    Boolean continuable = new Boolean(true);

		    // Execute the fetchObjects method using the specified parameters.
		    IndependentObjectSet set = search.fetchObjects(sqlObject, myPageSize, myFilter, continuable);

		    //writeToFile(logFile,true,"SQL: " + sqlObject.toString());
		    return set;

		    //return myObjects;
		}catch(Exception e){
			try{	
				System.out.println(e.getMessage());
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
			return null;
		}


	}
	
	public static void CheckFileNetColumn(String ExcelSheet)
	{ 
		try {
			File file=new File(ExcelSheet); 
			FileInputStream fis=new FileInputStream(file); 
			HSSFWorkbook wb=new HSSFWorkbook(fis); 
			HSSFSheet st=wb.getSheet(getFirstSheetName(ExcelSheet));
			HSSFRow header=st.getRow(0);
			Iterator it=header.cellIterator();
			boolean hasfilenet=false;
			int colno=0;
			while (it.hasNext()) {
				Cell c = (Cell) it.next();
				System.out.println(colno+c.getStringCellValue()+"        "+c.getStringCellValue().length());
				if(c.getStringCellValue().compareTo("Filenet")==0)
				{
					hasfilenet=true;
					break;
				}
				if(c == null || c.getCellType() == Cell.CELL_TYPE_BLANK||c.getStringCellValue().length()<=0)
				{break;}
				else{colno++;}
			}
			if(!hasfilenet)
			{
				
				Cell cell = header.createCell(colno, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Filenet");
				FileOutputStream fileOut = new FileOutputStream(ExcelSheet);
				wb.write(fileOut);
				fileOut.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static String getFirstSheetName(String excelPath)throws ClassNotFoundException, SQLException, Exception{
    	FileInputStream file = new FileInputStream(new File(excelPath));
    	try{	
    		String sheetName = "";
    		if(excelPath.contains(".xlsx")){
    			XSSFWorkbook wb = new XSSFWorkbook(file);
    			sheetName = wb.getSheetName(0);
    		}else{		
    			HSSFWorkbook wb = new HSSFWorkbook(file);
    			sheetName = wb.getSheetName(0);
    		} 
    		file.close();
    		return sheetName;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}finally{
    		file.close();
    	}
    }
	
	public static String GetNewMailValue(String OldMail)
	{
		String NewValue="";
		for (int j = 0; j < mails.size(); j++) {
//			if(mails.get(j).mail.equalsIgnoreCase(OldMail))
//			{
//				System.out.println("    set value with new value   "+mails.get(j).Value);
//				NewValue=mails.get(j).Value;
//			}
		}
		return NewValue;
	}
	
	public static void FindClassesAndPropertiesForChoiceList(ObjectStore os,String ChoiceListName)
	{
		ClassesUsers=new ArrayList<String>();
		PropertiesUsers =new ArrayList<String>();
		SearchScope scope = new SearchScope(os);
		
		SearchSQL sql = new SearchSQL("SELECT * FROM ClassDefinition"); // or "SELECT * FROM ClassDefinition WHERE SuperclassDefinition = OBJECT('{...}')" to filter by superclass, for example
		
		EngineCollection coll = scope.fetchObjects(sql, null, null, false);
		
		Iterator<?> it = coll.iterator();
		
		while (it.hasNext()) {
			Boolean HasUsers=false;
			ClassDefinition CD = (ClassDefinition) it.next();
			Iterator iter = CD.get_PropertyDefinitions().iterator();
			PropertyDefinition PD = null;
			while (iter.hasNext())
			{
				PD = (PropertyDefinition) iter.next();
				ChoiceList temp=PD.get_ChoiceList();
				String CLName="";
				if(temp!=null)
					CLName=temp.get_Name();
				if(CLName.equalsIgnoreCase(ChoiceListName))
				{
					if(!PropertiesUsers.contains(PD.get_SymbolicName()))
						PropertiesUsers.add(PD.get_SymbolicName());
					HasUsers=true;
				}
			}
			if(HasUsers)
				ClassesUsers.add(CD.get_SymbolicName());
		}
	}
	
	public static int subtractDates(Date beginDate,Date endDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		
		int minusDays = 0;
		while (true) {
		  minusDays++;

		  // Day increasing by 1
		  beginCalendar.add(Calendar.DAY_OF_MONTH, 1);

		  if (dateFormat.format(beginCalendar.getTime()).
		            equals(dateFormat.format(endCalendar.getTime()))) {
		    break;
		  }
		}
		return minusDays;
		//return 0;
	}
	
	public static void SetChoiceListForPropertyDefinitions(ObjectStore os,String[] Classes,String[] PropertyDefinitions,ChoiceList CL)
	{
		System.out.println("\n\nSetChoiceListForPropertyDefinitions\n");
		PropertyFilter PF = new PropertyFilter(); 
		PF.addIncludeProperty(new FilterElement(null, null, null, PropertyNames.PROPERTY_DEFINITIONS, null));
		ArrayList<String> Properties=new ArrayList<String>(Arrays.asList(PropertyDefinitions));
		for (int i = 0; i < Classes.length; i++) 
		{
			System.out.println("\nClass Definition = "+Classes[i]);
			ClassDefinition CD = Factory.ClassDefinition.fetchInstance(os, Classes[i], PF);
			Iterator iter = CD.get_PropertyDefinitions().iterator();
			PropertyDefinition PD = null;
			while (iter.hasNext())
			{
				PD = (PropertyDefinition) iter.next();
				String PropertySymbolicName = PD.get_SymbolicName();
				if(Properties.contains(PropertySymbolicName))
				{
					System.out.println("Property definition selected : " + PropertySymbolicName);
					PD.set_ChoiceList(CL);
				}
			}
			CD.save(RefreshMode.REFRESH);
		}
	}
	
	public static void SetChoiceListForPropertyTemplate(ObjectStore os,String[] PropertyTemplates,ChoiceList CL)
	{
		System.out.println("\n\nSetChoiceListForPropertyDefinitions\n");
		Iterator iter = os.get_PropertyTemplates().iterator();
		PropertyTemplate PT = null;
		ArrayList<String> Properties=new ArrayList<String>(Arrays.asList(PropertyTemplates));
		// Loop until property template found in object store
		while (iter.hasNext())
		{
			PT = (PropertyTemplate) iter.next();
			String PropertySymbolicName = PT.get_SymbolicName();
			if (Properties.contains(PropertySymbolicName))
			{
				System.out.println("Property Template selected : " + PropertySymbolicName);
				PT.set_ChoiceList(CL);
				PT.save(RefreshMode.REFRESH);
			}
		}
	}
	
	public static void UpdateContractFolderSecurity(ObjectStore os,Folder f)
	{
		String PAth=f.get_PathName();
		String[] foldersArray=PAth.substring(1).split("/");
		if(foldersArray.length==5)
		{
			String ProjectPath         = "/".concat(foldersArray[0]).concat("/").concat(foldersArray[1]);
			Folder ProjectFolder=Factory.Folder.fetchInstance(os, ProjectPath, null);
			AccessPermissionList ContractAPL=Factory.AccessPermission.createList();
			Iterator iter3=ProjectFolder.get_Permissions().iterator();
			int AdminAccessMask=999415;
			int ModifierAccessMask=135159;
			int ViewerAccessMask=131201;
			while (iter3.hasNext())
			{
				AccessPermission ap = Factory.AccessPermission.createInstance();
				AccessPermission type = (AccessPermission)iter3.next();
				if(type.get_GranteeType().toString().compareTo("USER")!=0&&type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")!=0)
				{
					ap.set_GranteeName(type.get_GranteeName());
					ap.set_AccessType(type.get_AccessType());
					ap.set_InheritableDepth(type.get_InheritableDepth());
					if(type.get_GranteeName().contains("Admin"))
					{
						ap.set_AccessMask(AdminAccessMask);
					}
					else if(type.get_GranteeName().contains("Modifier"))
					{
						ap.set_AccessMask(ModifierAccessMask);
					}
					else if(type.get_GranteeName().contains("Viewer"))
					{
						ap.set_AccessMask(ViewerAccessMask);
					}
					ContractAPL.add(ap);
				}
			}
			f.set_Permissions(ContractAPL);
			f.save(RefreshMode.REFRESH);
		}
	}
	
	public static Choice CreateStringChoice(String DisplayName,String Value) 
	{
		Choice objChoiceMidStr1 = Factory.Choice.createInstance();
		objChoiceMidStr1.set_ChoiceType(ChoiceType.STRING);
		objChoiceMidStr1.set_DisplayName(DisplayName);
		objChoiceMidStr1.set_ChoiceStringValue(Value);
		return objChoiceMidStr1;
	}
	
	public static void SetSecurityFolder(Document doc)
	{
		Folder f=(Folder)doc.get_SecurityFolder();
		if(f==null)
		{
			System.out.println("security folder = null");
			FolderSet fs=doc.get_FoldersFiledIn();
			Folder ParentFolder=(Folder)fs.iterator().next();
			doc.set_SecurityFolder(ParentFolder);
			doc.save(RefreshMode.REFRESH);
		}
		else
		{
			System.out.println("security folder != null");
		}
	}
	
	public static void RemovePermissions(Folder f,String[] PermissionList)
		{
			Iterator iter=f.get_Permissions().iterator();
			Boolean hasECM=false;
			List<String> p = Arrays.<String>asList(PermissionList);
			while (iter.hasNext()){
				AccessPermission type = (AccessPermission)iter.next();
				if(p.contains(type.get_GranteeName()))
				{
					hasECM=true;
					break;
				}
			}
			if(hasECM)
			{
				AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
				Iterator iter2=f.get_Permissions().iterator();
				while (iter2.hasNext()){
					AccessPermission type = (AccessPermission)iter2.next();
					if(!p.contains(type.get_GranteeName()))
					{
						listeAutorisation.add(type);
					}
				}
				f.set_Permissions(listeAutorisation);
				f.save(RefreshMode.REFRESH);
			}
		}

	public static void RemovePermissions(Document doc,String[] PermissionList)
	{
		Iterator iter=doc.get_Permissions().iterator();
		Boolean hasECM=false;
		List<String> p = Arrays.<String>asList(PermissionList);
		while (iter.hasNext()){
			AccessPermission type = (AccessPermission)iter.next();
			if(p.contains(type.get_GranteeName()))
			{
				hasECM=true;
				break;
			}
		}
		if(hasECM)
		{
			AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
			Iterator iter2=doc.get_Permissions().iterator();
			while (iter2.hasNext()){
				AccessPermission type = (AccessPermission)iter2.next();
				if(!p.contains(type.get_GranteeName()))
				{
					listeAutorisation.add(type);
				}
			}
			doc.set_Permissions(listeAutorisation);
			doc.save(RefreshMode.REFRESH);
		}
	}
	
	public static IndependentObjectSet GetAllContractFoldersUnderPath(ObjectStore os,String FolderPath)
	{
		
		try {
			String select = "d.Id";
			String from = "ContractFolder";
//			String where = "d.FolderName = '"+"Pre Contract"+"' OR d.FolderName = '"+"pre contract"+"' AND d.This INSUBFOLDER '"+FolderPath+"'";
			String where = "d.This INSUBFOLDER '"+FolderPath+"'";
			IndependentObjectSet set = searchForFolders(os,select,from,where,null,0);
			return set;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Date parseDate(String date) {
	     try {
	         return new SimpleDateFormat("yyyy-mm-dd").parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }
	
	public static IndependentObjectSet GetAllDocumentsUnderPath(ObjectStore os,String FolderPath)
	{
		String select = "d.Id,d.Project";
		String from = "Document";
		String where = "d.This INSUBFOLDER '"+FolderPath+"' AND d.Project IS NOT NULL";
		IndependentObjectSet set = searchForDocuments(os, select, from, where, null, 0);
		return set;
	}
	
	public static void ReplaceProjectinDocument(Document Doc,String NewProject,String Devlopment)
	{
		try {
			Doc.refresh();
			Doc.getProperties().getStringListValue("Project").remove(0);
			Doc.getProperties().getStringListValue("Project").add(NewProject);
			System.out.println("Changed to "+NewProject);
			Doc.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			
		}
//		try {
//			Doc.refresh();
//			Doc.getProperties().putObjectValue("Development", Devlopment);
//			System.out.println("Changed to "+Devlopment);
//			Doc.save(RefreshMode.NO_REFRESH);
//		} catch (Exception e) {
//			
//		}
	}
	
	public static IndependentObjectSet GetAllDocumentsByDateCreated(ObjectStore os,String DocumentType,String[] Property)
	{
		String select = "d.Id";
		SimpleDateFormat SQLformatter = new SimpleDateFormat("yyyyMMdd");
		Date Date1 = new GregorianCalendar(2014, Calendar.MARCH, 2).getTime();
		Date Date2 = new GregorianCalendar(2015, Calendar.JUNE, 4).getTime();
		String fromDate = SQLformatter.format(Date1).concat("T000000Z");
		String toDate = SQLformatter.format(Date2).concat("T000000Z");
		String where = "d.DateCreated > "+fromDate+" AND d.DateCreated < "+toDate+" AND (";
		for (int i = 0; i < Property.length; i++) {
			if(i==Property.length-1)
				where+=" d."+Property[i]+" is not null )";
			else
				where+=" d."+Property[i]+" is not null OR";
		}
		System.out.println(where);
		IndependentObjectSet set = searchForDocuments(os, select, DocumentType, where, null, 0);
		return set;
	}
	
	public static void AssociateCPtoDocumentClass(ObjectStore os,String CPAid,String DCid)
	{
		//Create change preprocessor definition object.
		CmChangePreprocessorDefinition cpDef = Factory.CmChangePreprocessorDefinition.createInstance(os);
		cpDef.set_DisplayName("CP Test");
		cpDef.set_IsEnabled(true);

		//Get CmChangePreprocessorAction and set on definition object.
		CmChangePreprocessorAction action = Factory.CmChangePreprocessorAction.getInstance(os, "CmChangePreprocessorAction",new Id(CPAid));

		cpDef.set_ChangePreprocessorAction(action);

		CmChangePreprocessorDefinitionList cpdList=Factory.CmChangePreprocessorDefinition.createList();
		cpdList.add(cpDef);

		//Get AccountsReceivable class definition and set definition list object on it. 
		SubscribableClassDefinition objClassDef = Factory.SubscribableClassDefinition.getInstance(os, new Id(DCid));
		objClassDef.set_ChangePreprocessorDefinitions(cpdList);

		objClassDef.save(RefreshMode.REFRESH);
	}
	
	public static void MakePathFolders(ObjectStore os,String Path)
		{
			String[] foldersArray=Path.substring(1).split("/");
			for (int i = 0; i < foldersArray.length; i++) {
				if(i==0)//add folder
				{
					if(isFolderExist(os,"/".concat(foldersArray[0]))==false)
						addFolder(os,foldersArray[0]);
				}
				else//add subfolder
				{
					String ParentFolderPath="";
					for (int j = 0; j < i; j++) {
						ParentFolderPath+="/";
						ParentFolderPath+=foldersArray[j];
					}
					if(isFolderExist(os,ParentFolderPath.concat("/").concat(foldersArray[i]))==false)
						addSubFolder(os,ParentFolderPath,foldersArray[i]);
				}
			}
		}

	public static boolean isFolderExist (ObjectStore os, String folderPath){
			try{
				
				Folder f1;
				f1 = Factory.Folder.fetchInstance(os, folderPath, null);
				return true;
			}catch(Exception e){
				try{	
//					writeToFile(logFile,true,formatter.format(today)+"---Exception--------DocumentCreation.isFolderExist------------");
//					writeToFile(logFile,true,e.getMessage());
					}catch(Exception t){
						
					}
				return false;
			}
		}
		
	public static String GetClassName(String Type) throws IOException
		{
			if(Type.contains("("))
			    Type=Type.substring(0, Type.indexOf("("));
			
			if(Type.contains(","))
				Type=Type.substring(0, Type.indexOf(","));
			
			if(Type.contains("/"))
				Type=Type.replaceAll("/", "");
			
			if(Type.contains(" "))
				Type=Type.replaceAll(" ", "");
			
			if(Type.contains("&"))
				Type=Type.replaceAll("&", "");
			
			if(Type.contains("-"))
				Type=Type.replaceAll("-", "");
			
			return Type;
		}
	 
	public static boolean addFolder(ObjectStore os, String folderName) {
			
			try{
		      Folder folder = Factory.Folder.createInstance(os, null);
		      Folder rootFolder = os.get_RootFolder();
		      folder.set_FolderName(folderName);
		      folder.set_Parent(rootFolder);
		      folder.save(null);
		      return true;
			}catch(Exception e){
				try{
//					writeToFile(logFile,true,"---Exception---------DocumentCreation.addFolder------------");
//					writeToFile(logFile,true,e.getMessage());
					}catch(Exception t){
						System.out.println(t.getMessage());
					}
				return false;
			}
		   }
		
	public static boolean addSubFolder (ObjectStore os, String parentPath, String subFolderName){
			
			try{
			  Folder parentFolder = Factory.Folder.fetchInstance(os, parentPath, null);
		      Folder subFolder = parentFolder.createSubFolder(subFolderName);
		      subFolder.save(RefreshMode.REFRESH);
		      return true;
			}catch(Exception e){
				try{
//					writeToFile(logFile,true,"---Exception---------DocumentCreation.addSubFolder------------");
//					writeToFile(logFile,true,e.getMessage());
					}catch(Exception t){
						System.out.println(t.getMessage());
					}
				
				return false;
			}
		}
		
	public static void RemoveUsersFromPermissions(Folder f)
	{
		
		Iterator iter=f.get_Permissions().iterator();
		Boolean hasUsers=false;
		while (iter.hasNext()){
			AccessPermission type = (AccessPermission)iter.next();
			if(type.get_GranteeType().toString().compareTo("USER")==0||type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")==0)
			{
				hasUsers=true;
				break;
			}
		}
		if(hasUsers)
		{
			AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
			Iterator iter2=f.get_Permissions().iterator();
			while (iter2.hasNext()){
				AccessPermission type = (AccessPermission)iter2.next();
				if(type.get_GranteeType().toString().compareTo("USER")!=0&&type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")!=0)
				{
					listeAutorisation.add(type);
				}
			}
			f.set_Permissions(listeAutorisation);
			f.save(RefreshMode.REFRESH);
		}
	}

	public static String GetFolderLink(Folder f) throws IOException
		{
			String URL="http://fn52.test.com:9080/WorkplaceXT/getContent?id=%7B";  //On Vmware
//			String URL="http://172.16.10.38:9080/WorkplaceXT/getContent?id=";  //On Site
			String FolderID=f.get_Id().toString();
			URL+=FolderID.substring(FolderID.indexOf('{')+1,FolderID.indexOf('}'));
			URL+="%7D&objectStoreName=FNOBS1&objectType=folder";
			return URL;
		}
	 
	public static boolean CheckContractCopies(String MyID,ObjectStore os){
		Folder folder = Factory.Folder.fetchInstance(os, MyID, null);
		StringList FileNetCommandList=folder.getProperties().getStringListValue("FileNetCommand");
		ArrayList<String> CopyToList=new ArrayList<String>();
		String CopyOf="";
		for(int i=0;i<FileNetCommandList.size();i++){
			if(FileNetCommandList.get(i).toString().contains("Copy To:")){
				CopyToList.add(FileNetCommandList.get(i).toString().split(":")[1]);
			}else if(FileNetCommandList.get(i).toString().contains("Copy Of:")){
				CopyOf=FileNetCommandList.get(i).toString().split(":")[1];
			}
		}
		boolean hasCopies=false;
		if(CopyOf!=""||CopyToList.size()>0)
		{
			hasCopies=true;
		}
//		if(CopyToList.size()>0)//contain Copy to
//		{
//			for (int i = 0; i < CopyToList.size(); i++) {
//				ContactCopies.add(CopyToList.get(i));
//			}
//		}
//		else if(CopyOf!="")//contain copy of
//		{
//			ContactCopies.add(CopyOf);
//			//get parent object 
//			Folder Mainfolder = Factory.Folder.fetchInstance(os, CopyOf, null);
//			//get its children
//			StringList FileNetCommand=Mainfolder.getProperties().getStringListValue("FileNetCommand");
//			for(int i=0;i<FileNetCommand.size();i++){
//				if(FileNetCommand.get(i).toString().contains("Copy To:")){
//					String tempID= FileNetCommand.get(i).toString().split(":")[1];
//					if (tempID.compareTo(MyID)!=0) {
//						ContactCopies.add(tempID);
//					}
//				}
//			}
//		}
		
//		if(ContactCopies.size()>0)
//		return true;
//		else {
//			return false;
//		}
		return hasCopies;
	}
	
 	public static IndependentObjectSet searchForDocuments (ObjectStore o, String select,String from,String where,String orderBy ,int maxRecords){
		try{
			SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList(select);
		    sqlObject.setFromClauseInitialValue(from, "d", true);
		    sqlObject.setWhereClause(where);
		    if(orderBy != null) sqlObject.setOrderByClause(orderBy);
		    if(maxRecords != 0) sqlObject.setMaxRecords(maxRecords);
		   
		    SearchScope search = new SearchScope(o);
	
		    // Set the page size (Long) to use for a page of query result data. This value is passed 
		    // in the pageSize parameter. If null, this defaults to the value of 
		    // ServerCacheConfiguration.QueryPageDefaultSize.
		    Integer myPageSize = new Integer(100);
	
		    // Specify a property filter to use for the filter parameter, if needed. 
		    // This can be null if you are not filtering properties.
		    PropertyFilter myFilter = new PropertyFilter();
		    int myFilterLevel = 1;
		    myFilter.setMaxRecursion(myFilterLevel);
		    myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 
		        
		    // Set the (Boolean) value for the continuable parameter. This indicates 
		    // whether to iterate requests for subsequent pages of result data when the end of the 
		    // first page of results is reached. If null or false, only a single page of results is 
		    // returned.
		    Boolean continuable = new Boolean(true);
	
		    // Execute the fetchObjects method using the specified parameters.
		    IndependentObjectSet set = search.fetchObjects(sqlObject, myPageSize, myFilter, continuable);
		    
		    System.out.println("SQL: " + sqlObject.toString());
		    return set;
		    
		    //return myObjects;
		}catch(Exception e){
			try{	
				
				}catch(Exception t){
					
				}
			return null;
		}
		
	   
	}

 	public  static IndependentObjectSet searchForFolders (ObjectStore o, String select,String from,String where,String orderBy ,int maxRecords)throws Exception{
		try{
			SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList(select);
		    sqlObject.setFromClauseInitialValue(from, "d", false);
		    sqlObject.setWhereClause(where);
		    if(orderBy != null) sqlObject.setOrderByClause(orderBy);
		    if(maxRecords != 0) sqlObject.setMaxRecords(maxRecords);
		   
		    SearchScope search = new SearchScope(o);

		    // Set the page size (Long) to use for a page of query result data. This value is passed 
		    // in the pageSize parameter. If null, this defaults to the value of 
		    // ServerCacheConfiguration.QueryPageDefaultSize.
		    Integer myPageSize = new Integer(100);

		    // Specify a property filter to use for the filter parameter, if needed. 
		    // This can be null if you are not filtering properties.
		    PropertyFilter myFilter = new PropertyFilter();
		    int myFilterLevel = 1;
		    myFilter.setMaxRecursion(myFilterLevel);
		    myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 
		        
		    // Set the (Boolean) value for the continuable parameter. This indicates 
		    // whether to iterate requests for subsequent pages of result data when the end of the 
		    // first page of results is reached. If null or false, only a single page of results is 
		    // returned.
		    Boolean continuable = new Boolean(true);

		    // Execute the fetchObjects method using the specified parameters.
		    IndependentObjectSet set = search.fetchObjects(sqlObject, myPageSize, myFilter, continuable);
//		    System.out.println("SQL: " + sqlObject.toString());
		    //Uploader4.writeToFile(Uploader4.logFile,true,"SQL: " + sqlObject.toString());
		    return set;
		    
		    //return myObjects;
		}catch(Exception e){
			try{
				e.printStackTrace();
//				UploaderScreen.writeToFile("------------Uploader.searchForDocuments------------");
//				UploaderScreen.writeToFile(e.getMessage());
//				if(e.getMessage()==null)
//					UploaderScreen.writeToFile("null");
				}catch(Exception t){
					t.printStackTrace();
//					UploaderScreen.writeToFile(t.getMessage());
//					if(t.getMessage()==null)
//						UploaderScreen.writeToFile("null");
				
				}
			return null;
		}
		
	    
	}
 	 
	public static void DeleteAnnotationsFromFolder(Folder f)
	 {
		AnnotationSet anotations=f.get_Annotations();
	 	Iterator iter = anotations.iterator();
	 	while (iter.hasNext()) {
	 		Annotation a=(Annotation) iter.next();
	 		a.delete();
	 		a.save(RefreshMode.REFRESH);
		}
	 }
	
	public static void CustomizeColumns(ObjectStore os,Folder f,File xml) throws IOException{
		try{
			Annotation annObject = Factory.Annotation.createInstance(os, "Annotation");
			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
			ctObject.setCaptureSource(new FileInputStream(xml));
			ctObject.set_ContentType("application/x-filenet-folderprefs-columns");
			ContentElementList contentList = Factory.ContentElement.createList();
			annObject.set_MimeType("application/x-filenet-folderprefs-columns");
			annObject.set_DescriptiveText("Annotation applied to folder via CE Java API");
			annObject.set_AnnotatedObject(f);	
			contentList.add(ctObject);
			annObject.set_ContentElements(contentList);
			annObject.save(RefreshMode.REFRESH);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void AssociateEntryTemplate(ObjectStore os,Folder f,File xml) throws IOException{
		try{
			Annotation annObject = Factory.Annotation.createInstance(os, "CmXTFolderPreferences");
			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
			ctObject.setCaptureSource(new FileInputStream(xml));
			ctObject.set_ContentType("application/xml");
			ContentElementList contentList = Factory.ContentElement.createList();
			annObject.set_MimeType("application/x-filenet-folderprefs-templates");
			annObject.set_DescriptiveText("Annotation applied to folder via CE Java API");
			annObject.set_AnnotatedObject(f);	
			contentList.add(ctObject);
			annObject.set_ContentElements(contentList);
			annObject.save(RefreshMode.REFRESH);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static String getChoiceListValue(ObjectStore os, Id id,String choiceValue){
			try{
				com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
				com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
				Choice choice;
				Iterator choiceIt = choices.iterator();
				while (choiceIt.hasNext()){
					choice = (Choice)choiceIt.next();
					if(choice.get_DisplayName().compareTo(choiceValue)==0){
						return choice.get_ChoiceStringValue();
					}
				}
				return "";
				}catch(Exception e){
					e.printStackTrace();
					return "";
				}
	 }
	 
	public static String getGroupName(ObjectStore os, Id id,String choiceValue){
			
			try{
				com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
				com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
				Choice choice;
				Iterator choiceIt = choices.iterator();
				while (choiceIt.hasNext())
				{
					choice = (Choice)choiceIt.next();
					com.filenet.api.collection.ChoiceList choices2 = choice.get_ChoiceValues();
					Choice choice2;
					Iterator choiceIt2 = choices2.iterator();
					while (choiceIt2.hasNext())
						{
						choice2 = (Choice)choiceIt2.next();
						if(choice2.get_ChoiceStringValue().compareTo(choiceValue)==0){
						return choice.get_DisplayName();
						}
						}
					}
				return "";
				}catch(Exception e){
					
					return "";
			}
			
		}
		
	public static String getOutgoingLetterCode(ObjectStore o,String project)throws Exception{

			String select = "d.OutgoingLetterCode";
			String from = "SODICProject";
			String where = "d.OutgoingLetterCode is not null AND '"+project+"' in d.Project";
			
			IndependentObjectSet set = searchForDocuments(o,select,from,where,null,0);
			
			if(set.isEmpty()){
				return "";
			}else{
				int count =0;
				String code="";
				for( Iterator i = set.iterator(); i.hasNext(); )
				{
					com.filenet.api.core.CustomObject resDoc = (com.filenet.api.core.CustomObject)i.next();
					code = resDoc.getProperties().getStringValue("OutgoingLetterCode");
				    count ++;
				 }
				if(count==1){
					return code;
				}else{
					return "";
				}

			}

		}
	
	public static String[] GetFolderNames(){
		String text=readFile(FoldersTXT);
		String[] list=text.split(";");
		return list;
	}
	
	public static String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public static void UpdateFolderColumnsAndTemplates(ObjectStore os,Folder folder) throws IOException
	{
		
		
//		String title = folder.get_FolderName();
		
		String path = folder.get_PathName();
		String[] foldersArray = path.substring(1).split("/");
		String title = foldersArray[1];
		writeToFile(formatter.format(today)+"-----------Folder Cusomizer Trrigered------------Folder Type:- "+title);
		
			
				try {
					String filepath=Path+title+CustomizeColumnsFileName;
					File xml=new File(filepath);
					DeleteAnnotationsFromFolder(folder);
					CustomizeColumns(os, folder,xml);
				} catch (IOException e) {
					writeToFile(formatter.format(today)+"---Customize Columns Exception--------Folder Cusomizer Trrigered------------Folder Type:- "+title);
					
				}
				try {
					String filepath2=Path+title+EntryTemplateFileName;
					File xml2=new File(filepath2);
					AssociateEntryTemplate(os, folder, xml2);
				} catch (IOException e) {
					writeToFile(formatter.format(today)+"---Associate Entry Tamplate Exception--------Folder Cusomizer Trrigered------------Folder Type:- "+title);
				}
			
		
	}
	
	private static void writeToFile(String string) {
		// TODO Auto-generated method stub
		
	}

	public static void GetLogFilePath() throws IOException
		{
		SimpleDateFormat Logformatter = new SimpleDateFormat("yyyy-MM-dd");
		String FileName=Logformatter.format(today).concat(".txt");
		String ClogFile = "C:/IBM/Development/Automation2"; //OnVmware
		String DlogFile = "D:/IBM/Development/Automation2";  //On Site
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
	/*//update users choice list values
//		String ExcelPath="C:\\Users\\Mina.PROSOFT\\Desktop\\MAILS (20-4-2015).xls";
//		mails=getData(new File(ExcelPath));
//		String[] propList={"Info","ActionBy"};
//		String type="Submittal";
//		IndependentObjectSet DocumentsSet=GetAllDocumentsUnderPath(os, type, propList );
//		if(DocumentsSet.isEmpty()==true)
//		{
//			System.out.println("No Data Found");
//		}
//		else
//		{
//			Document doc = Factory.Document.createInstance(os, null);
//			for( Iterator i = DocumentsSet.iterator(); i.hasNext(); )
//			{
//				for (int l = 0; l < propList.length; l++) {
//					String Property=propList[l];
//					System.out.println(doc.get_Id().toString());
//					System.out.println("Property = "+Property);
//					//If Single Value
//					if(Property.equalsIgnoreCase("Attention")||Property.equalsIgnoreCase("By")||Property.equalsIgnoreCase("DirectedBy"))
//					{
//						doc = (Document) i.next();
//						doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
//						String Newvalue =GetNewMailValue(doc.getProperties().getStringValue(Property).substring(0, doc.getProperties().getStringValue(Property).lastIndexOf('_')));
//						System.out.println("New Value = "+Newvalue);
//						if(Newvalue!="")
//						{
//							doc.getProperties().putValue(Property, Newvalue);
//						}					
//					}
//					else
//					{
//						doc = (Document) i.next();
//						doc=Factory.Document.fetchInstance(os, doc.get_Id(), null);
//						StringList OldList=doc.getProperties().getStringListValue(Property);
//						StringList NewList=Factory.StringList.createList();
//						Boolean ValidValues=true;
//						for (int j = 0; j < OldList.size(); j++) 
//						{
//							String CurrentMail=OldList.get(j).toString();
//							String Newvalue =GetNewMailValue(CurrentMail.substring(0, CurrentMail.lastIndexOf('_')));
//							System.out.println("Old Value = "+CurrentMail+" New Value = "+Newvalue);
//							if(Newvalue.equalsIgnoreCase(""))
//							{
//								ValidValues=false;
//								break;
//							}
//							else
//							{
//								NewList.add(Newvalue);
//							}
//						}
//						if(ValidValues)
//						{
//							doc.getProperties().putValue(Property, NewList);
//						}
//					}
//				}
//				doc.save(RefreshMode.NO_REFRESH);
//				System.out.println("Saved");
//			}
//		}
*/	 
	public static void ExportContracts()
	{
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		try {
			IndependentObjectSet contracts=GetAllContractFoldersUnderPath(os, "/");
			if(contracts.isEmpty()==true)
		 	{
				System.out.println("No Data !!!");
			}
			else
			{
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFCreationHelper createhelper = wb.getCreationHelper();
				HSSFSheet sheet = wb.createSheet("Sheet1");
				Row row = null;
				Cell cell = null;
				Folder Contract = Factory.Folder.createInstance(os, "ContractFolder", null);
				int index = 1;
				
				for( Iterator i = contracts.iterator(); i.hasNext(); )
				{	
					row = sheet.createRow(index);
					Contract  = (Folder)i.next();
					Contract=Factory.Folder.fetchInstance(os, Contract.get_Id(), null);
					System.out.println(Contract.get_FolderName()+"\t"+Contract.getProperties().getStringValue("Company"));
					cell = row.createCell(0);
					cell.setCellValue((String) Contract.get_FolderName());
					cell = row.createCell(1);
					cell.setCellValue((String) Contract.getProperties().getStringValue("Development"));
					cell = row.createCell(2);
					int ProjectsSize= Contract.getProperties().getStringListValue("Project").size();
					String Projectv="";
					for (int j = 0; j < ProjectsSize; j++) 
					{
						Projectv+=Contract.getProperties().getStringListValue("Project").get(j).toString().concat(" , ");
					}
					cell.setCellValue(Projectv);
					cell = row.createCell(3);
					cell.setCellValue((String) Contract.getProperties().getStringValue("Company"));
					cell = row.createCell(4);
					boolean hascopies=CheckContractCopies(Contract.get_Id().toString(), os);
					
					cell.setCellValue(hascopies);
					cell = row.createCell(5);
					cell.setCellValue((String) Contract.getProperties().getStringValue("CompanyCategory"));
					cell = row.createCell(6);
					cell.setCellValue(Contract.get_Id().toString());
					cell = row.createCell(7);
					cell.setCellValue(Contract.getProperties().getStringListValue("Project").size());
					index++;
				}
				
				try 
		 		{
					File file=new File("C:\\Users\\mina\\Desktop\\New folder\\SodicContracts.xls");
					FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
					wb.write(out);
					out.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	 
	public static void ExportCompanies()
	{
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFCreationHelper createhelper = wb.getCreationHelper();
			HSSFSheet sheet = wb.createSheet("Sheet1");
			Row row = null;
			Cell cell = null;
			Cell cell2 = null;
			Cell cell3 = null;
			com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, new Id("{EB509EF4-EA3A-4378-B019-32F585B2E029}"), null);
			com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
			Choice GroupChoice;
			int index = 1;
			Iterator choiceIt = choices.iterator();
			while (choiceIt.hasNext())
			{
				GroupChoice = (Choice)choiceIt.next();
				com.filenet.api.collection.ChoiceList choices2 = GroupChoice.get_ChoiceValues();
				Choice ChildChoice;
				Iterator choiceIt2 = choices2.iterator();
				while (choiceIt2.hasNext())
				{
					ChildChoice = (Choice)choiceIt2.next();
//		.concat("$").concat(GroupChoice.get_DisplayName());
					row = sheet.createRow(index);
					cell = row.createCell(0);
					cell.setCellValue(ChildChoice.get_ChoiceStringValue());
					cell2 = row.createCell(1);
					cell2.setCellValue(ChildChoice.get_DisplayName());
					cell3 = row.createCell(2);
					cell3.setCellValue(GroupChoice.get_DisplayName());
					index++;
				}
			}
			try {
				File file=new File("C:\\Users\\mina\\Desktop\\New folder\\companies.xls");
				FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
				wb.write(out);
				out.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void ExportProjects()
	{
	Connection conn = Factory.Connection.getConnection(URI);
	Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
	UserContext uc = UserContext.get();
	uc.pushSubject(subj);
	Domain dom = Factory.Domain.getInstance(conn, null);
	ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
	try {
		String Project="";
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCreationHelper createhelper = wb.getCreationHelper();
		HSSFSheet sheet = wb.createSheet("Sheet1");
		Row row = null;
		Cell cell = null;
		com.filenet.api.security.MarkingSet markingSet = Factory.MarkingSet.fetchInstance(dom, new Id(projectMarkingSetId),null);
		MarkingList markingList = markingSet.get_Markings();
		Marking marking;
		Iterator markingIt = markingList.iterator();
		int index=1;
		while (markingIt.hasNext())
		{
			marking = (Marking)markingIt.next();
			Project=marking.get_MarkingValue();
			System.out.println("Project --> "+Project);
			String development= getGroupName(os,new Id(developmentGoupsChoiceListId),Project);
			System.out.println("Development --> "+development);
			String OutgoingLetterCode= getOutgoingLetterCode(os, Project);
			System.out.println("OutgoingLetterCode --> "+OutgoingLetterCode);
			row = sheet.createRow(index);
			cell = row.createCell(0);
			cell.setCellValue((String) development);
			cell = row.createCell(1);
			cell.setCellValue((String) Project);
			cell = row.createCell(2);
			cell.setCellValue((String) OutgoingLetterCode);
			index++;
		}
		try {
			File file=new File("C:\\Users\\mina\\Desktop\\New folder\\Projects.xls");
			FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
			wb.write(out);
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}
}
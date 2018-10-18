package temp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.text.*;

import javax.security.auth.Subject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.Factory.Permission;
import com.filenet.api.core.Folder;
import com.filenet.api.events.CmChangePreprocessorAction;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Marking;
import com.filenet.api.security.MarkingSet;
import com.filenet.api.security.User;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.*;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.apiimpl.core.ChoiceImpl;

import java.util.Calendar;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class SODICUsersCount {
	public static final String URI ="http://172.16.10.38:9080/wsi/FNCEWS40MTOM/";					//On SODIC Site
	 public static final String USERID = "filenet";													//On SODIC Site
	 public static final String PASSWORD = "Fn1234567";	
	public static void main(String[] args) {
		ArrayList<String> Creators=new ArrayList<String>();
		ArrayList<File> files=GetDocFromScanFolder();
		for (int fn = 0; fn < files.size(); fn++) 
		{
			String FilePath=files.get(fn).getAbsolutePath();
			String Content=readFile(FilePath);
			String[] Lines=Content.split("\n");
			for (int i = 0; i < Lines.length; i++) {
				if (Lines[i].contains("------DocumentUpdate Event Triggered---------------LastModifier = ")) {
					String creator=Lines[i].split(" = ")[1].split(" ")[0];
					if (!Creators.contains(creator)) {
						Creators.add(creator);
					}				
				}
			}
		}
		
		for (int i = 0; i < Creators.size(); i++) {
			System.out.println(Creators.get(i));
		}
	}
	
	public static ArrayList<File> GetDocFromScanFolder()
	{
		ArrayList<File> files=new ArrayList<File>();
		String folderpath="C:/Users/mina/Desktop/2016";
		File folder=new File(folderpath);
		List<File> listOfFiles2 = (List<File>)FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (int i = 0; i < listOfFiles2.size(); i++) 
		{
			if (listOfFiles2.get(i).isFile()&&listOfFiles2.get(i).getName().contains(".txt")) 
			{
				System.out.println("File " + listOfFiles2.get(i).getName());
				files.add(listOfFiles2.get(i));
			}
		}
		return files;
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
}

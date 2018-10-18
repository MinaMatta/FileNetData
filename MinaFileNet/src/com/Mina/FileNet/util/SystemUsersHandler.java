package com.Mina.FileNet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class SystemUsersHandler {

	public static void GetAllSystemUsers(String URI,String USERID,String PASSWORD,String ExcelPath)
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

		 		IndependentObjectSet set = FilenetSearch.searchForObjectsInObjectStore(os,select,from,where,"LastModifier",0);
		 		
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
					File file=new File(ExcelPath);
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

	public static String GetCreatorMail(ObjectStore os,Document Doc,String companyDomain)
	{
		String creatormail=Doc.get_Creator().toString().toLowerCase()+"@"+companyDomain;
		return creatormail;
	}
}

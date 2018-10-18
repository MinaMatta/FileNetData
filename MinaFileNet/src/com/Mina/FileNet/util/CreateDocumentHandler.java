package com.Mina.FileNet.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.ObjectStore;

public class CreateDocumentHandler {

	
	public static String GetQryForDuplicates(ObjectStore os,ArrayList FNattr,ArrayList propTypes,ArrayList ExcelValues) throws ParseException
	{
		SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat SQLformatter = new SimpleDateFormat("yyyyMMdd");
		String where="";
		ArrayList<String> conditions=new ArrayList<String>();
		for (int i = 0; i < FNattr.size(); i++) 
		{
			String PropertyType=propTypes.get(i).toString();
//			System.out.println(PropertyType);
			if (ExcelValues.get(i).toString()!=null) 
			{
				if(ExcelValues.get(i).toString()!="NULL_Value"&&ExcelValues.get(i).toString().compareTo("-")!=0&&ExcelValues.get(i).toString().length()>0&&ExcelValues.get(i).toString()!="") {
					if (PropertyType.compareTo("STRING")==0) 
					{
						conditions.add("d."+FNattr.get(i)+" = '"+ExcelValues.get(i)+"'");
					}
					else if (PropertyType.compareTo("DATE")==0) 
					{
//						Date d=dateformatter.parse(ExcelValues.get(i).toString());
//						String fromDate = SQLformatter.format(d).concat("T000000B");
//						String toDate = SQLformatter.format(d).concat("T235959B");
//						conditions.add("d."+FNattr.get(i)+" >= "+fromDate+" AND d."+FNattr.get(i)+" < "+toDate);
//						conditions.add("d."+FNattr.get(i)+" >= "+toDate);
					}
					else if (PropertyType.compareTo("NUMBER")==0) {
						conditions.add("d."+FNattr.get(i)+" = "+ExcelValues.get(i));
					}
				}
			}
			
		}
		for (int i = 0; i < conditions.size(); i++) {
			if (i!=0) {
				where+=" AND ";
			}
			where+=conditions.get(i);
		}
		return where;
	}
	
	public static void MoveFileAfterUploading(String ArchivingPath,File file)
	{
		String newFilePath=ArchivingPath.concat(file.getAbsolutePath().split(":")[1]);
		File archiveFile=new File(newFilePath);
		archiveFile.getParentFile().mkdirs();
		System.out.println(archiveFile.getAbsolutePath());
		try {
			FileUtils.moveFile(file, archiveFile);
		} catch (IOException e) {
		}
	}
	
	public static boolean IsDuplicateDocument(ObjectStore os,String FilenetClass,ArrayList FNattr,ArrayList PropTypes,ArrayList Values) throws Exception
	{
		String select = "d.Id";
		String from = FilenetClass;
		String where =GetQryForDuplicates(os,FNattr,PropTypes,Values);
		System.out.println(where);
		IndependentObjectSet set = FilenetSearch.searchForObjectsInObjectStore(os, select, from, where, null, 10);

		if(set.isEmpty()==true)
	 	{
			System.out.println("Not Duplicate !!!");
			return false;
		}
		else
		{
			System.out.println("Duplicated !!!");
			return true;
		}	
	}
	
	
	public static String getMimeType(String extension){
		String mimeType = "";
		if(extension.equalsIgnoreCase("tif") || extension.equalsIgnoreCase("tiff") ){
			mimeType="image/tiff";
		}else if (extension.equalsIgnoreCase("gif")|| extension.equalsIgnoreCase("giff")){
			mimeType="image/gif";
		}else if (extension.equalsIgnoreCase("jpeg")|| extension.equalsIgnoreCase("jpg")){
			mimeType="image/jpeg";
        }else if (extension.equalsIgnoreCase("bmp")|| extension.equalsIgnoreCase("dib")){
        	mimeType="image/bmp";
        }else if (extension.equalsIgnoreCase("pdf")){
        	mimeType="application/pdf";
        }else if (extension.equalsIgnoreCase("txt")||extension.equalsIgnoreCase("text")){
        	mimeType="text/plain";
        }else if (extension.equalsIgnoreCase("max")){
        	mimeType="application/vnd.max";
        }else if (extension.equalsIgnoreCase("doc")||extension.equalsIgnoreCase("dot")||extension.equalsIgnoreCase("rtf") || extension.equalsIgnoreCase("docx")){
        	mimeType="application/msword";
       }else if (extension.equalsIgnoreCase("msg")){
    	   mimeType="application/vnd.msg";
       }else if (extension.equalsIgnoreCase("xls")||extension.equalsIgnoreCase("xlt")||
    		extension.equalsIgnoreCase("xlm")||extension.equalsIgnoreCase("xld")||
			extension.equalsIgnoreCase("xla")||extension.equalsIgnoreCase("xlc")||
			extension.equalsIgnoreCase("xlw")||extension.equalsIgnoreCase("xll")|| 
			extension.equalsIgnoreCase("xlsx")){
    	   mimeType="application/vnd.ms-excel";
       }else if (extension.equalsIgnoreCase("ppt")||extension.equalsIgnoreCase("pot")||
    		extension.equalsIgnoreCase("ppa")||extension.equalsIgnoreCase("pps")||
			extension.equalsIgnoreCase("pwz")){
    	   mimeType="application/vnd.ms-powerpoint";
       }else if (extension.equalsIgnoreCase("zip")){
    	   mimeType="multipart/x-zip";
       }
		return mimeType;
	}
}

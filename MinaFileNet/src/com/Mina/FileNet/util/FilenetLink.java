package com.Mina.FileNet.util;

import java.io.IOException;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;

public class FilenetLink {
	public static String GetFolderLink(String WorkPlaceXTURL,String ObjectStoreName,Folder f) throws IOException
	{
		
		String URL=WorkPlaceXTURL+"/getContent?id=%7B";  //On Vmware
//		String URL="http://172.16.10.38:9080/WorkplaceXT/getContent?id=";  //On Site
		String FolderID=f.get_Id().toString();
		URL+=FolderID.substring(FolderID.indexOf('{')+1,FolderID.indexOf('}'));
		URL+="%7D&objectStoreName="+ObjectStoreName+"&objectType=folder";
		return URL;
	}
	public String DocumentPropertiesLink(String baseP8URL,String ObjectStoreName,Document doc,String LinkName)
	  {
		  String vsId = doc.get_VersionSeries().get_Id().toString();
		  String propertiesUrl = baseP8URL + "properties/Properties.jsf?objectStoreName="+ObjectStoreName + "&id=" + doc.get_Id().toString() + "&vsId=" + vsId + "&objectType=document&returnUrl=http%3A%2F%2F172.16.10.38%3A9080%2FWorkplaceXT%2FCloseWindowAjax.jsp%3FjsfViewId%3D%2FBrowse.jsp";
		  String html="\n<a href='"+propertiesUrl+"'>"+LinkName+"</a>";
		  return html;
	  }
	  
	public String DocumentContentLink(String baseP8URL,String ObjectStoreName,Document doc,String LinkName)
	  {
		  String vsId = doc.get_VersionSeries().get_Id().toString();
		  String contentUrl = baseP8URL + "getContent?objectStoreName="+ObjectStoreName + "&id=" + doc.get_Id().toString() + "&vsId=" + vsId + "&objectType=document";
		  String html="\n<a href='"+contentUrl+"'>"+LinkName+"</a>";
		  return html;
	  }
}

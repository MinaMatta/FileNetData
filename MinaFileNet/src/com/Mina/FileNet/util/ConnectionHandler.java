package com.Mina.FileNet.util;


import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Domain;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class ConnectionHandler {

public static UserContext UC;
	public static ObjectStore OpenConnectionWithFileNet(String URI,String USERID,String PASSWORD,String ObjectStoreName)
	{
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UC = UserContext.get();
		UC.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore o = Factory.ObjectStore.fetchInstance(dom,ObjectStoreName, null);
		return o;
	}
	
	public static void CloseConnectionWithFileNet()
	{
		if(UC!=null)
		UC.popSubject();
	}
	
}

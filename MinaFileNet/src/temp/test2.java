package temp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class test2 {

	
//	public static void main(String[] args) {
//		String[] f={"dwwlah@gmail.com","Mina_Adel@prosoft.com.eg"};
//		SendMail(f, "Subject1", "Deajhvjsfhvsjfvhjsfvsjfhvsjfvjsfvjsfhv");
//	}
	public static void SendMail(String[] To,String Subject,String Body)
	{
		final String username = "xeroxjavatest@gmail.com";
		final String password = "P@ssw0rd12345678";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props,new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("xeroxjavatest@gmail.com"));
			InternetAddress[] addressTo = new InternetAddress[To.length];
			for (int i = 0; i < To.length; i++) 
			{
				addressTo[i] = new InternetAddress(To[i],true);
			}
			message.addRecipients(Message.RecipientType.TO, addressTo);
			message.setSubject(Subject, "UTF-8");
			message.setText(Body, "UTF-8", "html");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}

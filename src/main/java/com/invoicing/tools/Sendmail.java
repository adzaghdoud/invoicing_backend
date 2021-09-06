package com.invoicing.tools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;

public class Sendmail {
	private String mailto;
	private String subject;
	private String contain;
	final org.apache.logging.log4j.Logger log =  LogManager.getLogger(this.getClass().getName());	
    public void sendmail() {
	try {
		InputStream input = new FileInputStream(System.getProperty("env.file.ext"));	
	    final Properties prop = new Properties();
	    prop.load(input);
 		Properties props = new Properties();
 		props.put("mail.smtp.auth", "true");
 		props.put("mail.smtp.starttls.enable","true");
 		props.put("mail.smtp.host",prop.getProperty("SMTP.HOST"));
 		props.put("mail.smtp.port",prop.getProperty("SMTP.PORT"));
 		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
 		Session session = Session.getInstance(props,
 		new javax.mail.Authenticator() {
 		protected PasswordAuthentication getPasswordAuthentication() {
 		return new PasswordAuthentication(prop.getProperty("SMTP.USERNAME"), prop.getProperty("SMTP.PASSWORD"));
 		}
 		});	
 		
 		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(prop.getProperty("COMPANY.EMAIL")));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(this.mailto));	
		message.setSubject(this.subject);
		
		
		Multipart multipart = new MimeMultipart();

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(this.contain, "text/html; charset=utf-8");
        multipart.addBodyPart(textBodyPart); 
        message.setContent(multipart);
		Transport.send(message);
	
	 }catch (Exception e) {
     log.error(ExceptionUtils.getStackTrace(e)); 
	 }
	 
}
public String getMailto() {
	return mailto;
}
public void setMailto(String mailto) {
	this.mailto = mailto;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getContain() {
	return contain;
}
public void setContain(String contain) {
	this.contain = contain;
}


}


package br.senai.sp.info.pweb.jucacontrol.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {

	/*
	 * Remetente do e-mail (quem envia)
	 */
	public static final String remetente = "senai132.info.2017.1s@gmail.com";
	
	/*
	 * Senha do remetente
	 */
	public static final String senhaRemetente = "TecInfoManha2017";
	
	private static Session getConfiguracoesDoEmail() {
		Properties props = new Properties();
		
		// Definir qual o servidor de envio de emails
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP é o nome de um protocolo de envio de emails (é mais para o pessoal da infraestrutura)
		
		// Define a classe de conexão SSL do java e a porta de conexão SSL
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "465");
		
		// Define a porta de conexão SSL do servidor de e-mails
		props.put("mail.smtp.port", "465");
		
		// Criando o objeto de configuração (Session)
		Session configuracao = Session.getInstance(props, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(remetente, senhaRemetente);
			}
			
		});
		
		return configuracao;
	}
	
	/***
	 * 
	 * @param titulo - O assunto do email
	 * @param corpo - O corpot do email
	 * @param destinatario - O endereço de email do destinatário
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public static void enviarMensagem(String titulo, String corpo, String destinatario) throws AddressException, MessagingException {
		Message msg = new MimeMessage(getConfiguracoesDoEmail());
		msg.setFrom(new InternetAddress(remetente));
		msg.addRecipient(RecipientType.TO, new InternetAddress(destinatario));
		msg.setSubject(titulo);
		msg.setText(corpo);
		
		Transport.send(msg);
	}
	
}

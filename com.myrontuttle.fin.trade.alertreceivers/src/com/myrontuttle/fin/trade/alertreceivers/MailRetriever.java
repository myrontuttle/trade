package com.myrontuttle.fin.trade.alertreceivers;

import java.io.*;
import java.util.*;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.api.TradeStrategyService;

/**
 * Code for reading unread mails from an email account (including Google Mail).
 * @author Myron Tuttle
 */
public class MailRetriever implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( MailRetriever.class );
	
	private final TradeStrategyService tradeStrategyService;
	private final String host;
	private final String protocol;
	private final int port;
	private final String user;
	private final String password;
	
	private final Properties props;

	// Constructor of the class.
	public MailRetriever(TradeStrategyService tradeStrategyService, String host, String protocol,
					int port, String user, String password) {
		this.tradeStrategyService = tradeStrategyService;
		this.host = host;
		this.protocol = protocol;
		this.port = port;
		this.user = user;
		this.password = password;

		/* Set the mail properties */
		this.props = System.getProperties();
		props.setProperty("mail.store.protocol", protocol);
	}

	public void run() {

		try {
			/* Create the session and get the store for read the mail. */
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore(protocol);
			store.connect(host, port, user, password);

			/* Mention the folder name which you want to read. */
			Folder inbox = store.getFolder("Inbox");
			if (inbox.getUnreadMessageCount() > 0) {
				logger.debug("Unread Messages Found: {}", inbox.getUnreadMessageCount());
			}

			/* Open the inbox using store. */
			inbox.open(Folder.READ_WRITE);

			/* Get the messages which are unread in the Inbox */
			Message messages[] = inbox.search(new FlagTerm(
					new Flags(Flag.SEEN), false));

			/* Use a suitable FetchProfile */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.FLAGS);
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			inbox.fetch(messages, fp);

			String subject = null;
			for (Message message : messages) {
				try {
					subject = message.getSubject();
				} catch (MessagingException me) {
					logger.warn("Problem reading mail: {}", me.getMessage());
					subject = message.getHeader("Subject")[0];
				}

				logger.debug("Retrieved email: {}", subject);
				tradeStrategyService.eventOccurred(subject);
				inbox.setFlags(new Message[] {message}, new Flags(Flags.Flag.SEEN), true);
			}

			inbox.close(true);
			store.close();
		} catch (NoSuchProviderException e) {
			logger.warn("Unable to retrieve email.", e);
		} catch (MessagingException e) {
			logger.warn("Unable to retrieve email.", e);
		}
	}

	/*
	void printAllMessages(Message[] msgs) throws Exception {
		for (int i = 0; i < msgs.length; i++) {
			System.out.println("MESSAGE #" + (i + 1) + ":");
			printEnvelope(msgs[i]);
		}
	}

	// Print the envelope(FromAddress,ReceivedDate,Subject)
	void printEnvelope(Message message) throws Exception {
		Address[] a;
		// FROM
		if ((a = message.getFrom()) != null) {
			for (int j = 0; j < a.length; j++) {
				System.out.println("FROM: " + a[j].toString());
			}
		}
		// TO
		if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++) {
				System.out.println("TO: " + a[j].toString());
			}
		}
		String subject = message.getSubject();
		Date receivedDate = message.getReceivedDate();
		String content = message.getContent().toString();
		System.out.println("Subject : " + subject);
		System.out.println("Received Date : " + receivedDate.toString());
		System.out.println("Content : " + content);
		getContent(message);
	}

	void getContent(Message msg) {
		try {
			String contentType = msg.getContentType();
			System.out.println("Content Type : " + contentType);
			Multipart mp = (Multipart) msg.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				dumpPart(mp.getBodyPart(i));
			}
		} catch (Exception ex) {
			System.out.println("Exception arose during getContent");
			ex.printStackTrace();
		}
	}

	void dumpPart(Part p) throws Exception {
		// Dump input stream ..
		InputStream is = p.getInputStream();
		// If InputStream is not already buffered, wrap a BufferedInputStream
		// around it.
		if (!(is instanceof BufferedInputStream)) {
			is = new BufferedInputStream(is);
		}
		int c;
		System.out.println("Message : ");
		while ((c = is.read()) != -1) {
			System.out.write(c);
		}
	}
	*/
}
package com.myrontuttle.fin.trade.alertreceivers;

import static org.junit.Assert.*;

import java.security.Security;
import java.util.List;

import javax.mail.Flags;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.icegreen.greenmail.store.MailFolder;
import com.icegreen.greenmail.store.StoredMessage;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class MailRetrieverTest {
	
	String userId;
	GreenMail greenMail;
	TradeStrategyService mockedTss;
	
	@Before
	public void setUp() {
		Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
	    greenMail = new GreenMail(ServerSetupTest.ALL);
	    greenMail.start();
	    
	    mockedTss = mock(TradeStrategyService.class);
	}

	@Test
	public void testRunMatchesAlert() throws Exception {

	    // Setup mail server and user
	    final String user = "test@localhost";
	    final String password = "testpass";
	    final String host = "localhost";
	    final String protocol = EmailAlertReceiver.IMAPS;
	    GreenMailUser gmUser = greenMail.setUser(user, user, password);
	    final int port = greenMail.getImaps().getPort();

	    // Create random message to avoid potential residual lingering problems
	    final String subject = GreenMailUtil.random();
	    final String body = GreenMailUtil.random();
	    GreenMailUtil.sendTextEmailSecureTest(user, "from@someplace.com", subject, body);
	    
	    // Wait for max 5s for 1 email to arrive
	    assertTrue(greenMail.waitForIncomingEmail(5000, 1));
	    
	    // Arrange mock
	    doNothing().when(mockedTss).eventOccurred(subject);
	    
	    // Run
	    MailRetriever mailRetriever = new MailRetriever(mockedTss, host, protocol, port, user, password);
	    mailRetriever.run();
	    
	    // Assert
	    MailFolder inbox = greenMail.getManagers().getImapHostManager().getInbox(gmUser);
	    @SuppressWarnings("unchecked")
		List<StoredMessage> messages = inbox.getMessages();
	    if (!messages.isEmpty()) {
	        System.out.println("Subject is: " + messages.get(0).getMimeMessage().getSubject());

	    	assertTrue(messages.get(0).getFlags().contains(Flags.Flag.SEEN));
	    } else {
	        fail("No email for user arrived");
	    }
	}

	@After
	public void tearDown() throws Exception {
	    if (greenMail != null) {
	        greenMail.stop();
	    }
	}
}

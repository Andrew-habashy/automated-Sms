package com.twilio;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.message.Feedback;
import com.twilio.rest.messaging.v1.Service;
import com.twilio.type.PhoneNumber;

@WebServlet(name = "Confirm", urlPatterns = {"/Confirm"})

public class sendSms extends HttpServlet {

	public static final String ACCOUNT_SID = "add your account SID";
	public static final String AUTH_TOKEN = " add your Authroizeation_token";
	public static final String twilio_number = "purchased twilio phone number";
	static ResourceSet<Message> messagesRecord;
	public static Message message;
	public static String send_to;
	public static String Cname;
	public static String error_message;
	public static HttpServletRequest request;
	public static HttpServletResponse response;
	public static Feedback feedback;
	//public static Service service;
	
	
	public static boolean check_connection() {
		Socket sock = new Socket();
		InetSocketAddress addr = new InetSocketAddress("www.google.com", 80);
		try {
			sock.connect(addr, 3000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	// capital first letter in the name from the user input 
	public static boolean check_name() {
		try {
			Cname = Cname.substring(0, 1).toUpperCase() + Cname.substring(1);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	

	public static String sms() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		messagesRecord = Message.reader().read();
		
		String sms = String
				.format("add your massage here", Cname);
		return sms;
	}

	public static boolean createSms() {
		try {
			
			message = Message.creator(new PhoneNumber("+1" + send_to), " your Msg sid ", sms()).setProvideFeedback(true).create();
			
			//System.out.print(service.getStatusCallback());
		} catch (ApiException e) {
			return false;
		}
		
		return true;
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String uniqueId = request.getParameter("id");
	    // Lookup variable `uniqueId` in a database to find messageSid
	    String messageSid = message.getSid();

	    Feedback feedback =
	        Feedback.creator(messageSid).setOutcome(Feedback.Outcome.CONFIRMED).create();
	}
	
	
	
	public static void main(String[] args) throws IOException {
	
	}

}

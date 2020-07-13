package com.twilio;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.message.Feedback;

public class frame extends sendSms {

	private JFrame frmSendReview;
	private JTextField name;
	private JLabel lblEnterPhoneNumber;
	private JTextField number;
	private static JLabel Result;
	private JTabbedPane tabbedPane;
	private JPanel send;
	private JPanel report;
	private JScrollPane scrollPane;
	private JTable table;
	private static String[] coloumNames;
	private static String[][] data;
	private DefaultTableModel model;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame window = new frame();
					window.frmSendReview.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSendReview = new JFrame();
		frmSendReview.setTitle("send review");
		frmSendReview.setBounds(100, 100, 875, 567);
		frmSendReview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSendReview.getContentPane().setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 833, 494);
		frmSendReview.getContentPane().add(tabbedPane);

		send = new JPanel();
		tabbedPane.addTab("Send sms", null, send, null);
		send.setLayout(null);

		JLabel lblEnterCoustomerName = new JLabel("Enter coustomer name");
		lblEnterCoustomerName.setBounds(39, 77, 175, 22);
		send.add(lblEnterCoustomerName);
		lblEnterCoustomerName.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblEnterCoustomerName.setHorizontalAlignment(SwingConstants.CENTER);

		name = new JTextField();
		name.setBounds(296, 74, 146, 28);
		send.add(name);
		name.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		name.setColumns(10);

		lblEnterPhoneNumber = new JLabel("Enter phone number");
		lblEnterPhoneNumber.setBounds(39, 125, 160, 22);
		send.add(lblEnterPhoneNumber);
		lblEnterPhoneNumber.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblEnterPhoneNumber.setHorizontalAlignment(SwingConstants.CENTER);

		number = new JTextField();
		number.setBounds(296, 122, 156, 28);
		send.add(number);
		number.setFont(new Font("Times New Roman", Font.BOLD, 18));
		number.setColumns(10);

		JButton btnNewButton = new JButton("send review");
		btnNewButton.setBounds(282, 185, 170, 31);
		send.add(btnNewButton);
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));

		JLabel label = new JLabel("+1");
		label.setBounds(267, 127, 17, 19);
		send.add(label);
		label.setFont(new Font("Times New Roman", Font.BOLD, 16));

		Result = new JLabel("New label");
		Result.setBounds(182, 229, 386, 46);
		send.add(Result);
		Result.setFont(new Font("Times New Roman", Font.BOLD, 18));
		Result.setHorizontalAlignment(SwingConstants.CENTER);
		Result.setHorizontalTextPosition(SwingConstants.CENTER);

		report = new JPanel();
		tabbedPane.addTab("Report", null, report, null);
		report.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 804, 438);
		report.add(scrollPane);

		table = new JTable();
		coloumNames = new String[] { "Name", "Number", "Status", "Date" };
		data = new String[][] {};
		model = new DefaultTableModel(data, coloumNames);
		table.setModel(model);
		scrollPane.setViewportView(table);
		//create_report();

		Result.setVisible(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cname = name.getText();
				send_to = number.getText();
				boolean name = check_Cname(Cname);
				boolean connection = check_connection();
				
				if (connection == false) {
					Result.setVisible(true);
					Result.setText("No internet connection");
					Result.setForeground(Color.RED);
				} else {
					boolean sms = createSms();
					if (sms == false ) {
						Result.setVisible(true);
						Result.setText("Message not Sent, Wronge Phone Number");
						Result.setForeground(Color.RED);

					} else {
						Result.setVisible(true);
						Result.setText("Message sent");
						Result.setForeground(Color.green);
													
					}
				}
				String[] dataRow = { Cname, send_to, null, null };
				model.addRow(dataRow);
			}
		});

	}

	private void create_report() {
		String customer_name;
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		messagesRecord = Message.reader().read();
		for (Message record : messagesRecord) {
			try {
				customer_name = record.getBody().substring(3, record.getBody().indexOf("\n"));
			} catch (Exception e) {
				customer_name = null;
			}
			String to_number = record.getTo();
			String status = record.getStatus().toString();
			String date = record.getDateSent().minusHours(5).toString();
			String[] dataRow = { customer_name, to_number, status, date };
			model.addRow(dataRow);
		}

	}

	public static boolean check_Cname(String Cname) {
		if (Cname.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

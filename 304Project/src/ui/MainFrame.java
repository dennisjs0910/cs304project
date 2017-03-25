package ui;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import query.QueryFacade;
import row.Coach;
import row.Customer;
import row.Employee;
import row.LessonReportRow;
import row.ReservableTennisCourt;
import row.AggregationLessonCountRow;
import row.AggregationLessonAgeRow;
import row.NestedAggregationRow;
import row.DivisionCourtRow;
import row.DivisionCoachRow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by dennisjsyi on 2017-03-16.
 */
public class MainFrame extends JFrame implements ActionListener{
    private static final int  WIDTH = 900;
    private static final int  HEIGHT = 900;
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JLabel msglabel;
    
    private String userType;
    private String userName;
    private String userPassword;
    private AuthenticateUser auth;
    private int EmployeeID;
    private Customer customer;
    private Employee admin;
    private QueryFacade q;
    private Connection conn;
    
    private String lessonId;
    private String reserveCourtId;
    private String cancelCourtId;
    private String reservationDate;
    private String rStartTime;
    private String rEndTime;

    public MainFrame(){
    	q = new QueryFacade(false, "");
    	try {
			q.connect();
			System.out.println("connection!");
		} catch(Exception err){ 
			System.out.println("no connection turn on your server");
		}

    	auth = new AuthenticateUser();
        createHomeFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    
    public void newFrame() {
    	mainFrame.setVisible(false);
        mainFrame = new JFrame();
        mainFrame.setSize(WIDTH,HEIGHT);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowAdapter windowEvent){
                System.exit(0);
            }
        });

        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    public void produceCustomerFrame() {
        newFrame();
        System.out.println(customer);
        headerLabel.setText("Welcome " + customer.getName());
        JButton searchBookTennisCourt = new JButton("Tennis Court Reservation");
        JButton searchEnrollLesson = new JButton("Tennis Court Lessons");
        JButton updatePersonalInfo = new JButton("Update your personal information");
        JButton goBack = new JButton("back");
        controlPanel.add(searchBookTennisCourt);
        controlPanel.add(searchEnrollLesson);
        controlPanel.add(updatePersonalInfo);
        controlPanel.add(goBack);


        searchBookTennisCourt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookTennisCourtSubFrame();

            }
        });

        searchEnrollLesson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollLessonSubFrame();

            }
        });

        updatePersonalInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                custUpdateSubFrame();

            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHomeFrame();
            }
        });
    }

    public void produceEmployeeFrame(){
        newFrame();
        headerLabel.setText("Welcome Employee" + EmployeeID);
        
      //Button for update lessons Frame
        JButton employeeLessonsButton = new JButton("See Lesson Information");
        controlPanel.add(employeeLessonsButton);
        employeeLessonsButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		createEmployeeLessonsFrame("AVG");
        	}
        });
      //Button for update courts Frame
        JButton employeecourtsButton = new JButton("See court booking information");
        controlPanel.add(employeecourtsButton);
        employeecourtsButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		createEmployeeCourtsFrame();
        	}
        });
        
      //Button for search employees Frame
        JButton searchEmployeeButton = new JButton("Search Employees");
        controlPanel.add(searchEmployeeButton);
        searchEmployeeButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		createEmployeeSearchFrame();
        	}
        });
        
      //Button for search customers Frame
        JButton searchCustomersButton = new JButton("Search Customers");
        controlPanel.add(searchCustomersButton);
        searchCustomersButton.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		createCustomerSearchFrame();
        	}
        });
        
        //back button 
        JButton goBack = new JButton("back");
        controlPanel.add(goBack);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHomeFrame();
            }
        });
    }
    // Frame for employees to update lesson information
    private void createEmployeeLessonsFrame(String agg) {
    	final JFrame main = createSubFrame();
    	main.setVisible(true);
    	JPanel panel = new JPanel();
    	main.add(panel);
    	panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.add(new JLabel("Lesson information"));
    	
        //Lesson count
        JPanel lessonCountPanel = new JPanel();
        lessonCountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lessonCountPanel.setLayout(new BoxLayout(lessonCountPanel, BoxLayout.Y_AXIS));
        JLabel lessonLabel = new JLabel("Number of Students Enrolled:");
        lessonCountPanel.add(lessonLabel);
        try{
        	List<AggregationLessonCountRow> lessonCountRow = q.getAggregationLessonCount();
        	String[] columnNames = {"Number of Students", "Lesson ID", "Lesson Level"};
        	final Object[][] data = new Object[lessonCountRow.size()][3];

			int i=0;
			for(AggregationLessonCountRow row: lessonCountRow){
	        	String[] info = { String.valueOf(row.getNumStudents()), row.getLID(), row.getLevel()};
	        	data[i] = info;
	        	i++;
	        }
			final JTable aggregateLessonCountTable = new JTable(data,columnNames);
			aggregateLessonCountTable.setPreferredSize(new Dimension(400, 100));
			JScrollPane scrollPane = new JScrollPane(aggregateLessonCountTable);
			scrollPane.setPreferredSize(new Dimension(400, 100));
			lessonCountPanel.add(scrollPane);
        } catch (SQLException e){
        	System.out.println("Problem retrieving aggregation lesson count");
			e.printStackTrace();
        }
        panel.add(lessonCountPanel);
        //Average lesson age query
        JPanel lessonAgePanel = new JPanel();
        lessonAgePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lessonAgePanel.setLayout(new BoxLayout(lessonAgePanel, BoxLayout.Y_AXIS));

        String[] aggList= {"MAX", "MIN", "AVG"};
        final JComboBox<String> aggComboBox = new JComboBox<String>(aggList);
        aggComboBox.setSelectedItem(agg);
        aggComboBox.setVisible(true);
        lessonAgePanel.add(aggComboBox);
        JLabel lessonAgeLabel = new JLabel(" age of Students Enrolled:");
        lessonAgePanel.add(lessonAgeLabel);
        try{
        	List<AggregationLessonAgeRow> lessonAgeRow = q.getAggregationAgeLesson((String) aggComboBox.getSelectedItem());
        	String[] columnNames = {aggComboBox.getSelectedItem() + " Age of Students", "Lesson ID", "Lesson Level"};
        	final Object[][] data = new Object[lessonAgeRow.size()][3];

			int i=0;
			for(AggregationLessonAgeRow row: lessonAgeRow){
	        	String[] info = { String.valueOf(row.getAverageAge()), row.getLID(), row.getLevel()};
	        	data[i] = info;
	        	i++;
	        }
			final JTable aggregateLessonAgeTable = new JTable(data,columnNames);
			aggregateLessonAgeTable.setPreferredSize(new Dimension(400, 100));
			JScrollPane scrollPane = new JScrollPane(aggregateLessonAgeTable);
			scrollPane.setPreferredSize(new Dimension(400, 100));
			lessonAgePanel.add(scrollPane);
        } catch (SQLException e){
        	System.out.println("Problem retrieving aggregation lesson age");
			e.printStackTrace();
        }
        panel.add(lessonAgePanel);
      //close button 
        JButton close = new JButton("Close");
        panel.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	main.setVisible(false);
            }
        });
        
        aggComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String selected = (String) aggComboBox.getSelectedItem();
				main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
	            createEmployeeLessonsFrame(selected);
            }
        });
        
    }
    // Frame for employees to update court information
    private void createEmployeeCourtsFrame() {
    	final JFrame main = createSubFrame();
    	main.setVisible(true);
    	JPanel panel = new JPanel();
    	main.add(panel);
    	panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.add(new JLabel("Court information"));
      //Nested Aggregation query for maximum reservations
        JPanel maxReservationsPanel = new JPanel();
        maxReservationsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        maxReservationsPanel.setLayout(new BoxLayout(maxReservationsPanel, BoxLayout.Y_AXIS));
       
        JLabel maxReservationsLabel = new JLabel("Customer with the most reservations:");
        maxReservationsPanel.add(maxReservationsLabel);
        try{
        	List<NestedAggregationRow> NestedRow = q.getNestedAggregation();
        	String[] columnNames = {"Customer ID", "Customer Name", "Number of Reservations"};
        	final Object[][] data = new Object[NestedRow.size()][3];

			int i=0;
			for(NestedAggregationRow row: NestedRow){
	        	String[] info = { row.getCID(), row.getCName(), String.valueOf(row.getMaxNumReservations())};
	        	data[i] = info;
	        	i++;
	        }
			final JTable NestedTable = new JTable(data,columnNames);
			NestedTable.setPreferredSize(new Dimension(400, 100));
			JScrollPane scrollPane = new JScrollPane(NestedTable);
			scrollPane.setPreferredSize(new Dimension(400, 100));
			maxReservationsPanel.add(scrollPane);
        } catch (SQLException e){
        	System.out.println("Problem retrieving nested aggregation for maximum reservations");
			e.printStackTrace();
        }
        panel.add(maxReservationsPanel);
      //close button 
        JButton close = new JButton("Close");
        panel.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	main.setVisible(false);
            }
        });
    }
    // TODO
    // Frame for employees to search employees
    private void createEmployeeSearchFrame() {
    	final JFrame main = createSubFrame();
    	main.setVisible(true);
    	JPanel panel = new JPanel();
    	JScrollPane outerScroll = new JScrollPane(panel);
    	outerScroll.setPreferredSize(new Dimension(800, 700));
    	main.add(outerScroll);
    	panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.add(new JLabel("Search Employees"));

        JLabel empLabel = new JLabel("Employees:");
        
        JPanel employeesPanel = new JPanel();
        employeesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        employeesPanel.setLayout(new BoxLayout(employeesPanel, BoxLayout.Y_AXIS));
        employeesPanel.add(empLabel);
        try {
			List<Employee> employees = q.getEmployees();
			String[] columnNames = {"SIN", "Phone Number", "Name", "Address", "CentreID"};
			final Object[][] data = new Object[employees.size()][5];

			int i=0;
			for(Employee row: employees){
	        	String[] info = {row.getSin(), row.getPhone(), row.getName(), row.getAddress(), row.getCentreID()};
	        	data[i] = info;
	        	i++;
	        }
			
			final JTable lessonTable = new JTable(data, columnNames);

			lessonTable.getColumnModel().getColumn(0).setPreferredWidth(40);
			lessonTable.getColumnModel().getColumn(1).setPreferredWidth(40);
			lessonTable.getColumnModel().getColumn(3).setPreferredWidth(130);
			lessonTable.getColumnModel().getColumn(4).setPreferredWidth(20);
			lessonTable.setPreferredSize(new Dimension(500, 200));
			JScrollPane scrollPane = new JScrollPane(lessonTable);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			employeesPanel.add(scrollPane);
			
	        JButton deleteEmployeeButton = new JButton("Delete employee");
	        deleteEmployeeButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	String sin = (String) data[lessonTable.getSelectedRow()][0];
	            	String name = (String) data[lessonTable.getSelectedRow()][2];
	            	try {
						q.deleteEmployee(sin);
						main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
		            	createEmployeeSearchFrame();
						JFrame popUp = createPopup("Success!", "Deletion of employee " + name + " with SIN: " + sin + " was successful.");
		            	popUp.setVisible(true);
					} catch (SQLException e1) {
						JFrame popUp = new JFrame("Delete was unsuccessful." + e1.getMessage());
					}
	            }
	        });
	        
	        employeesPanel.add(deleteEmployeeButton);
		} catch (SQLException e1) {
			// TODO Error Handling
			System.out.println("Something went wrong while getting Employees");
			e1.printStackTrace();
		}
        
        panel.add(employeesPanel);
        
        JPanel coachesPanel = new JPanel();
        coachesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        coachesPanel.setLayout(new BoxLayout(coachesPanel, BoxLayout.Y_AXIS));
        
        coachesPanel.add(new JLabel("Coaches"));
        try {
			List<Coach> coaches = q.getCoaches();
			String[] columnNames = {"SIN", "Certification Number"};
			Object[][] data = new Object[coaches.size()][2];
			int i = 0;
			for(Coach coach: coaches){
	        	String[] info = {coach.getSIN(), coach.getCertificationID()};
	        	data[i] = info;
	        	i++;
	        }
			JTable lessonTable = new JTable(data, columnNames);
			JScrollPane scrollPane = new JScrollPane(lessonTable);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			coachesPanel.add(scrollPane);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong while getting Coaches");
			e1.printStackTrace();
		}
        
        panel.add(coachesPanel);
    
  //Division Coach Row
	JPanel divisionCoachPanel = new JPanel();
	divisionCoachPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	divisionCoachPanel.setLayout(new BoxLayout(divisionCoachPanel, BoxLayout.Y_AXIS));
   
    JLabel divisionCoachLabel = new JLabel("Coaches teaching all lessons of a level:");
    divisionCoachPanel.add(divisionCoachLabel);
    String[] levelList = {"Novice", "Intermediate", "Advanced"};
	final JComboBox<String> levelComboBox = new JComboBox<String>(levelList);
	divisionCoachPanel.add(levelComboBox);
	//Combobox listener
	levelComboBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox)e.getSource();
            String level = (String)combo.getSelectedItem();
            System.out.println(level);
        }
    });
    try{
    	List<DivisionCoachRow> DivisionRow = q.getDivisionCoaches(String.valueOf(levelComboBox.getSelectedIndex() + 1));
    	String[] columnNames = {"Coach Name", "Coach Phone Number"};
    	final Object[][] data = new Object[DivisionRow.size()][2];
		int i=0;
		for(DivisionCoachRow row: DivisionRow){
        	String[] info = { row.getName(), row.getPhone()};
        	data[i] = info;
        	i++;
        }
		final JTable DivisionCoachTable = new JTable(data,columnNames);
		DivisionCoachTable.setPreferredSize(new Dimension(600, 100));
		JScrollPane scrollPane = new JScrollPane(DivisionCoachTable);
		scrollPane.setPreferredSize(new Dimension(600, 100));
		divisionCoachPanel.add(scrollPane);
    } catch (SQLException e){
    	System.out.println("Problem retrieving division coaches");
		e.printStackTrace();
    }
    
    
    panel.add(divisionCoachPanel);
    
	//close button 
    JButton close = new JButton("Close");
    panel.add(close);
    close.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	main.setVisible(false);
        }
    });
    }
    
    
    
    
    // Frame for employees to search for customers
    private void createCustomerSearchFrame() {
    	final JFrame main = createSubFrame();
    	main.setVisible(true);
    	JPanel panel = new JPanel();
    	main.add(panel);
    	panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.add(new JLabel("Customer Reservation information"));
    	//Division Court Row
    	JPanel divisionCourtPanel = new JPanel();
    	divisionCourtPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	divisionCourtPanel.setLayout(new BoxLayout(divisionCourtPanel, BoxLayout.Y_AXIS));
       
        JLabel divisionCourtLabel = new JLabel("Customers that reserved hard surface courts:");
        divisionCourtPanel.add(divisionCourtLabel);
        try{
        	List<DivisionCourtRow> DivisionRow = q.getDivisionCourts();
        	String[] columnNames = {"Customer ID", "Customer Name"};
        	final Object[][] data = new Object[DivisionRow.size()][2];

			int i=0;
			for(DivisionCourtRow row: DivisionRow){
	        	String[] info = { row.getCID(), row.getCName()};
	        	data[i] = info;
	        	i++;
	        }
			final JTable DivisionCourtTable = new JTable(data,columnNames);
			DivisionCourtTable.setPreferredSize(new Dimension(400, 100));
			JScrollPane scrollPane = new JScrollPane(DivisionCourtTable);
			scrollPane.setPreferredSize(new Dimension(400, 100));
			divisionCourtPanel.add(scrollPane);
        } catch (SQLException e){
        	System.out.println("Problem retrieving division courts");
			e.printStackTrace();
        }
        panel.add(divisionCourtPanel);

    	//close button 
        JButton close = new JButton("Close");
        panel.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	main.setVisible(false);
            }
        });
    }
    

    private void createHomeFrame(){
        if (mainFrame != null){
            mainFrame.setVisible(false);
        }

        mainFrame = new JFrame();
        mainFrame.setSize(WIDTH,HEIGHT);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowAdapter windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        headerLabel.setText("Tennis Court Manager");
        
        //add radio button and set listeners to distinguish type of user
        //disables the ability to select multiple radio buttons
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton custButton = new JRadioButton("Customer");
        custButton.setName("customer");
        JRadioButton adminButton = new JRadioButton("Admin");
        adminButton.setName("admin");
        radioGroup.add(custButton);
        radioGroup.add(adminButton);   
        controlPanel.add(custButton);
        controlPanel.add(adminButton);
        
        custButton.addActionListener(saveUserType);
        adminButton.addActionListener(saveUserType);
        
        //Text field to authenticate user   
        JTextField nameText = new JTextField("user name",10);
        JTextField passwordText = new JTextField("password",10);
        JButton login = new JButton("login");
        
        nameText.addActionListener(saveUserName);
//        passwordText.addActionListener(l);
        
        controlPanel.add(nameText);
        controlPanel.add(passwordText);
        controlPanel.add(login);
        nameText.addActionListener(saveUserName);
        passwordText.addActionListener(savePassword);
        login.addActionListener(auth);
        mainFrame.setVisible(true);
    }
    
    ActionListener saveUserType = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JRadioButton o = (JRadioButton)e.getSource();
    		String name = o.getName();
    		if (name.equals("customer")){
    			userType = "customer";
    			System.out.println(name);
    			//produceCustomerFrame();
    		} else {
    			userType = "admin";
    			System.out.println("I'm powerful");
//    			produceEmployeeFrame();
    		}
        }
    };
    
    ActionListener saveUserName = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String name = o.getText();
    		auth.setName(name);
        }
    };
    
    ActionListener savePassword = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String password = o.getText();
    		auth.setPassword(password);
        }
    };

    //Customer book tennis court sub frame.
    private void bookTennisCourtSubFrame(){
    	reserveCourtId= "";
        final JFrame btcSubFrame = createSubFrame();
        final JPanel btcControlPanel = new JPanel();
        btcControlPanel.setLayout(new FlowLayout());
        JLabel header = new JLabel("Search and Reserve a Tennis Court");
        btcControlPanel.add(header);       
        btcSubFrame.add(btcControlPanel);
        // here we would render all reservations grouped by the day
        
        try {
			List<ReservableTennisCourt> ltcs = q.getReservableTennisCourts();
			String[] columnNames = {"CourtId", "Surface", "CentreId"};
			JPanel reservePanel = new JPanel();
			reservePanel.setLayout(new BoxLayout(reservePanel, BoxLayout.PAGE_AXIS));
			Object[][] data = new Object[ltcs.size()][4];
			int i = 0;
			
			for(ReservableTennisCourt court: ltcs){
	        	String[] info = {court.getCourtId(), court.getSurface(), court.getCentreId()};
	        	data[i] = info;
	        	i++;
	        }
			
			final JTable reserveTable = new JTable(data, columnNames);
			JScrollPane scrollPane = new JScrollPane(reserveTable);
			btcSubFrame.add(scrollPane);
			
			JButton showReservationButton = new JButton("Show Reservation");
			JButton deleteReservation = new JButton("Cancel a Reservation");
			final JButton reservationButton = new JButton("Reserve Court");
			btcSubFrame.add(showReservationButton);
			btcSubFrame.add(reservationButton);
			btcSubFrame.add(deleteReservation);
			btcSubFrame.setVisible(true);
			reserveTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	reserveCourtId = reserveTable.getValueAt(reserveTable.getSelectedRow(), 0).toString();
		            
		            
		            
		        }
		    });
			
			showReservationButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					Border raisedbevel = BorderFactory.createRaisedBevelBorder();
					Border loweredbevel = BorderFactory.createLoweredBevelBorder();
					Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
					
					final JFrame resInfo = createSubFrame();
			    	JPanel panel = new JPanel();
			    	panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			    	resInfo.add(panel);
					JLabel reserveHeader = new JLabel("Court "+reserveCourtId);
					reserveHeader.setSize(50,50);
					panel.add(reserveHeader);
					try {
						if (reserveCourtId == null|| reserveCourtId == ""){
							return;
						}
						System.out.println("Search " + reserveCourtId);
						List<Date[]> dates =  q.getCourtReservation(reserveCourtId);
						if (dates.size() == 0) {
							JLabel reserveInfo = new JLabel("Has not been reserved yet");
							reserveInfo.setBorder(compound);
							panel.add(reserveInfo);
						} else {
							for(Date[] date: dates) {
								String noticeLabel = "Reserved on " + date[0]+ " from " + date[1]+ " to "+date[2];
								JLabel reserveInfo = new JLabel(noticeLabel);
								reserveInfo.setBorder(compound);
								panel.add(reserveInfo);
								System.out.println(noticeLabel);
							}
						}
						
					resInfo.setVisible(true);	
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			reservationButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					JPanel resDatePanel = new JPanel();
					btcSubFrame.add(resDatePanel);
					JTextField rDate = new JTextField("YYYY-MM-DD", 20);
					JLabel startLabel = new JLabel("start");
					JTextField start = new JTextField("24:00", 10);
					JLabel endLabel = new JLabel("end");
					JTextField end = new JTextField("24:00", 10);
					JButton createReserve = new JButton("Create a reservation");
					resDatePanel.add(rDate);
					resDatePanel.add(startLabel);
					resDatePanel.add(start);
					resDatePanel.add(endLabel);
					resDatePanel.add(end);
					resDatePanel.add(createReserve);
					
					SwingUtilities.updateComponentTreeUI(btcSubFrame);
					
					rDate.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							JTextField o = (JTextField)e.getSource();
				    		reservationDate = o.getText();
				    		System.out.println(reservationDate);
							
						}
						
					});
					
					start.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {			
							JTextField o = (JTextField)e.getSource();
				    		rStartTime = o.getText();
				    		System.out.println(rStartTime);
						}
					});
					
					end.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							JTextField o = (JTextField)e.getSource();
				    		rEndTime = o.getText();
				    		System.out.println(rEndTime);
						}
					});
					
					createReserve.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								boolean createdReservation = q.createReservation(customer.getCid(), reserveCourtId, reservationDate, rStartTime, rEndTime);
								if (createdReservation) {
									System.out.println("Success!");
									btcSubFrame.setVisible(false);
									btcSubFrame.dispose();
									bookTennisCourtSubFrame();
								} else{
									System.out.println("Fail!");
								}
								return;
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
					});
				}
			});
			
			
			deleteReservation.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrame cancelFrame = createSubFrame();
					List<ReservableTennisCourt> reserved = null;
					try {
						reserved = q.getReservableTennisCourts();
					
					if (reserved == null ||reserved.isEmpty()){
						return;
					}
					String[] columnNames = {"CourtId", "Surface", "CentreId"};
					JPanel reservePanel = new JPanel();
					reservePanel.setLayout(new BoxLayout(reservePanel, BoxLayout.PAGE_AXIS));
					Object[][] data = new Object[reserved.size()][4];
					int i = 0;
					
					for(ReservableTennisCourt court: reserved){
						if (q.customerReserved(customer.getCid(), court.getCourtId())){
				        	String[] info = {court.getCourtId(), court.getSurface(), court.getCentreId()};
				        	data[i] = info;
				        	i++;
						}
			        }
					
					final JTable reservedTable = new JTable(data, columnNames);
					JScrollPane scrollPane = new JScrollPane(reservedTable);
					cancelFrame.add(scrollPane);
					
					JButton cancel = new JButton("Cancel");
					cancelFrame.add(cancel);
					cancelFrame.setVisible(true);
					reservedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				        public void valueChanged(ListSelectionEvent event) {
				        	cancelCourtId = reservedTable.getValueAt(reservedTable.getSelectedRow(), 0).toString();
				        }
				    });
					
					cancel.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								q.deleteReservation(customer.getCid(), cancelCourtId);
								btcSubFrame.setVisible(false);
								btcSubFrame.dispose();
								bookTennisCourtSubFrame();
							} catch (SQLException err) {
								// TODO Auto-generated catch block
								err.printStackTrace();
							}
							
						}
						
					});
					
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  }
			});
			
			
        } catch (SQLException e1) {
			System.out.println("Something went wrong while loading ReservableTennisCourt");
			e1.printStackTrace();
		}
    }

    private void enrollLessonSubFrame(){
        String[] levelList = {"Novice", "Intermediate", "Advanced"};
        final JFrame elSubFrame = createSubFrame();
        JPanel elControlPanel = new JPanel();
        elControlPanel.setLayout(new FlowLayout());
        elSubFrame.add(new JLabel("Search and Enroll in a lesson"));
        elSubFrame.add(new JLabel("Select your level"));
        final JComboBox<String> levelComboBox = new JComboBox<String>(levelList);
        levelComboBox.setVisible(true);
        elControlPanel.add(levelComboBox);
        elSubFrame.add(elControlPanel);
        
        try {
			List<LessonReportRow> lrrs = q.generateReport();
			String[] columnNames = {"LessonId", "Level", "Coach Name", "CentreID", "Enrolled"};
			JPanel lessonPanel = new JPanel();
			lessonPanel.setLayout(new BoxLayout(lessonPanel, BoxLayout.PAGE_AXIS));
			Object[][] data = new Object[lrrs.size()][4];
			int i = 0;
			
			for(LessonReportRow lrr: lrrs){
				boolean customerEnrolled = q.customerEnrollsIn(customer.getCid(), lrr.getLID());
				String[] info;
				if (customerEnrolled) {
					info = new String[] {lrr.getLID(), lrr.getLevel(), lrr.getCoachName(), lrr.getCentreID(), "Enrolled"};
				} else {
					info = new String[]{lrr.getLID(), lrr.getLevel(), lrr.getCoachName(), lrr.getCentreID(), "Not Enrolled"};
				}
	        	//String[] info = {lrr.getLID(), lrr.getLevel(), lrr.getCoachName(), lrr.getCentreID()};
	        	data[i] = info;
	        	i++;
	        }
			
			final JTable lessonTable = new JTable(data, columnNames);
			JScrollPane scrollPane = new JScrollPane(lessonTable);
			
			lessonTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	lessonId = lessonTable.getValueAt(lessonTable.getSelectedRow(), 0).toString();
		            System.out.println("You have selected " + lessonId); 
		        }
		    });
			
			elSubFrame.add(scrollPane);
			JButton register = new JButton("Register");
			JButton unEnroll = new JButton("Unenroll");
			elSubFrame.add(register);
			elSubFrame.add(unEnroll);
			elSubFrame.setVisible(true);
			
			register.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						q.enrollCustomerLesson(customer.getCid(), lessonId);
						elSubFrame.setVisible(false);
						elSubFrame.dispose();
						enrollLessonSubFrame();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			});
			
			unEnroll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						boolean deleted = q.unenrollCustomerLessons(customer.getCid(), lessonId);
						if (deleted) {
							elSubFrame.setVisible(false);
							elSubFrame.dispose();
							enrollLessonSubFrame();
						}
						System.out.println(deleted);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			});
		} catch (SQLException e1) {
			System.out.println("Something went wrong while generatingReport");
			e1.printStackTrace();
		}
        
        
        // add listeners to text fields and combo box and constantly each field?
        levelComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String level = (String)cb.getSelectedItem();
                System.out.println(level);
            }
        });
        


    }

    private void custUpdateSubFrame(){
    	final JFrame frame = createSubFrame();
    	JPanel updateControlPanel = new JPanel();
    	updateControlPanel.setLayout(new BoxLayout(updateControlPanel, BoxLayout.PAGE_AXIS));
    	JLabel updateLabel = new JLabel("Update your information "+ customer.getName());
    	// Text fields for all customer fields
    	JTextField nameText = new JTextField(customer.getName(),20);
        JTextField ccnumber = new JTextField(customer.getCcnumber(),20);
        JTextField phone = new JTextField(customer.getPhone(),20);
        JTextField address = new JTextField(customer.getAddress(),20);
        JTextField age = new JTextField(customer.getAge(),20);
        JButton finishUpdateButton = new JButton("finish");
        // adding listeners to each text fields
        nameText.addActionListener(updateCustName);
        phone.addActionListener(updateCustPhone);
        address.addActionListener(updateCustAddress);
        age.addActionListener(updateCustAge);
        ccnumber.addActionListener(updateCustCard);
        
        // adding text fields into control panel and then into update frame
        frame.add(updateLabel);
        updateControlPanel.add(nameText);
        updateControlPanel.add(ccnumber);
        updateControlPanel.add(phone);
        updateControlPanel.add(address);
        updateControlPanel.add(age);
        updateControlPanel.add(finishUpdateButton);
        frame.add(updateControlPanel);
        frame.setVisible(true);
        finishUpdateButton.addActionListener(new ActionListener(){
        	@Override
            public void actionPerformed(ActionEvent e) {
        		try{
        		customer = q.getCustomer(customer.getCid(), customer.getPhone());
        		}catch(Exception err){
        			System.out.println("Error while updating");
        			err.printStackTrace();
        		}
        		frame.setVisible(false);
        		frame.dispose();
        		produceCustomerFrame();
        	}
        });
    }
    
    
    ActionListener updateCustName = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String custName = o.getText();
    		try{
    			if(q == null) {
    				System.out.println("sometihng is wrong");
    			}
    			q.updateCustomerName(customer.getCid(), custName);
    			customer.setName(custName);
    		}catch(Exception err){
    			System.out.println("something went wrong while updating customer name");
    			err.printStackTrace();
    		}
    		
        }
    };
    
    ActionListener updateCustCard = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String card = o.getText();
    		try{
    			if(q == null) {
    				System.out.println("sometihng is wrong");
    			}
    			q.updateCustomerCard(customer.getCid(), card);
    			customer.setCcNumber(card);
    		}catch(Exception err){
    			System.out.println("something went wrong while updating customer name");
    			err.printStackTrace();
    		}
    		
        }
    };
    
    ActionListener updateCustPhone = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String custPhone = o.getText();
    		try{
    			q.updateCustomerPhone(customer.getCid(), custPhone);
    			customer.setPhone(custPhone);
    		}catch(Exception err){
    			System.out.println("something went wrong while updating customer phone");
    			err.printStackTrace();
    		}
    		
        }
    };
    
    ActionListener updateCustAddress = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String custAddress = o.getText();
    		try{
    			q.updateCustomerAddress(customer.getCid(), custAddress);
    			customer.setAddress(custAddress);
    		}catch(Exception err){
    			System.out.println("something went wrong while updating customer address");
    			err.printStackTrace();
    		}	
        }
    };
    
    ActionListener updateCustAge = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		JTextField o = (JTextField)e.getSource();
    		String age = o.getText();
    		try{
    			q.updateCustomerAge(customer.getCid(), Integer.parseInt(age));
    			customer.setAge(age);
    		}catch(Exception err){
    			System.out.println("something went wrong while updating customer age");
    			err.printStackTrace();
    		}	
        }
    };

    private JFrame createSubFrame(){
        final JFrame frame = new JFrame();
        frame.setSize(750, 750);
        frame.setLayout(new FlowLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                frame.dispose();
            }
        });
        return frame;
    }
    
    private JFrame createPopup(String title, String message)
    {
    	final JFrame frame = new JFrame(title);
        frame.setSize(700, 100);
        frame.setLayout(new FlowLayout());
        
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                frame.dispose();
            }
        });
        return frame;
    }
    
    
    private class AuthenticateUser implements ActionListener {
    	private String id;
    	private String pass;
    	
    	public AuthenticateUser(){
    		 
    	}
    	
    	public void setName(String userName){
    		id = userName;
    	}
    	public void setPassword(String password){
    		pass = password;
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
			try {
				if (userType.equals("customer")){
					customer = q.getCustomer(id,pass);
					if (customer == null) {
						System.out.println("Customer does not exist");
	        			controlPanel.add(new JLabel("Username or Password is incorrect"));
	        			} else {
	        				System.out.println("producing customer frame");
	        				produceCustomerFrame();
	        			}
					
	    		} else if (userType.equals("admin")){
	    			admin = q.getAdmin(id);
	    			produceEmployeeFrame();
	    		}
			} catch (SQLException e1) {
				System.out.println("Something went wrong while loading customer");
				e1.printStackTrace();
			}
        }
    }
}
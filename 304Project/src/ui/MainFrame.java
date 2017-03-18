package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    

    public MainFrame(){
    	auth = new AuthenticateUser();
        createHomeFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void produceCustomerFrame() {
        mainFrame.setVisible(false);
//        mainFrame.dispose();
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
        headerLabel.setText("Welcome Customer!");
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
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
//        msglabel = new JLabel("", JLabel.CENTER);
        headerLabel.setText("Welcome Employee!");
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
        JButton goBack = new JButton("back");
        controlPanel.add(goBack);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHomeFrame();
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
    			System.out.println(name);
    			produceCustomerFrame();
    		} else {
    			System.out.println("I'm powerful");
    			produceEmployeeFrame();
    		}
        }
    };
    
    ActionListener saveUserName = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		System.out.println("I have been hit");
    		JTextField o = (JTextField)e.getSource();
    		String name = o.getText();
    		auth.setName(name);
        }
    };
    
    ActionListener savePassword = new ActionListener(){
    	@Override
        public void actionPerformed(ActionEvent e) {
    		System.out.println("I have been hit");
    		JTextField o = (JTextField)e.getSource();
    		String password = o.getText();
    		auth.setPassword(password);
        }
    };

    private void bookTennisCourtSubFrame(){
        JFrame btcSubFrame = createSubFrame();
        JPanel btcControlPanel = new JPanel();
        btcControlPanel.setLayout(new FlowLayout());
        JLabel header = new JLabel("Search and Reserve a Tennis Court");

        btcControlPanel.add(header);

        btcSubFrame.add(btcControlPanel);
        btcSubFrame.setVisible(true);
        // here we would render all reservations grouped by the day

    }

    private void enrollLessonSubFrame(){
        String[] levelList = {"Novice", "Intermediate", "Advanced"};
        JFrame elSubFrame = createSubFrame();
        JPanel elControlPanel = new JPanel();
        elControlPanel.setLayout(new FlowLayout());
        elSubFrame.add(new JLabel("Search and Enroll in a lesson"));
        elSubFrame.add(new JLabel("Select you level"));
        final JComboBox<String> levelComboBox = new JComboBox<String>(levelList);
        levelComboBox.setVisible(true);
        elControlPanel.add(levelComboBox);
        elSubFrame.add(elControlPanel);
        elSubFrame.setVisible(true);

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

    }

    private JFrame createSubFrame(){
        final JFrame frame = new JFrame();
        frame.setSize(750, 750);
        frame.setLayout(new FlowLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                frame.dispose();
            }
        });
//        frame.setVisible(true);
        return frame;
    }
    
    
    private class AuthenticateUser implements ActionListener {
    	private String name;
    	private String pass;
    	
    	public AuthenticateUser(){
    		 
    	}
    	
    	public void setName(String userName){
    		name = userName;
    	}
    	public void setPassword(String password){
    		pass = password;
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
            System.out.println(name);
            System.out.println(pass);
        }
    }


}

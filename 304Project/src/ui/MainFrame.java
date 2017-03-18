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

    public MainFrame(){
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
        mainFrame.setVisible(true);
        JButton customerButton = new JButton("Customer");
        JButton employeeButton = new JButton("Employee");
        controlPanel.add(customerButton);
        controlPanel.add(employeeButton);

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                produceCustomerFrame();

            }
        });

        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                produceEmployeeFrame();
            }
        });
    }

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

        // add listeners to textfields and combobox and constantly each field?
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



}

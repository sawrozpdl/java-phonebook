package phonebook;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class TelephoneWindow implements ActionListener {
    
    private JFrame frame;
    private JTextField textFieldFN;
    private JTextField textFieldLN;
    private JTextField textFieldP;
    private TablePane table;
    private JCheckBox checkBoxPrivate;
    private JRadioButton radioFL;
    private JRadioButton radioLF;
    private JButton btnSearch;
    private JButton btnClear;
    private JButton btnAdd;
    private JButton btnRemove;
    private JMenuItem editAdd;
    private JMenuItem editRemove;
    private JMenuItem editSearch;
    private JMenuItem editClear;
    private JMenuItem fileSave;
    private JMenuItem fileSaveAs;
    private JMenuItem fileExit;
    private JMenuItem fileOpen;
    private JMenuItem fileNew;
    private JMenuItem helpAbout;
    private File filed;
    private boolean firstLast;

    public TelephoneWindow() {
        try {
            frame = new JFrame();
            textFieldFN = new JTextField();
            textFieldLN = new JTextField();
            textFieldP = new JTextField();
            filed = new File("book.txt");
            if(filed.createNewFile())
                JOptionPane.showMessageDialog(frame, "File was not found so, a new save file was created"); //creates a new file in the class path
        } 
        catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "File Error! Saving function will not work!");
        }
    }
    
    public void launch() {
        frame.setTitle("Phone Book");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // prevent possible data loss if user has not saved the file
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        
        // --------------------------------------- MenuBar -------------------------------------------
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        // ------------------------------------- File ----------------------------
        
        JMenu file = new JMenu("File");
        fileNew = new JMenuItem("New");
        fileOpen = new JMenuItem("Open");
        fileSave = new JMenuItem("Save");
        fileSaveAs = new JMenuItem("Save As");
        fileExit = new JMenuItem("Exit");
        fileOpen.addActionListener(this);
        fileNew.addActionListener(this);
        fileExit.addActionListener(this);
        fileSave.addActionListener(this);
        fileSaveAs.addActionListener(this);
        file.add(fileNew);
        file.add(fileOpen);
        file.add(fileSave);
        file.add(fileSaveAs);
        file.add(fileExit);
        
        menuBar.add(file);
        
        // ------------------------------ Edit ---------------------------------------
        
        JMenu edit = new JMenu("Edit");
        editClear = new JMenuItem("Clear");
        editClear.addActionListener(this);
        edit.add(editClear);
        
        editSearch = new JMenuItem("Search");
        editSearch.addActionListener(this);
        edit.add(editSearch);
        
        JSeparator separator = new JSeparator();
        edit.add(separator);
        
        editAdd = new JMenuItem("Add");
        editAdd.addActionListener(this);
        edit.add(editAdd);
        
        editRemove = new JMenuItem("Remove");
        editRemove.addActionListener(this);
        edit.add(editRemove);
        
        menuBar.add(edit);
        
        //------------------------------------------- Help -----------------------------------
        
        JMenu help = new JMenu("Help");
        helpAbout = new JMenuItem("About");
        helpAbout.addActionListener(this);
        help.add(helpAbout);
        
        menuBar.add(help);
        
        
        //------------------------------------------- RadioButtons for sort---------------------------
        
        JPanel panelFileAs = new JPanel();
        panelFileAs.setBounds(475, 271, 407, 99);
        panelFileAs.setLayout(null);
        String title = "File As:";
        Border border = BorderFactory.createTitledBorder(title);
        panelFileAs.setBorder(border);
        frame.add(panelFileAs);
        
        radioFL = new JRadioButton("Firstname, Lastname");
        radioFL.setFont(new Font("Rockwell", Font.PLAIN, 20));
        radioFL.setBounds(54, 17, 247, 32);
        radioFL.addActionListener(this);
        radioFL.setFocusPainted(false);
        
        radioLF = new JRadioButton("Lastname, Firstname");
        radioLF.setFont(new Font("Rockwell", Font.PLAIN, 20));
        radioLF.setBounds(54, 60, 247, 32);
        radioLF.addActionListener(this);
        radioLF.setFocusPainted(false);
        
        
        ButtonGroup radios = new ButtonGroup();
        radios.add(radioFL);
        radios.add(radioLF);
        
        panelFileAs.add(radioFL);
        panelFileAs.add(radioLF);
        
        //-------------------------------------- table -------------------------------------------
        
        table = new TablePane(new String[] {"A", "B", "C", "D"});
        table.setBounds(10, 11, 453, 525);
        table.setBorder("Name:");
        try {
            int n = table.populateFrom(filed);
            if (n != -1) { // -1 would mean the file is a new file (no data in the file)
                firstLast =  (n == 1);
                toogleRadio(); // if the file has 'true' in first line toggleradio would select first-name, lastname
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "File not found! Saving function Will not work!"); 
        }
        frame.add(table);
        
        //---------------------------------------- Input Area -------------------------------------
        
        JPanel panelInfo = new JPanel();
        panelInfo.setBounds(475, 9, 407, 250);
        panelInfo.setLayout(null);
        title = "Info:";
        border = BorderFactory.createTitledBorder(title);
        panelInfo.setBorder(border);
        frame.add(panelInfo);
            
        textFieldFN.setBounds(181, 33, 200, 37);
        panelInfo.add(textFieldFN);
        textFieldFN.setColumns(10);
        
        textFieldLN.setColumns(10);
        textFieldLN.setBounds(181, 80, 200, 37);
        panelInfo.add(textFieldLN);
        
        textFieldP.setColumns(10);
        textFieldP.setBounds(181, 128, 200, 37);
        panelInfo.add(textFieldP);
        
        checkBoxPrivate = new JCheckBox("Private");
        checkBoxPrivate.setFont(new Font("Rockwell", Font.PLAIN, 20));
        checkBoxPrivate.setBounds(31, 173, 169, 56);
        checkBoxPrivate.setFocusPainted(false);
        panelInfo.add(checkBoxPrivate);
        
        JLabel labelFirstName = new JLabel("First Name:");
        labelFirstName.setFont(new Font("Rockwell", Font.PLAIN, 20));
        labelFirstName.setBounds(21, 47, 150, 23);
        panelInfo.add(labelFirstName);
        
        JLabel labelLastName = new JLabel("Last Name:");
        labelLastName.setFont(new Font("Rockwell", Font.PLAIN, 20));
        labelLastName.setBounds(21, 91, 150, 23);
        panelInfo.add(labelLastName);
        
        JLabel labelPhone = new JLabel("Phone:");
        labelPhone.setFont(new Font("Rockwell", Font.PLAIN, 20));
        labelPhone.setBounds(21, 139, 150, 23);
        panelInfo.add(labelPhone);
        
        panelInfo.setEnabled(false);


        //--------------------------------- Buttons --------------------------------------------
        
        JPanel panelBtns = new JPanel();
        panelBtns.setBounds(475, 384, 405, 151);
        panelBtns.setLayout(new GridLayout(2,2));
        
        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Rockwell", Font.PLAIN, 20));
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(this);
        panelBtns.add(btnClear);
        
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(this);
        btnSearch.setFont(new Font("Rockwell", Font.PLAIN, 20));
        btnSearch.setFocusPainted(false);
        panelBtns.add(btnSearch);
        
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(this);
        btnAdd.setFont(new Font("Rockwell", Font.PLAIN, 20));
        btnAdd.setFocusPainted(false);
        panelBtns.add(btnAdd);
        
        btnRemove = new JButton("Remove");
        btnRemove.addActionListener(this);
        btnRemove.setFont(new Font("Rockwell", Font.PLAIN, 20));
        btnRemove.setFocusPainted(false);
        panelBtns.add(btnRemove);
            
        frame.add(panelBtns);
        
        frame.getRootPane().setDefaultButton(btnAdd);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (table.hasChanged()) { // show the dialouge if the user has made changes and not saved
                    int foo = JOptionPane.showConfirmDialog(frame, "Changes have been made to the table\nSave the changes made?");
                    if (foo == JOptionPane.YES_OPTION) {
                        fileSave.doClick();
                        System.exit(0);
                    }
                    else if (foo == JOptionPane.NO_OPTION)
                        System.exit(0);
                }
                else 
                    System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == fileExit) System.exit(0);
        
        if (e.getSource() == fileOpen) {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (JFileChooser.APPROVE_OPTION == result) {
                try {
                    filed = chooser.getSelectedFile();
                    int n = table.populateFrom(filed);
                    if (n != -1) {
                        firstLast =  n == 1;
                        toogleRadio();
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Failed to load the file!");
                }
            }
        }
        
        if (e.getSource() == fileNew) {
            int foo = JOptionPane.showConfirmDialog(frame, "Save the current work before creating a new file?");
            if (foo == JOptionPane.YES_OPTION) 
                fileSaveAs.doClick();
            else if (foo == JOptionPane.CANCEL_OPTION)
                return;
            try {
                filed = new File("book.txt");
                filed.delete();
                filed.createNewFile();
                table.clear();
                radioFL.setEnabled(true);
                radioLF.setEnabled(true);
                radioFL.setSelected(false);
                radioLF.setSelected(false);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame, "Error in new file creation!");
            }
        }
        
        if (e.getSource() == fileSave) {
            try {
                if (filed.createNewFile()) 
                    JOptionPane.showMessageDialog(frame, "File was not found! \nData saved to a new file");
                table.populateTo(filed, firstLast); //populating to default save file 
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame, "Save failed!");
            }
        }
        
        if (e.getSource() == fileSaveAs) {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(null);
            if (JFileChooser.APPROVE_OPTION == result) {
                try {
                    filed = chooser.getSelectedFile();  //getting the user demanded file
                    table.populateTo(filed, firstLast); //populating data to the user demanded file
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Save failed!");
                }
            }
        }
        
        if (e.getSource() == editAdd) btnAdd.doClick();
        
        if (e.getSource() == editRemove) btnRemove.doClick();
        
        if (e.getSource() == editSearch) btnSearch.doClick();
        
        if (e.getSource() == editClear) btnClear.doClick();
        
        if (e.getSource() == helpAbout) JOptionPane
                                    .showMessageDialog(frame, "It is still in trial version!");
        
        if (e.getSource() == btnClear) {
            textFieldFN.setText("");
            textFieldLN.setText("");
            textFieldP.setText("");
            checkBoxPrivate.setSelected(false);
        }
        
        if (e.getSource() == btnAdd) {
            if (!radioFL.isSelected() && !radioLF.isSelected()) //if user doesnot select radio buttons it automatically finalizes the selected one or first one
                radioFL.doClick();
            else if (radioFL.isEnabled() && radioLF.isEnabled()) {
                if (radioFL.isSelected()) radioFL.doClick();
                else radioLF.doClick();
            }
            String fn = textFieldFN.getText();
            String ln = textFieldLN.getText();
            String pn = textFieldP.getText();
            try {
                validate(fn, Type.name); //validating the firstname
                validate(ln, Type.name);
                validate(pn, Type.phone);
            } 
            catch (InvalidPhoneNumberException f) {
                JOptionPane.showMessageDialog(frame,
                                            "Invalid Phone number!");
                return;
            }
            catch (InvalidNameException g) {
                JOptionPane.showMessageDialog(frame,
                                            "Invalid Name!");
                return;
            }
            for (int i = 0;i < table.getRowCount();i++) {
                if (table.getValueAt(i, 2).equals(pn)) { //checking for the inputed number in other rows
                    JOptionPane.showMessageDialog(frame,
                                        "Phone Number already exists!");
                    return;
                }
            } 
            if (!fn.equals("") && !textFieldP.getText().equals(""))     
                table.addRow(new String[] {firstLast ? fn : ln,
                                           firstLast ? ln : fn,
                                           pn, checkBoxPrivate
                                                    .isSelected() ?
                                                            "Private" :
                                                            ""
                                            });
            else JOptionPane.showMessageDialog(frame,
                                            "Incomplete Information!");
        }
        
        if (e.getSource() == btnRemove) {
            try {
                table.removeSelectedRow();
            }
            catch (ArrayIndexOutOfBoundsException ee) {
                JOptionPane.showMessageDialog(frame,
                                            "Select one row for deletiion!");
            }
        }
        
        if (e.getSource() == btnSearch) 
                JOptionPane
                    .showMessageDialog(frame,
                                    "Search feature will be added soon!");
        
        if (e.getSource() == radioFL) {
            firstLast = true;
            radioLF.setEnabled(false);
        }
        
        if (e.getSource() == radioLF) {
            firstLast = false;
            radioFL.setEnabled(false);
        }
    }
    
    private void toogleRadio() { //update the radio activations in firstname,lastname and lastname, firstname radio buttons
        if (firstLast) {
            radioFL.setEnabled(true);
            radioFL.setSelected(true);
            radioLF.setEnabled(false);
        }
        else {
            radioLF.setEnabled(true);
            radioLF.setSelected(true);
            radioFL.setEnabled(false);
        }       
    }

    private void validate(String str, Type type) 
            throws InvalidPhoneNumberException, InvalidNameException { //input validations
        Pattern pattern;
        if (type == Type.phone) {
            pattern = Pattern
                    .compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
            if (!pattern.matcher(str).matches())
                throw new InvalidPhoneNumberException("Invalid Phone Number");
        }
        if (type == Type.name) {
            pattern = Pattern
                    .compile("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
            if (!pattern.matcher(str).matches())
                throw new InvalidNameException("Invalid Name");
        }
    }
    
    public static void main(String[] args) {
        new TelephoneWindow().launch();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


public class TeamMAPAnalaysis extends Component {
    private DatabaseHandler dbh = null;
    private InterpolationAlgorithm alg = null;

    // GUI Objects
    private JFrame guiFrame = new JFrame();
    private JPanel analysisPanel = new JPanel(new GridBagLayout());

    private JLabel pathLbl = new JLabel("DB Path:");
    private JTextField pathTxt = new JTextField(30);
    private JFileChooser fc = new JFileChooser();
    private JButton browseBtn = new JButton("Browse");
    private JButton dBConnectBtn = new JButton("Connect");

    private JLabel readTableLbl = new JLabel("Read Table:");
    private JComboBox readTableCmb = new JComboBox(new String[] {""});

    private JLabel writeTableLbl = new JLabel("Write Table:");
    private JComboBox writeTableCmb = new JComboBox(new String[] {""});

    private JButton runBtn = new JButton("Run");

    private String filePath = "C:\\Users\\Zachary\\Desktop\\projtwo.db";
    private String readTable;
    private String writeTable;

    public static void main(String[] args) throws Exception {
        new TeamMAPAnalaysis();
    }

    public TeamMAPAnalaysis() {
        configureGUI();
        pathTxt.setText("C:\\Users\\Zachary\\Desktop\\projtwo.db");
    }

    private void runAnalysis() throws ClassNotFoundException {
        filePath = pathTxt.getText();
        System.out.println("test");

//        alg = new ZachAlgorithm();
        ArrayList<DataPoint> data = dbh.readDBData(readTable);
//        ArrayList<DataPoint> results = alg.analyze(data);
//        dbh.writeDBData(writeTable, results);

        // Upload to Fusion Table
    }

    private void configureGUI() {
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("TeamMAP Analysis");
//        guiFrame.setSize(640,480);
        guiFrame.setLocationRelativeTo(null);

        // Row 1
        // Path Label
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridwidth = 1;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.ipadx = 10;
        c1.anchor = GridBagConstraints.LINE_START;
        c1.insets = new Insets(10,10,5,5);
        analysisPanel.add(pathLbl, c1);

        // Path text
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = 1;
        c2.gridy = 0;
        c2.gridwidth = 4;
        c2.weightx = 1.0;
        c2.insets = new Insets(10,5,5,5);
        analysisPanel.add(pathTxt, c2);
        pathTxt.setText(filePath);

        // Browse Button
        ActionListener al;
        al = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                switch (fc.showOpenDialog(TeamMAPAnalaysis.this))
                {
                    case JFileChooser.APPROVE_OPTION:
                        filePath = fc.getSelectedFile().toString();
                        pathTxt.setText(fc.getSelectedFile().toString());
                        break;

                    case JFileChooser.ERROR_OPTION:
                        JOptionPane.showMessageDialog(TeamMAPAnalaysis.this, "Error",
                                "TeamMAPAnalaysis",
                                JOptionPane.OK_OPTION);
                }
            }
        };

        browseBtn.addActionListener(al);
        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 5;
        c3.gridy = 0;
        c3.gridwidth = 1;
        c3.insets = new Insets(10,5,5,10);
        analysisPanel.add(browseBtn, c3);

        // Connect Button
        ActionListener a2;
        a2 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    dbh = new DatabaseHandler(filePath);
                    updateComboBoxes();
                    readTableCmb.setEnabled(true);
                    writeTableCmb.setEnabled(true);
                    writeTableCmb.setSelectedItem(null);
                    dBConnectBtn.setEnabled(false);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        dBConnectBtn.addActionListener(a2);
        GridBagConstraints c4 = new GridBagConstraints();
        c4.gridx = 6;
        c4.gridy = 0;
        c4.gridwidth = 1;
        c4.insets = new Insets(10,5,5,10);
        analysisPanel.add(dBConnectBtn, c4);

        // Row 2
        // ReadTable Label
        GridBagConstraints c5 = new GridBagConstraints();
        c5.gridx = 0;
        c5.gridy = 1;
        c5.gridwidth = 1;
        c5.ipadx = 10;
        c5.anchor = GridBagConstraints.LINE_START;
        c5.insets = new Insets(5,10,5,5);
        analysisPanel.add(readTableLbl, c5);

        ActionListener a4;
        a4 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                readTable = (String) readTableCmb.getSelectedItem();
            }
        };

//        readTableCmb.setEditable(true);
        readTableCmb.addActionListener(a4);
        readTableCmb.setEnabled(false);
        GridBagConstraints c6 = new GridBagConstraints();
        c6.gridx = 2;
        c6.gridy = 1;
        c6.gridwidth = 4;
        c6.insets = new Insets(5,5,5,10);
        c6.anchor = GridBagConstraints.LINE_START;
        analysisPanel.add(readTableCmb, c6);

        // Row 3
        // writeTable Label
        GridBagConstraints c7 = new GridBagConstraints();
        c7.gridx = 0;
        c7.gridy = 2;
        c7.gridwidth = 1;
        c7.ipadx = 10;
        c7.anchor = GridBagConstraints.LINE_START;
        c7.insets = new Insets(5,10,5,5);
        analysisPanel.add(writeTableLbl, c7);

        ActionListener a5;
        a5 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (ae.getActionCommand().equals("comboBoxEdited")) {
                    String writeTableName = (String) writeTableCmb.getSelectedItem();
                    readTableCmb.insertItemAt(writeTableName, 0);
                    dbh.createNewTable(writeTableName);
                }

            }
        };

        writeTableCmb.setEditable(true);
        writeTableCmb.addActionListener(a5);
        writeTableCmb.setEnabled(false);
        GridBagConstraints c8 = new GridBagConstraints();
        c8.gridx = 2;
        c8.gridy = 2;
        c8.gridwidth = 4;
        c8.insets = new Insets(5,5,5,10);
        c8.anchor = GridBagConstraints.LINE_START;
        analysisPanel.add(writeTableCmb, c8);

        // Row 4
        // Run Button
        ActionListener a3;
        a3 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    runAnalysis();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        runBtn.addActionListener(a3);

        GridBagConstraints c12 = new GridBagConstraints();
        c12.gridx = 6;
        c12.gridy = 3;
        c12.gridwidth = 1;
        c12.insets = new Insets(5,5,10,10);
        analysisPanel.add(runBtn, c12);

        guiFrame.add(analysisPanel);
        guiFrame.pack();
        guiFrame.setVisible(true);
    }

    private void updateComboBoxes() {
        ArrayList<String> tableList = dbh.getTableList();
        readTableCmb.removeAllItems();
        writeTableCmb.removeAllItems();
        for (String table : tableList) {
            readTableCmb.addItem(table);
            writeTableCmb.addItem(table);
        }
    }

}

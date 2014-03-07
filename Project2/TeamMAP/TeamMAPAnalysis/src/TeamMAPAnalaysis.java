import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class TeamMAPAnalaysis extends Component {

    /* CHANGE ALGORITHM USED HERE!!
    /************************************************************/
    private InterpolationAlgorithm alg = new MapAlgorithm2();
    /***********************************************************/

    private DatabaseHandler dbh = null;

    // GUI Objects
    private JFrame guiFrame = new JFrame();
    private JPanel analysisPanel = new JPanel(new GridBagLayout());

    private JLabel pathLbl = new JLabel("DB Path:");
    private JTextField pathTxt = new JTextField(30);
    private JFileChooser fc = new JFileChooser();
    private JButton browseBtn = new JButton("Browse");
    private JButton dBConnectBtn = new JButton("Connect");

    private JLabel prepareTableLbl = new JLabel("Prepare Table:");
    private JComboBox prepareTableCmb = new JComboBox(new String[] {""});
    private JButton prepareBtn = new JButton("Prepare");

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
        ArrayList<DataPoint> data = dbh.readDBData(readTable);
        ArrayList<DataPoint> results = alg.analyze(data);
        dbh.writeDBData(writeTable, results);
        new Thread(new FusionUpload(results)).start();

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
                    prepareTableCmb.setEnabled(true);
                    prepareBtn.setEnabled(true);
                    runBtn.setEnabled(true);
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
        // Prepare Label
        GridBagConstraints c9 = new GridBagConstraints();
        c9.gridx = 0;
        c9.gridy = 1;
        c9.gridwidth = 1;
        c9.ipadx = 10;
        c9.anchor = GridBagConstraints.LINE_START;
        c9.insets = new Insets(5,10,5,5);
        analysisPanel.add(prepareTableLbl, c9);

        ActionListener a9;
        a9 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                readTable = (String) prepareTableCmb.getSelectedItem();
            }
        };

        prepareTableCmb.addActionListener(a9);
        prepareTableCmb.setEnabled(false);
        GridBagConstraints c11 = new GridBagConstraints();
        c11.gridx = 2;
        c11.gridy = 1;
        c11.gridwidth = 4;
        c11.insets = new Insets(5,5,5,10);
        c11.anchor = GridBagConstraints.LINE_START;
        analysisPanel.add(prepareTableCmb, c11);

        ActionListener a3;
        a3 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String prepareTableName = (String) prepareTableCmb.getSelectedItem();
                dbh.prepareDBData(prepareTableName);
                updateComboBoxes();
            }
        };
        prepareBtn.addActionListener(a3);

        prepareBtn.setEnabled(false);
        GridBagConstraints c13 = new GridBagConstraints();
        c13.gridx = 6;
        c13.gridy = 1;
        c13.gridwidth = 1;
        c13.insets = new Insets(5,5,10,10);
        analysisPanel.add(prepareBtn, c13);

        // Row 3
        // ReadTable Label
        GridBagConstraints c5 = new GridBagConstraints();
        c5.gridx = 0;
        c5.gridy = 2;
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
        c6.gridy = 2;
        c6.gridwidth = 4;
        c6.insets = new Insets(5,5,5,10);
        c6.anchor = GridBagConstraints.LINE_START;
        analysisPanel.add(readTableCmb, c6);

        // Row 4
        // writeTable Label
        GridBagConstraints c7 = new GridBagConstraints();
        c7.gridx = 0;
        c7.gridy = 3;
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
                writeTable = (String) writeTableCmb.getSelectedItem();
            }
        };

        writeTableCmb.setEditable(true);
        writeTableCmb.addActionListener(a5);
        writeTableCmb.setEnabled(false);
        GridBagConstraints c8 = new GridBagConstraints();
        c8.gridx = 2;
        c8.gridy = 3;
        c8.gridwidth = 4;
        c8.insets = new Insets(5,5,5,10);
        c8.anchor = GridBagConstraints.LINE_START;
        analysisPanel.add(writeTableCmb, c8);

        // Row 5
        // Run Button
        ActionListener a6;
        a6 = new ActionListener()
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
        runBtn.addActionListener(a6);

        runBtn.setEnabled(false);
        GridBagConstraints c12 = new GridBagConstraints();
        c12.gridx = 6;
        c12.gridy = 4;
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
        prepareTableCmb.removeAllItems();
        for (String table : tableList) {
            readTableCmb.addItem(table);
            writeTableCmb.addItem(table);
            prepareTableCmb.addItem(table);
        }
    }

}

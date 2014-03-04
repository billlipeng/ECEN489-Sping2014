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
    private JPanel sortPanel = new JPanel(new GridBagLayout());

    private JLabel pathLbl = new JLabel("DB Path:");
    private JTextField pathTxt = new JTextField(30);
    private JFileChooser fc = new JFileChooser();
    private JButton browseBtn = new JButton("Browse");

    private JButton runBtn = new JButton("Run");

    private String filePath = "";

    public static void main(String[] args) throws Exception {
        new TeamMAPAnalaysis();
    }

    public TeamMAPAnalaysis() {
        configureGUI();
    }

    private void runAnalysis() throws ClassNotFoundException {
        dbh = new DatabaseHandler(filePath);
        alg = new ZachAlgorithm();
        ArrayList<DataPoint> data = dbh.readDBData();
        ArrayList<DataPoint> results = alg.analyze(data);
        dbh.writeDBData(results);

        // Upload to Fusion Table
    }

    private void configureGUI() {
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("TeamMAP Analysis");
//        guiFrame.setSize(640,480);
        guiFrame.setLocationRelativeTo(null);

        // Row 1
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridwidth = 1;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.ipadx = 10;
        c1.anchor = GridBagConstraints.LINE_START;
        c1.insets = new Insets(10,10,5,5);
        sortPanel.add(pathLbl, c1);

        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = 1;
        c2.gridy = 0;
        c2.gridwidth = 4;
        c2.weightx = 1.0;
        c2.insets = new Insets(10,5,5,5);
        sortPanel.add(pathTxt, c2);


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
        sortPanel.add(browseBtn, c3);

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
        c12.gridx = 5;
        c12.gridy = 1;
        c12.gridwidth = 1;
        c12.insets = new Insets(5,5,10,10);
        sortPanel.add(runBtn, c12);

        guiFrame.add(sortPanel);
        guiFrame.pack();
        guiFrame.setVisible(true);
    }

}

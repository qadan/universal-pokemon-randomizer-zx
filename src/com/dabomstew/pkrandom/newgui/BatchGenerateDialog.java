package com.dabomstew.pkrandom.newgui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class BatchGenerateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField outputFile;
    private JTextField seedsToCreate;
    private JLabel outputFileLabel;
    private JLabel seedsToCreateLabel;
    private JButton selectFileButton;
    private JFileChooser saveLocationChooser;
    private boolean completed = false;

    public boolean isCompleted() {
        return completed;
    }

    public BatchGenerateDialog(JFrame frame) {
        super(frame, true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonOK.setEnabled(false);
        buttonCancel.addActionListener(e -> onCancel());
        selectFileButton.addActionListener(e -> onSelectFile());
        seedsToCreate.addActionListener(e -> onSeedsToCreateChange());
        saveLocationChooser = new JFileChooser();
        saveLocationChooser.setCurrentDirectory(new File("./"));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setSize(346, 136);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void onSeedsToCreateChange() {
        validateButtonOK();
    }

    private void onSelectFile() {
        saveLocationChooser.setSelectedFile(null);
        int returnVal = saveLocationChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputFile.setText(saveLocationChooser.getSelectedFile().getAbsolutePath());
        }
        validateButtonOK();
    }

    private void validateButtonOK() {
        buttonOK.setEnabled(outputFileIsValid() && seedsToCreateIsValid());
    }

    private boolean seedsToCreateIsValid() {
        return seedsToCreate.getText().matches("-?\\d+");
    }

    private boolean outputFileIsValid() {
        String path = outputFile.getText();
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException exception) {
            return false;
        }
        return !path.isEmpty();
    }

    private void onOK() {
        completed = true;
        setVisible(false);
    }

    private void onCancel() {
        completed = false;
        setVisible(false);
    }

    public File getFile() {
        if (outputFileIsValid()) {
            return new File(outputFile.getText());
        }
        return null;
    }

    public int getBatchCount() {
        if (seedsToCreateIsValid()) {
            int count = Integer.parseInt(seedsToCreate.getText());
            return count;
        }
        return 0;
    }
}

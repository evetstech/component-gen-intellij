import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ComponentGenDialogWrapper extends DialogWrapper {
    private JTextField nameInput = new JTextField(10);
    private JTextField directoryInput = new JTextField(10);
    private String baseProjectPath;
    private boolean isTypeFeature;
    private boolean isFolderNameAppended;
    public ComponentGenDialogWrapper(String projectPath) {
        super(true); // use current window as parent
        init();
        setTitle("Component Template Generator");
        baseProjectPath = projectPath;
        isTypeFeature = true;
        isFolderNameAppended = false;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel selectorPanel = new JPanel(new SpringLayout());
        JPanel optionsPanel = new JPanel((new SpringLayout()));
        mainPanel.add(selectorPanel);
        mainPanel.add(optionsPanel);

        JRadioButton componentOption = new JRadioButton("Component");
        JRadioButton featureOption = new JRadioButton("Feature");
        featureOption.setSelected(true);
        JLabel nameLabel = new JLabel("Name", JLabel.TRAILING);
        JLabel directoryLabel = new JLabel("Directory", JLabel.TRAILING);
        JLabel empty = new JLabel("");
        JLabel empty2 = new JLabel("");
        JLabel empty3 = new JLabel("");
        JLabel empty4 = new JLabel("");
        JLabel nameDesc = new JLabel("Example: 'TestBtn' or 'Test Btn' will output src/features/<directory>/TestBtn/TestBtn.jsx, etc; Use PascalCase");
        JLabel directoryDesc = new JLabel("Example: 'FirstPartyData' [Will be relative to src/features; Use PascalCase]");
        JLabel outputLabel = new JLabel("Output Folder:");
        JLabel outputFull = new JLabel("");

        JCheckBox createFolderCheckbox = new JCheckBox();
        createFolderCheckbox.setText("Append component folder name to output");

        selectorPanel.add(featureOption);
        selectorPanel.add(componentOption);

        optionsPanel.add(nameLabel);
        nameLabel.setLabelFor(nameInput);
        optionsPanel.add(nameInput);
        optionsPanel.add(empty);
        optionsPanel.add(nameDesc);
        optionsPanel.add(directoryLabel);
        directoryLabel.setLabelFor(directoryInput);
        optionsPanel.add(directoryInput);
        optionsPanel.add(empty2);
        optionsPanel.add(directoryDesc);
        optionsPanel.add(empty3);
        optionsPanel.add(createFolderCheckbox);
        optionsPanel.add(outputLabel);
        optionsPanel.add(outputFull);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));

            }
        };
        ActionListener featureActionListener = e -> {
            directoryInput.setEditable(true);
            directoryDesc.setText("Example: 'FirstPartyData' [Will be relative src/features; Use PascalCase]");
            nameDesc.setText("Example: 'TestBtn' or 'Test Btn' will output in src/features/<directory>/TestBtn/TestBtn.jsx, etc; Use PascalCase");
            isTypeFeature = true;
            featureOption.setSelected(true);
            componentOption.setSelected(false);
            outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));
            createFolderCheckbox.setEnabled(true);

        };

        ActionListener componentActionListener = e -> {
            directoryInput.setText("");
            directoryInput.setEditable(false);
            createFolderCheckbox.setSelected(false);
            createFolderCheckbox.setEnabled(false);
            isFolderNameAppended = false;
            directoryDesc.setText("Will output to src/components/<ComponentName>/");
            nameDesc.setText("Example: 'TestBtn' or 'Test Btn' will output in src/components/TestBtn/TestBtn.jsx, etc; Use PascalCase");
            isTypeFeature = false;
            featureOption.setSelected(false);
            componentOption.setSelected(true);
            outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));

        };

        ActionListener addFolderActionListener = e -> {
            isFolderNameAppended = createFolderCheckbox.isSelected();
            outputFull.setText(createOutputPath(nameInput.getText(), directoryInput.getText()));
        };

        createFolderCheckbox.addActionListener(addFolderActionListener);
        featureOption.addActionListener(featureActionListener);
        componentOption.addActionListener(componentActionListener);
        nameInput.getDocument().addDocumentListener(dl);
        directoryInput.getDocument().addDocumentListener(dl);
        SpringUtilities.makeCompactGrid(selectorPanel,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        SpringUtilities.makeCompactGrid(optionsPanel,
                6, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        pack();

        return mainPanel;
    }
    @Override
    protected void doOKAction() {
        CreateFiles cr = new CreateFiles();
        try {
            String outputDir = createOutputPath(nameInput.getText(), directoryInput.getText());

            if(!outputDir.isBlank() || !outputDir.isEmpty()) {
                cr.createComponentFiles(ComponentGenUtils.toPascalCase(nameInput.getText()), outputDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        close(OK_EXIT_CODE);
    }

    private String createOutputPath(String name, String output) {
        if(name.isEmpty() || name.isBlank()) {
            return "";
        }
        String prefixDir = baseProjectPath + File.separator + "src" + File.separator + (isTypeFeature ? "features" : "components") + File.separator;

        if(isTypeFeature) {
            if(output.isBlank() || output.isEmpty()) {
                return "";
            }

            return prefixDir + output + File.separator + (isFolderNameAppended ? ComponentGenUtils.toPascalCase(name) + File.separator : "");
        }

        return prefixDir + ComponentGenUtils.toPascalCase(name) + File.separator;
    }
}
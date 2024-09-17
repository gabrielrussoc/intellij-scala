package org.jetbrains.plugins.scala.console.configuration;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.execution.ui.ConfigurationModuleSelector;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.plugins.scala.console.ScalaReplBundle;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

@SuppressWarnings("SameParameterValue")
public class ScalaConsoleRunConfigurationForm {

    private RawCommandLineEditor javaOptionsEditor;
    private JPanel myPanel;
    private RawCommandLineEditor consoleArgsEditor;
    private TextFieldWithBrowseButton workingDirectoryField;
    private ModulesComboBox moduleComboBox;

    private final ConfigurationModuleSelector myModuleSelector;

    public ScalaConsoleRunConfigurationForm(final Project project,
                                            final ScalaConsoleRunConfiguration configuration) {
        myModuleSelector = new ConfigurationModuleSelector(project, moduleComboBox);
        myModuleSelector.reset(configuration);
        moduleComboBox.setEnabled(true);
        javaOptionsEditor.setName(ScalaReplBundle.message("scala.console.config.vm.options"));
        javaOptionsEditor.setText("-Djline.terminal=NONE");
        consoleArgsEditor.setName(ScalaReplBundle.message("scala.console.config.console.arguments"));
        consoleArgsEditor.setText("-usejavacp");
        addFileChooser(ScalaReplBundle.message("scala.console.config.test.run.config.choose.working.directory"), workingDirectoryField, project);
        VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
        String path = baseDir != null ? baseDir.getPath() : "";
        workingDirectoryField.setText(path);
    }

    public JPanel getPanel() {
        return myPanel;
    }

    public String getJavaOptions() {
        return javaOptionsEditor.getText();
    }

    public void setJavaOptions(String s) {
        javaOptionsEditor.setText(s);
    }

    public void apply(ScalaConsoleRunConfiguration configuration) {
        setJavaOptions(configuration.javaOptions());
        setConsoleArgs(configuration.consoleArgs());
        setWorkingDirectory(configuration.workingDirectory());

        myModuleSelector.applyTo(configuration);
    }

    public String getConsoleArgs() {
        return consoleArgsEditor.getText();
    }

    public void setConsoleArgs(String consoleArgs) {
        this.consoleArgsEditor.setText(consoleArgs);
    }

    public String getWorkingDirectory() {
        return workingDirectoryField.getText();
    }

    public void setWorkingDirectory(String s) {
        workingDirectoryField.setText(s);
    }

    public Module getModule() {
        return myModuleSelector.getModule();
    }

    @SuppressWarnings("SameParameterValue")
    private void addFileChooser(final String title,
                                final TextFieldWithBrowseButton textField,
                                final Project project) {
        final FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false) {
            @Override
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                return super.isFileVisible(file, showHiddenFiles) && file.isDirectory();
            }
        };
        fileChooserDescriptor.withTitle(title);
        textField.addBrowseFolderListener(project, fileChooserDescriptor);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        myPanel = new JPanel();
        myPanel.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        myPanel.add(spacer1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("messages/ScalaReplBundle", "scala.console.config.vm.options"));
        myPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        javaOptionsEditor = new RawCommandLineEditor();
        javaOptionsEditor.setText("-Djline.terminal=NONE");
        myPanel.add(javaOptionsEditor, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("messages/ScalaReplBundle", "scala.console.config.console.arguments"));
        myPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        consoleArgsEditor = new RawCommandLineEditor();
        consoleArgsEditor.setText("-usejavacp");
        myPanel.add(consoleArgsEditor, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("messages/ScalaReplBundle", "scala.console.config.working.directory"));
        myPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        workingDirectoryField = new TextFieldWithBrowseButton();
        myPanel.add(workingDirectoryField, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("messages/ScalaReplBundle", "scala.console.config.use.classpath.and.sdk.of.module"));
        myPanel.add(label4, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        moduleComboBox = new ModulesComboBox();
        myPanel.add(moduleComboBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return myPanel;
    }

}

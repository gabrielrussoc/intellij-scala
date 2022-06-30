package org.jetbrains.plugins.scala.debugger.filters;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.plugins.scala.ScalaBundle;
import org.jetbrains.plugins.scala.icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

/**
 * @author ilyas
 */
public class ScalaDebuggerSettingsConfigurable implements Configurable {
    private JPanel myPanel;
    private JCheckBox friendlyDisplayOfScalaCheckBox;
    private JCheckBox dontShowRuntimeRefs;
    private JCheckBox showOuterVariables;
    private final ScalaDebuggerSettings mySettings;

    public ScalaDebuggerSettingsConfigurable(final ScalaDebuggerSettings settings) {
        mySettings = settings;
        friendlyDisplayOfScalaCheckBox.setSelected(settings.FRIENDLY_COLLECTION_DISPLAY_ENABLED);
        dontShowRuntimeRefs.setSelected(settings.DONT_SHOW_RUNTIME_REFS);
        showOuterVariables.setSelected(settings.SHOW_VARIABLES_FROM_OUTER_SCOPES);
    }

    @Override
    @Nls
    public String getDisplayName() {
        return ScalaBundle.message("scala.debug.caption");
    }

    public Icon getIcon() {
        return Icons.SCALA_SMALL_LOGO;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        return myPanel;
    }

    @Override
    public boolean isModified() {
        return mySettings.FRIENDLY_COLLECTION_DISPLAY_ENABLED != friendlyDisplayOfScalaCheckBox.isSelected() ||
                mySettings.DONT_SHOW_RUNTIME_REFS != dontShowRuntimeRefs.isSelected() ||
                mySettings.SHOW_VARIABLES_FROM_OUTER_SCOPES != showOuterVariables.isSelected();
    }

    @Override
    public void apply() throws ConfigurationException {
        mySettings.FRIENDLY_COLLECTION_DISPLAY_ENABLED = friendlyDisplayOfScalaCheckBox.isSelected();
        mySettings.DONT_SHOW_RUNTIME_REFS = dontShowRuntimeRefs.isSelected();
        mySettings.SHOW_VARIABLES_FROM_OUTER_SCOPES = showOuterVariables.isSelected();
    }

    @Override
    public void reset() {
        friendlyDisplayOfScalaCheckBox.setSelected(mySettings.FRIENDLY_COLLECTION_DISPLAY_ENABLED);
        dontShowRuntimeRefs.setSelected(mySettings.DONT_SHOW_RUNTIME_REFS);
        showOuterVariables.setSelected(mySettings.SHOW_VARIABLES_FROM_OUTER_SCOPES);
    }

    @Override
    public void disposeUIResources() {
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
        myPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        myPanel.add(spacer1, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        friendlyDisplayOfScalaCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(friendlyDisplayOfScalaCheckBox, this.$$$getMessageFromBundle$$$("messages/ScalaBundle", "friendly.collection.display.enabled"));
        myPanel.add(friendlyDisplayOfScalaCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dontShowRuntimeRefs = new JCheckBox();
        this.$$$loadButtonText$$$(dontShowRuntimeRefs, this.$$$getMessageFromBundle$$$("messages/ScalaBundle", "dont.show.runtime.refs"));
        myPanel.add(dontShowRuntimeRefs, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showOuterVariables = new JCheckBox();
        this.$$$loadButtonText$$$(showOuterVariables, this.$$$getMessageFromBundle$$$("messages/ScalaBundle", "show.variables.from.outer.scopes.in.variables.view"));
        myPanel.add(showOuterVariables, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
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
            component.setMnemonic(mnemonic);
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

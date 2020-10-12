package consulo.javaee.run.configuration.editor;

import com.intellij.execution.Executor;
import com.intellij.javaee.J2EEBundle;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Couple;
import com.intellij.ui.*;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.ListTableModel;
import consulo.javaee.run.configuration.JavaEEConfigurationImpl;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author VISTALL
 * @since 11-Jul-17
 */
public class JavaEEStartupConfigurationEditor extends SettingsEditor<JavaEEConfigurationImpl>
{
	@Nonnull
	@Override
	protected JComponent createEditor()
	{
		JPanel panel = new JPanel(new VerticalFlowLayout());
		JBList<Executor> executorList = new JBList<>(new CollectionListModel<>(Executor.EP_NAME.getExtensionList()));
		executorList.setCellRenderer(new ColoredListCellRenderer<Executor>()
		{
			@Override
			protected void customizeCellRenderer(@Nonnull JList<? extends Executor> list, Executor value, int index, boolean selected, boolean hasFocus)
			{
				setIcon(value.getIcon());
				append(value.getActionName());
			}
		});
		JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(executorList);
		scrollPane.setPreferredSize(JBUI.size(-1, 90));
		panel.add(scrollPane);

		JBTextField startupScript = new JBTextField();
		JBTextField shutdownScript = new JBTextField();

		JBCheckBox startScriptDefault = new JBCheckBox(J2EEBundle.message("checkbox.edit.script.properties.use.default"));
		JBCheckBox shutdownScriptDefault = new JBCheckBox(J2EEBundle.message("checkbox.edit.script.properties.use.default"));

		JPanel scriptPanel = new JPanel(new GridLayout(2, 3));
		panel.add(scriptPanel);
		scriptPanel.add(new JBLabel(J2EEBundle.message("label.run.configuration.editor.startup.script")));
		scriptPanel.add(startupScript);
		scriptPanel.add(startScriptDefault);
		scriptPanel.add(new JBLabel(J2EEBundle.message("label.run.configuration.editor.shutdown.script")));
		scriptPanel.add(shutdownScript);
		scriptPanel.add(shutdownScriptDefault);

		JPanel envPanel = new JPanel(new BorderLayout());
		envPanel.setBorder(IdeBorderFactory.createTitledBorder(J2EEBundle.message("border.run.configuration.editor.environment.variables"), false));
		JBCheckBox passEnvVariables = new JBCheckBox(J2EEBundle.message("checkbox.run.configuration.editor.pass.environment.variables"));
		envPanel.add(passEnvVariables, BorderLayout.NORTH);

		java.util.List<Couple<String>> env = new ArrayList<>();
		ColumnInfo[] columnInfos = {
				new ColumnInfo.StringColumn("Name"),
				new ColumnInfo.StringColumn("Value")
		};
		TableView<Couple<String>> tableView = new TableView<>(new ListTableModel<>(columnInfos, env));
		ToolbarDecorator decorator = ToolbarDecorator.createDecorator(tableView);
		decorator.disableUpDownActions();

		JPanel comp = decorator.createPanel();
		comp.setPreferredSize(JBUI.size(-1, 100));
		envPanel.add(comp, BorderLayout.CENTER);

		panel.add(envPanel);
		return ScrollPaneFactory.createScrollPane(panel);
	}

	@Override
	protected void resetEditorFrom(JavaEEConfigurationImpl configuration)
	{

	}

	@Override
	protected void applyEditorTo(JavaEEConfigurationImpl configuration) throws ConfigurationException
	{

	}
}

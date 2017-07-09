/*
 * Copyright (c) 2004 - 2009 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland
 */

package com.intellij.javaee.oss.server;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jetbrains.annotations.NotNull;
import com.intellij.javaee.oss.JavaeeBundle;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.run.configuration.PredefinedLogFilesListener;
import com.intellij.javaee.run.configuration.PredefinedLogFilesProviderEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.util.EventDispatcher;

public abstract class JavaeeRunSettingsEditor<T extends JavaeeServerModel> extends SettingsEditor<CommonModel> implements PredefinedLogFilesProviderEditor
{
	private final EventDispatcher<PredefinedLogFilesListener> dispatcher = EventDispatcher.create(PredefinedLogFilesListener.class);

	private JavaeeIntegration myAppServerIntegration;

	protected JavaeeRunSettingsEditor(JavaeeIntegration appServerIntegration)
	{
		myAppServerIntegration = appServerIntegration;
	}

	public void addListener(PredefinedLogFilesListener listener)
	{
		dispatcher.addListener(listener);
	}

	public void removeListener(PredefinedLogFilesListener listener)
	{
		dispatcher.removeListener(listener);
	}

	@Override
	@SuppressWarnings({"unchecked"})
	protected void resetEditorFrom(CommonModel config)
	{
		resetEditorFrom((T) config.getServerModel());
	}

	@Override
	@SuppressWarnings({"unchecked"})
	protected void applyEditorTo(CommonModel config) throws ConfigurationException
	{
		applyEditorTo((T) config.getServerModel());
	}

	@Override
	@NotNull
	protected JComponent createEditor()
	{
		JComponent editor = getEditor();
		String name = myAppServerIntegration.getName();
		editor.setBorder(BorderFactory.createTitledBorder(JavaeeBundle.getText("RunEditor.title", name)));
		return editor;
	}

	@Override
	protected void disposeEditor()
	{
	}

	protected void fireLogFilesChanged()
	{
		try
		{
			dispatcher.getMulticaster().predefinedLogFilesChanged(getSnapshot());
		}
		catch(ConfigurationException ignore)
		{
		}
	}

	protected static int getPort(JTextField text, String message) throws ConfigurationException
	{
		return Integer.parseInt(text.getText());
	}

	@NotNull
	protected abstract JComponent getEditor();

	protected abstract void resetEditorFrom(T model);

	protected abstract void applyEditorTo(T model) throws ConfigurationException;
}
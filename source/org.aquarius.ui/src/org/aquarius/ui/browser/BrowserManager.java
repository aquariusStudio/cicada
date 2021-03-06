/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.aquarius.ui.browser;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import org.aquarius.ui.UiActivator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

/**
 *
 */
public class BrowserManager extends Observable {
	protected List<IBrowserDescriptor> browsers;
	protected IBrowserDescriptor currentBrowser;

	private IPreferenceChangeListener pcl;
	protected boolean ignorePreferenceChanges = false;

	protected static BrowserManager instance;

	public static BrowserManager getInstance() {
		if (instance == null)
			instance = new BrowserManager();
		return instance;
	}

	private BrowserManager() {
		pcl = event -> {
			String property = event.getKey();
			if (!ignorePreferenceChanges && property.equals("browsers")) { //$NON-NLS-1$
				loadBrowsers();
			}
			if (!property.equals(WebBrowserPreference.PREF_INTERNAL_WEB_BROWSER_HISTORY)) {
				setChanged();
				notifyObservers();
			}
		};

		IScopeContext instanceScope = InstanceScope.INSTANCE;
		IEclipsePreferences prefs = instanceScope.getNode(UiActivator.PLUGIN_ID);
		prefs.addPreferenceChangeListener(pcl);
	}

	protected static void safeDispose() {
		if (instance == null)
			return;
		instance.dispose();
	}

	protected void dispose() {
		IScopeContext instanceScope = InstanceScope.INSTANCE;
		IEclipsePreferences prefs = instanceScope.getNode(UiActivator.PLUGIN_ID);
		prefs.removePreferenceChangeListener(pcl);
	}

	public IBrowserDescriptorWorkingCopy createExternalWebBrowser() {
		return new BrowserDescriptorWorkingCopy();
	}

	public List<IBrowserDescriptor> getWebBrowsers() {
		if (browsers == null)
			loadBrowsers();
		return new ArrayList<>(browsers);
	}

	public void loadBrowsers() {

		String xmlString = Platform.getPreferencesService().getString(UiActivator.PLUGIN_ID, "browsers", null, //$NON-NLS-1$
				null);
		if (xmlString != null && xmlString.length() > 0) {
			browsers = new ArrayList<>();

			try {
				ByteArrayInputStream in = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
				Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				IMemento memento = XMLMemento.createReadRoot(reader);

				IMemento system = memento.getChild("system"); //$NON-NLS-1$
				if (system != null && WebBrowserUtil.canUseSystemBrowser())
					browsers.add(new SystemBrowserDescriptor());

				IMemento[] children = memento.getChildren("external"); //$NON-NLS-1$
				int size = children.length;
				for (int i = 0; i < size; i++) {
					BrowserDescriptor browser = new BrowserDescriptor();
					browser.load(children[i]);
					browsers.add(browser);
				}

				Integer current = memento.getInteger("current"); //$NON-NLS-1$
				if (current != null) {
					currentBrowser = browsers.get(current.intValue());
				}
			} catch (Exception e) {
				UiActivator.logError("Could not load browsers: " + e.getMessage(), e);
			}

			IBrowserDescriptor system = new SystemBrowserDescriptor();
			if (WebBrowserUtil.canUseSystemBrowser() && !browsers.contains(system)) {
				browsers.add(0, system);
				currentBrowser = system;
				saveBrowsers();
			}
		} else {
			setupDefaultBrowsers();
			saveBrowsers();
		}

		if (currentBrowser == null && browsers.size() > 0)
			currentBrowser = browsers.get(0);
		setChanged();
		notifyObservers();
	}

	protected void saveBrowsers() {
		try {
			ignorePreferenceChanges = true;
			XMLMemento memento = XMLMemento.createWriteRoot("web-browsers"); //$NON-NLS-1$

			Iterator<IBrowserDescriptor> iterator = browsers.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj instanceof BrowserDescriptor) {
					BrowserDescriptor browser = (BrowserDescriptor) obj;
					IMemento child = memento.createChild("external"); //$NON-NLS-1$
					browser.save(child);
				} else if (obj instanceof SystemBrowserDescriptor) {
					memento.createChild("system"); //$NON-NLS-1$
				}
			}

			memento.putInteger("current", browsers.indexOf(currentBrowser)); //$NON-NLS-1$

			StringWriter writer = new StringWriter();
			memento.save(writer);
			String xmlString = writer.getBuffer().toString();
			IScopeContext instanceScope = InstanceScope.INSTANCE;
			IEclipsePreferences prefs = instanceScope.getNode(UiActivator.PLUGIN_ID);
			prefs.put("browsers", xmlString); //$NON-NLS-1$
			prefs.flush();
		} catch (Exception e) {
			UiActivator.logError("Could not save browsers", e);
		}
		ignorePreferenceChanges = false;
	}

	protected void setupDefaultBrowsers() {
		browsers = new ArrayList<>();

		// add system browser
		if (WebBrowserUtil.canUseSystemBrowser()) {
			IBrowserDescriptor system = new SystemBrowserDescriptor();
			browsers.add(system);
		}

		// by default, if internal is there, that is current, else set the first
		// external one
		if (!browsers.isEmpty() && currentBrowser == null)
			currentBrowser = browsers.get(0);
	}

	protected void addBrowser(IBrowserDescriptor browser) {
		if (browsers == null)
			loadBrowsers();
		if (!browsers.contains(browser))
			browsers.add(browser);
		if (browsers.size() == 1)
			setCurrentWebBrowser(browser);
	}

	protected void removeWebBrowser(IBrowserDescriptor browser) {
		if (browsers == null)
			loadBrowsers();
		browsers.remove(browser);

		if (currentBrowser == null || currentBrowser.equals(browser)) {
			currentBrowser = null;
			if (browsers.size() > 0)
				currentBrowser = browsers.get(0);
		}
	}

	public IBrowserDescriptor getCurrentWebBrowser() {
		if (browsers == null)
			loadBrowsers();

		if (currentBrowser == null && browsers.size() > 0)
			return browsers.get(0);

		return currentBrowser;
	}

	public void setCurrentWebBrowser(IBrowserDescriptor wb) {
		if (wb == null)
			throw new IllegalArgumentException();

		if (browsers.contains(wb))
			currentBrowser = wb;
		else
			throw new IllegalArgumentException();
		saveBrowsers();
	}

}

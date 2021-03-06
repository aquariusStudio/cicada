/**
 * 
 */
package org.aquarius.cicada.workbench.util;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author aquarius.github@hotmail.com
 *
 */
public final class WorkbenchUtil {

	private static final String InitMarker = "_Key_._Init_";

	/**
	 * 
	 */
	private WorkbenchUtil() {
		// Nothing to do
	}

	/**
	 * To indicate whether a marker is inited or not .<BR>
	 * The marker is class name and specified string.<BR>
	 * 
	 * @param store
	 * @param clazz
	 * @return
	 */
	public static final boolean isInited(IPreferenceStore store, Class<?> clazz) {
		String id = clazz.getClass().getName() + "." + InitMarker;
		return store.getBoolean(id);
	}

	/**
	 * Set whether a marker is inited or not .<BR>
	 * The marker is class name and specified string.<BR>
	 * 
	 * @param store
	 * @param clazz
	 * @param inited
	 */
	public static final void setInited(IPreferenceStore store, Class<?> clazz, boolean inited) {
		String id = clazz.getClass().getName() + "." + InitMarker;
		store.setValue(id, inited);
	}

	/**
	 * 
	 * @return
	 */
	public static IWorkbenchPart getActivePart() {
		IWorkbenchPage page = getActivePage();

		if (null == page) {
			return null;
		}

		return page.getActivePart();
	}

	/**
	 * 
	 * @return
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (null == window) {
			return null;
		}

		return window.getActivePage();
	}

	public static IEditorPart findEditor(IEditorInput editorInput) {
		IWorkbenchPage workbenchPage = getActivePage();

		if (null == workbenchPage) {
			return null;
		}

		return workbenchPage.findEditor(editorInput);
	}

	/**
	 * 
	 * @return
	 */
	public static IEditorPart getActiveEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (null == window) {
			return null;
		}

		IWorkbenchPage workbenchPage = window.getActivePage();

		if (null == workbenchPage) {
			return null;
		}

		return workbenchPage.getActiveEditor();
	}

}

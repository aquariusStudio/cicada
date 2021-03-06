package org.aquarius.cicada.workbench.editor.action;

import org.aquarius.cicada.workbench.Messages;
import org.aquarius.cicada.workbench.util.WorkbenchUtil;
import org.aquarius.util.spi.IRefreshable;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Parse movie info.<BR>
 *
 * @author aquarius.github@gmail.com
 *
 */
public class RefreshEditorAction extends Action {

	/**
	 * 
	 */
	public RefreshEditorAction() {
		super(""); //$NON-NLS-1$

		this.setText(Messages.RefreshEditorAction_Label);

		setImageDescriptor(org.aquarius.cicada.workbench.WorkbenchActivator.getImageDescriptor("/icons/refreshEditor.png")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {

		IWorkbenchPart workbenchPart = WorkbenchUtil.getActivePart();
		if (workbenchPart instanceof IRefreshable) {
			IRefreshable refreshable = (IRefreshable) workbenchPart;
			refreshable.refreshContent();
		}
	}

}
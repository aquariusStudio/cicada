/**
 *
 */
package org.aquarius.ui.base;

import java.util.ArrayList;
import java.util.List;

import org.aquarius.log.LogUtil;
import org.aquarius.util.AssertUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.slf4j.Logger;

/**
 * @author aquarius.github@gmail.com
 *
 */
public class SelectionProviderAdapter implements ISelectionProvider {

	private List<ISelectionChangedListener> listeners = new ArrayList<>();

	private ISelection selection;

	private Logger logger = LogUtil.getLogger(getClass());

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		AssertUtil.assertNotNull(listener);
		this.listeners.add(listener);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public ISelection getSelection() {
		return this.selection;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void setSelection(ISelection selection) {

		if (this.selection == selection) {
			return;
		}
		this.selection = selection;

		for (ISelectionChangedListener listener : this.listeners) {
			try {
				listener.selectionChanged(new SelectionChangedEvent(this, selection));
			} catch (Exception e) {
				this.logger.error("setSelection", e);
			}
		}
	}

}
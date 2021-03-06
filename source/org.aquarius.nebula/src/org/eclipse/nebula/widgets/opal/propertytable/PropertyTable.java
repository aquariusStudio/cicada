/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.widgets.opal.propertytable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Instances of this class are property sheets
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>PTPropertyChange</dd>
 * </dl>
 * </p>
 */
public class PropertyTable extends Composite {

	final static int VIEW_AS_FLAT_LIST = 0;
	final static int VIEW_AS_CATEGORIES = 1;

	boolean showButtons;
	boolean showDescription;
	boolean sorted;
	int styleOfView;
	final List<PTProperty> properties;
	private boolean hasBeenBuilt = false;
	private final List<PTPropertyChangeListener> changeListeners;

	private PTWidget widget;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     </ul>
	 *
	 */
	public PropertyTable(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		this.showButtons = false;
		this.showDescription = false;
		this.sorted = false;
		this.styleOfView = VIEW_AS_FLAT_LIST;
		this.properties = new ArrayList<PTProperty>();
		this.changeListeners = new ArrayList<PTPropertyChangeListener>();

		this.widget = PTWidgetFactory.build(this);

		addListener(SWT.Resize, event -> {
			// Draw the widget on first displaying
			if (!this.hasBeenBuilt) {
				this.widget.build();
				this.hasBeenBuilt = true;
			}
		});

	}

	/**
	 * Add a change listener (event fired when the value of a property is changed)
	 *
	 * @param listener
	 */
	public void addChangeListener(final PTPropertyChangeListener listener) {
		this.changeListeners.add(listener);
	}

	/**
	 * Add a property in this widget
	 *
	 * @param property property to add
	 * @return the property
	 */
	public PTProperty addProperty(final PTProperty property) {
		if (this.properties.contains(property)) {
			throw new IllegalArgumentException("A property called '" + property.getName() + "' has already been declared.");
		}

		this.properties.add(property);
		property.setParentTable(this);
		return property;
	}

	/**
	 * Find the property with specified name.<BR>
	 *
	 * @param propertyName
	 * @return
	 */
	public PTProperty findProperty(final String propertyName) {
		for (PTProperty property : this.properties) {
			if (property.getName().equals(propertyName)) {
				return property;
			}
		}

		return null;
	}

	/**
	 * Add a property in this widget
	 *
	 * @param property property to add
	 * @return the property
	 */
	public void removeProperty(final PTProperty property) {
		if (!this.properties.contains(property)) {
			throw new IllegalArgumentException("A property called '" + property.getName() + "' has not been declared.");
		}

		this.properties.remove(property);
		property.setParentTable(null);
		this.rebuild();
	}

	public void removeProperty(int index) {
		PTProperty property = this.properties.remove(index);
		property.setParentTable(null);
		this.rebuild();
	}

	public void clearProperties() {
		this.properties.clear();
		this.rebuild();
	}

	/**
	 * Fire the event "a value of a property has changed"
	 *
	 * @param property property which value has changed
	 */
	public void firePTPropertyChangeListeners(final PTProperty property) {
		for (final PTPropertyChangeListener listener : this.changeListeners) {
			listener.propertyHasChanged(property);
		}
	}

	/**
	 * @return the values stored in this object in a map. Keys are property's name,
	 *         values are values stored in a the property.
	 */
	public Map<String, Object> getProperties() {
		final Map<String, Object> map = new HashMap<String, Object>();
		for (final PTProperty prop : this.properties) {
			map.put(prop.getName(), prop.getValue());
		}
		return map;
	}

	/**
	 * @return the properties stored in a list
	 */
	public List<PTProperty> getPropertiesAsList() {
		return new ArrayList<PTProperty>(this.properties);
	}

	/**
	 * Hide all buttons
	 *
	 * @return this property table
	 */
	public PropertyTable hideButtons() {
		this.showButtons = false;
		return rebuild();
	}

	/**
	 * Hide description
	 *
	 * @return this property table
	 */
	public PropertyTable hideDescription() {
		this.showDescription = false;
		return rebuild();
	}

	/**
	 * Rebuild the whole table
	 *
	 * @return this property table
	 */
	private PropertyTable rebuild() {
		this.widget = this.widget.disposeAndBuild(this);
		if (this.hasBeenBuilt) {
			setLayout(new FillLayout());
			this.widget.build();
			layout();
		}
		return this;
	}

	/**
	 * Update the component when some values has changed
	 */
	public void refreshValues() {
		rebuild();
	}

	/**
	 * Remove a change listener
	 *
	 * @param listener listener to remove
	 */
	public void removeChangeListener(final PTPropertyChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	/**
	 * @param newValues
	 */
	public void setProperties(final Map<String, Object> newValues) {
		for (final PTProperty prop : this.properties) {
			if (newValues == null) {
				prop.setValue(null);
			} else {
				final Object value = newValues.get(prop.getName());
				prop.setValue(value);
			}
		}
		rebuild();
	}

	/**
	 * Show all buttons
	 *
	 * @return this property table
	 */
	public PropertyTable showButtons() {
		this.showButtons = true;
		return rebuild();
	}

	/**
	 * Show description
	 *
	 * @return this property table
	 */
	public PropertyTable showDescription() {
		this.showDescription = true;
		return rebuild();
	}

	/**
	 * Sort the properties
	 *
	 * @return this property table
	 */
	public PropertyTable sort() {
		this.sorted = true;
		this.widget.refillData();
		return this;
	}

	/**
	 * Show properties not sorted
	 *
	 * @return this property table
	 */
	public PropertyTable unsort() {
		this.sorted = false;
		this.widget.refillData();
		return this;

	}

	/**
	 * View the properties as categories
	 *
	 * @return this property table
	 */
	public PropertyTable viewAsCategories() {
		this.styleOfView = VIEW_AS_CATEGORIES;
		return rebuild();
	}

	/**
	 * View the properties as a flat list
	 *
	 * @return this property table
	 */
	public PropertyTable viewAsFlatList() {
		this.styleOfView = VIEW_AS_FLAT_LIST;
		return rebuild();
	}

	/**
	 * @return the widget
	 */
	public PTWidget getWidget() {
		return this.widget;
	}
}

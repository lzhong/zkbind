/* BindTracker.java

	Purpose:
		
	Description:
		
	History:
		Jun 29, 2011 6:33:22 PM, Created by henri

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkbind.sys.tracker;

import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkbind.sys.Binding;
import org.zkoss.zkbind.sys.LoadBinding;
import org.zkoss.zkbind.sys.PropertyBinding;

/**
 * Bind tracker to maintain a binding dependency graph.
 * e.g. @{a.b.c, save-onchange:d.e.f} then it forms a dependency from d.e.f to a.b.c for save. 
 * @author henri
 */
public interface Tracker {
	/**
	 * Add a tracking that associate a binding to a dot series under the specified {@link Component}
	 * @param comp the associated component
	 * @param series the dot series as an array of property name
	 * @param binding the binding
	 */
	public void addTracking(Component comp, String[] series, Binding binding);
	
	/**
	 * Remove all tracking associated with the specified {@link Component}. 
	 * @param comp the associated component
	 */
	public void removeTrackings(Component comp);

	/**
	 * Tie a property to its corresponding value under the specified {@link Component}.
	 * @param comp the associated component
	 * @param base the base object of the property
	 * @param script the field script
	 * @param propName the resolved property name from the field script
	 * @param value the value of the property
	 */
	public void tieValue(Object comp, Object base, Object script, Object propName, Object value);
	
	/**
	 * Returns all bindings that associated with the specified property.
	 * @param base base object
	 * @param propName property name
	 * @return all LoadBindings that associated with the specified property.
	 */
	public Set<LoadBinding> getLoadBindings(Object base, String propName);
}

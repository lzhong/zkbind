/* PropertyBinding.java

	Purpose:
		
	Description:
		
	History:
		Jul 26, 2011 3:48:41 PM, Created by henri

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkbind.sys;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkbind.Converter;

/**
 * A binding tells how to deal with Load or Save a field of a source object
 * (usually an UI component) and a property of a target object(usually a backing bean).
 * @author henri
 */
public interface PropertyBinding extends Binding {
	/**
	 * Returns the associated _converter with this binding. 
	 * @return the associated _converter with this binding.
	 */
	public Converter getConverter();
	
	/**
	 * Returns the field name of the source object.
	 * @return the field name of the source object.
	 */
	public String getFieldName();
	
	/**
	 * Returns the associated command name of this binding; null if not speicified.
	 * @return the associated command name of this binding; null if not speicified.
	 */
	public String getCommandName();
	
	/**
	 * Returns the property expression script of this binding.
	 * @return the property expression script of this binding. 
	 */
	public String getPropertyString();
	
	/**
	 * Returns whether bind this binding after execute associated command(true); 
	 * 	otherwise it shall bind before execute associated command(false).
	 * @return whether bind this binding after execute associated command(true); 
	 * 	otherwise it shall bind before execute associated command(false).
	 */
	public boolean isAfter();
}

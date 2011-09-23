/* ZKBindLoad1Composer.java

	Purpose:
		
	Description:
		
	History:
		Aug 2, 2011 1:01:07 PM, Created by henri

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */

package test2;

import org.zkoss.zkbind.GenericBindComposer;
import org.zkoss.zkbind.NotifyChange;

/**
 * @author henri
 * 
 */
public class Test2ZKBindPropertyComposer extends GenericBindComposer {
	private String value1;
	private String value2;
	private String value3;
	private String value4;

	public Test2ZKBindPropertyComposer() {
		value1 = "A";
		value2 = "B";
		value3 = "C";
		value4 = "D";
	}

	public String getValue1() {
		return value1;
	}

	@NotifyChange
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	@NotifyChange
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	@NotifyChange
	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	
	public void commandA1(){
		
	}
	
	public void commandA2(){
		
	}
	
	public void commandA3(){
		value3 += "-byA3";
	}
	
	//@Command("commandA4")
	public void commandA4(){
		
	}
}

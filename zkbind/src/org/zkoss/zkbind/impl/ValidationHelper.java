package org.zkoss.zkbind.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.zkoss.xel.ExpressionX;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkbind.BindContext;
import org.zkoss.zkbind.Binder;
import org.zkoss.zkbind.Property;
import org.zkoss.zkbind.Validator;
import org.zkoss.zkbind.sys.BindEvaluatorX;
import org.zkoss.zkbind.sys.Binding;
import org.zkoss.zkbind.sys.SaveBinding;
import org.zkoss.zkbind.sys.SaveFormBinding;
import org.zkoss.zkbind.sys.SavePropertyBinding;
/**
 * a internal stateless helper to helps BindImpl to the validation.
 * 
 * @author dennis
 *
 */
/*public*/ class ValidationHelper {
	
	private final Binder _binder;
	private final InfoProvider _infoProvider;
	
	private Map<Binding,Set<Property>> _validatesCache;
	
	public ValidationHelper(Binder binder,InfoProvider infoProvider){
		this._binder = binder;
		this._infoProvider = infoProvider;
		this._validatesCache = new HashMap<Binding,Set<Property>>(2);
	}
	
	// a binder validation information provider, it is related to implementation of BindImpl 
	interface InfoProvider {
		Map<String, List<SavePropertyBinding>> getSaveBeforeBindings();
		Map<String, List<SavePropertyBinding>> getSaveAfterBindings();
		Map<String, List<SaveFormBinding>> getSaveFormBeforeBindings();
		Map<String, List<SaveFormBinding>> getSaveFormAfterBindings();
		Map<String, List<SavePropertyBinding>> getSaveEventBindings();
		String dualId(String uuid, String attr);
		
	}
	
	//TODO move all evalArgs to a utiltity
	private Map<String, Object> evalArgs(Component comp, Map<String, Object> args) {
		if (args == null) {
			return null;
		}
		final BindEvaluatorX eval = _binder.getEvaluatorX();
		final Map<String, Object> result = new LinkedHashMap<String, Object>(args.size()); 
		for(final Iterator<Entry<String, Object>> it = args.entrySet().iterator(); it.hasNext();) {
			final Entry<String, Object> entry = it.next(); 
			final String key = entry.getKey();
			final Object value = entry.getValue();
			//evaluate the arg if it was a ExpressionX
			final Object evalValue = value == null ? null : 
				(value instanceof ExpressionX) ? eval.getValue(null, comp, (ExpressionX)value) : value;
			result.put(key, evalValue);
		}
		return result;
	}
	
	
	//doCommand -> doValidate ->
	public void collectSaveBefore(Component comp, String command, Event evt, Set<Property> validates){
		collectSavePropertyBefore(comp, command, evt, validates);
		collectSaveFormBefore(comp, command, evt, validates);
	}
	
	//doCommand -> doValidate -> collectSaveBefore ->
	private void collectSavePropertyBefore(Component comp, String command, Event evt, Set<Property> validates) {
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveBeforeBindings().get(command);//_saveBeforeBindings.get(command);
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				collectSavePropertyBinding(comp, binding, command, evt, validates);
			}
		}
	}
	
	//doCommand -> doValidate -> collectSaveBefore ->
	private void collectSaveFormBefore(Component comp, String command, Event evt, Set<Property> validates) {
		final List<SaveFormBinding> bindings = _infoProvider.getSaveFormBeforeBindings().get(command);//_saveFormBeforeBindings.get(command);
		if (bindings != null) {
			for (SaveFormBinding binding : bindings) {
				collectSaveFormBinding(comp, binding, command, evt, validates);
			}
		}
	}
	
	
	
	
	//doValidate -> 
	public void collectSaveAfter(Component comp, String command, Event evt, Set<Property> validates) {
		collectSavePropertyAfter(comp, command, evt, validates);
		collectSaveFormAfter(comp, command, evt, validates);
	}
	
	
	//doValidate -> collectSaveAfter ->
	private void collectSavePropertyAfter(Component comp, String command, Event evt, Set<Property> validates) {
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveAfterBindings().get(command);//_saveAfterBindings.get(command);
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				collectSavePropertyBinding(comp, binding, command, evt, validates);
			}
		}
	}
	
	//doValidate -> collectSaveAfter ->
	private void collectSaveFormAfter(Component comp, String command, Event evt, Set<Property> validates) {
		final List<SaveFormBinding> bindings = _infoProvider.getSaveFormAfterBindings().get(command);//_saveFormAfterBindings.get(command);
		if (bindings != null) {
			for (SaveFormBinding binding : bindings) {
				collectSaveFormBinding(comp, binding, command, evt, validates);
			}
		}
	}	
	
	
	
	//doValidate -> 
	public void collectSaveEvent(Component comp, String command, Event evt, Set<Property> validates) {
		final String evtnm = evt == null ? null : evt.getName(); 
		final String evtId = _infoProvider.dualId(comp.getUuid(), evtnm);;//dualId(comp.getUuid(), evtnm);
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveEventBindings().get(evtId);//_saveEventBindings.get(evtId);
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				collectSavePropertyBinding(comp, binding, command, evt, validates);
			}
		}
	}

	//validations

	public boolean validateSaveBefore(Component comp,String command, Set<Property> validates,boolean valid) {
		boolean r = valid;
		r &= validateSavePropertyBefore(comp, command, validates,r);
		r &= validateSaveFormBefore(comp, command, validates,r);
		return r;
	}
	
	//doCommand -> doValidate -> validateSaveBefore ->
	private boolean validateSavePropertyBefore(Component comp,String command, Set<Property> validates,boolean valid) {
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveBeforeBindings().get(command);//_saveBeforeBindings.get(command);
		boolean r = valid;
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				r &= validateSavePropertyBinding(comp, binding, command, validates, r);
			}
		}
		return r;
	}
	
	//doCommand -> doValidate -> validateSaveBefore ->
	private boolean validateSaveFormBefore(Component comp,String command, Set<Property> validates,boolean valid) {
		final List<SaveFormBinding> bindings = _infoProvider.getSaveFormBeforeBindings().get(command);//_saveFormBeforeBindings.get(command);
		boolean r = valid;
		if (bindings != null) {
			for (SaveFormBinding binding : bindings) {
				r &= validateSaveFormBinding(comp, binding, command, validates, r);
			}
		}
		return r;
	}	
	
	public boolean validateSaveAfter(Component comp,String command, Set<Property> validates, boolean valid) {
		boolean r = valid;
		r &= validateSavePropertyAfter(comp, command, validates,r);
		r &= validateSaveFormAfter(comp, command, validates,r);
		return r;
	}
	
	//doCommand -> doValidate -> validateSaveBefore ->
	private boolean validateSavePropertyAfter(Component comp,String command, Set<Property> validates, boolean valid) {
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveAfterBindings().get(command);//_saveBeforeBindings.get(command);
		boolean r = true;
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				r &= validateSavePropertyBinding(comp, binding, command, validates,valid);
			}
		}
		return r;
	}
	
	//doCommand -> doValidate -> validateSaveBefore ->
	private boolean validateSaveFormAfter(Component comp,String command, Set<Property> validates, boolean valid) {
		final List<SaveFormBinding> bindings = _infoProvider.getSaveFormAfterBindings().get(command);//_saveFormBeforeBindings.get(command);
		boolean r = valid;
		if (bindings != null) {
			for (SaveFormBinding binding : bindings) {
				r &= validateSaveFormBinding(comp, binding, command, validates, r);
			}
		}
		return r;
	}	
	
	
	
	
	public boolean validateSaveEvent(Component comp, String command, Event evt, Set<Property> validates, boolean valid) {
		final String evtnm = evt == null ? null : evt.getName(); 
		final String evtId = _infoProvider.dualId(comp.getUuid(), evtnm);;//dualId(comp.getUuid(), evtnm);
		final List<SavePropertyBinding> bindings = _infoProvider.getSaveEventBindings().get(evtId);//_saveEventBindings.get(evtId);
		boolean r = valid;
		if (bindings != null) {
			for (SavePropertyBinding binding : bindings) {
				r &= validateSavePropertyBinding(comp, binding, command, validates, r);
			}
		}
		return r;
	}
	
	
	
	
	
	//collect properties from a save-binding
	private void collectSavePropertyBinding(Component comp, SavePropertyBinding binding, String command, Event evt, Set<Property> validates) {
		Set<Property> vs = _validatesCache.get(binding);
		if(vs!=null) {
			validates.addAll(vs);
			return;
		}
		
		final Map<String, Object> args = evalArgs(comp, binding.getArgs());
		final BindContext ctx = new BindContextImpl(_binder, binding, true, command, binding.getComponent(), evt, args);
		validates.addAll(vs = binding.getValidates(ctx)); //collect properties to be validated
		_validatesCache.put(binding, vs);
	}
	
	//correct properties form a save-form-binding
	private void collectSaveFormBinding(Component comp, SaveFormBinding binding, String command, Event evt, Set<Property> validates) {
		Set<Property> vs = _validatesCache.get(binding);
		if(vs!=null) {
			validates.addAll(vs);
			return;
		}
		
		final Map<String, Object> args = evalArgs(comp, binding.getArgs());
		final BindContext ctx = new BindContextImpl(_binder, binding, true, command, binding.getComponent(), evt, args);
		//by spec., no way to do prompt save of a form 
		//validate command save, no VALIDATE phase for each binding save
		validates.addAll(vs = binding.getValidates(ctx)); //collect properties to be validated
		
		_validatesCache.put(binding, vs);
	}
	
	
	//collect properties from a save-binding
	private boolean validateSavePropertyBinding(Component comp,SavePropertyBinding binding,String command, Set<Property> validates, boolean valid) {
		final Map<String, Object> args = evalArgs(comp, binding.getArgs());
		final BindContext ctx = new BindContextImpl(_binder, binding, true, command, binding.getComponent(), null, args);
		Validator validator = binding.getValidator();
		Set<Property> vs = _validatesCache.get(binding); 
		Property p = vs.size()>0?vs.iterator().next():null;
		ValidationContextImpl vContext = new ValidationContextImpl(command,p,validates,ctx,valid);
		validator.validate(vContext);
		return vContext.isValid();
	}
	
	//correct properties form a save-form-binding
	private boolean validateSaveFormBinding(Component comp, SaveFormBinding binding, String command, Set<Property> validates, boolean valid) {
		final Map<String, Object> args = evalArgs(comp, binding.getArgs());
		final BindContext ctx = new BindContextImpl(_binder, binding, true, command, binding.getComponent(), null, args);
		//TODO , form need to do extra action(ex, nested property-validation)for validation state.
		Validator validator = binding.getValidator();
		Set<Property> vs = _validatesCache.get(binding); 
		Property p = vs.size()>0?vs.iterator().next():null;
		ValidationContextImpl vContext = new ValidationContextImpl(command,p,validates,ctx,valid);
		validator.validate(vContext);
		return vContext.isValid();
		
	}
	
	
}

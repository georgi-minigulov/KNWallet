package org.gfm.wallet.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Gerodot
 *
 * Holds action status and error/warrning message(if needed)
 */
public class Action {
	/**
	 * @author Gerodot
	 * 
	 * Action status
	 */
	public static enum ActionStatus {NEW, OK, WARN, FAIL};

	private ActionStatus status; 
	private String message;
	
	private Action() {
		this.status = ActionStatus.NEW;
		this.message = null;
	}
	
	public ActionStatus getStatus() {
		return this.status;
	}
	public String getMessage() {
		return this.message;
	}
	
	@JsonIgnore
	public boolean isOpen() {
		return status == ActionStatus.NEW;
	}
	
	public Action ok() {
		if (this.status == ActionStatus.NEW) {
			this.status = ActionStatus.OK;
		}
		return this;
	}
	public Action error(String message) {
		if (this.status == ActionStatus.NEW) {
			this.status = ActionStatus.FAIL;
			this.message = message;
		}
		return this;
	}
	public Action warn(String message) {		
		if (this.status == ActionStatus.NEW) {
			this.status = ActionStatus.WARN;
			this.message = message;
		}
		return this;
	}
	
	public static Action newAction() {
		return new Action();
	}
	public static Action errorAction(String msg) {
		return new Action().error(msg);
	}
}

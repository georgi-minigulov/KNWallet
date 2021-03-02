package org.gfm.wallet.data;

/**
 * Action types:
 * {@link #CREATE}
 * {@link #SEARCH}
 * {@link #REMOVE}
 * {@link #UPDATE}
 * {@link #LINK}
 * 
 * @author Gerodot 
 */
public enum ActionTypes {
	
	/**
	 * Creates new object
	 */
	CREATE, 
	/**
	 * Searches for data
	 */
	SEARCH,
	/**
	 * Removes object
	 */
	REMOVE,
	/**
	 * Updtes object
	 */
	UPDATE,
	/**
	 * Connects two objects
	 */
	LINK
}

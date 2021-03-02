package org.gfm.wallet.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Hold user data
 * Can contain multiple wallets
 * Unique by login
 * 
 * @author Gerodot
 */
@Entity
@Table(name = "t_users", uniqueConstraints = {@UniqueConstraint(columnNames={"login"})})
public class User extends DataItemBase {
	
	public User() {
		super();
	}
	
	public User(String login, String name) {
		this();
		this.login = login;
		this.name = name;
	}
	
	@Column(name = "login")
	protected String login;

	@Column(name = "name")
	protected String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Wallet> wallets;
	
	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	@Override
    public int hashCode() {
		return 31*31*super.hashCode()+Objects.hash(login, name);
	}	   
	
	@Override
    public String toString() {
        return "User [id=" + String.valueOf(id)+
       			   ", login="+ String.valueOf(login) + 
        	  	   ", name="+ String.valueOf(name) + 
        		   ", created=" + String.valueOf(created) + "]";
    }
}

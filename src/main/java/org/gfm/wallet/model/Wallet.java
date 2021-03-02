package org.gfm.wallet.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.util.StringUtils;

/**
 * Hold wallet data
 * Wallet is linked to user
 * Unique by currency for single user
 * 
 * @author Gerodot
 */
@Entity
@Table(name = "t_wallets", uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "currency_code"})})
//@EntityScan("org.gfm.wallet.model")
public class Wallet extends DataItemBase {
	
	@Column(name = "balance")
	private BigDecimal balance;

	@Convert(converter = CurrencyConverter.class)
	@Transient
	private Currency currency;

	@Column(name = "currency_code")
	private String currencyCode;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;

	public Wallet() {
		super();
	}
	
	public Wallet(User user, Currency currency) {
		this ();
		this.user = user;
		this.balance = BigDecimal.ZERO;
		setCurrency(currency);
	}
	
	public void setCurrency(Currency val) {
		this.currency = val;
		this.currencyCode = val == null ? null : val.getCurrencyCode();
	}

	public void setCurrencyCode(String val) {
		this.currencyCode = val;
		this.currency = StringUtils.hasText(val) ? Currency.getInstance(val) : null;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	/**
	 * Adds sum to balance
	 * Sum must be positive
	 * 
	 * @param sum
	 * @return 0 if succeed, -1 otherwise
	 */
	public int increment(BigDecimal sum) {
		if (sum == null || BigDecimal.ZERO.compareTo(sum)>0)
			return -1;
		balance = balance.add(sum);
		return 0;
	}
	
	/**
	 * Subtracts sum from balance
	 * Sum must by positive
	 * Result must be positive
	 * 
	 * @param sum
	 * @return
	 */
	public int decrement(BigDecimal sum) {
		if (sum == null || BigDecimal.ZERO.compareTo(sum)>0 || BigDecimal.ZERO.compareTo(balance.subtract(sum))>0)
			return -1;
		this.balance = balance.subtract(sum);
		return 0;
	}

	public Currency getCurrency() {
		if (currency == null) {
			setCurrencyCode(currencyCode);
		}
		return currency;
	}

	public User getUser() {
		return user;
	}
	
	@Override
    public int hashCode() {
		return 31*31*super.hashCode()+Objects.hash(balance, currency);
	}

	@Converter(autoApply = true)
	protected class CurrencyConverter implements AttributeConverter<Currency, String> {
	    @Override
	    public String convertToDatabaseColumn(Currency val) {
	    	return val == null ? null : val.getCurrencyCode();
	    }

	    @Override
	    public Currency convertToEntityAttribute(String val) {
	    	return StringUtils.hasText(val) ? Currency.getInstance(val) : null;
	    }
	}	
	
	@Override
    public String toString() {
        return "User [id=" + String.valueOf(id)+
       			   ", balance="+ String.valueOf(balance) + 
        	  	   ", currency="+ String.valueOf(currency) + 
        	  	   ", user_id="+ String.valueOf(user.getId()) + 
        		   ", created=" + String.valueOf(created) + "]";
    }
}

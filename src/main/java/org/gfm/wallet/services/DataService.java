package org.gfm.wallet.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.gfm.wallet.data.Action;
import org.gfm.wallet.data.ActionTypes;
import org.gfm.wallet.model.DataItemBase;
import org.gfm.wallet.model.HistoryRecord;
import org.gfm.wallet.model.User;
import org.gfm.wallet.model.Wallet;
import org.gfm.wallet.repository.HistoryRepository;
import org.gfm.wallet.repository.UserRepository;
import org.gfm.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Performs actions with users and wallets
 * 
 * @author Gerodot
 */
@Service
public class DataService {
		
	@Autowired 
	private UserRepository userRep; 
	@Autowired 
	private WalletRepository walletRep; 
	@Autowired 
	private HistoryRepository historyRep;
	
	/**
	 * Get all users
	 * 
	 * @return
	 */
	public List<User> findAllUsers(){
		Iterable<User> inner = userRep.findAll(); 
		return StreamSupport.stream(inner.spliterator(), false).collect(Collectors.toList());
	}
	
	/**
	 * Find User by id
	 * 
	 * @param id
	 * @return
	 */
	public User findUserById(Long id){
		Optional<User> res = userRep.findById(id);
		return res.isEmpty() ? null : res.get();
	}
	
	/**
	 * Find user by login (unique)
	 * 
	 * @param login
	 * @return
	 */
	public User findUserByLogin(String login){
		return userRep.findByLogin(login);
	}

	/**
	 * Get all wallets, related to User
	 * 
	 * @param user
	 * @return
	 */
	public List<Wallet> findWalletsByUser(User user){
		return walletRep.findByUser(user.getId());
	}

	/**
	 * Get user wallet for currency
	 * 
	 * @param user
	 * @param curr
	 * @return
	 */
	public Wallet findWalletByUserAndCurrency(User user, Currency curr){
		return walletRep.findByUserAndCurrency(user.getId(), curr.getCurrencyCode());
	}
	
	/**
	 * Get user by id
	 * 
	 * @param id
	 * @return
	 */
	public Wallet findWalletById(Long id){
		Optional<Wallet> res = walletRep.findById(id);
		return res.isEmpty() ? null : res.get();
	}

	/**
	 * Get history related to item
	 * 
	 * @param item
	 * @return
	 */
	public List<HistoryRecord> findHistoryForItem(DataItemBase item){
		if (item.getClass().isAssignableFrom(HistoryRecord.class)) {
			Optional<HistoryRecord> res = historyRep.findById(item.getId()); 
			return res.isEmpty() ? Arrays.asList() : Arrays.asList(res.get());
		} else 
			return historyRep.findForObject(item.getClass().getSimpleName().toLowerCase(), item.getId());
	}

	/**
	 * Get history by id
	 * 
	 * @param id
	 * @return
	 */
	public HistoryRecord findHistoryById(Long id){
		Optional<HistoryRecord> res = historyRep.findById(id);
		return res.isEmpty() ? null : res.get();
	}

	/**
	 * Adds user to system
	 *
	 * @param action - action state
	 * @param login - user login, unique
	 * @param name - visible user name
	 * @return
	 */
	public User addUser(Action action, String login, String name) {
		User res = null;
		if (!StringUtils.hasText(login)) {
			action.error("Cannot create user, login is not provided");
		} else if (!StringUtils.hasText(name)) {
			action.error("Cannot create user, name is not provided");
		} else {
			login = login.trim().toLowerCase();
			name = name.trim().toLowerCase();
			res = findUserByLogin(login);
			if (res != null) {
				action.error("User with login "+login+" is already present in system");
			} else {
				res = new User(login, name);
				userRep.save(res);
				writeHistory(action, null, res, ActionTypes.CREATE, "Created user "+res.getName()+" with login "+res.getLogin()+" , new id is "+res.getId(), res.getId().toString());
			}
		}
		return res;
	}
	
	/**
	 * Adds new wallect with new currency to user
	 * 
	 * @param action - action state
	 * @param user - target user
	 * @param curr - required currency
	 * @return
	 */
	public Wallet addWallet(Action action, User user, Currency curr) {
		Wallet res = null;
		if (user == null) {
			action.error("Cannot create add wallet, user is not provided");
		} else if (curr == null) {
			action.error("Cannot create add wallet, currency is not provided");
		} else {
			res = findWalletByUserAndCurrency(user, curr);
			if (res != null) {
				action.error("User with login "+user.getLogin()+" is already has wallet with currency "+curr.getCurrencyCode());
			} else {
				res = new Wallet(user, curr);
				walletRep.save(res);
				writeHistory(action, user, res, ActionTypes.CREATE, "Added wallet with currency "+res.getCurrency()+" to user with login  "+res.getUser().getLogin()+" , new id is "+res.getId(), res.getId().toString());
			}
		}
		return res;
	}

	/**
	 * Writes history record
	 * 
	 * @param action - action state
	 * @param source - action source 
	 * @param target - action target
	 * @param actionType - performed action type
	 * @param message - action message
	 * @param value - action field value
	 * @return history record
	 */
	public HistoryRecord writeHistory(Action action, DataItemBase source, DataItemBase target, ActionTypes actionType, String message, String value) {
		HistoryRecord res = null;
		if (actionType == null) {
			action.error("Cannot write history, action type is not provided");
		} else if (message == null) {
			action.error("Cannot write history, message is not provided");
		} else {
			res = new HistoryRecord(actionType, source, target, message, value);
			historyRep.save(res);
		}
		return res;		
	}
	
	/**
	 * Adds money to wallet
	 * 
	 * @param action - action state
	 * @param target - target wallet
	 * @param sum - amount to add (can be only positive) 
	 * @return target wallet
	 */
	public Wallet addToWallet(Action action, Wallet target, BigDecimal sum) {
		if (target == null) {
			action.error("Cannot add to wallet, wallet is not provided");
		} else if (sum == null) {
			action.error("Cannot add to wallet, sum is not provided");
		} else {
			if (BigDecimal.ZERO.compareTo(sum)>0) {
				action.error("Can add only positive sum");
			} else {
				int res = target.increment(sum);
				if (res<0) {
					action.error("Failed to add to balance");
				} else {
					walletRep.save(target);
					writeHistory(action, null, target, ActionTypes.UPDATE, "added "+sum.toString()+" to wallet with id "+target.getId(), sum.toString());
				}
			}
		}
		return target;		
	}

	/**
	 * Removes money from wallet
	 * 
	 * @param action - action state
	 * @param target - target wallet
	 * @param sum  - amount to remove (can be only positive, can't exceed balance) 
	 * @return target wallet
	 */
	public Wallet removeFromWallet(Action action, Wallet target, BigDecimal sum) {
		if (target == null) {
			action.error("Cannot substract from wallet, wallet is not provided");
		} else if (sum == null) {
			action.error("Cannot substract from wallet, sum is not provided");
		} else {
			if (sum.compareTo(BigDecimal.ZERO)<0) {
				action.error("Can substract only positive sum");
			} else {
				int res = target.decrement(sum);
				if (res<0) {
					action.error("Can't remove sum from wallet with id "+target.getId()+", requested amount is greater than current balance");
				} else {
					walletRep.save(target);
					writeHistory(action, null, target, ActionTypes.UPDATE, "removed "+sum.toString()+" from wallet with id "+target.getId(), sum.toString());
				}
			}
		}
		return target;		
	}

	/**
	 * Removes money from one wallet and adds to another
	 * 
	 * @param action  - action state
	 * @param source - source wallet
	 * @param target - target wallet
	 * @param sum - amount to remove (can be only positive, wallets should have similar currency, can't exceed source wallet balance) 
	 * @return target wallet
	 */
	public Wallet transfer(Action action, Wallet source, Wallet target, BigDecimal sum) {
		if (source == null) {
			action.error("Cannot transfer, source wallet is not provided");
		} else if (target == null) {
			action.error("Cannot transfer, target wallet is not provided");
		} else if (sum == null) {
			action.error("Cannot substract from wallet, sum is not provided");
		} else if (BigDecimal.ZERO.compareTo(sum)>0) {
			action.error("Cannot substract from wallet, sum must be positive");
		} else if (source.getId().equals(target.getId())) {
			action.error("Wallet is source and target at same time");
		} else if (source.getCurrency()!=target.getCurrency()) {
			action.error("Cannot transfer, source wallet currency ("+source.getCurrency().getCurrencyCode()+") does not match target wallet currency("+target.getCurrency().getCurrencyCode()+")");
		} else {
			removeFromWallet(action, source, sum);
			if (action.isOpen()) {
				target = addToWallet(action, target, sum);
			}
		}
		return target;		
	}
}

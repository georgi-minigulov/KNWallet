package org.gfm.wallet.rest;

import java.util.Currency;
import java.util.List;

import org.gfm.wallet.data.Action;
import org.gfm.wallet.model.HistoryRecord;
import org.gfm.wallet.model.User;
import org.gfm.wallet.model.Wallet;
import org.gfm.wallet.requests.WalletRequest;
import org.gfm.wallet.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletRest {
	
	@Autowired
	DataService dataService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "/wallet", produces = "application/json")
	public ResponseEntity<Object> getWallet(@RequestParam(value = "user_id") Long userId, @RequestParam(value = "wallet_id") Long walletId) {
		User rUser = dataService.findUserById(userId);
		Wallet rWallet = dataService.findWalletById(walletId);
		ResponseEntity<Object> res;
		if (rUser == null) {
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
		} else if (rWallet == null) {
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Wallet not found"));
		} else if (!rUser.getId().equals(rWallet.getUser().getId())){
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Wallet user is not matching provided user"));
		} else {
			res = ResponseEntity.ok(rWallet);
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "/wallets", produces = "application/json")
	public ResponseEntity<Object> getUserWallets(@RequestParam(value = "user_id") Long userId) {
		User rUser = dataService.findUserById(userId);
		ResponseEntity<Object> res;
		if (rUser == null) {
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
		} else {
			List<Wallet> rWallets = dataService.findWalletsByUser(rUser);
			res = ResponseEntity.ok(rWallets);
		}
		return res;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/wallet/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addWallet(@RequestBody WalletRequest walletData) {
		ResponseEntity<Object> res;
		if (walletData.getUserId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("User not provided"));
		} else if (!StringUtils.hasText(walletData.getCurrencyCode())) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Currency code not provided"));
		} else {
			Currency curr;
			try {
				curr = Currency.getInstance(walletData.getCurrencyCode());
			} catch (Exception e) {
				curr = null;
			}
			if (curr == null) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Unable to parse currency code"));
			} else {
				User user = dataService.findUserById(walletData.getUserId());
				if (user == null) {
					res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));					
				} else {
					Action action = Action.newAction();		
					Wallet rWallet = dataService.addWallet(action, user, curr);
					if (action.isOpen()) {
						res = ResponseEntity.ok(rWallet);
					} else {
						 res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(action);
					}					
				}
			}
		}
		return res; 
	}
	
	@GetMapping(path="/wallet/history", produces = "application/json")
	public ResponseEntity<Object> getWalletHistory(@RequestParam(value = "wallet_id") Long id) {
		Wallet wallet = dataService.findWalletById(id);
		ResponseEntity<Object> res;
		if (wallet==null) {
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
		} else {
			List<HistoryRecord> hRecords = dataService.findHistoryForItem(wallet);
			res = ResponseEntity.ok(hRecords);
		}
		return res;
	}
}

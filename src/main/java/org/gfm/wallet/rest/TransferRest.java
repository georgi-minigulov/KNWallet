package org.gfm.wallet.rest;

import java.math.BigDecimal;

import org.gfm.wallet.data.Action;
import org.gfm.wallet.model.User;
import org.gfm.wallet.model.Wallet;
import org.gfm.wallet.requests.ModRequest;
import org.gfm.wallet.requests.TransferRequest;
import org.gfm.wallet.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferRest {
	@Autowired
	DataService dataService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path= "/money/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> moneyAdd(@RequestBody ModRequest transferData) {
		ResponseEntity<Object> res;
		if (transferData.getUserId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("User not provided"));
		} else if (transferData.getWalletId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Wallet not provided"));
		} else if (!StringUtils.hasText(transferData.getSum())) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Sum not provided"));
		} else {
			User user = dataService.findUserById(transferData.getUserId());	
			Wallet wallet = dataService.findWalletById(transferData.getWalletId());			
			BigDecimal sum;
			try {
				sum = new BigDecimal(transferData.getSum().trim());
			} catch (NumberFormatException e) {
				sum = null;
			}
			if (user==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
			} else if (wallet==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Wallet not found"));
			} else if (sum==null) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Failed to parse sum"));
			} else if (!wallet.getUser().getId().equals(user.getId())) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Wallet user not matching provided user"));
			} else {
				Action action = Action.newAction();
				wallet = dataService.addToWallet(action, wallet, sum);
				if (action.isOpen()) {
					res = ResponseEntity.ok(wallet);
				} else {
					res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(action);
				}
			}
		}
		return res;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path= "/money/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> moneyRemove(@RequestBody ModRequest transferData) {
		ResponseEntity<Object> res;
		if (transferData.getUserId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("User not provided"));
		} else if (transferData.getWalletId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Wallet not provided"));
		} else if (!StringUtils.hasText(transferData.getSum())) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Sum not provided"));
		} else {
			User user = dataService.findUserById(transferData.getUserId());	
			Wallet wallet = dataService.findWalletById(transferData.getWalletId());			
			BigDecimal sum;
			try {
				sum = new BigDecimal(transferData.getSum().trim());
			} catch (NumberFormatException e) {
				sum = null;
			}
			if (user==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
			} else if (wallet==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Wallet not found"));
			} else if (sum==null) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Failed to parse sum"));
			} else if (!wallet.getUser().getId().equals(user.getId())) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Wallet user not matching provided user"));
			} else {
				Action action = Action.newAction();
				wallet = dataService.removeFromWallet(action, wallet, sum);
				if (action.isOpen()) {
					res = ResponseEntity.ok(wallet);
				} else {
					res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(action);
				}
			}
		}
		return res;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path= "/money/transfer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> moneyTransfer(@RequestBody TransferRequest transferData) {
		ResponseEntity<Object> res;
		if (transferData.getSourceUserId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Source user not provided"));
		} else if (transferData.getSourceWalletId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Source wallet not provided"));
		} else if (transferData.getTargetUserId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Target user not provided"));
		} else if (transferData.getTargetWalletId()==null) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Target wallet not provided"));
		} else if (!StringUtils.hasText(transferData.getSum())) {
			res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Sum not provided"));
		} else {
			User sUser = dataService.findUserById(transferData.getSourceUserId());	
			Wallet sWallet = dataService.findWalletById(transferData.getSourceWalletId());			
			User tUser = dataService.findUserById(transferData.getTargetUserId());	
			Wallet tWallet = dataService.findWalletById(transferData.getTargetWalletId());			
			BigDecimal sum;
			try {
				sum = new BigDecimal(transferData.getSum().trim());
			} catch (NumberFormatException e) {
				sum = null;
			}
			if (sUser==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Source user not found"));
			} else if (sWallet==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Source wallet not found"));
			} else if (tUser==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Target user not found"));
			} else if (tWallet==null) {
				res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("Target wallet not found"));
			} else if (sum==null) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Failed to parse sum"));
			} else if (!sWallet.getUser().getId().equals(sUser.getId())) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Source wallet user not matching provided source user"));
			} else if (!tWallet.getUser().getId().equals(tUser.getId())) {
				res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Action.errorAction("Target wallet user not matching provided target user"));
			} else {
				Action action = Action.newAction();
				tWallet = dataService.transfer(action, sWallet, tWallet, sum);
				if (action.isOpen()) {
					res = ResponseEntity.ok(tWallet);
				} else {
					res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(action);
				}
			}
		}
		return res;
	}

}

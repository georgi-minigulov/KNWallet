package org.gfm.wallet.rest;

import java.util.List;

import org.gfm.wallet.data.Action;
import org.gfm.wallet.model.HistoryRecord;
import org.gfm.wallet.model.User;
import org.gfm.wallet.requests.UserRequest;
import org.gfm.wallet.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRest {

	@Autowired
	DataService dataService;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path="/login", produces = "application/json")
	public User getLogin(@RequestParam(value = "user_login") String login) {
		return dataService.findUserByLogin(login);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path="/user", produces = "application/json")
	public User getUser(@RequestParam(value = "user_id") Long id) {
		return dataService.findUserById(id);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path="/users", produces = "application/json")
	public List<User> getUsesr() {
		return dataService.findAllUsers();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path= "/user/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addUser(@RequestBody UserRequest userData) {
		Action action = Action.newAction();
		User user = dataService.addUser(action, userData.getLogin(), userData.getName());
		ResponseEntity<Object> res;
		if (action.isOpen()) {
			res = ResponseEntity.ok(user);
		} else {
			res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(action);
		}
		return res;
	}

	@GetMapping(path="/user/history", produces = "application/json")
	public ResponseEntity<Object> getUserHistory(@RequestParam(value = "user_id") Long id) {
		User user = dataService.findUserById(id);
		ResponseEntity<Object> res;
		if (user==null) {
			res = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Action.errorAction("User not found"));
		} else {
			List<HistoryRecord> hRecords = dataService.findHistoryForItem(user);
			res = ResponseEntity.ok(hRecords);
		}
		return res;
	}
}

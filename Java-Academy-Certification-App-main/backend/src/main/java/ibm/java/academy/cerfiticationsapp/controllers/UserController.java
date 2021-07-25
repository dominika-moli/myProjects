package ibm.java.academy.cerfiticationsapp.controllers;

import ibm.java.academy.cerfiticationsapp.model.ConfirmationToken;
import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;
import ibm.java.academy.cerfiticationsapp.service.ConfirmationTokenService;
import ibm.java.academy.cerfiticationsapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@Log
public class UserController {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
    UserJpaRepository userRepo;

	private final UserService userService;

	private final ConfirmationTokenService confirmationTokenService;

	@GetMapping("/sign-in")
	@ResponseBody
	public String signIn() {

		return "sign-in";
	}

	@PostMapping(path = "/sign-in", produces = "application/json")
	@ResponseBody
	public ConfirmationToken signIn(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password) {
		return userService.signInUser(email, password);
	}


	@PostMapping(path = "/sign-in-token", produces = "application/json")
	@ResponseBody
	public ConfirmationToken signIn(@RequestParam(name = "token") String token) {
		return confirmationTokenService.loginViaToken(token);
	}

	@GetMapping("/sign-up")
	@ResponseBody
	public String signUp() {
		return "sign-up";
	}

	@PostMapping(path = "/sign-up", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public User signUp(@RequestBody User user) {
		return userService.signUpUser(user);
	}

	@GetMapping(path = "/find-user")
	@ResponseBody
	public Optional<User> userById(@RequestParam("id") Long id) {
		return userRepo.findById(id);
	}


	@RequestMapping(value = "/update-user/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User updateUser(@RequestBody User user, @PathVariable("id") Long id) {

		User existing = userRepo.findOneById(id);
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
		userService.copyNonNullProperties(user, existing);
		userRepo.save(existing);
		return existing;
	}

    @PutMapping(value = "/changeRole/{id}")
    @ResponseBody
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestParam("role") String roleId) {
        try {
            User user = userService.updateRole(id, roleId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/addVoucher", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addVoucher(@RequestBody User userRequest, @RequestParam("id") Long id, @RequestParam("voucherId") Long voucherId) {
	    try {
	        User user = userService.addVoucher(id, voucherId);
	        return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

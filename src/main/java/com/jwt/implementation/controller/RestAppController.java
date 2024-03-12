package com.jwt.implementation.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.implementation.config.JwtGeneratorValidator;
import com.jwt.implementation.model.FileDB;
import com.jwt.implementation.model.LoginSuccess;
import com.jwt.implementation.model.Role;
import com.jwt.implementation.model.User;
import com.jwt.implementation.model.UserDTO;
import com.jwt.implementation.repository.RoleRepository;
import com.jwt.implementation.repository.UserRepository;
import com.jwt.implementation.service.DefaultUserService;
import com.jwt.implementation.service.FileStorageService;

@RestController
public class RestAppController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtGeneratorValidator jwtGenVal;

	@Autowired
	BCryptPasswordEncoder bcCryptPasswordEncoder;

	@Autowired
	DefaultUserService userService;

	@Autowired
    FileStorageService storageService;

	@PostMapping("/registration")
	public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDto) {
		// User users = userService.save(userDto);
		try {
			User users = userService.save(userDto);
			return generateRespose("User saved successfully : " + users.getId(), HttpStatus.OK, users);
			// Handle successful save
		} catch (IllegalArgumentException e) {
			// Handle the case where the email or username already exists
			return generateRespose("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST, userDto);
			// System.out.println("Error: " + e.getMessage());
		}

	}

	@CrossOrigin
	@PostMapping("/genToken")
	public LoginSuccess generateJwtToken(@RequestBody UserDTO userDto) throws Exception {

		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setToken(jwtGenVal.generateToken(authentication));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User authRole = userRepo.findByUserName(username);
		Role checkRole = roleRepo.findByRole("ROLE_ADMIN");
		if (authRole.getRole().contains(checkRole)) {
			loginSuccess.setRole("admin");
		} else {
			loginSuccess.setRole("user");
		}
		return loginSuccess;

	}

	@CrossOrigin
	@GetMapping("/welcomeAdmin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String welcome() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User authRole = userRepo.findByUserName(username);
		Role checkRole = roleRepo.findByRole("admin");
		if (authRole.getRole().contains(checkRole)) {
			return "admin";
		}
		return "user";
	}

	
    @CrossOrigin
	@GetMapping("/welcomeUser")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String welcomeUser() {
		return "WelcomeUSER";
	}

	public ResponseEntity<Object> generateRespose(String message, HttpStatus st, Object responseobj) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meaasge", message);
		map.put("Status", st.value());
		map.put("data", responseobj);

		return new ResponseEntity<Object>(map, st);
	}

	@CrossOrigin
    @GetMapping("/files/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') ")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

}

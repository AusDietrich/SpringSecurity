package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Customer;
import com.example.demo.repo.CustomerRepository;

@RestController
public class LoginController {

	@Autowired
	CustomerRepository custRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Customer cust){
		
		Customer savedCustomer = null;
		ResponseEntity response = null;
		try {
			String hashPwd = passwordEncoder.encode(cust.getPwd());
			cust.setPwd(hashPwd);
			savedCustomer = custRepo.save(cust);
			if(savedCustomer.getId() > 0) {
				response = ResponseEntity
						.status(HttpStatus.CREATED)
						.body("Given user details successfully added.");
			}
		}catch (Exception ex) {
			response = ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An exception occured due to: "+ex.getMessage());
		}
		return response;
	}
}

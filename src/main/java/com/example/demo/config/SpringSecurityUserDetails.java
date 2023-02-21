package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Customer;
import com.example.demo.repo.CustomerRepository;

@Service
public class SpringSecurityUserDetails implements UserDetailsService{

	@Autowired
	CustomerRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
	
		String userName, password = null;
		List<GrantedAuthority> authorities =null;
		List<Customer> customer = userRepo.findByEmail(username);
		if (customer.size() == 0) {
			throw new UsernameNotFoundException("Username not forund for email: " + username);
		} else {
			userName = customer.get(0).getEmail();
			password = customer.get(0).getPwd();
			authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
		}
		return new User(username, password, authorities);
	}

}

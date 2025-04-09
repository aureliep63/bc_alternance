package com.example.BC_alternance.service.impl;


import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.model.enums.RolesEnum;
import com.example.BC_alternance.repository.UtilisateurRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UtilisateurRepository utilisateurRepository;

	public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
		this.utilisateurRepository = utilisateurRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Utilisateur> userResult = utilisateurRepository.findByEmail(email);
		if(userResult.isEmpty()) {
			throw new UsernameNotFoundException(email + " not found.");
		}
		Utilisateur user = userResult.get();
//		return new User(user.getEmail(), user.getMotDePasse(), getRoles(user.getRole()));
		return new User(user.getEmail(), user.getMotDePasse(), getRoles(user.getRole()));

	}

	private List<GrantedAuthority> getRoles(RolesEnum role) {
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_" + role));
		return roles;
	}
}

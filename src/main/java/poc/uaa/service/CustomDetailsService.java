package poc.uaa.service;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import poc.uaa.entity.CustomUser;

@Service
public class CustomDetailsService implements UserDetailsService {

	@Override
	public CustomUser loadUserByUsername(final String username) {
		return new CustomUser("username", "$2a$10$NVIQhBH51iiVYfje3nsnUu/eQ1V0Xpi12cgMofxhRd2DVNyhyyhia" // "password"
				, true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("user")));
	}
}
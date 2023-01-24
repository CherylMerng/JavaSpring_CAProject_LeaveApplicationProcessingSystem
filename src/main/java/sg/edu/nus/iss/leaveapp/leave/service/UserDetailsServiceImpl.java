package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.repository.UserRepository;
 
@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private UserRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       sg.edu.nus.iss.leaveapp.leave.model.User retrievedUser = userRepository.getUserByUsername(username);

        if (retrievedUser == null) {
            throw new UsernameNotFoundException("User not found");
        } else {

            List<Role> roles = retrievedUser.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
			for(Role role : roles) {
				ga.add(new SimpleGrantedAuthority(role.getRoleName()));
			}

           return new org.springframework.security.core.userdetails.User(
							username,
							encoder.encode(retrievedUser.getPassword()), ga);  
        }
    }

}
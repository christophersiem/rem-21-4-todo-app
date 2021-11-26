package de.neuefische.backend.security.service;

import de.neuefische.backend.security.model.AppUser;
import de.neuefische.backend.security.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private final AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> optionalUser = appUserRepo.findById(username);
        if (optionalUser.isPresent()) {
            return User
                    .withUsername(username)
                    .password(optionalUser.get().getPassword())
                    .authorities("user").build();
        } else {
            throw new UsernameNotFoundException("Username does not exist:" + username);
        }
    }

}

package csd.backend.Account.MS.Model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired 
    private AppUserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AppUserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public void registerUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider(AppUser.AuthProvider.LOCAL);
        userRepository.save(user);
    }

    @Transactional
    public void processOAuthLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        AppUser user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new AppUser();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setAuthProvider(AppUser.AuthProvider.GOOGLE);
            userRepository.save(user);
        }
    }
}

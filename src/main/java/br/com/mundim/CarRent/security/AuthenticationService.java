package br.com.mundim.CarRent.security;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.exception.UnauthorizedRequestException;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static br.com.mundim.CarRent.exception.config.BaseErrorMessage.UNAUTHORIZED_USER;

@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public boolean verifyUserAuthentication(User user) {
        User loggedUser = findUserByBearer();
        if(!loggedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && !loggedUser.equals(user)){
            throw new UnauthorizedRequestException(UNAUTHORIZED_USER.getMessage());
        }
        return true;
    }

    public User findUserByBearer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName);
    }
}

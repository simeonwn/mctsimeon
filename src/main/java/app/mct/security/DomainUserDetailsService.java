package app.mct.security;

import app.mct.config.Constants;
import app.mct.domain.MobileUser;
import app.mct.domain.User;
import app.mct.repository.MobileUserRepository;
import app.mct.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final MobileUserRepository mobileUserRepository;

    private final PasswordEncoder passwordEncoder;

    public DomainUserDetailsService(UserRepository userRepository, MobileUserRepository mobileUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mobileUserRepository = mobileUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        if (login.startsWith(Constants.MOBILE_USER_PREFIX)) {
            String mobileNumber = login.replaceFirst(Constants.MOBILE_USER_PREFIX, "");
            return mobileUserRepository.findOneByMobileNumber(mobileNumber)
                .map(mobileUser -> createSpringSecurityUser(mobileNumber, mobileUser))
                .orElseThrow(() -> new MobileNumberNotFoundException("Mobile number " + mobileNumber + " was not found in the database"));
        } else {
            String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
            return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
                .map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
        }

    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
            user.getPassword(),
            grantedAuthorities);
    }


    /**
     * Create user detail for mobile user. Atm only comparing mobile number combine with email
     *
     * @param lowercaseLogin
     * @param mobileUser
     * @return User Detail
     */
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, MobileUser mobileUser) {
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList("ROLE_MOBILE_USER").stream()
            .map(authority -> new SimpleGrantedAuthority(authority))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(mobileUser.getMobileNumber(),
            passwordEncoder.encode(Constants.MOBILE_USER_PREFIX + mobileUser.getMobileNumber() + mobileUser.getEmail()),
            grantedAuthorities);
    }
}

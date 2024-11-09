package users.service;

import users.model.QuizUserAdapter;
import users.model.entity.QuizUser;
import users.repository.QuizUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class QuizUserDetailsServiceImpl implements UserDetailsService {

    private final QuizUserRepository repository;

    public QuizUserDetailsServiceImpl(QuizUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        QuizUser user = repository
                .findQuizUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email"));

        return new QuizUserAdapter(user);
    }
}

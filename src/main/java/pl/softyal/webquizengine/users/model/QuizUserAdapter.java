package pl.softyal.webquizengine.users.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.Collection;
import java.util.List;

public class QuizUserAdapter implements UserDetails {

    private final QuizUser user;

    public QuizUserAdapter(QuizUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return user.getId();
    }

    public QuizUser getEntity() {
        return user;
    }
}

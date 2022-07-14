package com.cadaverback.security.config;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    // @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities)
    {
        this.id = id;
        this.username = username;
        // this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // public static UserDetailsImpl build(User user)
    // {
    // List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    // return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    public Long getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(authorities, email, id, password, username);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDetailsImpl other = (UserDetailsImpl) obj;
        return Objects.equals(authorities, other.authorities) && Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(password, other.password)
                && Objects.equals(username, other.username);
    }

}

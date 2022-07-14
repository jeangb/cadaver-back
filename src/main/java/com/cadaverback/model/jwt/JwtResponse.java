package com.cadaverback.model.jwt;

import java.io.Serializable;

public class JwtResponse implements Serializable
{

    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;

    private final Long id;

    public JwtResponse(String jwttoken, Long long1)
    {
        this.jwttoken = jwttoken;
        this.id = long1;
    }

    public String getToken()
    {
        return this.jwttoken;
    }

    public Long getId()
    {
        return id;
    }
}

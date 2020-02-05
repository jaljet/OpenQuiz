package com.xartifex.simplequiz.rest;

import com.xartifex.simplequiz.data.GameDAO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/rules")
public class RuleRest {

    @Inject
    GameDAO gameDAO;

    @GET
    public String getRules()  {
        return gameDAO.getRule().getText();
    }
}

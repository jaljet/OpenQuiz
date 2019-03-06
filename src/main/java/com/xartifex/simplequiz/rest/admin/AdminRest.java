package com.xartifex.simplequiz.rest.admin;

import com.xartifex.simplequiz.data.GameDAO;
import com.xartifex.simplequiz.model.Question;
import com.xartifex.simplequiz.user.UserDAO;
import com.xartifex.simplequiz.user.UserInfo;
import com.xartifex.simplequiz.util.Util;
import org.slf4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;

@RolesAllowed({"admins"})
@Path("/admin")
@Stateless
public class AdminRest {

    @Inject
    Logger log;

    @Inject
    GameDAO gameDAO;

    @Inject
    UserDAO userDAO;

    @POST
    @Path("/addQuestions")
    @Consumes("text/plain")
    @Produces("text/plain")
    public Response addQuestions(String data) {
        Collection<Question> questions = Util.getQuestions(data);
        gameDAO.addQuestions(questions);
        log.info("Questions to add: " + questions);
        return Response.status(Response.Status.OK).entity("Questions added.").build();
    }

    @POST
    @Path("/addPlayers")
    @Consumes("text/plain")
    @Produces("text/plain")
    public Response addPlayers(String data) {
        Set<UserInfo> users = Util.getUsers(data);
        userDAO.addUsers(users);
        log.info("Players to add: " + users);
        return Response.status(Response.Status.OK).entity("Users added.").build();
    }

    @GET
    @Path("/rules")
    public String getRules()  {
        return gameDAO.getRule(400003).getText();
    }
}

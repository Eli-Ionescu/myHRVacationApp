package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Team;
import com.mycompany.myapp.repository.TeamRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Lion King on 11-Aug-16.
 */
@RestController
@RequestMapping("api")
public class TeamResource {

    @Inject
    private TeamRepository teamRepository;

    @RequestMapping(value="/teams/:id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/teams",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> createDepartment(@RequestBody Team team) throws URISyntaxException {
        teamRepository.save(team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/teams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Team> getTeams() throws URISyntaxException {
        return teamRepository.findAll();
    }
}

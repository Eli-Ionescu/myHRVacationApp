package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.VacationStock;
import com.mycompany.myapp.repository.VacationStockRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VacationStock.
 */
@RestController
@RequestMapping("/api")
public class VacationStockResource {

    private final Logger log = LoggerFactory.getLogger(VacationStockResource.class);
        
    @Inject
    private VacationStockRepository vacationStockRepository;
    
    /**
     * POST  /vacation-stocks : Create a new vacationStock.
     *
     * @param vacationStock the vacationStock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacationStock, or with status 400 (Bad Request) if the vacationStock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacation-stocks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationStock> createVacationStock(@RequestBody VacationStock vacationStock) throws URISyntaxException {
        log.debug("REST request to save VacationStock : {}", vacationStock);
        if (vacationStock.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vacationStock", "idexists", "A new vacationStock cannot already have an ID")).body(null);
        }
        VacationStock result = vacationStockRepository.save(vacationStock);
        return ResponseEntity.created(new URI("/api/vacation-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vacationStock", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vacation-stocks : Updates an existing vacationStock.
     *
     * @param vacationStock the vacationStock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacationStock,
     * or with status 400 (Bad Request) if the vacationStock is not valid,
     * or with status 500 (Internal Server Error) if the vacationStock couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacation-stocks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationStock> updateVacationStock(@RequestBody VacationStock vacationStock) throws URISyntaxException {
        log.debug("REST request to update VacationStock : {}", vacationStock);
        if (vacationStock.getId() == null) {
            return createVacationStock(vacationStock);
        }
        VacationStock result = vacationStockRepository.save(vacationStock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vacationStock", vacationStock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacation-stocks : get all the vacationStocks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vacationStocks in body
     */
    @RequestMapping(value = "/vacation-stocks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VacationStock> getAllVacationStocks() {
        log.debug("REST request to get all VacationStocks");
        List<VacationStock> vacationStocks = vacationStockRepository.findAll();
        return vacationStocks;
    }

    /**
     * GET  /vacation-stocks/:id : get the "id" vacationStock.
     *
     * @param id the id of the vacationStock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacationStock, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vacation-stocks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationStock> getVacationStock(@PathVariable Long id) {
        log.debug("REST request to get VacationStock : {}", id);
        VacationStock vacationStock = vacationStockRepository.findOne(id);
        return Optional.ofNullable(vacationStock)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vacation-stocks/:id : delete the "id" vacationStock.
     *
     * @param id the id of the vacationStock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vacation-stocks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVacationStock(@PathVariable Long id) {
        log.debug("REST request to delete VacationStock : {}", id);
        vacationStockRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vacationStock", id.toString())).build();
    }

}

package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.VacationRequest;
import com.mycompany.myapp.domain.VacationStock;
import com.mycompany.myapp.repository.VacationRequestRepository;
import com.mycompany.myapp.service.EmployeeService;
import com.mycompany.myapp.service.VacationRequestService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VacationRequest.
 */
@RestController
@RequestMapping("/api")
public class VacationRequestResource {

    private final Logger log = LoggerFactory.getLogger(VacationRequestResource.class);

    @Inject
    private VacationRequestRepository vacationRequestRepository;

    @Inject
    private VacationStockResource vacationStockResource;

    @Inject
    private VacationRequestService vacationRequestService;


    /**
     * POST  /vacation-requests : Create a new vacationRequest.
     *
     * @param vacationRequest the vacationRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacationRequest, or with status 400 (Bad Request) if the vacationRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacation-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationRequest> createVacationRequest(@RequestBody VacationRequest vacationRequest) throws URISyntaxException {
        log.debug("REST request to save VacationRequest : {}", vacationRequest);

        Employee employee = vacationRequest.getEmployee();
        VacationStock vacationStock = employee.getVacationStockId();
        BigDecimal freeDays = vacationStock.getFreeDays();

        BigDecimal vacationDays = vacationRequest.getVacationDays();
        if(vacationDays.intValue() > freeDays.intValue()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vacationRequest", "idexists", "Sorry, you have just " + freeDays + " days available!")).body(null);
        }


        if (vacationRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vacationRequest", "idexists", "A new vacationRequest cannot already have an ID")).body(null);
        }

        freeDays = BigDecimal.valueOf(freeDays.intValue() - vacationDays.intValue());
        vacationStock.setFreeDays(freeDays);

        vacationStockResource.updateVacationStock(vacationStock);


        VacationRequest result = vacationRequestRepository.save(vacationRequest);
        return ResponseEntity.created(new URI("/api/vacation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vacationRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vacation-requests : Updates an existing vacationRequest.
     *
     * @param vacationRequest the vacationRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacationRequest,
     * or with status 400 (Bad Request) if the vacationRequest is not valid,
     * or with status 500 (Internal Server Error) if the vacationRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacation-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationRequest> updateVacationRequest(@RequestBody VacationRequest vacationRequest) throws URISyntaxException {
        log.debug("REST request to update VacationRequest : {}", vacationRequest);
        if (vacationRequest.getId() == null) {
            return createVacationRequest(vacationRequest);
        }

        VacationRequest result = vacationRequestRepository.save(vacationRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vacationRequest", vacationRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacation-requests : get all the vacationRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vacationRequests in body
     */
    @RequestMapping(value = "/vacation-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VacationRequest> getAllVacationRequests() {
        log.debug("REST request to get all VacationRequests");
        List<VacationRequest> vacationRequests = vacationRequestRepository.findAll();
        return vacationRequests;
    }

    /**
     * GET  /vacation-requests/:id : get the "id" vacationRequest.
     *
     * @param id the id of the vacationRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacationRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vacation-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationRequest> getVacationRequest(@PathVariable Long id) {
        log.debug("REST request to get VacationRequest : {}", id);
        VacationRequest vacationRequest = vacationRequestRepository.findOne(id);
        return Optional.ofNullable(vacationRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vacation-requests/:id : delete the "id" vacationRequest.
     *
     * @param id the id of the vacationRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vacation-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVacationRequest(@PathVariable Long id) {
        log.debug("REST request to delete VacationRequest : {}", id);
        vacationRequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vacationRequest", id.toString())).build();
    }



}

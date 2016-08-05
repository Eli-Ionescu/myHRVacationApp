package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.VacationRequest;
import com.mycompany.myapp.repository.VacationRequestRepository;
import com.mycompany.myapp.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by Eli Ionescu on 8/4/2016.
 */
@Service
@Transactional
public class VacationRequestService {

    private final Logger log = LoggerFactory.getLogger(VacationRequestService.class);

    @Inject
    private VacationRequestRepository vacationRequestRepository;


    public VacationRequest save(VacationRequest vacationRequest){

        if(vacationRequest.getId() == null){
            // We create a new  vacation request
            vacationRequest.setStatus("pending");
        }

        if( SecurityUtils.isCurrentUserInRole("ROLE_MANAGER") == true) {

        }


        VacationRequest result = vacationRequestRepository.save(vacationRequest);
        return result;
    }

    public VacationRequest changeVacationRequest(Long vacationId, String status){
        VacationRequest vacationRequest = vacationRequestRepository.findOne(vacationId);
        vacationRequest.setStatus(status);

        vacationRequest = vacationRequestRepository.save(vacationRequest);
        return  vacationRequest;
    }






}

package com.neml.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;
import com.neml.service.SugarDataService;

@RestController
@RequestMapping("/api")
public class SugarDataController {

	@Autowired
	SugarDataService sugarDataService;

	static Logger log = LoggerFactory.getLogger("sugarController");

	@PostMapping("/sugarid")
	public ResponseEntity<List<SugarDataResponse>> SugarDetailsById(@RequestBody SugarDataRequest sugarDataRequest) {
		log.info("Received request to get sugar details by id: {}", sugarDataRequest);
		List<SugarDataResponse> response = new ArrayList<>();
		try {

			response = sugarDataService.getSugarDetailsByIdService(sugarDataRequest);

			if (response.size() > 0) {
				log.info("sugar details found: {}", response);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				log.info("No sugar details found for id: {}", sugarDataRequest);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error occurred while fetching sugar details: {}", e.getMessage());
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}




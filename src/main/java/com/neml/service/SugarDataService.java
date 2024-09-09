package com.neml.service;

import java.util.List;
import java.util.Optional;

import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;

public interface SugarDataService {


	List<SugarDataResponse> getSugarDetailsByIdService(SugarDataRequest sugarDataRequest);

}

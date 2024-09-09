package com.neml.dao;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;

import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;

public interface SugarDataDao {

	List<SugarDataResponse> getSugarDetailsById(SugarDataRequest sugarDataRequest, Logger log);






}

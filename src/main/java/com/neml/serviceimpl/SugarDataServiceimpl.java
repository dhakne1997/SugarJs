package com.neml.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neml.dao.SugarDataDao;
import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;
import com.neml.service.SugarDataService;
import com.neml.util.Util;

@Service
public class SugarDataServiceimpl implements SugarDataService {

	@Autowired
	SugarDataDao sugarDataDao;

	static Logger log = LoggerFactory.getLogger("SugarDetails");

	@Override
	public List<SugarDataResponse> getSugarDetailsByIdService(SugarDataRequest sugarDataRequest) {
		log.info("Getting sugar details by id: {}", sugarDataRequest);
		List<SugarDataResponse> res = new ArrayList<>();
		if (!Util.isNeitherNullNorEmpty(sugarDataRequest)) {
			log.debug("SugarDataRequest is not null or empty, proceeding to fetch sugar details");
			return res;
		}
		res = sugarDataDao.getSugarDetailsById(sugarDataRequest, log);

		return res;
	}
}

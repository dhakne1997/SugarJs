package com.neml.service;

import org.slf4j.Logger;

import com.neml.model.SugarDataRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PdfService {

	SugarDataRequest generatepdf(SugarDataRequest request, Logger log);


//	void downloadPdfFile(String filePath, HttpServletResponse response, HttpServletRequest request, Logger log);

}

package com.neml.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.neml.model.SugarDataRequest;
import com.neml.service.PdfService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "/Ncdfipdf")
public class PdfController {

    private static Logger log = LoggerFactory.getLogger("Ncdfipdf");

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "/generatePdf", method = RequestMethod.POST)
    public ResponseEntity<byte[]> pdfGenerate(@RequestBody SugarDataRequest request) throws IOException {
        SugarDataRequest pdfModelObj = pdfService.generatepdf(request, log);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfModelObj.getPdfPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Sugar_Auction_info.pdf");

        return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
    }
}
	




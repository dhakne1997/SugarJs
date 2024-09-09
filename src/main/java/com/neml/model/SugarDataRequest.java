package com.neml.model;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class SugarDataRequest  implements Serializable{
    private static final long serialVersionUID = 1L;

	public Date toDate;
	public Date fromDate;
	public String pdfPath;
	

}

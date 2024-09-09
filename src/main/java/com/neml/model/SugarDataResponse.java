package com.neml.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SugarDataResponse {

	
    private String sugarMillName;
    private double reservePrice;
    private String season;
    private String warehouse;
    private String auctionId;
    private int totalQuantityOffered;
    private double h1BidPrice;
    private int h1BidQuantity;
    private int quantityMatched;
    private String tradeNo;
//    private int obligationNo;
    private String buyerName;
    private double firstRoundBidPrice;
    private int firstRoundQuantity;
    private Timestamp firstRoundBidLogTime;
    private int quantityApprovedToBidder;
//    private int obligationQuantity;
    private int noOfBags;
    private String grade;
//    private int eachDOQuantity;
   


   	

}

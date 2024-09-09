package com.neml.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.neml.dao.SugarDataDao;
import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;
import com.neml.service.PdfService;
import com.neml.util.DbConnectionUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfServiceImpl implements PdfService {

	@Autowired
	DbConnectionUtil util;
	@Autowired
	SugarDataDao sugarDataDao;
	
	@Override
	public SugarDataRequest generatepdf(SugarDataRequest request, Logger log) {
		SugarDataRequest response = new SugarDataRequest();
		List<SugarDataResponse> sugarresp=new ArrayList<SugarDataResponse>();
		Connection conn = null;
		try {

			conn = util.getDBConnection("generatepdf", log);
			sugarresp = sugarDataDao.getSugarDetailsById(request, log);

			response.setPdfPath(initializePDF(sugarresp, request));

		} catch (Exception e) {
			log.error("Exception in  BrokerPdfServiceImpl------>>brokerpdf::" + e);

			e.printStackTrace();
		} finally {
			try {
				if (!conn.isClosed()) {
					util.closeConnection(conn, "", log);
					;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
		
		
		
		
		
		
		

	private String initializePDF(List<SugarDataResponse> pdfModelList, SugarDataRequest request) {
		Document document = null;
		PdfWriter writer = null;
		String filePath = "D:\\pdffiles\\";
		try {
			document = new Document(PageSize.LEDGER);
			filePath = filePath + "Sugar_Auction_info.pdf";
			writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
//		    writer.setPageEvent(new PdfPageEventHelperc()); // Add 
			document.open();
			
			 document.add(new Paragraph(" "));
			
			initializeSchemeBody(document, pdfModelList);
			return filePath;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(document))
				document.close();
			if (Objects.nonNull(writer))
				writer.close();
		}
		return null;

	}









	private void initializeSchemeBody(Document document, List<SugarDataResponse> pdfModelList) {
		try {
			Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10);
			PdfPTable table = new PdfPTable(17);
			PdfPCell headingCell = generateHeadingCell();
			List<PdfPCell> headingCells = generateTableHeader();
			table.setWidthPercentage(100);


			
			table.addCell(headingCell);
			for (PdfPCell cell : headingCells) {
				table.addCell(cell);
			}

			
			if (pdfModelList != null) {
			    for (SugarDataResponse pdfObj : pdfModelList) {
			        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define the date format

			    	table.addCell(new Paragraph(pdfObj.getSugarMillName(), font));
			    	table.addCell(new Paragraph(pdfObj.getSeason(),font));
			    	table.addCell(new Paragraph(pdfObj.getWarehouse(),font));
			    	table.addCell(new Paragraph(pdfObj.getAuctionId(),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getReservePrice()), font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getTotalQuantityOffered()),font));
			    	table.addCell(new Paragraph(pdfObj.getTradeNo(),font));
			    	table.addCell(new Paragraph(pdfObj.getBuyerName(), font));
			    	table.addCell(new Paragraph(pdfObj.getGrade(),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getFirstRoundBidPrice()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getFirstRoundQuantity()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getFirstRoundBidLogTime()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getH1BidQuantity()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getH1BidPrice()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getQuantityMatched()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getQuantityApprovedToBidder()),font));
			    	table.addCell(new Paragraph(String.valueOf(pdfObj.getNoOfBags()),font));
			    			    			
			        table.completeRow();
			    }
			} else {
			    // Handle the case where pdfModelList is null
			    // You can add a message or log an error, for example:
			    System.out.println("pdfModelList is null");
			}
			

			document.add(table);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}









	private List<PdfPCell> generateTableHeader() {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);

		Paragraph sugarMil = new Paragraph("MillName", font); //1
		sugarMil.setAlignment(Element.ALIGN_LEFT);
		PdfPCell sugarMilCell = new PdfPCell(sugarMil);
		sugarMilCell.setPaddingBottom(1f);
		sugarMilCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph season = new Paragraph("Season", font); //1
		season.setAlignment(Element.ALIGN_LEFT);
		PdfPCell SeasonCell = new PdfPCell(season);
		SeasonCell.setPaddingBottom(1f);
		SeasonCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph warehouse = new Paragraph("Warehouse", font); //1
		warehouse.setAlignment(Element.ALIGN_LEFT);
		PdfPCell warehouseCell = new PdfPCell(warehouse);
		warehouseCell.setPaddingBottom(1f);
		warehouseCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		Paragraph auctionId = new Paragraph("AuctionId", font); //1
		auctionId.setAlignment(Element.ALIGN_LEFT);
		PdfPCell auctionIdCell = new PdfPCell(auctionId);
		auctionIdCell.setPaddingBottom(1f);
		auctionIdCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		Paragraph ReservePrice = new Paragraph("ReservePrice", font); //1
		ReservePrice.setAlignment(Element.ALIGN_LEFT);
		PdfPCell ReservePriceCell = new PdfPCell(ReservePrice);
		ReservePriceCell.setPaddingBottom(1f);
		ReservePriceCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		Paragraph TotalQuantity = new Paragraph("QuantityOffered", font); //1
		TotalQuantity.setAlignment(Element.ALIGN_LEFT);
		PdfPCell TotalQuantityCell = new PdfPCell(TotalQuantity);
		TotalQuantityCell.setPaddingBottom(1f);
		TotalQuantityCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		Paragraph tradeNo = new Paragraph("TradeNo", font); //1
		tradeNo.setAlignment(Element.ALIGN_LEFT);
		PdfPCell tradeNoCell = new PdfPCell(tradeNo);
		tradeNoCell.setPaddingBottom(1f);
		tradeNoCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph buyerName = new Paragraph("BuyerName", font); //2
		buyerName.setAlignment(Element.ALIGN_LEFT);
		PdfPCell buyerNameCell = new PdfPCell(buyerName);
		buyerNameCell.setPaddingBottom(5f);
		buyerNameCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph grade = new Paragraph("Grade", font); //2
		grade.setAlignment(Element.ALIGN_LEFT);
		PdfPCell gradeCell = new PdfPCell(grade);
		gradeCell.setPaddingBottom(5f);
		gradeCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph firstroundprice = new Paragraph("1stRoundBidPrice", font); //2
		firstroundprice.setAlignment(Element.ALIGN_LEFT);
		PdfPCell firstroundpriceCell = new PdfPCell(firstroundprice);
		firstroundpriceCell.setPaddingBottom(5f);
		firstroundpriceCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph firstroundqty = new Paragraph("1stRoundQty", font); //2
		firstroundqty.setAlignment(Element.ALIGN_LEFT);
		PdfPCell firstroundqtyCell = new PdfPCell(firstroundqty);
		firstroundqtyCell.setPaddingBottom(5f);
		firstroundqtyCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph firstroundBidLogTime = new Paragraph("1stRoundBidTime", font); //2
		firstroundBidLogTime.setAlignment(Element.ALIGN_LEFT);
		PdfPCell firstroundBidLogTimeCell = new PdfPCell(firstroundBidLogTime);
		firstroundBidLogTimeCell.setPaddingBottom(5f);
		firstroundBidLogTimeCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph h1BidQty = new Paragraph("H1BidQty", font); //2
		h1BidQty.setAlignment(Element.ALIGN_LEFT);
		PdfPCell h1BidQtyCell = new PdfPCell(h1BidQty);
		h1BidQtyCell.setPaddingBottom(5f);
		h1BidQtyCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph h1Bidprice = new Paragraph("H1BidPrice", font); //2
		h1Bidprice.setAlignment(Element.ALIGN_LEFT);
		PdfPCell h1BidpriceCell = new PdfPCell(h1Bidprice);
		h1BidpriceCell.setPaddingBottom(5f);
		h1BidpriceCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph QuantityMat = new Paragraph("QtyMatched", font); //2
		QuantityMat.setAlignment(Element.ALIGN_LEFT);
		PdfPCell QuantityMatCell = new PdfPCell(QuantityMat);
		QuantityMatCell.setPaddingBottom(5f);
		QuantityMatCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		Paragraph QuantityApproved = new Paragraph("QtyApprovedBidder", font); //2
		QuantityApproved.setAlignment(Element.ALIGN_LEFT);
		PdfPCell QuantityApprovedCell = new PdfPCell(QuantityApproved);
		QuantityApprovedCell.setPaddingBottom(5f);
		QuantityApprovedCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		Paragraph bags = new Paragraph("NoOfBags", font); //2
		bags.setAlignment(Element.ALIGN_LEFT);
		PdfPCell bagsCell = new PdfPCell(bags);
		bagsCell.setPaddingBottom(5f);
		bagsCell.setBackgroundColor(new BaseColor(237, 203, 123));
		
		
		return Arrays.asList(
				sugarMilCell,
				SeasonCell,
				warehouseCell,
				auctionIdCell,
				ReservePriceCell,
				TotalQuantityCell,
				tradeNoCell,
				buyerNameCell,
				gradeCell,
				firstroundpriceCell,
				firstroundqtyCell,
				firstroundBidLogTimeCell,
				h1BidQtyCell,
				h1BidpriceCell,
				QuantityMatCell,
				QuantityApprovedCell,
				bagsCell);
	}









	private PdfPCell generateHeadingCell() {
Font font = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
		
		Paragraph schemeHeading = new Paragraph("SugarReport ", font);

		schemeHeading.setAlignment(Element.ALIGN_CENTER);
		PdfPCell headingCell = new PdfPCell(schemeHeading);
		headingCell.setPaddingRight(50f);
		headingCell.setPaddingBottom(5f);
		headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headingCell.setBackgroundColor(new BaseColor(94, 224, 205));
		headingCell.setColspan(17);
		
		
		
		
		return headingCell;
	}








//	@Override
//	 public void downloadPdfFile(String filePath, HttpServletResponse response, HttpServletRequest request, Logger log) {
//        ServletOutputStream outStream = null;
//        FileInputStream inStream = null;
//
//        try {
//            File downloadFile = new File(filePath);
//            if (!downloadFile.exists()) {
//                log.error("File not found: " + filePath);
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                return;
//            }
//
//            inStream = new FileInputStream(downloadFile);
//            ServletContext context = request.getServletContext();
//            String mimeType = context.getMimeType(filePath);
//            log.info("MIME type: " + mimeType);
//            if (mimeType == null) {
//                mimeType = "application/octet-stream";
//            }
//
//            response.setContentType(mimeType);
//            response.setContentLength((int) downloadFile.length());
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
//
//            outStream = response.getOutputStream();
//            byte[] buffer = new byte[1024 * 1024];
//            int bytesRead;
//            while ((bytesRead = inStream.read(buffer)) != -1) {
//                outStream.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            log.error("Exception occurred while downloading file", e);
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        } finally {
//            try {
//                if (inStream != null) {
//                    inStream.close();
//                }
//                if (outStream != null) {
//                    outStream.flush();
//                    outStream.close();
//                }
//            } catch (IOException e) {
//                log.error("Error closing streams", e);
//            }
//        }
//    }
}



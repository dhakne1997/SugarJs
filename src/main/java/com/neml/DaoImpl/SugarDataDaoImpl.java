package com.neml.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.neml.config.HikariConfigs;
import com.neml.dao.SugarDataDao;
import com.neml.model.SugarDataRequest;
import com.neml.model.SugarDataResponse;

@Repository
public class SugarDataDaoImpl implements SugarDataDao {

	@Autowired

	HikariConfigs configs;

	@Override
	public List<SugarDataResponse> getSugarDetailsById(SugarDataRequest sugarDataRequest, Logger log) {
		Connection connection = null;
		PreparedStatement stmt = null;
		String query = null;
		ResultSet rs = null;
		List<SugarDataResponse> res = new ArrayList<>();
		try {
			query = "SELECT " + "rm.org_nm AS Name_of_Sugar_mill, " + "fos.ord_paramvalue8 AS Season, "
					+ "fos.ord_paramvalue5 AS Warehouse, " + "fos.ord_unique_number AS Auction_ID, "
					+ "COALESCE(fos.ord_ord_price, 0) AS Reserve_Price_offered_by_mill, "
					+ "COALESCE(fos.ord_ord_qty, 0) AS Total_Qty_Offered, " + "tm.execution_id AS trade_no, "
					+ "tm1.tm_name AS name_of_buyer, " + "fos.ord_paramvalue6 AS grade, "
					+ "COALESCE(fob.ord_ord_price, 0) AS Bid_price_in_1st_round, "
					+ "COALESCE(fob.ord_ord_qty, 0) AS Qty_in_1st_round, "
					+ "fob.ord_ord_exch_dt AS Bid_log_time_of_1st_round, "
					+ "COALESCE(fob.ord_super_h1matchqty, 0) AS H1_Bid_qty, "
					+ "COALESCE(fob.ord_super_h1price, 0) AS H1_bid_price, "
					+ "COALESCE(ptm.conf_qty, 0) AS Qty_Matched, "
					+ "COALESCE(tm.last_fill_qty, 0) AS Qty_approved_to_bidder, "
					+ "COALESCE(tm.last_fill_qty*2, 0) AS No_of_Bags " + "FROM fo_orders fos " + "INNER JOIN ( "
					+ "    SELECT * " + "    FROM fo_orders " + "    WHERE ord_ord_type = 'AB' "
					+ "    AND ord_status IN('FILL', 'PFILL') "
					+ ") fob ON fos.ord_unique_number = fob.ord_unique_number "
					+ "LEFT JOIN registration_mstr rm ON fos.ord_tm_id = rm.org_id " + "LEFT JOIN ( "
					+ "    SELECT tm_name, tm_id " + "    FROM tm_mstr " + ") tm1 ON tm1.tm_id = fob.ord_tm_id "
					+ "INNER JOIN trade_mstr tm ON fob.ord_exch_ord_no = tm.order_id "
					+ "INNER JOIN provisional_trade_mstr ptm ON fob.ord_exch_ord_no = ptm.order_id "
					+ "WHERE fos.ord_ord_type = 'AO' "
					+ "AND fob.ord_ord_exch_dt >= ? AND fob.ord_ord_exch_dt <=? order by fob.ord_ord_exch_dt asc;";

			log.info("query: " + query);

			connection = configs.dataSource().getConnection();
			stmt = connection.prepareStatement(query);

			stmt.setDate(1, sugarDataRequest.getFromDate());
			stmt.setDate(2, sugarDataRequest.getToDate());

			rs = stmt.executeQuery();

			while (rs.next()) {
				SugarDataResponse sugar = new SugarDataResponse();
				sugar.setSugarMillName(rs.getString("Name_of_Sugar_mill"));
				sugar.setReservePrice(rs.getDouble("Reserve_Price_offered_by_mill"));
				sugar.setSeason(rs.getString("Season"));
				sugar.setWarehouse(rs.getString("Warehouse"));
				sugar.setAuctionId(rs.getString("Auction_ID"));
				sugar.setTotalQuantityOffered(rs.getInt("Total_Qty_Offered"));
				sugar.setH1BidPrice(rs.getDouble("H1_bid_price"));
				sugar.setH1BidQuantity(rs.getInt("H1_Bid_qty"));
				sugar.setQuantityMatched(rs.getInt("Qty_Matched"));
				sugar.setTradeNo(rs.getString("trade_no"));
				sugar.setBuyerName(rs.getString("name_of_buyer"));
				sugar.setFirstRoundBidPrice(rs.getDouble("Bid_price_in_1st_round"));
				sugar.setFirstRoundQuantity(rs.getInt("Qty_in_1st_round"));
				sugar.setFirstRoundBidLogTime(rs.getTimestamp("Bid_log_time_of_1st_round"));
				sugar.setQuantityApprovedToBidder(rs.getInt("Qty_approved_to_bidder"));
				sugar.setNoOfBags(rs.getInt("No_of_Bags"));
				sugar.setGrade(rs.getString("grade"));
				res.add(sugar);
			}
		} catch (SQLException e) {
			log.error("Error executing query", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Error closing resources", e);
			}
		}
		return res;
	}



	
}

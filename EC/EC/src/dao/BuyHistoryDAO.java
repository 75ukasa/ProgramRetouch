package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import base.DBManager;
import beans.ItemDataBeans;

public class BuyHistoryDAO {


	public static ArrayList<ItemDataBeans> getBuyHistory(int buyId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT * FROM t_buy"
				   + " JOIN t_buy_detail"
				   + " ON t_buy_detail.buy_id = t_buy.id"
				   + " JOIN m_item"
				   + " ON t_buy_detail.item_id = m_item.id"
				   + " WHERE t_buy.id = ? ");

			st.setInt(1, buyId);

			ResultSet rs = st.executeQuery();
			ArrayList<ItemDataBeans> BuyItemList = new ArrayList<ItemDataBeans>();

			while (rs.next()) {
				ItemDataBeans idb = new ItemDataBeans();
				idb.setId(rs.getInt("id"));
				idb.setPrice(rs.getInt("price"));
				idb.setName(rs.getString("name"));
				BuyItemList.add(idb);
			}

			System.out.println("searching BuyDataBeans by buyID has been completed");

			return  BuyItemList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

}


package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import base.DBManager;
import beans.BuyDataBeans;
import beans.ItemDataBeans;

/**
 *
 * @author d-yamaguchi
 *
 */
public class BuyDAO {


	/**
	 * 購入情報登録処理
	 * @param bdb 購入情報
	 * @throws SQLException 呼び出し元にスローさせるため
	 */
	public static int insertBuy(BuyDataBeans bdb) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		int autoIncKey = -1;
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement(
					"INSERT INTO t_buy(user_id,total_price,delivery_method_id,create_date) VALUES(?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, bdb.getUserId());
			st.setInt(2, bdb.getTotalPrice());
			st.setInt(3, bdb.getDelivertMethodId());
			st.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				autoIncKey = rs.getInt(1);
			}
			System.out.println("inserting buy-datas has been completed");

			return autoIncKey;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * 購入IDによる購入情報検索
	 * @param buyId
	 * @return BuyDataBeans
	 * 				購入情報のデータを持つJavaBeansのリスト
	 * @throws SQLException
	 * 				呼び出し元にスローさせるため
	 */
	public static BuyDataBeans getBuyDataBeansByBuyId(int buyId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT * FROM t_buy"
							+ " JOIN m_delivery_method"
							+ " ON t_buy.delivery_method_id = m_delivery_method.id"
							+ " WHERE t_buy.id = ?");
			st.setInt(1, buyId);

			ResultSet rs = st.executeQuery();

			BuyDataBeans bdb = new BuyDataBeans();
			if (rs.next()) {
				bdb.setId(rs.getInt("id"));
				bdb.setTotalPrice(rs.getInt("total_price"));
				bdb.setBuyDate(rs.getTimestamp("create_date"));
				bdb.setDelivertMethodId(rs.getInt("delivery_method_id"));
				bdb.setUserId(rs.getInt("user_id"));
				bdb.setDeliveryMethodPrice(rs.getInt("price"));
				bdb.setDeliveryMethodName(rs.getString("name"));
			}

			System.out.println("searching BuyDataBeans by buyID has been completed");

			return bdb;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}





	public static List<BuyDataBeans> buydata(int userId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement("SELECT * FROM t_buy"
							+ " JOIN m_delivery_method"
							+ " ON t_buy.delivery_method_id = m_delivery_method.id"
							+ " WHERE t_buy.user_id = ?"
							+ " ORDER BY create_date DESC");

			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();

			List<BuyDataBeans> BuyDataList = new ArrayList<BuyDataBeans>();

			while (rs.next()) {
				BuyDataBeans bdb = new BuyDataBeans();
				bdb.setId(rs.getInt("id"));
				bdb.setBuyDate(rs.getTimestamp("create_date"));
				bdb.setDeliveryMethodPrice(rs.getInt("m_delivery_Method.Price"));
				bdb.setDeliveryMethodName(rs.getString("m_delivery_Method.Name"));
				bdb.setTotalPrice(rs.getInt("total_Price"));
				BuyDataList.add(bdb);
			}
			System.out.println("searching all DeliveryMethodDataBeans has been completed");

			return BuyDataList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}

	}


	public static BuyDataBeans buyDetail(String t_buyId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT * FROM t_buy"
							+ " JOIN m_delivery_method"
							+ " ON t_buy.delivery_method_id = m_delivery_method.id"
							+ " WHERE t_buy.id = ?");
			st.setString(1, t_buyId);

			ResultSet rs = st.executeQuery();

			BuyDataBeans bdb = new BuyDataBeans();
			if (rs.next()) {
				bdb.setId(rs.getInt("id"));
				bdb.setTotalPrice(rs.getInt("total_price"));
				bdb.setBuyDate(rs.getTimestamp("create_date"));
				bdb.setDelivertMethodId(rs.getInt("delivery_method_id"));
				bdb.setDeliveryMethodPrice(rs.getInt("price"));
				bdb.setDeliveryMethodName(rs.getString("name"));
			}

			System.out.println("searching BuyDataBeans by buyID has been completed");

			return bdb;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}
	public static ArrayList<ItemDataBeans> buyItem(String t_buyId) throws SQLException {
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
					   + " WHERE t_buy.id = ?");
			st.setString(1, t_buyId);

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

			return BuyItemList;
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

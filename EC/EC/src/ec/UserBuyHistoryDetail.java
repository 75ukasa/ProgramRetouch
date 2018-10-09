package ec;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BuyDataBeans;
import beans.ItemDataBeans;
import dao.BuyDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Date = request.getParameter("id");

		BuyDAO buyDao = new BuyDAO();
		try {
			BuyDataBeans buyDetail = buyDao.buyDetail(Date);
			ArrayList<ItemDataBeans> buyItem = buyDao.buyItem(Date);


			request.setAttribute("BuyDetail",buyDetail);
			request.setAttribute("BuyItemList",buyItem);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/userbuyhistorydetail.jsp");
			dispatcher.forward(request, response);


		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


	}
}

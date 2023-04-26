package Servlet;

	import java.io.IOException;
	import java.sql.Connection;
	import java.sql.SQLException;

	import jakarta.servlet.RequestDispatcher;
	import jakarta.servlet.ServletException;
	import jakarta.servlet.annotation.WebServlet;
	import jakarta.servlet.http.HttpServlet;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;
	
	import Beans.UserAccount;
	import Utils.DBUtils;
	import Utils.MyStoreUtils;

	@WebServlet(urlPatterns = { "/createUser" })
	public class CreateUserServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public CreateUserServlet() {
			super();
		}

		// Show product creation page.
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/createUserView.jsp");
			dispatcher.forward(request, response);
		}

		// When the user enters the product information, and click Submit.
		// This method will be called.
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Connection conn = MyStoreUtils.getStoredConnection(request);

			String userName = (String) request.getParameter("userName");
			String gender = (String) request.getParameter("gender");
			String password = (String) request.getParameter("password");;
			// todo select list
			
			UserAccount user = new UserAccount(userName, gender, password);

			String errorString = null;

			// Product ID is the string literal [a-zA-Z_0-9]
			// with at least 1 character
			String regex = "\\w+";

			//if (code == null || !code.matches(regex)) {
				//errorString = "Product Code invalid!";
			//}

			if (errorString == null) {
				try {
					DBUtils.insertProduct(conn, user);
				} catch (SQLException e) {
					e.printStackTrace();
					errorString = e.getMessage();
				}
			}

			// Store information to request attribute, before forward to views.
			request.setAttribute("errorString", errorString);
			request.setAttribute("product", product);

			// If error, forward to Edit page.
			if (errorString != null) {
				RequestDispatcher dispatcher = request.getServletContext()
						.getRequestDispatcher("/WEB-INF/views/createUserView.jsp");
				dispatcher.forward(request, response);
			}
			// If everything nice.
			// Redirect to the product listing page.
			else {
				response.sendRedirect(request.getContextPath() + "/userInfo");
			}
		}
	}
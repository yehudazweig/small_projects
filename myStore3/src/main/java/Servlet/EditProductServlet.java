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
	
	import Beans.Product;
	import Utils.DBUtils;
	import Utils.MyStoreUtils;

	@WebServlet(urlPatterns = { "/editProduct" })
	public class EditProductServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public EditProductServlet() {
			super();
		}

		// Show product edit page.
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Connection conn = MyStoreUtils.getStoredConnection(request);

			String code = (String) request.getParameter("code");

			Product product = null;

			String errorString = null;

			try {
				product = DBUtils.findProduct(conn, code);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}

			// If no error.
			// The product does not exist to edit.
			// Redirect to productList page.
			if (errorString != null && product == null) {
				response.sendRedirect(request.getServletPath() + "/productList");
				return;
			}

			// Store errorString in request attribute, before forward to views.
			request.setAttribute("errorString", errorString);
			request.setAttribute("product", product);

			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
			dispatcher.forward(request, response);

		}

		// After the user modifies the product information, and click Submit.
		// This method will be executed.
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Connection conn = MyStoreUtils.getStoredConnection(request);

			String code = (String) request.getParameter("code");
			String name = (String) request.getParameter("name");
			String priceStr = (String) request.getParameter("price");
			float price = 0;
			try {
				price = Float.parseFloat(priceStr);
			} catch (Exception e) {
			}
			Product product = new Product(code, name, price);

			String errorString = null;

			try {
				DBUtils.updateProduct(conn, product);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
			// Store information to request attribute, before forward to views.
			request.setAttribute("errorString", errorString);
			request.setAttribute("product", product);

			// If error, forward to Edit page.
			if (errorString != null) {
				RequestDispatcher dispatcher = request.getServletContext()
						.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
				dispatcher.forward(request, response);
			}
			// If everything nice.
			// Redirect to the product listing page.
			else {
				response.sendRedirect(request.getContextPath() + "/productList");
			}
		}

	}
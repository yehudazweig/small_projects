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
	
	import Utils.DBUtils;
	import Utils.MyStoreUtils;


	@WebServlet(urlPatterns = { "/deleteProduct" })
	public class DeleteProductServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public DeleteProductServlet() {
			super();
		}

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Connection conn = MyStoreUtils.getStoredConnection(request);

			String code = (String) request.getParameter("code");

			String errorString = null;

			try {
				DBUtils.deleteProduct(conn, code);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			} 
			
			// If has an error, redirect to the error page.
			if (errorString != null) {
				// Store the information in the request attribute, before forward to views.
				request.setAttribute("errorString", errorString);
				// 
				RequestDispatcher dispatcher = request.getServletContext()
						.getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
				dispatcher.forward(request, response);
			}
			// If everything nice.
			// Redirect to the product listing page.		
			else {
				response.sendRedirect(request.getContextPath() + "/productList");
			}

		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
		}

	}
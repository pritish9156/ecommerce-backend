package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

	private static final String UPLOAD_ROOT = "E:" + File.separator + "ShopSphereUploads";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		File file = new File(UPLOAD_ROOT, pathInfo);

		if (!file.exists() || !file.isFile()) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		String contentType = getServletContext().getMimeType(file.getName());

		if (contentType == null) {

			contentType = "application/octet-stream";
		}

		response.setContentType(contentType);

		response.setContentLengthLong(file.length());

		Files.copy(file.toPath(), response.getOutputStream());
	}
}
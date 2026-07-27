package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ApiResponse;
import dto.UploadResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/upload/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 20)
public class UploadServlet extends HttpServlet {

	private ObjectMapper objectMapper;

	private static final String UPLOAD_ROOT = "E:" + File.separator + "ShopSphereUploads";

	@Override
	public void init() {

		objectMapper = new ObjectMapper();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		switch (path) {

		case "/brand":

			uploadBrandImage(request, response);

			break;

		case "/product":

			uploadProductImage(request, response);

			break;

		default:

			response.sendError(404);
		}
	}

	private void uploadBrandImage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Part part = request.getPart("image");

		String fileName = UUID.randomUUID() + "_" + Paths.get(part.getSubmittedFileName()).getFileName().toString();

//		String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator
//				+ "brands";

		String uploadPath = UPLOAD_ROOT + File.separator + "brands";

		File directory = new File(uploadPath);

		if (!directory.exists()) {

			directory.mkdirs();
		}

		part.write(uploadPath + File.separator + fileName);

		String imageUrl = "/uploads/brands/" + fileName;

		response.setContentType("application/json");

		UploadResponse uploadResponse = new UploadResponse(true, imageUrl);

		objectMapper.writeValue(response.getWriter(), uploadResponse);
	}

	private void uploadProductImage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Part part = request.getPart("image");

		String fileName = UUID.randomUUID() + "_" + Paths.get(part.getSubmittedFileName()).getFileName().toString();

//		String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator
//				+ "products";

		String uploadPath = UPLOAD_ROOT + File.separator + "products";

		File directory = new File(uploadPath);

		if (!directory.exists()) {

			directory.mkdirs();
		}

		part.write(uploadPath + File.separator + fileName);

		String imageUrl = "/uploads/products/" + fileName;

		response.setContentType("application/json");

		UploadResponse uploadResponse = new UploadResponse(true, imageUrl);

		objectMapper.writeValue(response.getWriter(), uploadResponse);
	}
}
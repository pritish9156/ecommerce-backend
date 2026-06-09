package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.AddressRequestDTO;
import dto.ApiResponse;
import entity.Address;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AddressService;

@WebServlet("/address/*")
public class AddressServlet extends HttpServlet {

	private AddressService addressService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		addressService = new AddressService();
		objectMapper = new ObjectMapper();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		AddressRequestDTO dto = objectMapper.readValue(request.getInputStream(), AddressRequestDTO.class);

		ApiResponse apiResponse = addressService.addAddress(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		List<Address> addresses = addressService.getUserAddresses(email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), addresses);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		AddressRequestDTO dto = objectMapper.readValue(request.getInputStream(), AddressRequestDTO.class);

		ApiResponse apiResponse = addressService.updateAddress(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address ID is required");

			return;
		}

		Long addressId = Long.parseLong(pathInfo.substring(1));

		String email = (String) request.getAttribute("email");

		ApiResponse apiResponse = addressService.deleteAddress(addressId, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}
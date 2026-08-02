package service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.request.ReviewAIRequestDTO;
import dto.response.ApiResponse;
import dto.response.ReviewAIResponseDTO;

public class ReviewAIService {

	private static Properties properties;

	private final ObjectMapper objectMapper;
	private final HttpClient httpClient;

	static {

		InputStream inputStream = ReviewAIService.class.getClassLoader().getResourceAsStream("application.properties");

		if (inputStream == null) {

			throw new RuntimeException("Unable to find application.properties");
		}

		properties = new Properties();

		try {

			properties.load(inputStream);

		} catch (IOException e) {

			throw new RuntimeException("Unable to load application.properties", e);
		}
	}

	public ReviewAIService() {

		objectMapper = new ObjectMapper();

		httpClient = HttpClient.newHttpClient();
	}

	public ApiResponse improveReview(ReviewAIRequestDTO dto) {

		if (dto.getReviewText() == null || dto.getReviewText().trim().isEmpty()) {

			return new ApiResponse(false, "Please write a review before using AI.");
		}

		try {

			String prompt = """

					You are helping a customer improve a product review.

					Improve the customer's review while preserving
					their original opinion and experience.

					RULES:

					- Fix grammar and spelling.
					- Make the review natural and clear.
					- Do not change the customer's opinion.
					- Do not invent product features.
					- Do not invent experiences.
					- Do not exaggerate.
					- Keep the review concise.
					- Create a suitable short review title.
					- Return ONLY valid JSON.
					- Do not include markdown or code blocks.

					Return exactly this format:

					{
					  "reviewTitle": "...",
					  "reviewText": "..."
					}

					Current Review Title:
					%s

					Current Review:
					%s

					""".formatted(dto.getReviewTitle() == null ? "" : dto.getReviewTitle(),

					dto.getReviewText());

			Map<String, Object> body = Map.of(

					"model", "openrouter/free",

					"messages", List.of(

							Map.of("role", "user",

									"content", prompt)));

			String requestBody = objectMapper.writeValueAsString(body);

			String apiKey = System.getenv("OPENROUTER_API_KEY");

			if (apiKey == null || apiKey.isBlank()) {
			    throw new RuntimeException(
			        "OPENROUTER_API_KEY environment variable is not configured."
			    );
			}

			HttpRequest request = HttpRequest.newBuilder()

					.uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))

					.header("Authorization", "Bearer " + apiKey)

					.header("Content-Type", "application/json")

					.header("HTTP-Referer", "http://localhost:5173")

					.header("X-Title", "ShopVerse")

					.POST(HttpRequest.BodyPublishers.ofString(requestBody))

					.build();

			HttpResponse<String> response = httpClient.send(

					request,

					HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() < 200 || response.statusCode() >= 300) {

				System.err.println("OpenRouter Error: " + response.body());

				return new ApiResponse(false, "Unable to improve review.");
			}

			Map<?, ?> responseBody = objectMapper.readValue(response.body(), Map.class);

			List<?> choices = (List<?>) responseBody.get("choices");

			Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);

			Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

			String content = (String) message.get("content");

			int start = content.indexOf("{");

			int end = content.lastIndexOf("}");

			if (start == -1 || end == -1) {

				return new ApiResponse(false, "AI returned an invalid response.");
			}

			String json = content.substring(start, end + 1);

			ReviewAIResponseDTO aiResponse = objectMapper.readValue(json, ReviewAIResponseDTO.class);

			return new ApiResponse(true, "Review improved successfully.", aiResponse);

		} catch (Exception e) {

			e.printStackTrace();

			return new ApiResponse(false, "Unable to improve review.");
		}
	}
}
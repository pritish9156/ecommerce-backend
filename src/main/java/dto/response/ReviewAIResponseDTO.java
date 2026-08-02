package dto.response;

public class ReviewAIResponseDTO {

    private String reviewTitle;
    private String reviewText;

    public ReviewAIResponseDTO() {
    }

    public ReviewAIResponseDTO(
            String reviewTitle,
            String reviewText) {

        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
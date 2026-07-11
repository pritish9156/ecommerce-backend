package dto;

public class UploadResponse {

    private boolean success;

    private String imageUrl;

    public UploadResponse(
            boolean success,
            String imageUrl) {

        this.success = success;
        this.imageUrl = imageUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
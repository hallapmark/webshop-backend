package ee.markh.webshopbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final String cloudfrontBaseUrl;

    public ImageService(@Value("${cloudfront.baseUrl:}") String cloudfrontBaseUrl) {
        this.cloudfrontBaseUrl = cloudfrontBaseUrl;
    }

    /**
     * Generates the full CloudFront URL for a product image based on its slug.
     * Returns null if CloudFront is not configured.
     * Note: The backend does not verify if the image exists. If the image is missing,
     * the frontend should use a bundled placeholder image as a fallback (via img onerror handler).
     */
    public String getImageUrl(String slug) {
        if (cloudfrontBaseUrl == null || cloudfrontBaseUrl.isBlank()) {
            return null;
        }
        return cloudfrontBaseUrl + "/images/" + slug + ".webp";
    }
}

package util;

import java.security.SecureRandom;

public class PNRGenerator {
    private static final String ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
    private static final int DEFAULT_PNR_LENGTH = 6;
    private final SecureRandom random;
    
    public PNRGenerator() {
        this.random = new SecureRandom();
    }
    
    public String generatePNR() {
        StringBuilder sb = new StringBuilder(DEFAULT_PNR_LENGTH);
        for (int i = 0; i < DEFAULT_PNR_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
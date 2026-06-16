package projekt.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {
    private static final String FILE_NAME = "max_threads.txt";

    // Hard, structural limits defined by the application requirements blueprint
    private static final int HARD_LIMIT_NON_VIPS = 44;
    private static final int HARD_LIMIT_VIPS = 22;

    private int maxNonVips = HARD_LIMIT_NON_VIPS; // Default fallback if file is unreadable or has too many therads
    private int maxVips = HARD_LIMIT_VIPS;

    public ConfigReader() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        File configFile = new File(FILE_NAME);

        if (!configFile.exists()) {
            System.out.println("[Config] '" + FILE_NAME + "' missing. Defaulting to full bounds (44/22).");
            return;
        }

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);

            // 1. Process Non-VIP setup configuration parameters
            String nonVipStr = props.getProperty("max_non_vips");
            if (nonVipStr != null) {
                int parsed = Integer.parseInt(nonVipStr.trim());
                // wymusza aby wartosc byla pomiedzy dwoma limitami
                this.maxNonVips = Math.clamp(parsed, 0, HARD_LIMIT_NON_VIPS);
            }

            String vipStr = props.getProperty("max_vips");
            if (vipStr != null) {
                int parsed = Integer.parseInt(vipStr.trim());
                this.maxVips = Math.clamp(parsed, 0, HARD_LIMIT_VIPS);
            }
            System.out.println("[Config] Initialized bounds successfully. Non-VIP Limit: "
                    + maxNonVips + ", VIP Limit: " + maxVips);
        } catch (IOException | NumberFormatException e) {
            System.err.println("[Config] Error processing file properties. Enforcing safe fallbacks (44/22).");
            this.maxNonVips = HARD_LIMIT_NON_VIPS;
            this.maxVips = HARD_LIMIT_VIPS;
        }
    }

    public int getMaxNonVips() {
        return maxNonVips;
    }

    public int getMaxVips() {
        return maxVips;
    }
}
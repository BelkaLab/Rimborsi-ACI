package belka.us.acirefund.utils;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by fabriziorizzonelli on 03/10/2016.
 */

public class Config {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("google");
    private static final ResourceBundle googleSecretresourceBundle = ResourceBundle.getBundle("googleapi");

    public static final String SHEET_ID = resourceBundle.getString("sheetId");
    public static final String BRAND_RANGE = resourceBundle.getString("brandRange");
    public static final String BELKA_URL = resourceBundle.getString("belkaUrl");
    public static final String API_KEY= googleSecretresourceBundle.getString("apiKey");

    private static final String REFUND_SHEET_RANGE = resourceBundle.getString("refundSheetRange");
    private static final String BENZINA_SHEET_NAME = resourceBundle.getString("benzinaSheetName");
    private static final String GASOLIO_SHEET_NAME = resourceBundle.getString("gasolioSheetName");
    private static final String ELETTRICO_SHEET_NAME = resourceBundle.getString("elettricoSheetName");
    private static final String GPL_SHEET_NAME = resourceBundle.getString("gplSheetName");

    public static String getRefundByBrandAndByFuelRange(String fuelType) {
        switch (fuelType) {
            default:
                return REFUND_SHEET_RANGE;
            case "BENZINA":
                return concatWorkSheetAndRange(BENZINA_SHEET_NAME, REFUND_SHEET_RANGE);
            case "GASOLIO":
                return concatWorkSheetAndRange(GASOLIO_SHEET_NAME, REFUND_SHEET_RANGE);
            case "ELETTRICO-IBRIDO":
                return concatWorkSheetAndRange(ELETTRICO_SHEET_NAME, REFUND_SHEET_RANGE);
            case "GPL-METANO":
                return concatWorkSheetAndRange(GPL_SHEET_NAME, REFUND_SHEET_RANGE);
        }
    }

    public static List<String> getRefundByBrandRanges() {
        return Lists.newArrayList(concatWorkSheetAndRange(BENZINA_SHEET_NAME, REFUND_SHEET_RANGE), concatWorkSheetAndRange(GASOLIO_SHEET_NAME, REFUND_SHEET_RANGE),
                concatWorkSheetAndRange(ELETTRICO_SHEET_NAME, REFUND_SHEET_RANGE), concatWorkSheetAndRange(GPL_SHEET_NAME, REFUND_SHEET_RANGE));
    }

    public static List<String> getBrandRanges() {
        return Lists.newArrayList(concatWorkSheetAndRange(BENZINA_SHEET_NAME, BRAND_RANGE), concatWorkSheetAndRange(GASOLIO_SHEET_NAME, BRAND_RANGE),
                concatWorkSheetAndRange(ELETTRICO_SHEET_NAME, BRAND_RANGE), concatWorkSheetAndRange(GPL_SHEET_NAME, BRAND_RANGE));
    }

    private static String concatWorkSheetAndRange(String workSheet, String range) {
        return workSheet.concat("!").concat(range);
    }
}
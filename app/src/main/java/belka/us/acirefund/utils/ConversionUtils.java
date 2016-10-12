package belka.us.acirefund.utils;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.collect.Lists.transform;

/**
 * Created by fabriziorizzonelli on 30/09/2016.
 */

public class ConversionUtils {

    public static double parseToDouble(String number) throws ParseException {
        return getLocalizedDecimalFormat().parse(number).doubleValue();
    }

    public static String concactList(String... elements) {
        return Joiner.on(" ").join(Lists.newArrayList(elements));
    }

    public static <T> List<T> removeDuplicate(final List<T> list) {
        return Lists.newArrayList(Sets.newLinkedHashSet(list));
    }

    public static List<List<Object>> convertBatchGetToList(BatchGetValuesResponse response) {
        return Lists.newArrayList(Iterables.concat(transform(response.getValueRanges(), new Function<ValueRange, List<List<Object>>>() {
            @Nullable
            @Override
            public List<List<Object>> apply(@Nullable ValueRange input) {
                return input.getValues();
            }
        })));
    }

    private static DecimalFormat getLocalizedDecimalFormat() {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator(',');
        formatSymbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(formatSymbols);

        return df;
    }
}

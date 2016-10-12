package belka.us.acirefund.model.rest;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by fabriziorizzonelli on 10/10/2016.
 */

public interface BrandApi {

    @GET
    Observable<BatchGetValuesResponse> getDistinctBrand(@Url String url, @Query("ranges") List<String> ranges, @Query("key") String apiKey);
}

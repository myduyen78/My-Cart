package hanu.a2_2001040051;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {
    @GET("mpr-cart-api/products.json")
    Call<List<Product>> getProducts();
}

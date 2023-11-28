package hanu.a2_2001040051;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    public static void loadImage(String path, ImageView imageView) {
        Executors.newSingleThreadExecutor().execute(() -> {
                    RequestCreator creator = Picasso.get().load(path);

                    new Handler(Looper.getMainLooper()).post(() -> creator.into(imageView));
                }
        );
    }

    public static void execute(Consumer<Void> function0) {
        Executors.newSingleThreadExecutor().execute(() -> function0.accept(null));
    }
    public static void onUIThread(Consumer<Void> function0) {
        new Handler(Looper.getMainLooper()).post(() -> function0.accept(null));
    }

    public static List<Product> getApiProductList() {
        try {
            return new Retrofit.Builder().baseUrl("https://hanu-congnv.github.io/").addConverterFactory(GsonConverterFactory.create()).build().create(ProductAPI.class).getProducts().execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

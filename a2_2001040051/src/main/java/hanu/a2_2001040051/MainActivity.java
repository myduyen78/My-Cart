package hanu.a2_2001040051;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import hanu.a2_2001040051.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CartDb db;
    Cart cart;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        db = Room.databaseBuilder(this, CartDb.class, "cartdb").fallbackToDestructiveMigration().build();
        cart = Cart.instance(db.cartDAO());
        adapter = new ProductAdapter(this);

        initProduct();
        initSearch();
    }

    private void initSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    private void initProduct() {
        Utils.execute((a)  -> {
            List<Product> productCart = db.cartDAO().getProducts();
            cart.submitList(productCart);

            List<Product> productApi = Utils.getApiProductList();
            productCart.forEach(product -> {
                if (productApi.contains(product)) {
                    productApi.set(productApi.indexOf(product), product);
                }
            });
            adapter.submitList(productApi);

            Utils.onUIThread((__)  -> {
                binding.productRecycler.setAdapter(adapter);
                binding.loadingProgress.setVisibility(View.GONE);
            });
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,CartActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
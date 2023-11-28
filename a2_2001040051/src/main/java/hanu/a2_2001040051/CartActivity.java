package hanu.a2_2001040051;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import hanu.a2_2001040051.databinding.ActivityMyCartBinding;

public class CartActivity extends AppCompatActivity {
    ActivityMyCartBinding binding;
    Cart cart;
    ProductAdapter productAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cart);
        cart = Cart.instance(null);
        cart.setOnSumPriceChanged(binding::setSumPrice);

        productAdapter = new ProductAdapter(this);
        productAdapter.submitList(cart.getList());
        binding.recyclerCart.setAdapter(productAdapter);
    }
}

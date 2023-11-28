package hanu.a2_2001040051;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hanu.a2_2001040051.databinding.ProductBinding;
import hanu.a2_2001040051.databinding.ProductCartBinding;

public class ProductAdapter extends ListAdapter<Product, RecyclerView.ViewHolder> {
    private Cart cart;
    private int activityType = 0;
    List<Product> baseList = new ArrayList<>();


    public ProductAdapter(AppCompatActivity activity) {
        this(new Util());
        if (activity.getClass().getSimpleName().equals("MainActivity")) {
            activityType = 0;
        } else {
            activityType = 1;
        }

        cart = Cart.instance(null);
    }

    private ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
    }

    private interface AnyHolder {
        void onBind(Product product, int index);
    }

    private class ProductHolder extends RecyclerView.ViewHolder implements AnyHolder {
        ProductBinding binding;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void onBind(Product product, int index) {
            binding.setProduct(product);

            Utils.loadImage(product.thumbnail, binding.imgProduct);
        }
        public ProductHolder(ProductBinding binding) {
            this(binding.getRoot());
            this.binding = binding;
            binding.setCart(cart);
        }

    }

    private class ProductCartHolder extends RecyclerView.ViewHolder implements AnyHolder {
        ProductCartBinding binding;

        public ProductCartHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void onBind(Product product, int index) {
            binding.setProduct(product);

            product.setOnQuantityChanged((quantity, totalPrice) -> {
                binding.setTotalPrice(totalPrice);
                binding.setQuantity(quantity);
                Log.e("AndroidRuntime", "onBind " + quantity);
                if (quantity == 0) {
                    cart.delete(product);
                    notifyItemRemoved(index);
                } else {
                    Utils.execute((a) -> Cart.cartDAO.insert(product));
                }

                cart.invalidateSum();
            });

            Utils.loadImage(product.thumbnail, binding.imgProduct);
        }
        public ProductCartHolder(ProductCartBinding binding) {
            this(binding.getRoot());
            this.binding = binding;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (activityType == 0) {
            ProductBinding binding = ProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new ProductHolder(binding);
        } else {
            ProductCartBinding binding = ProductCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new ProductCartHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AnyHolder) holder).onBind(getCurrentList().get(position), position);
    }

    private static class Util extends DiffUtil.ItemCallback<Product> {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.name.equals(newItem.name);
        }
    }

    @Override
    public void submitList(List<Product> list) {
        super.submitList(list);
        baseList = list;
    }

    public void submitList(List<Product> list,int ignore) {
        super.submitList(list);
    }

    public void filter(String keyword) {
        String trimKey = keyword.trim().toLowerCase();
        if (trimKey.isEmpty()) {
            submitList(baseList,0);
        } else {
            submitList(baseList.stream().filter(product -> product.name.toLowerCase().contains(trimKey)).collect(Collectors.toList()),0);
        }

    }
}

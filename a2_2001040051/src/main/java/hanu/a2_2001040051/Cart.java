package hanu.a2_2001040051;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Cart {
    private static Cart instance = null;
    public static CartDAO cartDAO = null;
    private ArrayList<Product> products;

    public int sumPrice = 0;

    @NonNull
    private Consumer<Integer> onSumPriceChanged = price -> {
    };

    private Cart() {
        this.products = new ArrayList<>();
    }

    public static Cart instance(CartDAO cartDAO) {
        if (cartDAO != null) {
            Cart.cartDAO = cartDAO;
        }
        return instance == null ? instance = new Cart() : instance;
    }

    public void invalidateSum() {
        sumPrice = products.stream().mapToInt(product -> product.totalPrice).sum();
        onSumPriceChanged.accept(sumPrice);
    }

    public void setOnSumPriceChanged(@NonNull Consumer<Integer> onSumPriceChanged) {
        this.onSumPriceChanged = onSumPriceChanged;
        this.onSumPriceChanged.accept(sumPrice);
    }

    public void add(Product product) {
        if (products.contains(product)) {
            products.get(products.indexOf(product)).increase();
        } else {
            product.increase();
            products.add(product);
        }
        Utils.execute((a) -> Cart.cartDAO.insert(product));
    }

    public void delete(Product product) {
        Utils.execute((a) -> Cart.cartDAO.delete(product));
        products.remove(product);
    }

    public void submitList(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public ArrayList<Product> getList() {
        return products;
    }
}

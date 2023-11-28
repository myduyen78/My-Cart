package hanu.a2_2001040051;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.function.BiConsumer;

@Entity
public class Product {
    @SerializedName("id")
    @PrimaryKey
    public int id;
    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("name")
    public String name;
    @SerializedName("category")
    public String category;

    @SerializedName("unitPrice")
    public int unitPrice;
    public int quantity = 0;

    @Ignore
    public int totalPrice = 0;

    @Ignore
    @NonNull
    private BiConsumer<Integer, Integer> onQuantityChanged = (a, b) -> {
    };

    public Product(int id, String thumbnail, String name, String category, int unitPrice, int quantity) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.unitPrice = unitPrice;
        this.quantity = quantity;

        invalidatePrice();
    }

    @Ignore
    public Product() {
    }

    public void increase() {
        onQuantityChanged.accept(++quantity, invalidatePrice());
    }

    public void decrease() {
        onQuantityChanged.accept(--quantity, invalidatePrice());
    }

    private int invalidatePrice() {
        this.totalPrice = quantity * unitPrice;
        return this.totalPrice;
    }

    public void setOnQuantityChanged(@NonNull BiConsumer<Integer, Integer> onQuantityChanged) {
        this.onQuantityChanged = onQuantityChanged;
        this.onQuantityChanged.accept(quantity, totalPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) return false;
        if (this == o) return true;

        return id == ((Product) o).id;
    }
}

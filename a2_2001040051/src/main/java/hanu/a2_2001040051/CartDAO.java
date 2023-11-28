package hanu.a2_2001040051;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM product")
    List<Product> getProducts();

    @Delete(entity = Product.class)
    void delete(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

}

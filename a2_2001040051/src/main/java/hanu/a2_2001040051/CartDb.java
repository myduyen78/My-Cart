package hanu.a2_2001040051;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Product.class, version = 1, exportSchema = false)
abstract class CartDb extends RoomDatabase {
    abstract CartDAO cartDAO();
}

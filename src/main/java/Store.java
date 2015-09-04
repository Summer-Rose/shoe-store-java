import java.util.List;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Store {
  private int id;
  private String name;
  private String city;
  private String state;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public Store(String name, String city, String state) {
    this.name = name;
    this.city = city;
    this.state = state;
  }

  @Override
  public boolean equals(Object otherStore) {
    if(!(otherStore instanceof Store)) {
      return false;
    } else {
      Store newStore = (Store) otherStore;
      return this.getName().equals(newStore.getName()) &&
             this.getId() == newStore.getId();
    }
  }

  public static List<Store> all() {
    String sql = "SELECT * FROM stores;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores (name, city, state) VALUES (:name, :city, :state);";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("city", this.city)
      .addParameter("state", state)
      .executeUpdate()
      .getKey();
    }
  }

   public static Store find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores WHERE id=:id";
      Store store = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Store.class);
      return store;
    }
  }

  public void editStoreName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stores SET name = :name WHERE id = :id";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
  try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM stores WHERE id=:id";
    con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    String joinDeleteQuery = "DELETE FROM brands_stores WHERE store_id=:storeId";
    con.createQuery(joinDeleteQuery)
      .addParameter("storeId", this.getId())
      .executeUpdate();
  }
}

  public void addBrand(Brand brand) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands_stores (brand_id, store_id) VALUES (:brand_id, :store_id)";
      con.createQuery(sql)
        .addParameter("brand_id", brand.getId())
        .addParameter("store_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Brand> getBrands() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT brand_id FROM brands_stores WHERE store_id = :store_id";
      List<Integer> brandIds = con.createQuery(sql)
        .addParameter("store_id", this.getId())
        .executeAndFetch(Integer.class);
      ArrayList<Brand> brands = new ArrayList<Brand>();
      for (Integer brandId : brandIds) {
        String brandQuery = "SELECT * FROM brands WHERE id = :brandId";
        Brand brand = con.createQuery(brandQuery)
          .addParameter("brandId", brandId)
          .executeAndFetchFirst(Brand.class);
        brands.add(brand);
      }
      return brands;
    }
  }


}

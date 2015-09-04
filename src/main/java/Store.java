import java.util.List;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Store {
  private int id;
  private String name;
  private String city;
  private String state;

  @Rule
  public DatabaseRule database = new DatabaseRule();

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

  public Brand(String name, String city, String state) {
    this.name = name;
    this.city = city;
    this.state = state;
  }

  @Override
  public boolean equals(Object otherBrand) {
    if(!(otherBrand instanceof Brand)) {
      return false;
    } else {
      Brand newBrand = (Brand) otherBrand;
      return this.getName().equals(newBrand.getName()) &&
             this.getId() == newBrand.getId();
    }
  }

  public static List<Brand> all() {
    String sql = "SELECT * FROM brands;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Brand.class);
    }
  }

  // public static void editBrandName(String name) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE brands SET name = :name WHERE id = :id";
  //     con.createQuery(sql)
  //     .addParameter("description", description)
  //     .addParameter("id", id)
  //     .executeUpdate();
  //   }
  // }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands (name, city, state) VALUES (:name, :city, :state);";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("city", this.city)
      .addParameter("state", state)
      .executeUpdate()
      .getKey();
    }
  }

  public static Brand find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM brands WHERE id=:id";
      Brand brand = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Brand.class);
      return brand;
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
}

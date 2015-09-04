import java.util.List;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

  public class Brand {
  	private int id;
  	private String name;

	public int getId() {
    	return id;
  	}

  	public String getName() {
    	return name;
  	}

  	public Brand(String name) {
  		this.name = name;
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

 	public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
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

  	public void editBrandName(String name) {
    	try(Connection con = DB.sql2o.open()) {
      		String sql = "UPDATE brands SET name = :name WHERE id = :id";
      		con.createQuery(sql)
      			.addParameter("name", name)
      			.addParameter("id", this.getId())
      			.executeUpdate();
    	}
  	}

  	public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM brands WHERE id=:id";
      con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();
      String joinDeleteQuery = "DELETE FROM brands_stores WHERE store_id=:storeId";
      con.createQuery(joinDeleteQuery)
        .addParameter("storeId", this.getId())
        .executeUpdate();
    	}
  	}

   	public void addStore(Store store) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands_stores (brand_id, store_id) VALUES (:brand_id, :store_id)";
      con.createQuery(sql)
        .addParameter("brand_id", this.getId())
        .addParameter("store_id", store.getId())
        .executeUpdate();
    	}
  	}

  	public ArrayList<Store> getStores() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT store_id FROM brands_stores WHERE brand_id = :brand_id";
      List<Integer> storeIds = con.createQuery(sql)
        .addParameter("brand_id", this.getId())
        .executeAndFetch(Integer.class);
      ArrayList<Store> stores = new ArrayList<Store>();
      for (Integer storeId : storeIds) {
        String storeQuery = "SELECT * FROM stores WHERE id = :storeId";
        Store store = con.createQuery(storeQuery)
          .addParameter("storeId", storeId)
          .executeAndFetchFirst(Store.class);
        stores.add(store);
      }
      return stores;
    }
  }
  }
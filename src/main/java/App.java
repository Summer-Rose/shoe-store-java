import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import java.util.Arrays;


public class App {
    public static void main(String[] args) {
    	staticFileLocation("/public");
    	String layout = "templates/layout.vtl";

    //Main Page
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Search for brands by store
    get("/brand-by-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.queryParams("store"));
      response.redirect("/store/" + storeId);
      return null;
    });

    //Search store by brand
     get("/store-by-brand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.queryParams("brand"));
      response.redirect("/brand/" + brandId);
      return null;
    });

    //Site Search Results Page
    get("/search-results", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String search = request.queryParams("main-search");
      List<Store> storeResults = Store.searchByName(search);
      List<Brand> brandResults = Brand.searchByName(search);
      model.put("storeResults", storeResults);
      model.put("brandResults", brandResults);
      model.put("search", search);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/search-results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

     /////////////////
        //Stores//
     /////////////////

    //View All Stores
    get("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Add new store, redirect to All Stores
    post("/add-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String city = request.queryParams("city");
      String state = request.queryParams("state");
      Store newStore = new Store(name, city, state);
      newStore.save();
      response.redirect("/stores");
      return null;
    });

    //Display Store Search-by-name results
    get("/stores/search-by-name", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      List<Store> nameResults = Store.searchByName(name);
      model.put("nameResults", nameResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/store-search-by-name.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Display searh-by-state results
    get("/stores/search-by-state", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String state = request.queryParams("state");
      List<Store> stateResults = Store.searchByState(state);
      model.put("stateResults", stateResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/store-search-by-state.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Delete selected store and redirect to View Stores
    get("/delete/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store deleteStore = Store.find(storeId);
      deleteStore.delete();
      response.redirect("/stores");
      return null;
    });

    //Delete selected store and stay on search by state page
    get("/delete/state/:state/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store deleteStore = Store.find(storeId);
      deleteStore.delete();
      String state = request.params(":state");
      List<Store> stateResults = Store.searchByState(state);
      model.put("stateResults", stateResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/store-search-by-state.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

     //Delete selected store and stay on search by name page
    get("/delete/name/:name/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store deleteStore = Store.find(storeId);
      deleteStore.delete();
      String name = request.params(":name");
      List<Store> nameResults = Store.searchByName(name);
      model.put("nameResults", nameResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/store-search-by-name.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Display stores is order by city
    get("/stores/order-by-city", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.allByCity());
      model.put("brands", Brand.all());
      model.put("template", "templates/stores-order-by-city.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Display stores in order by state
    get("/stores/order-by-state", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.allByState());
      model.put("brands", Brand.all());
      model.put("template", "templates/stores-order-by-state.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //display stores is order by id
    get("/stores/order-by-id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.allById());
      model.put("brands", Brand.all());
      model.put("template", "templates/stores-order-by-id.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Select store id and bring to edit modal
    get("/store/edit/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params("id"));
      Store editStore = Store.find(storeId);
      model.put("store", editStore);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("stores", Store.all());
      return new ModelAndView(model, "templates/edit-store.vtl");
    }, new VelocityTemplateEngine());

    //On submit edit, reroute to View Stores
    post("/store/submit-update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store editStore = Store.find(storeId);
      String name = request.queryParams("name");
      String city = request.queryParams("city");
      String state = request.queryParams("state");
      editStore.update(name, city, state);
      response.redirect("/stores");
      return null;
    });

    //Individual Store page - displays corresponding brands
    get("/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store store = Store.find(storeId);
      List<Brand> storebrands = store.getBrands();
      model.put("store", store);
      model.put("brands", Brand.all());
      model.put("stores", Store.all());
      model.put("storebrands", storebrands);
      model.put("template", "templates/store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Add new brand to store
    post("/store/:id/add-brand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store store = Store.find(storeId);
      Integer brandId = Integer.parseInt(request.queryParams("brand"));
      Brand brand = Brand.find(brandId);
      store.addBrand(brand);
      response.redirect("/store/" + storeId);
      return null;
    });

    //Delete brand from store
    get("/delete/brand/:brandid/store/:storeid", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":storeid"));
      Store store = Store.find(storeId);
      Integer brandId = Integer.parseInt(request.params(":brandid"));
      store.removeBrand(brandId);
      response.redirect("/store/" + storeId);
      return null;
    });

    ////////////////
       //Brands//
    ////////////////

    //View All Brands
    get("/brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("brands", Brand.all());
      model.put("stores", Store.all());
      model.put("template", "templates/brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Add new brand, redirect to All Stores
    post("/add-brand", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Brand newBrand = new Brand(name);
      newBrand.save();
      response.redirect("/brands");
      return null;
    });

     //Display Brand Search-by-name results
    get("/brands/search-by-name", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      List<Brand> nameResults = Brand.searchByName(name);
      model.put("nameResults", nameResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/brand-search-by-name.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

      //Delete selected brand and redirect to View Brands
    get("/delete/brand/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":id"));
      Brand deleteBrand = Brand.find(brandId);
      deleteBrand.delete();
      response.redirect("/brands");
      return null;
    });

    //Delete selected brand and stay on search by name page
    get("/delete/name/:name/brand/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":id"));
      Brand deleteBrand = Brand.find(brandId);
      deleteBrand.delete();
      String name = request.params(":name");
      List<Brand> nameResults = Brand.searchByName(name);
      model.put("nameResults", nameResults);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/brand-search-by-name.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

     //display brands is order by id
    get("/brands/order-by-id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("brands", Brand.allById());
      model.put("stores", Store.all());
      model.put("template", "templates/brands-order-by-id.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

      //Select brand id and bring to edit modal
    get("/brand/edit/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params("id"));
      Brand editBrand = Brand.find(brandId);
      model.put("brand", editBrand);
      model.put("brands", Brand.all());
      model.put("stores", Store.all());
      return new ModelAndView(model, "templates/edit-brand.vtl");
    }, new VelocityTemplateEngine());

     //On submit edit, reroute to View Brands
    post("/brand/submit-update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":id"));
      Brand editBrand = Brand.find(brandId);
      String name = request.queryParams("name");
      editBrand.update(name);
      response.redirect("/brands");
      return null;
    });

    //View individual brand page and corresponding stores
      get("/brand/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":id"));
      Brand brand = Brand.find(brandId);
      List<Store> storebrands = brand.getStores();
      model.put("brand", brand);
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("storebrands", storebrands);
      model.put("template", "templates/brand.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Add store to brand page
    post("/brand/:id/add-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":id"));
      Brand brand = Brand.find(brandId);
      Integer storeId = Integer.parseInt(request.queryParams("store"));
      Store store = Store.find(storeId);
      brand.addStore(store);
      response.redirect("/brand/" + brandId);
      return null;
    }); 

    //Delete store from brand page
    get("/delete/store/:storeid/brand/:brandid", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer brandId = Integer.parseInt(request.params(":brandid"));
      Brand brand = Brand.find(brandId);
      Integer storeId = Integer.parseInt(request.params(":storeid"));
      brand.removeStore(storeId);
      response.redirect("/brand/" + brandId);
      return null;
    });
  }
}

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

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("template", "templates/stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

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

    get("/stores/search-by-name", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      List<Store> nameResults = Store.searchByName(name);
      model.put("nameResults", nameResults);
      model.put("template", "templates/store-search-by-name.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/search-by-state", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String state = request.queryParams("state");
      List<Store> stateResults = Store.searchByState(state);
      model.put("stateResults", stateResults);
      model.put("template", "templates/store-search-by-state.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/delete/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store deleteStore = Store.find(storeId);
      deleteStore.delete();
      response.redirect("/stores");
      return null;
    });

    get("/delete/state/store/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer storeId = Integer.parseInt(request.params(":id"));
      Store deleteStore = Store.find(storeId);
      deleteStore.delete();
      response.redirect("/stores/search-by-state");
      return null;
    });


  }
}

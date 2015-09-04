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
        model.put("tasksAll", Task.all());
        model.put("categories", Category.all());
        model.put("template", "templates/index.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/add-category", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String newCategoryString = request.queryParams("category");
        Category newCategory = new Category(newCategoryString);
        newCategory.save();
        response.redirect("/");
        return new ModelAndView(model, layout);
      });

      get("/category/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Integer categoryId = Integer.parseInt(request.params(":id"));
        Category category = Category.find(categoryId);
        model.put("category", category);
        model.put("tasks", category.getTasks());
        model.put("categories", Category.all());
        model.put("template", "templates/index.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/add-task", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String newTaskString = request.queryParams("task");
        Task newTask = new Task(newTaskString);
        newTask.save();

        //For check box
        Object[] objectArray =  request.queryParams().toArray();
        String[] categoryStrings = Arrays.copyOf(objectArray, objectArray.length, String[].class);

        for (String categoryString : categoryStrings) {
          if(categoryString.contains("categories")) {
            Integer categoryId = Integer.parseInt(categoryString.split("-")[1]);
            Category checkedCategory = Category.find(categoryId);
            newTask.addCategory(checkedCategory);
          }
        }
        response.redirect("/");
        return null;
      });


      post("/update/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();

        String categoryId = request.params(":id");
        Object[] objectArray =  request.queryParams().toArray();
        String[] taskStrings = Arrays.copyOf(objectArray, objectArray.length, String[].class);

        for(String taskString : taskStrings) {
          if(taskString.contains("task")) {
            Integer taskId = Integer.parseInt(taskString.split("-")[1]);
            Task completedTask = Task.find(taskId);
            completedTask.completeTask();
          }
        }

        Object[] objectArray2 =  request.queryParams().toArray();
        String[] taskStrings2 = Arrays.copyOf(objectArray2, objectArray2.length, String[].class);

        for(String taskString : taskStrings2) {
          if(taskString.contains("delete")) {
            Integer taskId = Integer.parseInt(taskString.split("-")[1]);
            Task deletedTask = Task.find(taskId);
            deletedTask.delete();
          }
        }

        response.redirect("/category/" + categoryId);
        return null;
      });
      //
      // get("/to-do-list", (request, response) -> {
      //   HashMap<String, Object> model = new HashMap<String, Object>();
      //   //save info from form entry
      //   String categoryName = request.queryParams("category");
      //   Category newCategory = new Category(categoryName);
      //   newCategory.save();
      //   model.put("categories", Category.all());
      //   model.put("template", "templates/index.vtl");
      //   return new ModelAndView(model, layout);
      // }, new VelocityTemplateEngine());
      //
      // get("/tasks/:id", (request, response) -> {
      //   HashMap<String, Object> model = new HashMap<String, Object>();
      //   String description = request.queryParams("description");
      //   Task newTask = new Task(description);
      //
      //   if (description != null){
      //     newTask.save();
      //   }
      //
      //   model.put("categories", Category.all()); //displays category buttons
      //   //model.put("tasks", Task.getTasksByCategoryId(categoryId));
      //   model.put("category", Category.find(Integer.parseInt(request.params(":id"))));
      //   model.put("template", "templates/index.vtl");
      //   return new ModelAndView(model, layout);
      // }, new VelocityTemplateEngine());

      // get("/categories/:category_id/delete/:id", (request, response) -> {
      //   HashMap<String, Object> model = new HashMap<String, Object>();
      //   int taskId = Integer.parseInt(request.params(":id"));
      //   Task.removeTaskById(taskId);
      //   Integer categoryId = Integer.parseInt(request.params(":category_id"));
      //   model.put("categories", Category.all()); //buttons
      //   model.put("category", Category.find(categoryId));
      //   model.put("tasks", Task.getTasksByCategoryId(categoryId));
      //   model.put("template", "templates/index.vtl");
      //   return new ModelAndView(model, layout);
      // }, new VelocityTemplateEngine());

      // get("/delete/:categoryid", (request, response) -> {
      //   HashMap<String, Object> model = new HashMap<String, Object>();

      //   int categoryid = Integer.parseInt(request.params(":categoryid"));
      //   Category.removeCategory(categoryid);
      //   model.put("categories", Category.all()); //buttons
      //   model.put("category", Category.find(categoryid));
      //   model.put("tasks", Task.getTasksByCategoryId(categoryid));
      //   model.put("template", "templates/index.vtl");
      //   return new ModelAndView(model, layout);
      // }, new VelocityTemplateEngine());

    //   get("/edit/:category_id/:description/:id", (request, response) -> {
    //     HashMap<String, Object> model = new HashMap<String, Object>();
    //     Integer categoryid = Integer.parseInt(request.params(":category_id"));
    //     Task editTask = Task.find(Integer.parseInt(request.params(":id")));
    //     model.put("editTask", editTask);
    //     model.put("category", Category.find(categoryid));
    //     model.put("tasks", Task.getTasksByCategoryId(categoryid));
    //     model.put("categories", Category.all()); //buttons
    //     model.put("template", "templates/index.vtl");
    //     return new ModelAndView(model, layout);
    //   }, new VelocityTemplateEngine());

    //   get("/category/:category_id/edit/:id", (request, response) -> {
    //     HashMap<String, Object> model = new HashMap<String, Object>();
    //     Integer categoryid = Integer.parseInt(request.params(":category_id"));
    //     String description = request.queryParams("description");
    //     Integer taskId = Integer.parseInt(request.params(":id"));
    //     Task.editTask(taskId, description);
    //     model.put("category", Category.find(categoryid));
    //     model.put("tasks", Task.getTasksByCategoryId(categoryid));
    //     model.put("categories", Category.all()); //buttons
    //     model.put("template", "templates/index.vtl");
    //     return new ModelAndView(model, layout);
    //   }, new VelocityTemplateEngine());
    }
}

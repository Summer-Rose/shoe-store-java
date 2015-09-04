import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class StoreTest {

 @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Store.all().size());
  }

  @Test
  public void equals_returnsTrueIfInputIsTheSame() {
    Store store1 = new Store ("Payless Shoes", "Portland", "OR");
    Store store2 = new Store ("Payless Shoes", "Portland", "OR");
    assertTrue(store1.equals(store2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Store myStore = new Store ("DSW", "Portland", "OR");
    myStore.save();
    Store savedStore = Store.all().get(0);
    assertEquals(myStore, savedStore);
  }

  @Test
  public void getId_returnsId(){
    Store myStore = new Store ("Naturalizers", "Portland", "OR");
    myStore.save();
    Store savedStore = Store.all().get(0);
    assertEquals(savedStore.getId(), myStore.getId());
  }

  @Test
  public void getName_returnsName() {
    Store myStore = new Store ("Famous Footwear", "Portland", "OR");
    myStore.save();
    assertEquals("Famous Footwear", myStore.getName());
  }

  @Test
  public void find_findsStoreInDatabase_True() {
    Store myStore = new Store ("Target", "Portland", "OR");
    myStore.save();
    Store savedStore = Store.find(myStore.getId());
    assertTrue(myStore.equals(savedStore));
  }

  @Test
  public void editStoreName_changesName(){
    Store myStore = new Store ("Ross", "Portland", "OR");
    myStore.save();
    myStore.editStoreName("Nordstrom Rack");
    Store savedStore = Store.find(myStore.getId());
    assertEquals("Nordstrom Rack", savedStore.getName());
  }


  @Test
  public void delete_DeletesFromDatabase(){
    Store myStore = new Store ("Nike Factory Outlet", "Portland", "OR");
    myStore.save();
    myStore.delete();
    Store savedStore = Store.find(myStore.getId());
    assertEquals(false, myStore.equals(savedStore));
  }

  @Test
  public void getBrands_returnsBrands(){
    Brand myBrand = new Brand ("Keens");
    myBrand.save();
    Store myStore = new Store ("REI", "Portland", "OR");
    myStore.save();
    myStore.addBrand(myBrand);
    assertEquals(myStore.getBrands().size(), 1);
  }

  
}

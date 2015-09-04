import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BrandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Brand.all().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Brand brand1 = new Brand ("Puma");
    Brand brand2 = new Brand ("Puma");
    assertTrue(brand1.equals(brand2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Brand myBrand = new Brand ("Addidas");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(myBrand, savedBrand);
  }

  @Test
  public void getId_returnsId(){
    Brand myBrand = new Brand ("Converse");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(savedBrand.getId(), myBrand.getId());
  }

  @Test
  public void getName_returnsBrandName(){
    Brand myBrand = new Brand ("Nike");
    myBrand.save();
    Brand savedBrand = Brand.all().get(0);
    assertEquals(savedBrand.getName(), myBrand.getName());
  }

  @Test
  public void find_findsBrandInDatabase_True() {
    Brand myBrand = new Brand ("Crocs");
    myBrand.save();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertTrue(myBrand.equals(savedBrand));
  }

  @Test
  public void editBrandName_changesBrandName() {
    Brand myBrand = new Brand ("Naturalizers");
    myBrand.save();
    myBrand.editBrandName("Natures Sole");
    Brand savedBrand = Brand.find(myBrand.getId());
    assertEquals("Natures Sole", savedBrand.getName());
  }

  @Test
  public void delete_deletesFromDatabase(){
    Brand myBrand = new Brand ("Puma");
    myBrand.save();
    myBrand.delete();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertEquals(false, myBrand.equals(savedBrand));
  }

  @Test
  public void getStores_returnsStoresList(){
    Store myStore = new Store ("Puma Outlet", "Portland", "OR");
    myStore.save();
    Brand myBrand = new Brand ("Puma");
    myBrand.save();
    myBrand.addStore(myStore);
    assertEquals(myBrand.getStores().size(), 1);
  }
}

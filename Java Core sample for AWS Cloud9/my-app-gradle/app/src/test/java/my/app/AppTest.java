import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
  @Test public void testAppExists () {
    try {
      Class.forName("com.mycompany.app.App");
    } catch (ClassNotFoundException e) {
      fail("Should have a class named App.");
    }
  }
}
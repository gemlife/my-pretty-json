import com.abb.e7.modelXML.InputXML
import org.junit.Test

/**
 * Created by PLOLPIL on 10/25/2016.
 */
class PassedInputXmlTest {

  def xml = new InputXML()

  @Test
  public void post() {
    println xml.writer
  }
}

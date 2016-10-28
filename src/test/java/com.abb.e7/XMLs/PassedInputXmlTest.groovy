import com.abb.e7.core.NMarketService
import com.abb.e7.modelXML.InputXML
import io.restassured.path.xml.XmlPath
import org.junit.Test

class PassedInputXmlTest {

  def xml = new InputXML()
  def xmlResult = xml.writer.toString()

  @Test
  public void post() {
    println xmlResult

    String postRequest = NMarketService.postWithLogging(xmlResult)
    String result = XmlPath.from(postRequest).get("collection.InboundOutboundResult.results")

    println postRequest
    assert result.contains("with 0 error(s) and 0 warning(s)")

  }
}

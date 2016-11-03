import com.abb.e7.core.NMarketService
import com.abb.e7.modelXML.GenOfferData
import com.abb.e7.modelXML.HourliesData
import com.abb.e7.modelXML.InputXML
import groovy.xml.XmlUtil
import io.restassured.path.xml.XmlPath
import org.junit.Test

class PassedInputXmlTest {

  def hourFirst = new HourliesData(
      operatingHour: "2014-01-01T02:00:00-05:00",
      commitmentStatus: "ECONOMIC",
      userComment: "NOT DEFAult Comment"
  )
  def offerFirst = new GenOfferData(
      hourliesData: (hourFirst.xmlBuilder()),
      action: ("INSERT")
  )
  def genOffers = new InputXML(
      genOfferData: offerFirst.xmlBuilder(),
//      hourliesData: hourFirst.xmlBuilder()
  )
  def xmlResult = genOffers.xml

  @Test
  public void post() {

    String postRequest = NMarketService.postWithLogging(xmlResult)
    String result = XmlPath.from(postRequest).get("collection.InboundOutboundResult.results")
    String errorMessage = XmlPath.from(postRequest).get("collection.InboundOutboundResult.errorSummary")

    println postRequest
    println errorMessage
    assert result.contains("with 0 error(s) and 0 warning(s)")
  }
}

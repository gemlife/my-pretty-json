import com.abb.e7.core.NMarketService
import com.abb.e7.modelXML.AsOfferData
import com.abb.e7.modelXML.DailiesData
import com.abb.e7.modelXML.EnergyOfferData
import com.abb.e7.modelXML.GenOfferData
import com.abb.e7.modelXML.HourliesData
import com.abb.e7.modelXML.InputXML
import com.abb.e7.modelXML.RampCurveData
import io.restassured.path.xml.XmlPath
import org.junit.Test

class PassedInputXmlTest {
  def energyOffers = new EnergyOfferData(
      mw: ["50", "100", "150", "300"],
      sn: ["1", "2", "3", "4"],
      pr: ["20", "22", "25", "30"]
  )
  def asOfferData = new AsOfferData()
  def rampCurveData = new RampCurveData()
  def dailiesData = new DailiesData()

  def firstHour = new HourliesData(
      operatingHour: "2014-01-01T02:00:00-05:00",
      commitmentStatus: "ECONOMIC",
      userComment: "NOT Default Comment",
      energyOffersData: energyOffers,
      asOfferData: asOfferData,
      rampCurveData: rampCurveData,
  )
  def secondHour = new HourliesData(
      operatingHour: "2014-01-01T03:00:00-05:00",
      commitmentStatus: "ECONOMIC",
      userComment: "NOT Comment",
      energyOffersData: energyOffers,
      asOfferData: asOfferData,
      rampCurveData: rampCurveData,
  )
  def thirdHour = new HourliesData(
      operatingHour: "2014-01-01T04:00:00-05:00",
      commitmentStatus: "ECONOMIC",
      userComment: "NOT Comment",
      energyOffersData: energyOffers,
      asOfferData: asOfferData,
      rampCurveData: rampCurveData,
  )
  def offerFirst = new GenOfferData(
      hourliesData: [(firstHour.xmlBuilder()), (secondHour.xmlBuilder()), (thirdHour.xmlBuilder())],
      dailiesData: dailiesData.xmlBuilder(),
      action: "INSERT",
  )
  def genOffers = new InputXML(
      genOfferData: offerFirst.xmlBuilder(),
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

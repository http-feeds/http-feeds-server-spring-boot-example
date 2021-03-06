package org.httpfeeds.serverexample;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import org.httpfeeds.server.FeedAppender;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RandomDataGenerator {

  private final FeedAppender feedItemAppender;

  public RandomDataGenerator(FeedAppender feedItemAppender) {
    this.feedItemAppender = feedItemAppender;
  }

  @Configuration
  @EnableScheduling
  public static class RandomDataGeneratorConfiguration {
  }

  @Scheduled(initialDelayString = "PT5S", fixedDelayString = "PT0.200S")
  void generateInventoryData() throws InterruptedException {
    String sku = randomSku();
    String method = randomMethod();
    Inventory data = null;
    if (method == null) {
      data = new Inventory();
      data.sku = sku;
      data.updated = Instant.now().toString();
      data.quantity = (long) new Random().nextInt(20);
    }
    feedItemAppender.append("org.http-feeds.example.inventory",
        "https://example.http-feeds.org/inventory", Instant.now(), sku, method, data);

    Thread.sleep(200L);

  }

  static class Inventory {
    String sku;
    String updated;
    Long quantity;

    public String getSku() {
      return sku;
    }

    public String getUpdated() {
      return updated;
    }

    public Long getQuantity() {
      return quantity;
    }
  }

  private String randomMethod() {
    if (new Random().nextFloat() < 0.01) {
      return "DELETE";
    }
    return null;
  }

  private String randomSku() {

    var skus = List.of("9527253082603",
        "9529169185461",
        "9522489414228",
        "9520143717227",
        "9528510266767",
        "9520615996884",
        "9526058004018",
        "9527024121555",
        "9525573340021",
        "9522662606556",
        "9526244567860",
        "9527184129996",
        "9522093569147",
        "9529820384066",
        "9525292253954",
        "9523875607408",
        "9528855595423",
        "9528290257429",
        "9521687783686",
        "9529807974419",
        "9520039343523",
        "9526292022762",
        "9525983867866",
        "9527026220249",
        "9529480674101",
        "9520998911788",
        "9527806199390",
        "9528127599913",
        "9526964580040",
        "9526615879554",
        "9527813137897",
        "9525500981570",
        "9529126781514",
        "9521629786164",
        "9528386371978",
        "9528728794304",
        "9529217650408",
        "9520701771319",
        "9520798004154",
        "9527070101075",
        "9521878839055",
        "9525598136845",
        "9522239339191",
        "9522661838477",
        "9527606703308",
        "9525782390305",
        "9526077268507",
        "9525317464242",
        "9527204242490",
        "9526918251668",
        "9529197086204",
        "9525889785783",
        "9523324211392",
        "9522552748458",
        "9525371585426",
        "9521681894715",
        "9522767186137",
        "9526353690725",
        "9527363066807",
        "9521685274223",
        "9522084498630",
        "9527201908535",
        "9525745909445",
        "9523967424579",
        "9523425914901",
        "9526516389312",
        "9527857983290",
        "9524167401919",
        "9522166942327",
        "9529818506807",
        "9521506017824",
        "9526480487458",
        "9525650937090",
        "9523373567389",
        "9527590306493",
        "9524448807973",
        "9522267004177",
        "9521839285556",
        "9521669056630",
        "9523678264754",
        "9524248251624",
        "9528653087694",
        "9529157458829",
        "9524451533418",
        "9526536284734",
        "9525850025139",
        "9520644410351",
        "9525286886168",
        "9527799905121",
        "9526005552616",
        "9522161694078",
        "9526279408640",
        "9527600732984",
        "9521677018415",
        "9528143005689",
        "9521139053688",
        "9526047841792",
        "9521372781409",
        "9525619906617",
        "9526954451527",
        "9529152894653",
        "9526453062873",
        "9524930423575",
        "9528493498933",
        "9525113866936",
        "9522199144750",
        "9524815853985",
        "9527699848337",
        "9524971766082",
        "9521542742476",
        "9520636569104",
        "9528840862363",
        "9526593652910",
        "9524660833163",
        "9520822471846",
        "9522959279654",
        "9529279501991",
        "9529126234584",
        "9522676162710",
        "9525535419307",
        "9520834388125",
        "9526161892991",
        "9529262485765",
        "9520150603261",
        "9527079193040",
        "9522280760418",
        "9522328389656",
        "9523763039588",
        "9529573575179",
        "9520810000911",
        "9525890712884",
        "9521236655907",
        "9521894490261",
        "9524907927341",
        "9520929588461",
        "9526196283948",
        "9525234549299",
        "9528298745454",
        "9528612941531",
        "9528338307277",
        "9527994817465",
        "9520091109013",
        "9525867488743",
        "9520046774150",
        "9520419570488",
        "9520212956656",
        "9529841186038",
        "9523135881203",
        "9524006223788",
        "9521863965943",
        "9521530451847",
        "9524301551869",
        "9527685588070",
        "9520925363383",
        "9525207324489",
        "9521284103214",
        "9522612136515",
        "9525770945258",
        "9520658321605",
        "9529841038665",
        "9524939298266",
        "9526324067761",
        "9522442636384",
        "9527521938892",
        "9522326360695",
        "9523935169419",
        "9524923071028",
        "9521303920211",
        "9529316844746",
        "9524219519326",
        "9529890453860",
        "9526902422715",
        "9521844132128",
        "9522398917612",
        "9522409764815",
        "9520053210719",
        "9521829830506",
        "9523942676672",
        "9520530473675",
        "9525741248913",
        "9524419094500",
        "9523768124517",
        "9525358193279",
        "9526528483787",
        "9521808678730",
        "9528065629154",
        "9527557235316",
        "9528390901482",
        "9527031492723",
        "9521191440174",
        "9522952550071",
        "9526050108592",
        "9528304459337",
        "9523075491227",
        "9520113648728",
        "9525192161540",
        "9522866426462",
        "9527754286333",
        "9528435968463",
        "9527449748665");
    return skus.get(new Random().nextInt(skus.size()));
  }

}

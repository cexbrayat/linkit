import models.Interest;
import models.LightningTalk;
import models.Member;
import models.Talk;
import org.junit.*;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

public class JsonApiTest extends FunctionalTest {

    @BeforeClass
    public static void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("init-data.yml");
    }

    @AfterClass
    public static void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testSpeakers() {
        test("/api/members/speakers");
    }

    private void test(String url) {
        json(url);
        jsonDetails(url);
        jsonp(url);
        jsonpDetails(url);
    }

    private void json(String url) {
        Response response = GET(url);
        assertJson(response);
    }

    private void jsonDetails(String url) {
        Response response = GET(url+"?details=true");
        assertJson(response);
    }

    private void jsonp(String url) {
        Response response = GET(url+"?callback=mycallback");
        assertJsonp(response, "mycallback");
    }

    private void jsonpDetails(String url) {
        Response response = GET(url+"?callback=mycallback&details=true");
        assertJsonp(response, "mycallback");
    }

    @Test
    public void testSponsors() {
        test("/api/members/sponsors");
    }

    @Test
    public void testMembers() {
        test("/api/members");
    }

    @Test
    public void testMember() {
        Member m = Member.all().first();
        test("/api/members/"+m.id);
    }

    @Test
    public void testMember_notFound() {
        Response r = GET("/api/members/999999");
        assertStatus(404, r);
    }

    @Test
    public void testTalks() {
        test("/api/talks");
    }

    @Test
    public void testTalk() {
        Talk t = Talk.all().first();
        test("/api/talks/"+t.id);
    }

    @Test
    public void testTalk_notFound() {
        Response r = GET("/api/talks/999999");
        assertStatus(404, r);
    }

    @Test
    public void testLightningalks() {
        test("/api/lightningtalks");
    }

    @Test
    public void testLightningalk() {
        LightningTalk lt = LightningTalk.all().first();
        test("/api/lightningtalks/"+lt.id);
    }

    @Test
    public void testLightningalk_notFound() {
        Response r = GET("/api/lightningtalks/999999");
        assertStatus(404, r);
    }

    @Test
    public void testInterests() {
        test("/api/interests");
    }

    @Test
    public void testInterest() {
        Interest i = Interest.all().first();
        test("/api/interests/"+i.id);
    }

    @Test
    public void testInterest_notFound() {
        Response r = GET("/api/interests/999999");
        assertStatus(404, r);
    }

    private void assertJson(Response response) {
        assertIsOk(response);
        assertContentType("application/json", response);
        assertFalse(getContent(response).isEmpty());
    }

    private void assertJsonp(Response response, String callback) {
        assertJson(response);
        assertTrue(getContent(response).contains(callback));
    }
}
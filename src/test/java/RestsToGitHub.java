import com.codeborne.selenide.Configuration;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import java.util.List;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.Assert.assertEquals;

public class RestsToGitHub {
    @Test
    public void test1FirstTask() throws JSONException{
        Response response= given().when().
                get("https://api.github.com/search/repositories?q=selenide&sort=stars");
        assertEquals(200, response.getStatusCode());
        System.out.println(response.getStatusCode());

        List<String> Name = from(response.asString()).get("items.name");
        List<String> Opys = from(response.asString()).get("items.description");
        List<String> language = from(response.asString()).get("items.language");
        List<String> license = from(response.asString()).get("items.license.name");
        List<Integer> star = from(response.asString()).get("items.stargazers_count");

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        Configuration.browser = "chrome";
        open("https://github.com/");
        $(By.xpath("/html/body/div[1]/header/div/div[2]/div/div/div/div/form/label/input[1]"))
                .setValue("Selenide").pressEnter();


        String namerepos=$(By.xpath("//*[@id=\"js-pjax-container\"]/div/div[1]/div[2]/div/ul/div[1]/div[1]/h3/a/em")).getText();

        String description=$(By.xpath("//*[@id=\"js-pjax-container\"]/div/div[1]/div[2]/div/ul/div[1]/div[1]/p")).getText();

        String language1=$(By.xpath("//*[@id=\"js-pjax-container\"]/div/div[1]/div[2]/div/ul/div[1]/div[2]")).getText();

        String license1=$(By.xpath("//*[@id=\"js-pjax-container\"]/div/div[1]/div[2]/div/ul/div[1]/div[1]/div/p[1]")).getText();

        String stars1=$(By.xpath("//*[@id=\"js-pjax-container\"]/div/div[1]/div[2]/div/ul/div[1]/div[3]/a")).getText();
        if (namerepos.equals(Name.get(0))){
            System.out.println("Repository equals");
        }
        if(description.equals(Opys.get(0))){
            System.out.println("Description equals");
        }
        if(language1.equals(language.get(0))){
            System.out.println("Language equals");
        }

        if(license1.equalsIgnoreCase(license.get(0))){
            System.out.println("License equals");
        }
        if(Integer.parseInt(stars1)==(star.get(0))){
            System.out.println("Stars Equals");
        }


        JSONObject jsonResponse = new JSONObject(response.asString());
        String capital = jsonResponse.getString("total_count");
        Assert.assertEquals(capital, "524");

    }
    @Test
    public void test2SecondTask() {
        Response res=given()
                .get("https://api.github.com/search/repositories?q=language:JavaScript&s=stars");
        assertEquals(200,res.getStatusCode());
    }

    @Test
    public void test3ThirdTask() {
        Response createRepository=given().contentType("application/json").body("{\n" +
                "  \"name\": \"MyRepository\",\n" +
                "  \"description\": \"myrepository\",\n" +
                "  \"homepage\": \"https://github.com\",\n" +
                "  \"private\": false,\n" +
                "  \"has_issues\": true,\n" +
                "  \"has_projects\": true,\n" +
                "  \"has_wiki\": true\n" +
                "}"
        ).when().post("https://api.github.com/user/repos?access_token=23691af190cf62ae3037d1cca58ab4f01f44901a");
        assertEquals(201,createRepository.getStatusCode());

        String URL="https://api.github.com/repos/shefkin97/MyRepository?access_token=23691af190cf62ae3037d1cca58ab4f01f44901a";
        Response delete=given().contentType("application/json").
                body(" \"name\": \"MyRepository\"").when().
                delete(URL);

        Response comparison=given().when().get("https://api.github.com/repos/shefkin97/MyRepository");
        assertEquals(404,comparison.getStatusCode());
    }
}

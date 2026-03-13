package utilities.API_Utilities;

import config_Requirements.ConfigLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class Authentication {

    static ConfigLoader configLoader = new ConfigLoader();

    public static String generateToken() {

        // Base URL oluşturuluyor
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(configLoader.getApiConfig("base_url"))
                .build();

        // Request body oluşturuluyor
        JSONObject reqBody = new JSONObject();
        reqBody.put("email", configLoader.getApiConfig("adminEmail"));
        reqBody.put("password", configLoader.getApiConfig("adminPassword"));

        // POST isteği gönderiliyor
        Response response = given()
                .log().all()
                .spec(spec)
                .header("User-Agent", "PostmanRuntime/7.52.0") // Postman gibi davranmasını sağlar
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("x-api-key", configLoader.getApiConfig("x_api_key"))
                .body(reqBody.toString())
                .post("/api/token");

        // DEBUG: Response yazdır
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        // Status code kontrolü
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Token alınamadı! Status Code: "
                    + response.getStatusCode() + ", Response: " + response.asString());
        }
/*
        // JSON parse, null güvenli
        JsonPath repJP;
        try {
            repJP = response.jsonPath();
        } catch (Exception e) {
            throw new RuntimeException("Response JSON olarak parse edilemedi! Response: " + response.asString());
        }

        // Token alma: API'nin JSON yapısına göre değişebilir
        String token = null;

        if (repJP.getString("data.access_token") != null) {
            token = repJP.getString("data.access_token");
        } else if (repJP.getString("access_token") != null) {
            token = repJP.getString("access_token");
        }

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token JSON'da bulunamadı! Response: " + response.asString());
        }

        System.out.println("Token : " + token);
        return token;
    }



*/
        JsonPath repJP = response.jsonPath();

        String token = repJP.getString("data.access_token");
        System.out.println("Token : " + token);

        return token;


    }
}

package util;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class testGen {
    private String baseURI = "https://api.example.com"; // Replace with the base URI of your API

    @Test
    public  Map<String, String>  testMultipleUsers() throws IOException {
        RestAssured.baseURI = baseURI;

        String excelFilePath = "path/to/your/users.xlsx"; // Replace with the actual path to your Excel file

        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, String> userCredentials = new HashMap<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String username = row.getCell(0).getStringCellValue();
            String password = row.getCell(1).getStringCellValue();

            userCredentials.put(username, password);
        }

        workbook.close();
        fis.close();
        return userCredentials;
        

        // Now you have all the username-password pairs in the userCredentials map
        // You can use this map for further processing, making API calls, etc.

//        for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
//            String username = entry.getKey();
//            String password = entry.getValue();
//
//            String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
//
//            Response response =
//                given()
//                    .contentType(ContentType.JSON)
//                    .body(requestBody)
//                .when()
//                    .post("/api/login")
//                .then()
//                    .statusCode(200)
//                    .extract().response();
//
//            // Add your assertions or further API calls for each user here
//
//            // You can print the response body if needed
//            System.out.println("Response Body for user " + username + ": ");
//            System.out.println(response.getBody().asString());
//        }
    }
}
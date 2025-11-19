package stepdefinitions;


import config_Requirements.ConfigLoader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utilities.DB_utilities.DB_Utils;

import java.sql.SQLException;
import java.util.List;

public class DB_Stepdefinitions {
        ConfigLoader configLoader = new ConfigLoader();
        String dbUrl;
        String username;
        String password;
        Object totalMeetingTime;
        List<List<Object>> queryResults;

        // Background: Database connection established
        @Given("Database connection established")
        public void database_connection_established() {
            dbUrl = configLoader.getDatabaseConfig("URL");
            username = configLoader.getDatabaseConfig("USERNAME");
            password = configLoader.getDatabaseConfig("PASSWORD");

            DB_Utils.createConnection(dbUrl, username, password);
            System.out.println("Database connection established successfully.");

            try {
                // Bağlantı gerçekten açık mı test et
                boolean isValid = DB_Utils.connection.isValid(5); // 5 saniye içinde kontrol eder
                System.out.println("Database connection test (isValid): " + isValid);
            } catch (SQLException e) {
                throw new RuntimeException("Database bağlantı doğrulama hatası: " + e.getMessage());
            }

        }

        // US1: Calculate total meeting time for a user
        @When("Calculate the total meeting time in minutes for the user {string} in the  {string} table.")
        public void calculate_the_total_meeting_time_in_minutes_for_the_user_in_the_table(String emailColumn, String tableName) {
            /*
            String query = "SELECT SUM(meeting_duration) AS total_time " +
                    "FROM " + tableName + " WHERE " + emailColumn + " = 'oske.work@gmail.com'";
            totalMeetingTime = DB_Utils.getCellValue(query);
            System.out.println("Toplam görüşme süresi (dakika): " + totalMeetingTime);

             */
            // SQL sorgusunu klasik string ile birleştiriyoruz
            String query = "SELECT SUM(" +
                    "TIME_TO_SEC(STR_TO_DATE(SUBSTRING_INDEX(mt.time, '-', -1), '%h:%i%p')) - " +
                    "TIME_TO_SEC(STR_TO_DATE(SUBSTRING_INDEX(mt.time, '-', 1), '%h:%i%p'))" +
                    ") / 60 AS total_time " +
                    "FROM users u " +
                    "JOIN meetings m ON m.creator_id = u.id " +
                    "JOIN meeting_times mt ON mt.meeting_id = m.id " +
                    "WHERE u." + emailColumn + " = 'oske.work@gmail.com' " +
                    "AND mt.time REGEXP '^[0-9]{1,2}:[0-9]{2}(AM|PM)-[0-9]{1,2}:[0-9]{2}(AM|PM)$'";

            // Sorguyu çalıştır ve sonucu al
            totalMeetingTime = DB_Utils.getCellValue(query);

            System.out.println("Toplam görüşme süresi (dakika): " + totalMeetingTime);


        }

        @Then("Verify the information Results are obtained.")
        public void verify_the_information_results_are_obtained() {
            Assert.assertNotNull("Toplam görüşme süresi bulunamadı!", totalMeetingTime);
        }

        @Then("Database connection is closed")
        public void database_connection_is_closed() {
            DB_Utils.closeConnection();
        }

        // US2: Meetings grouped by status
        @Given("Preparation of query grouping according to statuses in the reserved meeting table")
        public void preparation_of_query_grouping_according_to_statuses_in_the_reserved_meeting_table() {
            String query = "SELECT status, COUNT(*) AS total_meetings, " +
                    "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM reserve_meetings)) AS percentage " +
                    "FROM reserve_meetings " +
                    "GROUP BY status;";
            queryResults = DB_Utils.getQueryResultList(query);
            queryResults.forEach(System.out::println);
        }

        @Then("The status should be {string}")
        public void the_status_should_be(String status) {
            boolean statusFound = queryResults.stream()
                    .anyMatch(row -> row.get(0).toString().equalsIgnoreCase(status));
            Assert.assertTrue("Status bulunamadı: " + status, statusFound);
        }

        @Then("The total_meetings should be {int}")
        public void the_total_meetings_should_be(Integer expectedTotal) {
            boolean match = queryResults.stream()
                    .anyMatch(row -> ((Number) row.get(1)).intValue() == expectedTotal);
            Assert.assertTrue("Toplam toplantı sayısı eşleşmedi!", match);
        }

        @Then("The percentage should be {double}")
        public void the_percentage_should_be(Double expectedPercentage) {
            boolean match = queryResults.stream()
                    .anyMatch(row -> Math.abs(((Number) row.get(2)).doubleValue() - expectedPercentage) < 0.001);
            Assert.assertTrue("Yüzde değeri eşleşmedi!", match);
        }
    }

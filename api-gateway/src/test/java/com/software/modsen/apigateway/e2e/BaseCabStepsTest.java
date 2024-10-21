package com.software.modsen.apigateway.e2e;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;

import static com.software.modsen.apigateway.util.E2ETestUtil.DEFAULT_CAR_REQUEST_BODY;
import static com.software.modsen.apigateway.util.E2ETestUtil.DEFAULT_DRIVER_REQUEST_BODY;
import static com.software.modsen.apigateway.util.E2ETestUtil.DEFAULT_PASSENGER_REQUEST_BODY;
import static com.software.modsen.apigateway.util.E2ETestUtil.DEFAULT_RATING_REQUEST_BODY;
import static com.software.modsen.apigateway.util.E2ETestUtil.DEFAULT_RIDE_REQUEST_BODY;

@CucumberContextConfiguration
public class BaseCabStepsTest {
    private String rideStatus;
    private Long carId;
    private Long passengerId;
    private Long driverId;
    private Long rideId;
    private double driverRating;
    private double passengerRating;

    @After
    @Rollback
    public void tearDown() {
    }

    @Given("a car with the following details: model {string}, licensePlate {string}, year {int}, color {string}")
    public void aCarWithTheFollowingDetailsModelLicensePlateYearColor(String model, String licensePlate, int year, String color) {
        String requestBodyCar = String.format(DEFAULT_CAR_REQUEST_BODY, model, licensePlate, year, color);
        carId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyCar)
                .when()
                .post("http://localhost:8080/driver-service/cars")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getLong("carId");
    }

    @And("a driver with the following details: name {string}, email {string}, phone {string}, sex {string}")
    public void aDriverWithTheFollowingDetailsNameEmailPhoneSexCarId(String name, String email, String phone, String sex) {
        String requestBodyDriver = String.format(DEFAULT_DRIVER_REQUEST_BODY, name, email, phone, sex, carId);
        driverId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyDriver)
                .when()
                .post("http://localhost:8080/driver-service/drivers")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("driverId");
    }

    @And("a passenger with the following details: name {string}, email {string}, phone {string}")
    public void aPassengerWithTheFollowingDetailsNameEmailPhone(String name, String email, String phone) {
        String requestBodyPassenger = String.format(DEFAULT_PASSENGER_REQUEST_BODY, name, email, phone);
        passengerId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyPassenger)
                .when()
                .post("http://localhost:8080/passenger-service/passengers")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("passengerId");
    }

    @When("requests a ride from {string} to {string}")
    public void requestsARideFromToForPassengerWithIdAndDriverWithId(String routeStart, String routeEnd) {
        String requestBodyPassenger = String.format(DEFAULT_RIDE_REQUEST_BODY, driverId, passengerId, routeStart, routeEnd);
        JsonPath jsonPathResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyPassenger)
                .when()
                .post("http://localhost:8080/ride-service/rides")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath();
        rideId = jsonPathResponse.getLong("id");
        rideStatus = jsonPathResponse.getString("status");
    }

    @Then("a ride should be created with status {string}")
    public void aRideShouldBeCreatedWithStatus(String status) {
        Assertions.assertEquals(status, rideStatus);
    }

    @When("the passenger rates the driver with a rate {int}, comment {string}")
    public void thePassengerRatesTheDriverWithARate(int rate, String comment) {
        String requestBodyRating = String.format(DEFAULT_RATING_REQUEST_BODY, rideId, rate, comment);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyRating)
                .when()
                .post("http://localhost:8080/rating-service/ratings/drivers")
                .then()
                .statusCode(201);
    }

    @And("requests a rating of driver")
    public void requestsARatingOfDriver() {
        driverRating = RestAssured.given()
                .when()
                .get("http://localhost:8080/rating-service/ratings/drivers/{id}", driverId)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getDouble("rate");
    }

    @Then("the driver's rating should be updated to {double}")
    public void theDriverSRatingShouldBeUpdatedTo(double rate) {
        Assertions.assertEquals(driverRating, rate);
    }

    @When("the driver rates the passenger with a rate {int}, comment {string}")
    public void theDriverRatesThePassengerWithARate(int rate, String comment) {
        String requestBodyRating = String.format(DEFAULT_RATING_REQUEST_BODY, rideId, rate, comment);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBodyRating)
                .when()
                .post("http://localhost:8080/rating-service/ratings/passengers")
                .then()
                .statusCode(201);
    }

    @And("requests a rating of passenger")
    public void requestsARatingOfPassenger() {
        passengerRating = RestAssured.given()
                .when()
                .get("http://localhost:8080/rating-service/ratings/passengers/{id}", passengerId)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getDouble("rate");
    }

    @Then("the passenger's rating should be updated to {double}")
    public void thePassengerSRatingShouldBeUpdatedTo(double rate) {
        Assertions.assertEquals(passengerRating, rate);
    }
}

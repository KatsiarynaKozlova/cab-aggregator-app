Feature: Cab App Usage

  Scenario: User books a ride and rates driver and passenger
    Given a car with the following details: model "Test model", licensePlate "test12", year 2023, color "black"
    And a driver with the following details: name "Test name", email "testemail@mail.ru", phone "1029384756", sex "M"
    And a passenger with the following details: name "Test name", email "testemail@gmailcom", phone "1029384756"

    When requests a ride from "Location A" to "Location B"
    Then a ride should be created with status "CREATED"

    When the passenger rates the driver with a rate 5, comment "super music"
    And requests a rating of driver
    Then the driver's rating should be updated to 5.0

    When the driver rates the passenger with a rate 3, comment "dirty env"
    And requests a rating of passenger
    Then the passenger's rating should be updated to 4.0

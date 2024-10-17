Feature: Passenger service test
  Scenario: Create a new Passenger
    Given Passenger request with name "Leon", email "gunboy@gmail.com", phone "1357908642"
    When Passenger is created
    Then the Passenger should have email "gunboy@gmail.com", phone "1357908642"

  Scenario: Update existing Passenger
    Given Existing Passenger with id 1
    When update passenger with email "newgunboy@gmail.com", phone "01357908642"
    Then The response should have passengerId 1
    And Response have email "newgunboy@gmail.com", phone "01357908642"

  Scenario: Get Passenger by id when Passenger exist
    Given existing Passenger with id 1, name "Leon", email "gunboy@gmail.com", phone "1357908642"
    When the id 1 is passed to the findById method
    Then The response should contain passenger with id 1




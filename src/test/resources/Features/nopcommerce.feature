Feature: This feature holds the scenarios for testing the Nop Commerce Demo Site
  
  As a QA Engineer, I want to test the functionalities of NOP Commerce Site

  @Tests
  Scenario Outline: To Verify the User Registration Process  - [<TC>]
    Given Navigate to Home Page
    When Navigate the User to <Page> Page
    And Enter the "Gender" Information as "Male" in the Registration Page
    And Enter the "FirstName" Information as "tester" in the Registration Page
    And Enter the "LastName" Information as "cooler" in the Registration Page
    And Enter the "DOB" Information as "24/1995" in the Registration Page
    And Enter the "Email" Information as "test221123@test.com" in the Registration Page
    And Enter the "Company" Information as "Hello" in the Registration Page
    And Enter the "Password" Information as "Cooler" in the Registration Page
    Then Perform Click on the <BtnClick> button
    Then Verify the Registration message "Registration is Completed"

    Examples: 
      | TC                              | Page           | BtnClick   |
      | 'PTC-User Registration Success' | 'Registration' | 'Register' |

  @Test
  Scenario Outline: To Verify the User Registration Process  - [<TC>]
    Given Navigate to Home Page
    When Navigate the User to <Page> Page
    When Enter User Information Without <FieldType> in the Registration Page
    Then Perform Click on the <BtnClick> button
    Then Verify Error message <Message>

    Examples: 
      | TC                      | Page           | BtnClick   | Message                   | FieldType   |
      | 'NTC-FirstName Missing' | 'Registration' | 'Register' | 'First name is required.' | 'firstname' |
      | 'NTC-LastName Missing'  | 'Registration' | 'Register' | 'Last name is required.'  | 'lastname'  |
      | 'NTC-Email Missing'     | 'Registration' | 'Register' | 'Email is required.'      | 'email'     |
      | 'NTC-Password Missing'  | 'Registration' | 'Register' | 'Password is required.'   | 'password'  |

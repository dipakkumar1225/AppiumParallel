@Login
Feature: Login

  @RegressionOnUAT
  Scenario Outline: Check all the validation message when "<Case>"
    When Enter incorrect or incomplete username "<UserName>", "<Password>" and submit
    Then Verify displayed error message "<ErrorMessage>"
    Examples:
      | Case                                    | UserName      | Password     | ErrorMessage                                                 |
      | username is blank                       |               | secret_sauce | Username is required.                                        |
#      | password is blank                       | standard_user |              | Password is required.                                        |
#      | when entered user is incorrect          | standard      | secret_sauce | Username and password do not match any user in this service. |
#      | when entered password is incorrect      | standard_user | secret       | Username and password do not match any user in this service. |
#      | when both entered details are incorrect | standard      | secret       | Username and password do not match any user in this service. |


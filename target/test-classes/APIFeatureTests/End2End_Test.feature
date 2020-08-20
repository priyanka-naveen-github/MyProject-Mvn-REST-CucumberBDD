Feature:  Swagger UI| BookStore API
  Description: The purpose of these tests are to cover end-2-end happy test scenarios for Book Store APIs
  Site URL:     https://bookstore.toolsqa.com/swagger/

#   Note:
#   The given below parametrization will not work for parametrization based on "Examples" keyword
#   'Examples' keyword only works along with 'Scenario Outline'
#   Background: User generates token for Authorisation
#   Given I am an authorized user for "<User_Nm>" and "<User_Pwd>"

  Scenario Outline: The Authorized user can Add and Remove a book.
    Given I am an authorized user for "<User_Nm>" and "<User_Pwd>"
    And A list of books are available
  # When I add a book to my reading list
    When The book is added to my reading list
    Then I remove a book from my reading list
  # And The book is removed

  # Examples section to parameterize and executed BDD feature file for multiple records
    Examples:
      | User_Nm           | User_Pwd   |
      | TesterA_          | Tester@123 |
      | TesterB_          | Tester@123 |


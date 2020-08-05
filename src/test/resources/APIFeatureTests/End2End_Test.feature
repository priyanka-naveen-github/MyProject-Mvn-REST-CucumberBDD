Feature:  Swagger UI| BookStore API
  Description: The purpose of these tests are to cover end-2-end happy test scenarios for Book Store APIs
  Site URL:     https://bookstore.toolsqa.com/swagger/

Background: User generates token for Authorisation
  Given I am an authorized user


Scenario: The Authorized user can Add and Remove a book.
  Given A list of books are available
  When I add a book to my reading list
  Then The book is added
  When I remove a book from my reading list
  Then The book is removed
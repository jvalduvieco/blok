Feature: Webserver operations
  The webserver is alive
  Fetching an unmapped URL returns 404

  Scenario: The webserver is alive
    Given I can connect to my test webserver
    When I run alive check I get a 200
Feature: Comment operations
  I can create a comment
  I can delete a comment
  I can list a comment
  Break the system

  Scenario: Add a comment
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Joan" with content="Lorem Ipsum"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has 1 comment
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has a comment with content "Lorem Ipsum"

  Scenario: Delete a comment
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Joan" with content="Lorem Ipsum"
    When I delete one comment of post 91ff2946-187e-4114-a185-712600ef1622
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has 0 comment

  Scenario: Delete non existent comment
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I delete a non existent comment of post 91ff2946-187e-4114-a185-712600ef1622 404 is given

  Scenario: List Multiple comments
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Joan" with content="Lorem Ipsum"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Josep" with content="Lorem Ipsum"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Ase" with content="Lorem Ipsum"
    When I add a comment to post 91ff2946-187e-4114-a185-712600ef1622 by author="Pere" with content="Lorem Ipsum"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has 4 comment

  Scenario:
    When I add comments to a non existent post 404 is given

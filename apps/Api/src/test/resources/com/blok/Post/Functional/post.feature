Feature: Post operations
  I can create posts
  I can edit posts
  I can delete posts
  Malformed data is sent

  Scenario: Add a post
    When I insert a post with title "Foo" and content "bar"
    Then I have 1 posts
    Then the post has title "Foo"
    Then the post has content "bar"

  Scenario: Edit the title
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I edit post 91ff2946-187e-4114-a185-712600ef1622 setting title="Good post" and content="foo bar zum!"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has title "Good post"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has content "foo bar zum!"

  Scenario: Edit the content
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Good title" content="foo bar zum!"
    When I edit post 91ff2946-187e-4114-a185-712600ef1622 setting title="Good title" and content="Foo bar zum! Zum zum!"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has title "Good title"
    Then the post 91ff2946-187e-4114-a185-712600ef1622 has content "Foo bar zum! Zum zum!"

  Scenario: Delete a post
    Given that on the DB there is a post with UUID=91ff2946-187e-4114-a185-712600ef1622 title="Bad title" content="foo bar zum!"
    When I delete post 91ff2946-187e-4114-a185-712600ef1622
    Then post 91ff2946-187e-4114-a185-712600ef1622 is not found

  Scenario: Fuck the system
    When I send an invalid JSON to /posts I get a 401 response
    When I send an incomplete JSON to /posts U get a 401 response
    When I fetch an nonexistent post I get a 404 response

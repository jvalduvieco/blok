CREATE USER blog_owner WITH PASSWORD 'sparkforthewin';
SET timezone='UTC';
CREATE DATABASE blog_post;
\c blog_post
CREATE TABLE posts (
  id uuid primary key,
  title text not null,
  content text,
  publishing_date TIMESTAMP
);

CREATE TABLE comments (
  id uuid primary key,
  post_id uuid references posts(id) ON DELETE CASCADE,
  author text,
  content text,
  approved bool,
  submission_date TIMESTAMP
);

CREATE TABLE posts_categories (
  id uuid references posts(id) ON DELETE CASCADE,
  category text
);

GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE ON ALL TABLES IN SCHEMA public  TO blog_owner;
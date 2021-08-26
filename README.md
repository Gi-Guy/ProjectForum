# Project Forum
##### This is our final project in the university

A Reasful Forum web application developed in Java 11 SE & Powered by Spring Framework, Thymeleaf & MySQL.

## IMPORTANT NOTE
This project has been developed in Hebrew, Which means all HTML pages are built from right to left only. The java code has nothing to do with Hebrew and can be used for other languages.

## Technologys
- MySQL database
- JDBC Driver
- Spring Data JPA
- Spring MVC
- Spring Web
- Spring Security
- Spring Boot
- Thymeleaf
- HTML & CSS
------------
## Features
#### Users
- Anonymous users can register and create new Users.
- Anonymous users can login into existing user via login page.
- Users can create new Topics in existing forums, and send new comments (Posts) in existing topics.
- Users can send private messages to other existing users.
- Users can edit their own profile details.

#### Admins
- Users can be promoted to Admin role via control panel.
- Admins can changes other Users roles (ADMIN/USER/BLOCKED) or delete other users.
- Admins can create new forums or edit existing forums  via control panel.
- Admins can delete other users topics and posts.
- **Admin CAN'T read other users private messages.**

#### Control Panel
- Only Admins can access the control panel.
- Admins can create new forums or edit existing forums  via control panel.
- Admins can changes the forums priority (This will change the forums order in homepage)
- Admins can access to all users lists and edit users via search option or click on Edit user button.
- Admins can change the web name & description via control panel.
- Admins can change delete time for old topics & posts (topics will be deleted if they will inactive for X days)
- Admins can limit users private messages inbox for X conversations.


#### Scheduled Methods
In DeleteService class there are two scheduled methods that are running daily, Each method will delete any inactive Topics or inactive Private Conversations.
- Admins can set the 'time to delete' value in the control panel.
- Admins can't turn this off by setting value 0, However they can shut it off by removing "<task:annotation-driven>" in application properties file.

#### Known bugs
- There are some security issues.
- Rest API is open for all users and not only for admins, We didn't had time to add JWT tokens.
- Remember me isn't working well, We have tried to create a table of tokens but it isn't finished.
- There are some issues with LocalDateTime, it giving wrong times.

![](https://github.com/Gi-Guy/ProjectForum/blob/main/ForumHomePage.png?raw=true)

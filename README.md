# ProjectForum
 This is my final project in the university - A Java forum web application.

##################################################################################################

LAST UPDATE:

This update has a servel majors updates:

**YOU MUST TO CREATE A WHOLE NEW DATABASE AT THIS POINT**

*I'll update the cloud database soon.

**New Roles System:**

I've removed the old roles system and created a new one which now works in all leves of the application, including via thymeleaf pages.

**Services:**

I've created new services files which are working on the business layer,

There are services for editing entitys and delete entitys.

**When deleting a user, all user's activity will be not deleted but will be attached to a dummy user.

**ADMIN USER CAN'T BE DELETED via control panel nor via application services.

**DUMMY USER CAN'T BE DELETED via control panel nor via application services.


**Control Panel:**

I've finally created the control panel files and an temporary controller page.

**I've only finished the forums section.


**Initializing:**

When first initializing the application, The application will automatically create:

*All Entity's tables, including joinTables

*Admin user to access the application control panel.

*Unknown Dummy user to keep deleted users activity.


**Bugs:**

Fixed alot of bugs, the application is running smoothly.


**Known bugs:**

*Sometimes login page randomly move users to incorrect target page.


# discordBot
School project


Discord bot with database and API query functionalities
-	Reads user input for command to run weather report query and for location of weather report. Reacts to word "weather" in discord.
-	Handles exceptions that result from invalid location input
-	Saves user data (username, discord id, amount of sent messages) in SQL database
	
Program is built with Java using the following dependencies / libraries
-	JDA library for Discord functionalities
-	Jackson for parsing JSON-data that results from API-query into POJO
-	H2 for database functions
-	Junit4 for unit testing

After logging the bot into Discord, the program listens to channels that it is member of for incoming messages and filters off bots.
When non-bot user writes into a message channel the program checks if user is writing the first time by comparing users Discord id to map “users” that contains users as java objects identified (keyed) by Discord id; if not, a new Java object that represents user is created, saved in the map “users” and an instance is created in database. If user is the first one to be saved in database, a new table is created (after dropping old one).
Program reads user messages and depending on the message can change values in the Boolean fields of object that represents the user. Program executes at set interval and executes differently depending on the statuses of users. Program has currently the ability to provide user with weather data for the location of users choise via RapidAPI / weatherAPI.com.
Database is created, read and updated with H2 framework. Database has a table that stores a database id for user, users discord id, username and amount of sent messages. Number of messages is updated at every message the user sends. Program prints data from database to console when creating a new database instance as well as when updating an existing one.

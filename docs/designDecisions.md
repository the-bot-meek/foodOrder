# Design Decisions

## Tech Stack Overview
Since I already have some familiarity with Micronaut, Angular, Gradle, and Dynamodb I will be using these frameworks and infrastructures as my core tech stack.  
To make sure that this application is portable and easy to demo I am going to Dockerize it.

## API design
The majority of crud operation will be preformed through a restfull API. 
The only exception I can think of is when the **Organiser** of a **Meal** is viewing the **Participants** orders as I would like them to receive live updates.

## Database design
I have decided to go with a single table design to learn more about NO-SQL design patterns.

## Micronaut features and Java library
A lot of components may be overengineered to allow me to familiarise my self with some unfamiliar Micronaut features.
This includes Micronaut Cache and Reactor.

Their will also be other Micronaut features and Java library added out of necessity. For example:
- The Micronaut YAML features


## Monolith vs serverless architecture
I have decided to go with a Monolith architecture for this application as this demand will be predictable and manageable with a single Monolith for now.
I intend to keep the server as stateless as possible to allow for easy horizontal scaling in future if needed. 
This will be reelected in the authentication methods that I use, specifically JWT will be used.

## Build Tool
I have decided to go with Gradle as the build tool for this application as it is very develop friendly and I have some experience with it.

## Testing
Since I am already familiar with spock I have decided to use this as the testing framework for the Micronaut part of this application. Spock will also allow me to efficiently write tests for the application.
As of starting this project I have not yet done any testing in Angular. I have decided to stick with Karma to test my Angular application since it is the default testing framework when creating a new Angular application.


## Git branching model
TODO: probably going to a trunk based system to allow for easy build pipe line processes.
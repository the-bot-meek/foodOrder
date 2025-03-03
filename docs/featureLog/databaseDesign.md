There are several database objects many be need this includes
- Organiser
- Meal
- Order
- Participants
- Venue
- Venue Menu
- Venue Menu Item


## Table item design

To keep inline with the best practices of dynamodb I will try to make sure that I know my 
access pattern before trying to design my database and I will also try keep the number of items per primary key consistent per item type. 


### Organiser
At the moment we don't actually need a Organiser item type.
No additional information will be needed that is not already provided by the identify provider the user is authenticated against.

### Meal
Meals will need to be searchable by its uid to allow the application to list all meals associated to the Organiser.
The sort key will also need to have a composite key of the date of the meal and the id. 
If we just used the date of the meal then a organisation could only have one meal per day.
The location and venueName belonging to the menu associated to the Meal are also stored on the meal item as they will be needed to find the menu associated to a meal.
**PK format:** "Meal_{{uid}}"  
**SK format** "{{date_of_meal}}"

| Type      | Format / Name                |
|-----------|------------------------------|
| PK        | Meal_{{uid}}                 |
| SK        | {{date_of_meal}} {{meal_id}} |
| Attribute | Meal Name                    |
| Attribute | Venue ID                     |
| Attribute | Meal Id                      |
| Attribute | location                     |

### Order
Orders will need to be searchable by the meal_id of the meal it belongs to allow **Organisers** to find all orders of a meal.
Orders will also need to be searchable by the uid to allow **Participants** to find all the orders they have placed
The Participants Name will need to be stored with the Orders because the organiser of a meal will need to be able to see who placed an order.
In the future, we might want to switch or add identify providers. There is a unlikely scenario where two separate accounts on two separate identify providers have the same ids.
Adding in the identify_provider to the GSI prevents the users from being able to access each other information. 

| Type      | Format / Name                        |
|-----------|--------------------------------------|
| PK        | Order_{{meal_id}}                    |
| GSI PK    | Orders_{{identify_provider}}_{{uid}} |
| GSI SK    | {{date_of_meal}}                     |
| Attribute | Order Information                    |
| Attribute | Participants Name                    |


### Participants
At the moment we don't actually need a Participants item type. 
No additional information will be needed that is not already provided by the identify provider the user is authenticated against.

### Venue Menu & Venue Menu Item
Venue Menus will need to be searchable by their location.
Since a venues menu is not very likely to be updated I think it is fine to store the venues Menu item with in the same item.

| Type      | Format / Name        |
|-----------|----------------------|
| PK        | {{location}}}        |
| SK        | {{name}}             |
| Attribute | {{venues menu info}} |


## Single table vs multiple tables
All the item type can be kep in one table other than the Order item type as it requires a GSI that should not 
be a applied to the rest of the items in the table.
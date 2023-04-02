# YAML is a format to store the data like json and xml.

# Key-Value pair(object) in yaml
# Note you must have a space between colon and value:
Fruit: Apple
Vegetable: Carrot
Liquid: Water
Meat: Chicken
# <=>
{
	Fruit: Apple,
	Vegetable: Carrot,
	Liquid: Water,
	Meat: Chicken
}


# Array
Fruits:
-   Orange
-   Apple
-   Banana
	
Vegetables:
-   Carrot
-   Cauliflower
-   Tomato
# <=>
{
	Fruits: [Orange, Apple, Banana]
	Vegetables: [Carrot, Cauliflower, Tomato]
}


# Dictionary:
# Note, you should have an equal number of whitespaces before properties of a single item.
Banana:
	Calories: 105
	Fat: 0.4 g
	Carbs: 27 g
	
Grapes:
	Calories: 62
	Fat: 0.3 g
	Carbs: 16 g
# <=>
{
	Banana: {
		Calories: 105,
		Fat: 0.4
		Carbs: 27
	},
	Grapes: {
		Calories: 62,
		Fat: 0.3,
		Carbs: 16
	}
}

# Another example:
Color: Blue
Model:
	Name: Carvette
	Year: 1995
Transmisstion: Manual
Price: 20000
# <=>
{
	Color: Blue,
	Model: {
		Name: Carvette,
		Year: 1995
	}
	Transmisstion: Manual,
	Price: 20000
}

# Example, array of objects
- 	Color: Blue
	Model:
		Name: Carvette
		Year: 1995
	Transmisstion: Manual
	Price: 20000
	
- 	Color: Grey
	Model:
		Name: Carvette
		Year: 1996
	Transmisstion: Manual
	Price: 22000


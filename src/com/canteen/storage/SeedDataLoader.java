package com.canteen.storage;

import com.canteen.model.Category;
import com.canteen.model.FoodItem;
import com.canteen.model.Role;
import com.canteen.model.User;

import java.util.UUID;

public class SeedDataLoader {

    public static void seed(DataStore store) {
        // Seed Users
        if (store.getUsers().isEmpty()) {
            User admin = new User(
                    UUID.randomUUID().toString(),
                    "admin",
                    "admin123",
                    "Canteen Head Manager",
                    "ADMIN-001",
                    "admin@campusbites.edu",
                    "+91 98765 00001",
                    Role.ADMIN
            );
            store.addUser(admin);

            User student1 = new User(
                    UUID.randomUUID().toString(),
                    "student",
                    "student123",
                    "Aarav Mehta",
                    "2024CS101",
                    "aarav.cs@college.edu",
                    "+91 98765 11111",
                    Role.STUDENT
            );
            store.addUser(student1);

            User student2 = new User(
                    UUID.randomUUID().toString(),
                    "priya",
                    "priya123",
                    "Priya Sharma",
                    "2024EC205",
                    "priya.ec@college.edu",
                    "+91 98765 22222",
                    Role.STUDENT
            );
            store.addUser(student2);
        }

        // Seed Menu Items
        if (store.getMenuItems().isEmpty()) {
            // Breakfast
            store.addMenuItem(new FoodItem(
                    "item-1", "Crispy Masala Dosa", Category.BREAKFAST, 60.0,
                    "Thin crispy golden rice crepe filled with spiced potato masala, served with fresh coconut chutney & sambar.",
                    true, 8, "🥞"
            ));
            store.addMenuItem(new FoodItem(
                    "item-2", "Idli Vada Combo (2+1)", Category.BREAKFAST, 50.0,
                    "Two piping hot steamed rice idlis and one crispy medu vada with hot lentil sambar & mint chutney.",
                    true, 5, "🥟"
            ));
            store.addMenuItem(new FoodItem(
                    "item-3", "Indori Poha with Sev", Category.BREAKFAST, 35.0,
                    "Steamed flattened rice cooked with onions, mustard seeds, turmeric, crunchy roasted peanuts and lemon.",
                    true, 5, "🥣"
            ));
            store.addMenuItem(new FoodItem(
                    "item-4", "Punjabi Aloo Paratha (2 Pcs)", Category.BREAKFAST, 65.0,
                    "Whole wheat tawa parathas stuffed with seasoned spiced potatoes, served with chilled curd & butter.",
                    true, 10, "🫓"
            ));
            store.addMenuItem(new FoodItem(
                    "item-5", "Upma with Coconut Chutney", Category.BREAKFAST, 30.0,
                    "Savory roasted semolina tempered with mustard, curry leaves, ginger, and cashews.",
                    true, 6, "🍲"
            ));

            // Meals & Lunch
            store.addMenuItem(new FoodItem(
                    "item-6", "Special College Thali", Category.MEALS, 110.0,
                    "Complete wholesome meal: 3 butter rotis, Dal Tadka, Paneer Butter Masala, Jeera Rice, fresh salad & sweet.",
                    true, 12, "🍱"
            ));
            store.addMenuItem(new FoodItem(
                    "item-7", "Hyderabadi Veg Dum Biryani", Category.MEALS, 120.0,
                    "Long-grain fragrant basmati rice layered with spiced garden vegetables, saffron & herbs. Served with chilled raita.",
                    true, 10, "🍚"
            ));
            store.addMenuItem(new FoodItem(
                    "item-8", "Chole Bhature (2 Giant Bhature)", Category.MEALS, 85.0,
                    "Fluffy deep-fried golden bhaturas served with robust spicy Delhi-style chickpea curry and pickled onions.",
                    true, 8, "🍛"
            ));
            store.addMenuItem(new FoodItem(
                    "item-9", "Paneer Butter Masala + 2 Garlic Naan", Category.MEALS, 130.0,
                    "Soft cottage cheese cubes simmered in a velvety rich tomato-cashew gravy with warm tandoor garlic naans.",
                    true, 12, "🍲"
            ));
            store.addMenuItem(new FoodItem(
                    "item-10", "Veg Fried Rice with Veg Manchurian", Category.MEALS, 115.0,
                    "Indo-Chinese wok-tossed fried rice served with crispy vegetable balls in savory tangy garlic gravy.",
                    true, 10, "🥡"
            ));

            // Quick Snacks
            store.addMenuItem(new FoodItem(
                    "item-11", "Crispy Punjabi Samosa (2 Pcs)", Category.SNACKS, 30.0,
                    "Golden flaky crust pastries stuffed with aromatic cumin-spiced potatoes and peas, served with sweet tamarind chutney.",
                    true, 4, "🥟"
            ));
            store.addMenuItem(new FoodItem(
                    "item-12", "Jumbo Veg Grilled Cheese Sandwich", Category.SNACKS, 70.0,
                    "Multi-grain toasted bread stuffed with molten mozzarella cheese, capsicum, tomatoes and spicy coriander paste.",
                    true, 7, "🥪"
            ));
            store.addMenuItem(new FoodItem(
                    "item-13", "Paneer Tikka Roll", Category.SNACKS, 80.0,
                    "Smoky tandoori paneer wrapped in a warm layered paratha with mint mayonnaise, crisp onion rings, and chaat masala.",
                    true, 8, "🌯"
            ));
            store.addMenuItem(new FoodItem(
                    "item-14", "Peri-Peri French Fries", Category.SNACKS, 55.0,
                    "Crispy golden-cut potatoes fried to perfection and tossed in zesty peri-peri spices with cheesy dip.",
                    true, 6, "🍟"
            ));
            store.addMenuItem(new FoodItem(
                    "item-15", "Veg Hakka Noodles", Category.SNACKS, 75.0,
                    "Street-style wok-tossed noodles with shredded cabbage, crunchy bell peppers, spring onions, and chili soya sauce.",
                    true, 8, "🍜"
            ));

            // Beverages & Drinks
            store.addMenuItem(new FoodItem(
                    "item-16", "Kadak Masala Chai", Category.BEVERAGES, 15.0,
                    "Authentic college canteen ginger-cardamom steeped tea brewed with whole milk. The ultimate study fuel.",
                    true, 3, "☕"
            ));
            store.addMenuItem(new FoodItem(
                    "item-17", "Cold Coffee with Vanilla Ice Cream", Category.BEVERAGES, 60.0,
                    "Rich blended espresso shake topped with a hearty scoop of vanilla ice cream and Hershey's chocolate syrup.",
                    true, 4, "🧋"
            ));
            store.addMenuItem(new FoodItem(
                    "item-18", "Alphonso Mango Lassi", Category.BEVERAGES, 50.0,
                    "Thick, creamy churned sweet yogurt blended with real Alphonso mango pulp and garnished with crushed pistachios.",
                    true, 3, "🥛"
            ));
            store.addMenuItem(new FoodItem(
                    "item-19", "Fresh Mint Lime Soda (Sweet & Salt)", Category.BEVERAGES, 35.0,
                    "Fizzy chilled club soda with squeezed limes, crushed fresh mint leaves, rock salt, and sugar syrup.",
                    true, 3, "🍹"
            ));

            // Desserts
            store.addMenuItem(new FoodItem(
                    "item-20", "Warm Sizzling Chocolate Brownie", Category.DESSERTS, 75.0,
                    "Fudgy dark chocolate walnut brownie served warm with melted chocolate fudge sauce.",
                    true, 5, "🍫"
            ));
            store.addMenuItem(new FoodItem(
                    "item-21", "Royal Gulab Jamun (2 Pcs)", Category.DESSERTS, 40.0,
                    "Soft melt-in-mouth milk dumplings simmered in warm saffron-infused cardamom sugar syrup.",
                    true, 2, "🥮"
            ));
        }
    }
}

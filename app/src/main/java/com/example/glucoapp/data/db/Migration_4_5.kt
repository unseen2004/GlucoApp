package com.example.glucoapp.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the 'delete_user_data' trigger
        database.execSQL("""
            CREATE TRIGGER delete_user_data
            AFTER DELETE ON Users
            FOR EACH ROW
            BEGIN
                DELETE FROM Notes WHERE userId = OLD.userId;
                DELETE FROM Meals WHERE userId = OLD.userId;
                DELETE FROM Activities WHERE userId = OLD.userId;
            END
        """)

        // Create the 'check_meal_data' trigger
        database.execSQL("""
            CREATE TRIGGER check_meal_data
            BEFORE INSERT OR UPDATE ON Meals
            FOR EACH ROW
            BEGIN
                -- If predefinedMealId is provided, populate fields from PredefinedMeals
                IF NEW.predefinedMealId IS NOT NULL THEN
                    SELECT foodName, protein, carbs, fat, kcal
                    INTO NEW.foodName, NEW.protein, NEW.carbs, NEW.fat, NEW.kcal
                    FROM PredefinedMeals
                    WHERE predefinedMealId = NEW.predefinedMealId;
                END IF;

                -- Allow overriding values with custom values
                IF NEW.customProtein IS NOT NULL THEN
                  SET NEW.protein = NEW.customProtein;
                END IF;
                IF NEW.customCarbs IS NOT NULL THEN
                  SET NEW.carbs = NEW.customCarbs;
                END IF;
                IF NEW.customFat IS NOT NULL THEN
                  SET NEW.fat = NEW.customFat;
                END IF;
                IF NEW.customKcal IS NOT NULL THEN
                  SET NEW.kcal = NEW.customKcal;
                END IF;
            END
        """)
    }
}
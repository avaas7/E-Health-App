package com.example.myhealthapplication.Quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myhealthapplication.Quiz.QuizContract.QuestionsTable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 3;
    private static QuizDbHelper instance;
    private SQLiteDatabase db;
    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                QuizContract.CategoriesTable.TABLE_NAME + "( " +
                QuizContract.CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                QuizContract.CategoriesTable.TABLE_NAME + "(" + QuizContract.CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    private void fillCategoriesTable() {
        Category c1 = new Category("Human Body");
        insertCategory(c1);
        Category c2 = new Category("Nutrition");
        insertCategory(c2);
        Category c3 = new Category("Fitness");
        insertCategory(c3);
    }
    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }
    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();
        for (Category category : categories) {
            insertCategory(category);
        }
    }
    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(QuizContract.CategoriesTable.TABLE_NAME, null, cv);
    }
    private void fillQuestionsTable() {

        //Human Body
        Question q1 = new Question("About how many hairs do most people lose every day?",
                "75 Strands", "35 Strands", "50 Strands", 1,
                Question.DIFFICULTY_EASY, Category.HUMANBODY);
        insertQuestion(q1);
        Question q2 = new Question("The most common eye color in the world is:",
                "Brown", "Blue", "Green", 1,
                Question.DIFFICULTY_EASY, Category.HUMANBODY);
        insertQuestion(q2);
        Question q3 = new Question("How many muscles do you use when you talk?",
                "45", "55", "72", 3,
                Question.DIFFICULTY_EASY, Category.HUMANBODY);
        insertQuestion(q3);
        Question q4 = new Question("What does mucus do?",
                "Filters out harmful bacteria", "helps for respiration", "makes our body clean", 1,
                Question.DIFFICULTY_EASY, Category.HUMANBODY);
        insertQuestion(q4);

        //NUTRITION
        Question p1 = new Question("Which of the following nutrients provides energy to the body?",
                "carbohydrates", "vitamins", "water", 1,
                Question.DIFFICULTY_EASY, Category.NUTRITION);
        insertQuestion(p1);
        Question p2 = new Question("What is the main reason for eating a wide variety of foods? ",
                "to learn the food label", "to provide all the nutrients you need ", "to keep from getting bored with your diet ", 2,
                Question.DIFFICULTY_EASY, Category.NUTRITION);
        insertQuestion(p2);
        Question p3 = new Question("Carbohydrates are primarily responsible for giving the body _________.",
                "muscle", "energy", "bone development", 2,
                Question.DIFFICULTY_EASY, Category.NUTRITION);
        insertQuestion(p3);
        Question p4 = new Question("Basic building material of all the body's cells. ",
                "nutrient", "protein", "fruit", 2,
                Question.DIFFICULTY_EASY, Category.NUTRITION);
        insertQuestion(p4);

        //Fitness
        Question f1 = new Question("Which exercise boosts metabolism 24 hours a day, seven days a week?",
                "strength training", "yoga", "kickboxing", 1,
                Question.DIFFICULTY_EASY, Category.FITNESS);
        insertQuestion(f1);
        Question f2 = new Question("This exercise tool uses your own body weight to loosen tight muscles and improve blood flow.",
                "stability ball", "resistance band", "foam roller", 3,
                Question.DIFFICULTY_EASY, Category.FITNESS);
        insertQuestion(f2);
        Question f3 = new Question("Yoga poses designed to stretch and strengthen your body are called:",
                "stretches", "asanas", "chakras", 2,
                Question.DIFFICULTY_EASY, Category.FITNESS);
        insertQuestion(f3);
        Question f4 = new Question("What is the best way to rehydrate after exercise?",
                "eat fruit", "drink water", "drinks sports drinks", 2,
                Question.DIFFICULTY_EASY, Category.FITNESS);
        insertQuestion(q4);

    }
    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }
    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();
        for (Question question : questions) {
            insertQuestion(question);
        }
    }
    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.CategoriesTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(QuizContract.CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(QuizContract.CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};
        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
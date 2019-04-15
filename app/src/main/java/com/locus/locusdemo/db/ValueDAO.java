package com.locus.locusdemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locus.locusdemo.model.DataModel;
import com.locus.locusdemo.model.DataMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueDAO implements HelperInterface {
    private DatabaseHelper dbHelper;
    Gson gson = new Gson();

    private static ValueDAO instance = null;

    public static ValueDAO getInstance() {

        if (instance == null) {

            instance = new ValueDAO();
        }

        return instance;

    }

    private ValueDAO() {
        dbHelper = DatabaseHelper.getInstance();
    }


    public void saveUserData() {

        DataMap emptyDatamap = new DataMap();
        String emptyString = gson.toJson(emptyDatamap);
        DataMap optionDatamap = new DataMap();
        optionDatamap.setOptions((new ArrayList<>(Arrays.asList("Good,OK,Bad".split(",")))));
        String optionString = gson.toJson(optionDatamap);
        DataMap commentDatamap = new DataMap();
        commentDatamap.setComment("");
        commentDatamap.setShowComment(false);
        String commentString = gson.toJson(commentDatamap);

        String data = " [\n" +
                "{\n" +
                "type : PHOTO\",\n" +
                "\"id\" : \"pic1\",\n" +
                "\"title\" : \"Photo 1\",\n" +
                "\"dataMap\" :" + emptyString +
                "},\n" +
                "{\n" +
                "\"type\" : \"SINGLE_CHOICE\",\n" +
                "\"id\" : \"choice1\",\n" +
                "\"title\" : \"Photo 1 choice\",\n" +
                "\"dataMap\" : " + optionString +
                "},\n" +
                "{\n" +
                "\"type\" : \"COMMENT\",\n" +
                "\"id\" : \"comment1\",\n" +
                "\"title\" : \"Photo 1 comments\",\n" +
                "\"dataMap\":" + commentString +
                "},\n" +
                "{\n" +
                "\"type\" : \"PHOTO\",\n" +
                "\"id\" : \"pic2\",\n" +
                "\"title\" : \"Photo 2\",\n" +
                "\"dataMap\" :" + emptyString +
                "},\n" +
                "{\n" +
                "\"type\" : \"SINGLE_CHOICE\",\n" +
                "\"id\" : \"choice2\",\n" +
                "\"title\" : \"Photo 2 choice\",\n" +
                "\"dataMap\" : " + optionString +
                "},\n" +
                "{\n" +
                "\"type\" : \"COMMENT\",\n" +
                "\"id\" : \"comment2\",\n" +
                "\"title\" : \"Photo 2 comments\",\n" +
                "\"dataMap\" :" + commentString +
                "}\n" +
                "]";
        Type listType = new TypeToken<List<DataModel>>() {
        }.getType();

        Log.e("size", gson.toJson(data));

        List<DataModel> myModelList = new Gson().fromJson(data, listType);


        for (DataModel model : myModelList
        ) {
            Log.e("size", gson.toJson(model.getOptionModel().getOptions()));
        }
//        for (DataModel m :
//                myModelList) {
//            if (m.getType().equals("COMMENT"))
//                m.seoptionModel(new DataMap(new ArrayList<>(Arrays.asList("Good, OK, Bad".split(", ")))));
//        }


        synchronized (DatabaseHelper.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();
//
                for (DataModel m :
                        myModelList) {


                    ContentValues values = new ContentValues();
                    String options = "";
                    if (m.getOptionModel() != null)
                        options = gson.toJson(m.getOptionModel().getOptions());
                    values.put(DATA_ID, m.getId());
                    values.put(DATA_TITLE, m.getTitle());
                    values.put(DATA_TYPE, m.getType());
                    values.put(DATA_OPTIONS, options);
                    db.insert(DATA_TBLE, null, values);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void deleteData() {
        synchronized (DatabaseHelper.lock) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            try {
                db.beginTransaction();

                db.execSQL("delete from " + DATA_TBLE);
                db.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }

    }

    public ArrayList<DataModel> getData() {
        synchronized (DatabaseHelper.lock) {
            ArrayList<DataModel> namelist = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + DATA_TBLE, null);
            if (c.moveToFirst()) {
                do {
                    DataModel model = new DataModel();
                    String data = c.getString(3);

                    DataMap dataMap = new DataMap();
                    switch (c.getString(1))
                    {
                        case "PHOTO":
                            break;
                        case "SINGLE_CHOICE":
                                List<String> options = new ArrayList<>(Arrays.asList(c.getString(3).split(",")));
                                dataMap.setOptions(options);
                                break;

                        case "COMMENT":
                            if (data.contains("true"))
                            dataMap =  gson.fromJson(data, DataMap.class);
                            break;
                    }

                    model.setType(c.getString(1));
                    model.setId(c.getString(2));
                    model.setOptionModel(dataMap);
                    model.setTitle(c.getString(4));
                    namelist.add(model);
                } while (c.moveToNext());
            }

            c.close();
            db.close();

            return namelist;
        }
    }


    public void updateData(ArrayList<DataModel> dataModels) {
        synchronized (DatabaseHelper.lock) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                for (DataModel m :
                        dataModels) {
                    String options = gson.toJson(m.getOptionModel());
                    options = options.replace("\"", "");
                    ContentValues args = new ContentValues();
                    args.put(DATA_TITLE, m.getTitle());
                    args.put(DATA_TYPE, m.getType());
                    args.put(DATA_OPTIONS, options);
                    args.put(DATA_TITLE, m.getTitle());
                    db.update(DATA_TBLE, args, DATA_ID + "=" + "'" + m.getId() + "'", null);
                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
}

package com.example.databasetest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CourseRepository extends Repository<Course> {

	public CourseRepository(Context context) {
		sqLiteOpenHelper = new CourseOpenHelper(context, "", null, 1);
	}

	/**
	 * R�cuperation de la liste de tous les produits
	 */
	public List getAll() {
		// R�cup�ration de la liste des courses

		Cursor cursor = maBDD.query(CourseOpenHelper.COURSE_TABLE_NAME,
				new String[] { CourseOpenHelper.COLUMN_ID,
						CourseOpenHelper.COLUMN_PRODUIT,
						CourseOpenHelper.COLUMN_QUANTITE,
						CourseOpenHelper.COLUMN_ACHETE }, null, null, null,
				null, null, null);

		return ConvertCursorToListObject(cursor);
	}

	/**
	 * Retourne un seul produit
	 */
	public Course GetById(int id) {
		
		Cursor cursor = maBDD.query(CourseOpenHelper.COURSE_TABLE_NAME, new String[] { CourseOpenHelper.COLUMN_ID,
						CourseOpenHelper.COLUMN_PRODUIT,
						CourseOpenHelper.COLUMN_QUANTITE,
						CourseOpenHelper.COLUMN_ACHETE }, CourseOpenHelper.COLUMN_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
		
		return ConvertCursorToObject(cursor);
	}

	/**
	 * ENregistre un produit dans la base de donn�es
	 */
	public void save(Course entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CourseOpenHelper.COLUMN_PRODUIT, entite.getProduit());
		contentValues.put(CourseOpenHelper.COLUMN_PRODUIT, entite.getQuantite());
		contentValues.put(CourseOpenHelper.COLUMN_ACHETE, entite.isAchete());
		
		maBDD.insert(CourseOpenHelper.COURSE_TABLE_NAME, null, contentValues);
	}

	/**
	 * Met � jour un produit
	 */
	public void update(Course entite) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(CourseOpenHelper.COLUMN_PRODUIT, entite.getProduit());
		contentValues.put(CourseOpenHelper.COLUMN_PRODUIT, entite.getQuantite());
		contentValues.put(CourseOpenHelper.COLUMN_ACHETE, entite.isAchete());


	maBDD.update(CourseOpenHelper.COURSE_TABLE_NAME, contentValues, CourseOpenHelper.COLUMN_ID+"=?", new String[]{String.valueOf(entite.getId())});
	}

	/**
	 * Supprime un produit
	 */
	public void Delete(int id) {
		maBDD.delete(CourseOpenHelper.COURSE_TABLE_NAME, CourseOpenHelper.COLUMN_ID+"=?", new String[]{String.valueOf(id)});

	}

	
	/**
	 * Convertit un curseur en une liste de produits
	 */
	public List ConvertCursorToListObject(Cursor c) {

		List liste = new ArrayList<Course>();
		
		//Si la liste est vide
		if(c.getCount() == 0)
		{
			return liste;
		}
		else
		{
			//position sur le premier item
			
			//pour chaque item
			do
			{
				Course course = ConvertCursorToObject(c);
				liste.add(course);
			}
			while(c.moveToNext());
			
			
		}
		
		//fermeture du curseur
		c.close();
		
		return liste;
		
	}
	
	//M�thode utilis�e par ConvertCursorToObject et COnvertCursorToListObject

	public Course ConvertCursorToObject(Cursor c) {

		Course course = new Course(
				c.getString(CourseOpenHelper.NUM_COLUMN_PRODUIT),
				c.getInt(CourseOpenHelper.NUM_COLUMN_QUANTITE)
				);
		course.setId(c.getInt(CourseOpenHelper.NUM_COLUMN_ID));
		course.setAchete(c.getInt(CourseOpenHelper.NUM_COLUMN_ACHETE) != 0);

		return course;
	}

	
	/**
	 * Convertit un curseur en un produit
	 */
	public Course ConvertCursorToOneObject(Cursor c) {
		
		c.moveToFirst();
		
		Course course = ConvertCursorToObject(c);
		
		c.close();
		return course;
	}

}

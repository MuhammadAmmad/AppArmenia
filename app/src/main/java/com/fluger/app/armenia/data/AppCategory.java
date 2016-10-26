package com.fluger.app.armenia.data;

import java.util.ArrayList;
import java.util.List;

public class AppCategory {
    public int index;
	public String name;
    public String color;
    public int iconDrawableId;

	public List<AppCategoryItemData> childrenToShow = new ArrayList<AppCategoryItemData>();
	public ArrayList<AppCategoryItemData> children = new ArrayList<AppCategoryItemData>();
	
	public AppCategory(int index, String name, String color, int iconDrawableId) {
		this.index = index;
		this.name = name;
		this.color = color;
		this.iconDrawableId = iconDrawableId;
	}
}

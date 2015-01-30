package org.javaprotrepticon.android.tabatatime.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Exercise implements Parcelable {
	
	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;

	public Exercise() {}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private Exercise(Parcel parcel) {
		setId(parcel.readInt()); 
		setName(parcel.readString()); 
		setDescription(parcel.readString()); 
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId()); 
		parcel.writeString(getName());
		parcel.writeString(getDescription()); 
	} 

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
		public Exercise createFromParcel(Parcel parcel) {
			return new Exercise(parcel);
		}

		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
}

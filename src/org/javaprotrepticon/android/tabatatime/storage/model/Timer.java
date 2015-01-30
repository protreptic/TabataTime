package org.javaprotrepticon.android.tabatatime.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Timer implements Parcelable {
	
	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private Integer prepareInterval;
	
	@DatabaseField
	private Integer workInterval;
	
	@DatabaseField
	private Integer restInterval;
	
	@DatabaseField
	private Integer rounds;

	public Timer() {}
	
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

	public Integer getPrepareInterval() {
		return prepareInterval;
	}

	public void setPrepareInterval(Integer prepareInterval) {
		this.prepareInterval = prepareInterval;
	}

	public Integer getWorkInterval() {
		return workInterval;
	}

	public void setWorkInterval(Integer workInterval) {
		this.workInterval = workInterval;
	}

	public Integer getRestInterval() {
		return restInterval;
	}

	public void setRestInterval(Integer restInterval) {
		this.restInterval = restInterval;
	}

	public Integer getRounds() {
		return rounds;
	}

	public void setRounds(Integer rounds) {
		this.rounds = rounds;
	}
	
	private Timer(Parcel parcel) {
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
	
	public static final Parcelable.Creator<Timer> CREATOR = new Parcelable.Creator<Timer>() {
		public Timer createFromParcel(Parcel parcel) {
			return new Timer(parcel);
		}

		public Timer[] newArray(int size) {
			return new Timer[size];
		}
	};
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + id + ", name=" + name + ", description=" + description + ", prepareInterval=" + prepareInterval + ", workInterval=" + workInterval + ", restInterval=" + restInterval + ", rounds=" + rounds + "]";
	}
	
}

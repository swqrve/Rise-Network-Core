package me.swerve.profile;


import org.bson.Document;

public abstract class Profile {
    public abstract void save(Document document);
}

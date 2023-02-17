package me.swerve.permission;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class Permission { // save creation date, and time

    @Getter private static final List<Permission> permissions = new ArrayList<>();
    @Getter @Setter private int permissionLevel;
    @Getter @Setter private String permissionName;


    public Permission(int permissionLevel, String permissionName) {
        this.permissionLevel = permissionLevel;
        this.permissionName = permissionName;

        permissions.add(this);
    }
}

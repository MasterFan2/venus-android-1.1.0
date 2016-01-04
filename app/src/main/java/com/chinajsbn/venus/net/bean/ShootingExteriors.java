package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/12/30.
 */
public class ShootingExteriors {
    private String description;
    private String shootingExteriorId;
    private String shootingExteriorName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShootingExteriorId() {
        return shootingExteriorId;
    }

    public void setShootingExteriorId(String shootingExteriorId) {
        this.shootingExteriorId = shootingExteriorId;
    }

    public String getShootingExteriorName() {
        return shootingExteriorName;
    }

    public void setShootingExteriorName(String shootingExteriorName) {
        this.shootingExteriorName = shootingExteriorName;
    }

    public ShootingExteriors(String description, String shootingExteriorId, String shootingExteriorName) {

        this.description = description;
        this.shootingExteriorId = shootingExteriorId;
        this.shootingExteriorName = shootingExteriorName;
    }
}

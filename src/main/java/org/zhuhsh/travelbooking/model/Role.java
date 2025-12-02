package org.zhuhsh.travelbooking.model;

public enum Role {
    ADMIN,
    AGENT,
    TRAVELER;

    public String getSpringName() {
        return "ROLE_" + this.name();
    }
}



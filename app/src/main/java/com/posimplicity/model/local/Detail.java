package com.posimplicity.model.local;

public class Detail {
    private String name;
    private boolean enable;
    private boolean confirmationPopup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isConfirmationPopup() {
        return confirmationPopup;
    }

    public void setConfirmationPopup(boolean confirmationPopup) {
        this.confirmationPopup = confirmationPopup;
    }
}

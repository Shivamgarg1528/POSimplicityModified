package com.posimplicity.model.local;

import android.support.annotation.NonNull;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoleParent {

    @SerializedName("details")
    private List<RoleModel> details;

    public List<RoleModel> getDetails() {
        return details;
    }

    public static class RoleModel implements IModel, Comparable<RoleModel> {

        @SerializedName("roleName")
        private String roleName;

        @SerializedName("rolePassword")
        private String rolePassword;

        @SerializedName("roleActive")
        private boolean roleActive;

        @SerializedName("roleId")
        private String roleId;

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRolePassword() {
            return rolePassword;
        }

        public void setRolePassword(String rolePassword) {
            this.rolePassword = rolePassword;
        }

        public boolean isRoleActive() {
            return roleActive;
        }

        public void setRoleActive(boolean roleActive) {
            this.roleActive = roleActive;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        @Override
        public long getRowId() {
            return 0;
        }

        @Override
        public void setRowId(long pRowId) {

        }

        @Override
        public int compareTo(@NonNull RoleModel o) {
            return Integer.parseInt(roleId) - Integer.parseInt(o.getRoleId());
        }
    }
}

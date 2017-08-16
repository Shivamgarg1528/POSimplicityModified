package com.posimplicity.model.response.api;

import android.support.annotation.NonNull;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivam on 17/6/17.
 */

public class CategoryParent {

    @SerializedName("categories_list")
    private List<Category> dataList;

    public List<Category> getDataList() {
        return dataList;
    }


    public static class Category implements IModel, Comparable<Category> {

        @SerializedName("name")
        private String deptName;

        @SerializedName("category_id")
        private String deptId;

        @SerializedName("is_active")
        private String isDeptActive;

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getIsDeptActive() {
            return isDeptActive;
        }

        public void setIsDeptActive(String isDeptActive) {
            this.isDeptActive = isDeptActive;
        }

        @Override
        public long getRowId() {
            return 0;
        }

        @Override
        public void setRowId(long pRowId) {

        }

        @Override
        public int compareTo(@NonNull Category o) {
            return Integer.parseInt(deptId) - Integer.parseInt(o.deptId);
        }
    }
}

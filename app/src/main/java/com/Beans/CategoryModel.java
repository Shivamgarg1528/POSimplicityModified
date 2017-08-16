package com.Beans;

import android.support.annotation.NonNull;

import com.easylibs.sqlite.IModel;

public class CategoryModel implements Comparable<CategoryModel>,IModel {

    private String deptId;
    private String depStatus;
    private String deptName;

    public CategoryModel(String deptId, String depStatus, String deptName) {
        super();
        this.deptId = deptId;
        this.depStatus = depStatus;
        this.deptName = deptName;
    }

    public CategoryModel() {
        /* Empty Constructor */
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDepStatus() {
        return depStatus;
    }

    public void setDepStatus(String depStatus) {
        this.depStatus = depStatus;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public int compareTo(@NonNull CategoryModel pCategoryModel) {
        return Integer.parseInt(deptId) - Integer.parseInt(pCategoryModel.getDeptId());
    }

    @Override
    public long getRowId() {
        return 0;
    }

    @Override
    public void setRowId(long pRowId) {
    }
}
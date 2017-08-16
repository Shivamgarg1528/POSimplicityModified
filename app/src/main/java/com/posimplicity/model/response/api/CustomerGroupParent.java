package com.posimplicity.model.response.api;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivam on 17/6/17.
 */

public class CustomerGroupParent {

    @SerializedName("Group_Details")
    private List<CustomerGroup> GroupDetails;

    public List<CustomerGroup> getGroupDetails() {
        return GroupDetails;
    }

    public static class CustomerGroup implements IModel {

        @SerializedName("customer_group_id")
        private String customerGroupId;

        @SerializedName("customer_group_code")
        private String customerGroupCode;

        public String getCustomerGroupId() {
            return customerGroupId;
        }

        public void setCustomerGroupId(String customerGroupId) {
            this.customerGroupId = customerGroupId;
        }

        public String getCustomerGroupCode() {
            return customerGroupCode;
        }

        public void setCustomerGroupCode(String customerGroupCode) {
            this.customerGroupCode = customerGroupCode;
        }

        @Override
        public long getRowId() {
            return 0;
        }

        @Override
        public void setRowId(long pRowId) {

        }
    }
}

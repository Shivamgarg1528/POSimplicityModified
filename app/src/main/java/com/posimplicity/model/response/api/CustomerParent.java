package com.posimplicity.model.response.api;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivam on 17/6/17.
 */

public class CustomerParent {

    @SerializedName("customer_list")
    private List<Customer> dataList;

    public List<Customer> getCustomerList() {
        return dataList;
    }

    public static class Customer implements IModel {

        @SerializedName("customer_id")
        private String customerId;

        @SerializedName("group_id")
        private String customerGroupId;

        @SerializedName("email")
        private String customerEmail;

        @SerializedName("firstname")
        private String customerFirstName;

        @SerializedName("lastname")
        private String customerLastName;

        @SerializedName("telephone")
        private String customerTelephone;

        @SerializedName("address")
        private String customerAddress;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerGroupId() {
            return customerGroupId;
        }

        public void setCustomerGroupId(String customerGroupId) {
            this.customerGroupId = customerGroupId;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getCustomerFirstName() {
            return customerFirstName;
        }

        public void setCustomerFirstName(String customerFirstName) {
            this.customerFirstName = customerFirstName;
        }

        public String getCustomerLastName() {
            return customerLastName;
        }

        public void setCustomerLastName(String customerLastName) {
            this.customerLastName = customerLastName;
        }

        public String getCustomerTelephone() {
            return customerTelephone;
        }

        public void setCustomerTelephone(String customerTelephone) {
            this.customerTelephone = customerTelephone;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
        }

        @Override
        public long getRowId() {
            return 0;
        }

        @Override
        public void setRowId(long pRowId) {

        }

        public String getFullName() {
            return getCustomerFirstName() + getCustomerLastName();
        }
    }
}

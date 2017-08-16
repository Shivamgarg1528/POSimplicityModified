package com.posimplicity.interfaces;

import com.posimplicity.model.response.api.CustomerParent;

public interface OnTransactionAssignListener {
    void onTransactionAssigned(CustomerParent.Customer pAssignedCustomer);
}

package com.posimplicity.model.response.other;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

    @SerializedName("Details")
    private List<DetailsBean> detailList;

    public List<DetailsBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailsBean> Details) {
        this.detailList = Details;
    }

    public static class DetailsBean {

        private String comment;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("increment_id")
        private String incrementId;
        @SerializedName("shipping_id")
        private String shippingId;
        @SerializedName("billing_id")
        private String billingId;
        private String status;
        @SerializedName("payment_method")
        private String paymentMode;
        @SerializedName("Order_details")
        private List<OrderDetailsBean> orderDetailList;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getIncrementId() {
            return incrementId;
        }

        public void setIncrementId(String incrementId) {
            this.incrementId = incrementId;
        }

        public String getShippingId() {
            return shippingId;
        }

        public void setShippingId(String shippingId) {
            this.shippingId = shippingId;
        }

        public String getBillingId() {
            return billingId;
        }

        public void setBillingId(String billingId) {
            this.billingId = billingId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public List<OrderDetailsBean> getOrderDetailList() {
            return orderDetailList;
        }

        public void setOrderDetailList(List<OrderDetailsBean> Order_details) {
            this.orderDetailList = Order_details;
        }

        public static class OrderDetailsBean {

            private String product_id;
            private String qty_ordered;
            private String price;
            private String tax_amount;
            private String tax_percent;
            private String product_discount;
            private List<?> option_details;

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getQty_ordered() {
                return qty_ordered;
            }

            public void setQty_ordered(String qty_ordered) {
                this.qty_ordered = qty_ordered;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTax_amount() {
                return tax_amount;
            }

            public void setTax_amount(String tax_amount) {
                this.tax_amount = tax_amount;
            }

            public String getTax_percent() {
                return tax_percent;
            }

            public void setTax_percent(String tax_percent) {
                this.tax_percent = tax_percent;
            }

            public String getProduct_discount() {
                return product_discount;
            }

            public void setProduct_discount(String product_discount) {
                this.product_discount = product_discount;
            }

            public List<?> getOption_details() {
                return option_details;
            }

            public void setOption_details(List<?> option_details) {
                this.option_details = option_details;
            }
        }
    }
}

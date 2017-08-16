package com.posimplicity.model.local;

import android.support.annotation.NonNull;

import java.util.List;

public class CheckoutParent {

    private int productId;
    private int productQty;

    private float productPrice;
    private float productDisPercentage;
    private float productDisDollar;
    private float productTaxRate;
    private float productOptionPrice;

    private boolean isProductImageAvailable;
    private boolean isProductSelected;
    private boolean isProductDisApplied;

    private String productName;
    private String productImageUrl;

    private List<ProductOptions> productOptions;

    //locally added
    private String mSelectedIds;

    public CheckoutParent() {
        this.productId = productId;
        this.productOptions = productOptions;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isProductDisApplied() {
        return isProductDisApplied;
    }

    public void setProductDisApplied(boolean productDisApplied) {
        isProductDisApplied = productDisApplied;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public float getProductDisPercentage() {
        return productDisPercentage;
    }

    public void setProductDisPercentage(float productDisPercentage) {
        this.productDisPercentage = productDisPercentage;
    }

    public float getProductDisDollar() {
        return productDisDollar;
    }

    public void setProductDisDollar(float productDisDollar) {
        this.productDisDollar = productDisDollar;
    }

    public boolean isIsProductSelected() {
        return isProductSelected;
    }

    public void setIsProductSelected(boolean isProductSelected) {
        this.isProductSelected = isProductSelected;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public boolean isProductImageAvailable() {
        return isProductImageAvailable;
    }

    public void setProductImageAvailable(boolean productImageAvailable) {
        this.isProductImageAvailable = productImageAvailable;
    }

    public float getProductTaxRate() {
        return productTaxRate;
    }

    public void setProductTaxRate(float productTaxRate) {
        this.productTaxRate = productTaxRate;
    }

    public float getProductOptionPrice() {
        return productOptionPrice;
    }

    public void setProductOptionPrice(float productOptionPrice) {
        this.productOptionPrice = productOptionPrice;
    }

    public List<ProductOptions> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptions> productOptions) {
        this.productOptions = productOptions;
    }

    // Local getter and setter method for changing qty, calculations {}

    /**
     * it will increase productQty by one iff productQty < 999
     */
    public boolean increaseQtyByOneIfPossible() {
        if (productQty < 999) {
            productQty++;
            return true;
        }
        return false;
    }

    /**
     * it will decrease productQty by one iff productQty > 1
     */
    public boolean decreaseQtyByOneIfPossible() {
        if (productQty > 1) {
            productQty--;
            return true;
        }
        return false;
    }

    public float getProductAndOptionPrice() {
        return (productPrice + productOptionPrice) * productQty;
    }

    public float getProductAndOptionDiscount() {
        return (getProductAndOptionPrice() * productDisPercentage * .01f) + productDisDollar;
    }

    public float getProductAndOptionTax() {
        return getProductAndOptionPrice() * productTaxRate * .01f;
    }

    public float getProductAndOptionPriceAfterDiscount() {
        return getProductAndOptionPrice() - getProductAndOptionDiscount();
    }

    public float getFinalPrice() {
        return getProductAndOptionPriceAfterDiscount() + getProductAndOptionTax();
    }

    public String getSelectedIds() {
        return mSelectedIds;
    }

    public void setSelectedIds(String mSelectedIds) {
        this.mSelectedIds = mSelectedIds;
    }

    public static class ProductOptions implements Comparable<ProductOptions> {

        private int optionId;
        private int optionSortOrder;
        private boolean optionMandatory;
        private String optionName;
        private List<OptionSubOptions> optionSubOptions;

        //locally added
        private boolean isOptionSelected;

        public int getOptionId() {
            return optionId;
        }

        public void setOptionId(int optionId) {
            this.optionId = optionId;
        }

        public String getOptionName() {
            return optionName;
        }

        public void setOptionName(String optionName) {
            this.optionName = optionName;
        }

        public int getOptionSortOrder() {
            return optionSortOrder;
        }

        public void setOptionSortOrder(int optionSortOrder) {
            this.optionSortOrder = optionSortOrder;
        }

        public boolean isOptionMandatory() {
            return optionMandatory;
        }

        public void setOptionMandatory(boolean optionMandatory) {
            this.optionMandatory = optionMandatory;
        }

        public List<OptionSubOptions> getOptionSubOptions() {
            return optionSubOptions;
        }

        public void setOptionSubOptions(List<OptionSubOptions> optionSubOptions) {
            this.optionSubOptions = optionSubOptions;
        }

        public boolean isOptionSelected() {
            return isOptionSelected;
        }

        public void setOptionSelected(boolean optionSelected) {
            isOptionSelected = optionSelected;
        }

        @Override
        public int compareTo(@NonNull ProductOptions o) {
            return optionSortOrder - o.optionSortOrder;
        }

        public static class OptionSubOptions implements Comparable<OptionSubOptions> {

            private int subOptionId;
            private int subOptionSortOrder;
            private float subOptionPrice;
            private boolean isSubOptionSelected;
            private String subOptionName;

            public int getSubOptionId() {
                return subOptionId;
            }

            public void setSubOptionId(int subOptionId) {
                this.subOptionId = subOptionId;
            }

            public String getSubOptionName() {
                return subOptionName;
            }

            public void setSubOptionName(String subOptionName) {
                this.subOptionName = subOptionName;
            }

            public float getSubOptionPrice() {
                return subOptionPrice;
            }

            public void setSubOptionPrice(float subOptionPrice) {
                this.subOptionPrice = subOptionPrice;
            }

            public boolean isSubOptionSelected() {
                return isSubOptionSelected;
            }

            public void setIsSubOptionSelected(boolean isSubOptionSelected) {
                this.isSubOptionSelected = isSubOptionSelected;
            }

            public int getSubOptionSortOrder() {
                return subOptionSortOrder;
            }

            public void setSubOptionSortOrder(int subOptionSortOrder) {
                this.subOptionSortOrder = subOptionSortOrder;
            }

            @Override
            public int compareTo(@NonNull OptionSubOptions o) {
                return subOptionSortOrder - o.subOptionSortOrder;
            }
        }
    }
}

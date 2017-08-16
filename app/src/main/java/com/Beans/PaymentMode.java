package com.Beans;

import java.util.List;

public class PaymentMode {


    /**
     * paymentModeId : 1
     * paymentModeName : Credit
     * paymentModeSortId : 1
     * paymentModeStatus : true
     * paymentModeDeletable : false
     * paymentModeType : tender
     * paymentModeDes : 
     */

    private List<PaymentModeBean> paymentModeList;

    public List<PaymentModeBean> getPaymentMode() {
        return paymentModeList;
    }

    public void setPaymentMode(List<PaymentModeBean> paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public static class PaymentModeBean implements Comparable<PaymentModeBean>{
    	
    	
        /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "PaymentModeBean [paymentModeId=" + paymentModeId
					+ ", paymentModeName=" + paymentModeName
					+ ", paymentModeSortId=" + paymentModeSortId + "]";
		}
		

		private int paymentModeId;
        private String paymentModeName;
        private int    paymentModeSortId;
        private boolean paymentModeStatus;
        private boolean paymentModeDeletable;
        private String paymentModeType;
        private String paymentModeDes;

        public int getPaymentModeId() {
            return paymentModeId;
        }

        public void setPaymentModeId(int paymentModeId) {
            this.paymentModeId = paymentModeId;
        }

        public String getPaymentModeName() {
            return paymentModeName;
        }

        public void setPaymentModeName(String paymentModeName) {
            this.paymentModeName = paymentModeName;
        }

        public int getPaymentModeSortId() {
            return paymentModeSortId;
        }

        public void setPaymentModeSortId(int paymentModeSortId) {
            this.paymentModeSortId = paymentModeSortId;
        }

        public boolean isPaymentModeStatus() {
            return paymentModeStatus;
        }

        public void setPaymentModeStatus(boolean paymentModeStatus) {
            this.paymentModeStatus = paymentModeStatus;
        }

        public boolean isPaymentModeDeletable() {
            return paymentModeDeletable;
        }

        public void setPaymentModeDeletable(boolean paymentModeDeletable) {
            this.paymentModeDeletable = paymentModeDeletable;
        }

        public String getPaymentModeType() {
            return paymentModeType;
        }

        public void setPaymentModeType(String paymentModeType) {
            this.paymentModeType = paymentModeType;
        }

        public String getPaymentModeDes() {
            return paymentModeDes;
        }

        public void setPaymentModeDes(String paymentModeDes) {
            this.paymentModeDes = paymentModeDes;
        }

		@Override
		public int compareTo(PaymentModeBean o) {
			return paymentModeSortId - o.paymentModeSortId;
		}
    }
}

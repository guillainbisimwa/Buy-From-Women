package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FinanceVendor Data
 */

public class Finance implements Parcelable {

    private boolean isOutstandingLoan;
    private boolean hasMobileMoneyAccount;

    //Loan Purpose
    private boolean isInput;
    private boolean isAggregation;
    private boolean isOtherLp;

    private int totLoanAmount;
    private int totOutstanding;
    private double interestRate;
    private int durationInMonth;
    private String loanProvider;

    public Finance() {

    }

    public Finance(boolean isOutstandingLoan, boolean hasMobileMoneyAccount, boolean isInput, boolean isAggregation,
                   boolean isOtherLp, int totOutstanding, double interestRate, int durationInMonth,
                   String loanProvider, int totLoanAmount) {
        this.totLoanAmount = totLoanAmount;
        this.isOutstandingLoan = isOutstandingLoan;
        this.hasMobileMoneyAccount = hasMobileMoneyAccount;
        this.isInput = isInput;
        this.isAggregation = isAggregation;
        this.isOtherLp = isOtherLp;
        this.totOutstanding = totOutstanding;
        this.interestRate = interestRate;
        this.durationInMonth = durationInMonth;
        this.loanProvider = loanProvider;
    }

    public Finance(Parcel data) {
        this.totLoanAmount = data.readInt();
        this.isOutstandingLoan = data.readByte() != 0;
        this.hasMobileMoneyAccount = data.readByte() != 0;
        this.isInput = data.readByte() != 0;
        this.isAggregation = data.readByte() != 0;
        this.isOtherLp = data.readByte() != 0;
        this.totOutstanding = data.readInt();
        this.interestRate = data.readDouble();
        this.durationInMonth = data.readInt();
        this.loanProvider = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totLoanAmount);
        parcel.writeByte((byte) (isOutstandingLoan ? 1 : 0));
        parcel.writeByte((byte) (hasMobileMoneyAccount ? 1 : 0));
        parcel.writeByte((byte) (isInput ? 1 : 0));
        parcel.writeByte((byte) (isAggregation ? 1 : 0));
        parcel.writeByte((byte) (isOtherLp ? 1 : 0));
        parcel.writeInt(totOutstanding);
        parcel.writeDouble(interestRate);
        parcel.writeInt(durationInMonth);
        parcel.writeString(loanProvider);
    }

    public int getTotLoanAmount() {
        return totLoanAmount;
    }

    public void setTotLoanAmount(int totLoanAmount) {
        this.totLoanAmount = totLoanAmount;
    }

    public boolean isOutstandingLoan() {
        return isOutstandingLoan;
    }

    public void setOutstandingLoan(boolean outstandingLoan) {
        isOutstandingLoan = outstandingLoan;
    }

    public boolean isHasMobileMoneyAccount() {
        return hasMobileMoneyAccount;
    }

    public void setHasMobileMoneyAccount(boolean hasMobileMoneyAccount) {
        this.hasMobileMoneyAccount = hasMobileMoneyAccount;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    public boolean isAggregation() {
        return isAggregation;
    }

    public void setAggregation(boolean aggregation) {
        isAggregation = aggregation;
    }

    public boolean isOtherLp() {
        return isOtherLp;
    }

    public void setOtherLp(boolean otherLp) {
        isOtherLp = otherLp;
    }

    public int getTotOutstanding() {
        return totOutstanding;
    }

    public void setTotOutstanding(int totOutstanding) {
        this.totOutstanding = totOutstanding;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getDurationInMonth() {
        return durationInMonth;
    }

    public void setDurationInMonth(int durationInMonth) {
        this.durationInMonth = durationInMonth;
    }

    public String getLoanProvider() {
        return loanProvider;
    }

    public void setLoanProvider(String loanProvider) {
        this.loanProvider = loanProvider;
    }

    public static final Parcelable.Creator<Finance> CREATOR = new Parcelable.Creator<Finance>() {
        @Override
        public Finance createFromParcel(Parcel parcel) {
            return new Finance(parcel);
        }

        @Override
        public Finance[] newArray(int i) {
            return new Finance[0];
        }
    };
}
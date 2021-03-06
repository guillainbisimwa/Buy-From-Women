package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * DemographicVendor Data
 */

public class DemographicVendor implements Parcelable {

    public static final Creator<DemographicVendor> CREATOR = new Creator<DemographicVendor>() {
        @Override
        public DemographicVendor createFromParcel(Parcel parcel) {
            return new DemographicVendor(parcel);
        }

        @Override
        public DemographicVendor[] newArray(int i) {
            return new DemographicVendor[0];
        }
    };
    private boolean isHouseHoldHead;
    private int houseHoldMember;
    private String spouseFirstName;
    private String spouseLastName;
    private String cellPhoneAlt;
    private String cellCarrier;
    private String memberShipId;

    public DemographicVendor() {

    }

    public DemographicVendor(boolean isHouseHold, int houseHoldMember, String spouseFirstName, String spouseLastName,
                             String cellPhoneAlt, String cellCarrier, String memberShipId) {
        this.isHouseHoldHead = isHouseHold;
        this.houseHoldMember = houseHoldMember;
        this.spouseFirstName = spouseFirstName;
        this.spouseLastName = spouseLastName;
        this.cellPhoneAlt = cellPhoneAlt;
        this.cellCarrier = cellCarrier;
        this.memberShipId = memberShipId;
    }

    public DemographicVendor(Parcel data) {
        this.isHouseHoldHead = data.readByte() != 0;
        this.houseHoldMember = data.readInt();
        this.spouseFirstName = data.readString();
        this.spouseLastName = data.readString();
        this.cellPhoneAlt = data.readString();
        this.cellCarrier = data.readString();
        this.memberShipId = data.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isHouseHoldHead ? 1 : 0));
        parcel.writeInt(houseHoldMember);
        parcel.writeString(spouseFirstName);
        parcel.writeString(spouseLastName);
        parcel.writeString(cellPhoneAlt);
        parcel.writeString(cellCarrier);
        parcel.writeString(memberShipId);
    }

    public boolean isHouseHold() {
        return isHouseHoldHead;
    }

    public void setHouseHold(boolean houseHold) {
        isHouseHoldHead = houseHold;
    }

    public int getHouseHoldMember() {
        return houseHoldMember;
    }

    public void setHouseHoldMember(int houseHoldMember) {
        this.houseHoldMember = houseHoldMember;
    }

    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getCellPhoneAlt() {
        return cellPhoneAlt;
    }

    public void setCellPhoneAlt(String cellPhoneAlt) {
        this.cellPhoneAlt = cellPhoneAlt;
    }

    public String getCellCarrier() {
        return cellCarrier;
    }

    public void setCellCarrier(String cellCarrier) {
        this.cellCarrier = cellCarrier;
    }

    public String getMemberShipId() {
        return memberShipId;
    }

    public void setMemberShipId(String memberShipId) {
        this.memberShipId = memberShipId;
    }
}

package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessToInformation implements Parcelable {

    public static final Creator<AccessToInformation> CREATOR = new Creator<AccessToInformation>() {
        @Override
        public AccessToInformation createFromParcel(Parcel parcel) {
            return new AccessToInformation(parcel);
        }

        @Override
        public AccessToInformation[] newArray(int i) {
            return new AccessToInformation[0];
        }
    };
    private boolean isAgricultureExtension;
    private boolean isClimateRelatedInformation;
    private boolean isSeed;
    private boolean isOrganicFertilizers;
    private boolean isInorganicFertilizers;
    private boolean isLabour;
    private boolean isWaterPumps;
    private boolean isSpreaderOrSprayer;
    private int seasonId;
    private int infoId;
    private String seasonName;

    public AccessToInformation() {
        this.infoId = 0;
    }


    public AccessToInformation(String seasonName, boolean isAgricultureExtension, boolean isClimateRelatedInformation, boolean isSeed,
                               boolean isOrganicFertilizers, boolean isInorganicFertilizers, boolean isLabour,
                               boolean isWaterPumps, boolean isSpreaderOrSprayer, int seasonId) {
        this.isAgricultureExtension = isAgricultureExtension;
        this.isClimateRelatedInformation = isClimateRelatedInformation;
        this.isSeed = isSeed;
        this.isOrganicFertilizers = isOrganicFertilizers;
        this.isInorganicFertilizers = isInorganicFertilizers;
        this.isLabour = isLabour;
        this.isWaterPumps = isWaterPumps;
        this.isSpreaderOrSprayer = isSpreaderOrSprayer;
        this.seasonName = seasonName;
        this.seasonId = seasonId;
    }

    public AccessToInformation(Parcel source) {
        this.isAgricultureExtension = source.readByte() != 0;
        this.isClimateRelatedInformation = source.readByte() != 0;
        this.isSeed = source.readByte() != 0;
        this.isOrganicFertilizers = source.readByte() != 0;
        this.isInorganicFertilizers = source.readByte() != 0;
        this.isLabour = source.readByte() != 0;
        this.isWaterPumps = source.readByte() != 0;
        this.isSpreaderOrSprayer = source.readByte() != 0;
        this.seasonName = source.readString();
        this.seasonId = source.readInt();
        this.infoId = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isAgricultureExtension ? 1 : 0));
        parcel.writeByte((byte) (isClimateRelatedInformation ? 1 : 0));
        parcel.writeByte((byte) (isSeed ? 1 : 0));
        parcel.writeByte((byte) (isOrganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isInorganicFertilizers ? 1 : 0));
        parcel.writeByte((byte) (isLabour ? 1 : 0));
        parcel.writeByte((byte) (isWaterPumps ? 1 : 0));
        parcel.writeByte((byte) (isSpreaderOrSprayer ? 1 : 0));
        parcel.writeString(seasonName);
        parcel.writeInt(seasonId);
        parcel.writeInt(infoId);
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getHarvsetSeason() {
        return seasonName;
    }

    public void setHarvsetSeason(String seasonName) {
        this.seasonName = seasonName;
    }

    public boolean isAgricultureExtension() {
        return isAgricultureExtension;
    }

    public void setAgricultureExtension(boolean agricultureExtension) {
        isAgricultureExtension = agricultureExtension;
    }

    public boolean isClimateRelatedInformation() {
        return isClimateRelatedInformation;
    }

    public void setClimateRelatedInformation(boolean climateRelatedInformation) {
        isClimateRelatedInformation = climateRelatedInformation;
    }

    public boolean isSeed() {
        return isSeed;
    }

    public void setSeed(boolean seed) {
        isSeed = seed;
    }

    public boolean isOrganicFertilizers() {
        return isOrganicFertilizers;
    }

    public void setOrganicFertilizers(boolean organicFertilizers) {
        isOrganicFertilizers = organicFertilizers;
    }

    public boolean isInorganicFertilizers() {
        return isInorganicFertilizers;
    }

    public void setInorganicFertilizers(boolean inorganicFertilizers) {
        isInorganicFertilizers = inorganicFertilizers;
    }

    public boolean isLabour() {
        return isLabour;
    }

    public void setLabour(boolean labour) {
        isLabour = labour;
    }

    public boolean isWaterPumps() {
        return isWaterPumps;
    }

    public void setWaterPumps(boolean waterPumps) {
        isWaterPumps = waterPumps;
    }

    public boolean isSpreaderOrSprayer() {
        return isSpreaderOrSprayer;
    }

    public void setSpreaderOrSprayer(boolean spreaderOrSprayer) {
        isSpreaderOrSprayer = spreaderOrSprayer;
    }
}

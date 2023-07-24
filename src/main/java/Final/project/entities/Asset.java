package Final.project.entities;

public class Asset {
    private int assetID;
    private String assetName;
    private String assetType;

    // Constructors, getters, setters, and other methods

    public int getAssetID() {
        return assetID;
    }

    public void setAssetID(int assetID) {
        this.assetID = assetID;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Asset asset = (Asset) o;

        if (assetID != asset.assetID) return false;
        if (!assetName.equals(asset.assetName)) return false;
        return assetType.equals(asset.assetType);
    }

    @Override
    public int hashCode() {
        int result = assetID;
        result = 31 * result + assetName.hashCode();
        result = 31 * result + assetType.hashCode();
        return result;
    }
}

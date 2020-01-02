package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City_Data_Datas {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public City_Data_Datas() {
    }

    /**
     *
     * @param area
     * @param pincode
     * @param city
     * @param id
     * @param status
     */
    public City_Data_Datas(String id, String city, String pincode, String area, String status) {
        super();
        this.id = id;
        this.city = city;
        this.pincode = pincode;
        this.area = area;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

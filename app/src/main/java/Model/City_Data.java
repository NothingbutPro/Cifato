package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class City_Data {

    @SerializedName("responce")
    @Expose
    private Boolean responce;
    @SerializedName("data")
    @Expose
    private ArrayList<City_Data_Datas> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public City_Data() {
    }

    /**
     *
     * @param data
     * @param responce
     */
    public City_Data(Boolean responce, ArrayList<City_Data_Datas> data) {
        super();
        this.responce = responce;
        this.data = data;
    }

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public ArrayList<City_Data_Datas> getData() {
        return data;
    }

    public void setData(ArrayList<City_Data_Datas> data) {
        this.data = data;
    }
}

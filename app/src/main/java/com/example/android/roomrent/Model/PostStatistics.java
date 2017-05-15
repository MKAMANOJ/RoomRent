package com.example.android.roomrent.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostStatistics {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("is_last_page")
    @Expose
    private Boolean isLastPage;
    @SerializedName("data")
    @Expose
    private List<PostDatum> data = null;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public PostStatistics(Integer count, Integer total, Integer offset, Boolean isLastPage, List<PostDatum> data, String code, String message) {
        this.count = count;
        this.total = total;
        this.offset = offset;
        this.isLastPage = isLastPage;
        this.data = data;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "PostStatistics{" +
                "count=" + count +
                ", total=" + total +
                ", offset=" + offset +
                ", isLastPage=" + isLastPage +
                ", data=" + data +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public Integer getCount() {
        return count;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public List<PostDatum> getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

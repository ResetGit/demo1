package com.example.demo.pojo;

import java.util.List;

public class ColumnarModel {
    /**
     * 柱状图的标题
     */
    private String title;

    /**
     * 顶部数据
     */
    private List<String> legendData;

    /**
     * 横坐标数据
     */
    private List<Object> xData;

    /**
     * 柱状图上的具体数据
     */
    private List<Object> seriesData;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLegendData() {
        return legendData;
    }

    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }

    public List<Object> getxData() {
        return xData;
    }

    public void setxData(List<Object> xData) {
        this.xData = xData;
    }

    public List<Object> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<Object> seriesData) {
        this.seriesData = seriesData;
    }
}

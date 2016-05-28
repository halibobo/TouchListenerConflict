package me.daei.touchlistenerconflict.bean;

/**
 * Created by su on 2016/5/28.
 */
public class GridCutItem {


    private String name;

    private String tip;

    public GridCutItem( String name ,String tip) {
        this.name = name;
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

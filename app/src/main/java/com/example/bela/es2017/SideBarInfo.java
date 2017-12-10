package com.example.bela.es2017;

/**
 * Classe que define os detalhes da SideBar sendo usada
 * Created by klaus on 09/12/17.
 */

public class SideBarInfo {

    private String title = "";
    private int layoutID;

    public SideBarInfo(String title, int layoutID) {
        this.title = title;
        this.layoutID = layoutID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }
}

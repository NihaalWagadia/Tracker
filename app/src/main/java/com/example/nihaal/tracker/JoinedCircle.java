package com.example.nihaal.tracker;

public class JoinedCircle {

    public String conected_id, connected_name;

    public String getConected_id() {
        return conected_id;
    }

    public void setConected_id(String conected_id) {
        this.conected_id = conected_id;
    }

    public String getConnected_name() {
        return connected_name;
    }

    public void setConnected_name(String connected_name) {
        this.connected_name = connected_name;
    }



    public JoinedCircle(String conected_id, String connected_name){
        this.conected_id = conected_id;
        this.connected_name = connected_name;
    }
    public JoinedCircle(){}
}

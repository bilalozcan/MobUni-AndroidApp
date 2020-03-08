package com.fmbg.moobuni.Model;

public class Department {
    private String bolum_id;
    private String fakulte_id;
    private String universite_id;
    private String name;
    private String status;

    public String getBolum_id() {
        return bolum_id;
    }

    public void setBolum_id(String bolum_id) {
        this.bolum_id = bolum_id;
    }

    public String getFakulte_id() {
        return fakulte_id;
    }

    public void setFakulte_id(String fakulte_id) {
        this.fakulte_id = fakulte_id;
    }

    public String getUniversite_id() {
        return universite_id;
    }

    public void setUniversite_id(String universite_id) {
        this.universite_id = universite_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

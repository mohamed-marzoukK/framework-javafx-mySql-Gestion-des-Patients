package com.hopital.miniprojet.data;

public class PatientMed {
    private int refMed;
    private String cinPat;

    public PatientMed(int refMed, String cinPat) {
        this.refMed = refMed;
        this.cinPat = cinPat;
    }

    public int getRefMed() {
        return refMed;
    }

    public void setRefMed(int refMed) {
        this.refMed = refMed;
    }

    public String getCinPat() {
        return cinPat;
    }

    public void setCinPat(String cinPat) {
        this.cinPat = cinPat;
    }

}

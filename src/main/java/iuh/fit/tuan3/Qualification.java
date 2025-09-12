package iuh.fit.tuan3;

/**
 * Copyright (c) 2025 by trant.
 * All rights reserved.
 * This file is part of tranthanhtai_22673121.
 */
public class Qualification {
    private String id;
    private String examination;
    private String borad;
    private float percentage;
    private int year;


    public Qualification(String id, String examination, String borad, float percentage, int year) {
        this.id = id;
        this.examination = examination;
        this.borad = borad;
        this.percentage = percentage;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getBorad() {
        return borad;
    }

    public void setBorad(String borad) {
        this.borad = borad;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Qualification{" +
                "id='" + id + '\'' +
                ", examination='" + examination + '\'' +
                ", borad='" + borad + '\'' +
                ", percentage=" + percentage +
                ", year=" + year +
                '}';
    }
}

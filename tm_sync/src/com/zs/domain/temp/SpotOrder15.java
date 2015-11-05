package com.zs.domain.temp;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Allen on 2015/10/29.
 */

public class SpotOrder15 {
    private String spotCode;
    private String courseCode;
    private String name;
    private String isbn;
    private String author;
    private String price;
    private String count;
    private String isDo;

    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIsDo() {
        return isDo;
    }

    public void setIsDo(String isDo) {
        this.isDo = isDo;
    }
}

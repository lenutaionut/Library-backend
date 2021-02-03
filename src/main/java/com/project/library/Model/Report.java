package com.project.library.Model;

import javax.persistence.*;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] file;

    @Column
    private String name;

    public Integer getReportId() {
        return reportId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

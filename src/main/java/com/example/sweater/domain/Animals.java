package com.example.sweater.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "animals")
public class Animals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id")
    private User master;

    private String animalType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    private String sex;

    @NotNull
    private String nickName;


    public Animals() {
    }

    public Animals(String animalType, Date birthDate, String sex, String nickName, User user) {
        this.master = user;
        this.animalType = animalType;
        this.birthDate = birthDate;
        this.sex = sex;
        this.nickName = nickName;
    }

    public String getMasterName() {
        return master != null ? master.getUsername() : "none";
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

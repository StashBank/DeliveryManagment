package com.stashbank.deliveryManagement.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


/**
 * Created by dn160671kav on 18.08.2016.
 */
@Table(name = "PairedDevice",  id = BaseColumns._ID)
public class PairedDevice extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    //тип устройства терминал pinpad - "t", printer - "p"
    @Column(name = "type")
    private String type;


    @Column(name = "serialnumber")
    private String serialnumber;


    public PairedDevice() {
        super();
    }

    public PairedDevice(String name, String address, String type) {
        super();
        this.name = name; //произвольное имя заданное пользователем
        this.address = address;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialnumber;
    }

    public void setSerialNumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }
}

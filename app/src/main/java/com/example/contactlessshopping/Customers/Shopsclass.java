package com.example.contactlessshopping.Customers;
import com.google.firebase.firestore.IgnoreExtraProperties;

public class Shopsclass {
    private String name;
    private String from;
    private String to,address,capacity,emailid,lat,lon,pass,phno,shopcat;



    public Shopsclass()
    {

    }
    public Shopsclass(String address,String capacity,String email_id,String from_time,String latitude,String longitude,String password,String phone_number,String shop_category,String shop_name,String to_time)
    {
        this.name=shop_name;
        this.from=from_time;
        this.to=to_time;
        this.address=address;
        this.capacity=capacity;
        this.emailid=email_id;
        this.lat=latitude;
        this.lon=longitude;
        this.pass=password;
        this.phno=phone_number;
        this.shopcat=shop_category;
    }

    public String getshop_name()
    {
        return name;
    }
    public String getfrom_time()
    {
        return from;
    }
    public String getto_time()
    {
        return to;
    }

    public void setshop_name(String n)
    {
        this.name=n;
    }

    public void setfrom_time(String n)
    {
        this.from=n;
    }

    public void setto_time(String n)
    {
        this.to=n;
    }
    public void setemail_id(String n)
    {
        this.emailid=n;
    }

}

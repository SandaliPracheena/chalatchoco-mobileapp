package lk.example.chalatchoco.model;

public class Order {

    private int id;
    private String product_id;

    private String name;
    private String user;
    private String mobile;
    private String type;
    private String price;
    private byte[] image;


    public Order(String product_id, String name, String type,String price, byte[] image) {
        this.id = id;
        this.product_id = product_id;
        this.name = name;
        this.user = user;
        this.mobile = mobile;
        this.type = type;
        this.price = price;
        this.image = image;
    }

    public Order(String name, float typeValue) {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

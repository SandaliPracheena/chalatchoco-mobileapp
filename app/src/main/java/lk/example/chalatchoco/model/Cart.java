package lk.example.chalatchoco.model;

public class Cart {

    private String id;
    private String product_id;
    private String name;
    private String user;
    private String price;
    private byte[] image;



    public Cart(String productId, String user, String name, String price, byte[] image) {
        this.id = id;
        this.product_id = productId;
        this.name = name;
        this.user = user;
        this.price = price;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

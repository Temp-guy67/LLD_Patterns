package Uber;

public abstract class User {
    protected String id;
    protected String name;
    protected String phone;
    protected String email;

    public User(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
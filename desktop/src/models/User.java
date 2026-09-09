package models;

import java.util.Date;
import enums.Role;
import enums.AccountStatus;

public class User {

    private final long id;
    private final long employee_id;
    private final String name;
    private final String email;
    private final String password_hash;
    private final Role role;
    private final AccountStatus account_status;
    private final Date created_at;
    private final Date updated_at;

    public User(long id, long employee_id, String name, String email, String password_hash, Role role, AccountStatus account_status, Date created_at, Date updated_at) {
        this.id = id;
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.account_status = account_status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return this.id;
    }

    public long getEmployee_id() {
        return this.employee_id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword_hash() {
        return this.password_hash;
    }

    public Role getRole() {
        return this.role;
    }

    public AccountStatus getAccount_status() {
        return this.account_status;
    }

    public Date getCreated_at() {
        return this.created_at;
    }

    public Date getUpdated_at() {
        return this.updated_at;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"id\": " + this.id + ",\n" +
                "  \"employee_id\": " + this.employee_id + ",\n" +
                "  \"username\": \"" + this.name + "\",\n" +
                "  \"email\": \"" + this.email + "\",\n" +
                "  \"password_hash\": \"" + this.password_hash + "\",\n" +
                "  \"role\": \"" + this.role + "\",\n" +
                "  \"account_status\": \"" + this.account_status + "\",\n" +
                "  \"created_at\": \"" + this.created_at + "\",\n" +
                "  \"updated_at\": \"" + this.updated_at + "\"\n" +
                "}";
    }
}
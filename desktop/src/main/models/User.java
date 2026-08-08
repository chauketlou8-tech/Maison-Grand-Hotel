package main.models;

import java.util.Date;
import utils.enums.Role;
import utils.enums.AccountStatus;

/**
 * Represents anyone who can log into the hotel management system.
 *
 * This class stores user authentication and authorization information
 * for all system users including admins, receptionists, managers,
 * and other hotel staff members.
 */
public class User {

    private final long user_id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Role role;
    private final AccountStatus accountStatus;
    private final Date createdAt;
    private final Date lastLoginAt;

    /**
     * Constructs a new User object with the specified details.
     *
     * @param user_id       Unique user identifier
     * @param firstName     User's first name
     * @param lastName      User's last name
     * @param email         User's email address
     * @param password      User's password (will be hashed before storage)
     * @param role          User's role determining permissions
     * @param accountStatus Current status of the account
     * @param createdAt     Account creation timestamp
     * @param lastLoginAt   Timestamp of the last login
     */
    public User(long user_id, String firstName, String lastName, String email, String password, Role role, AccountStatus accountStatus, Date createdAt, Date lastLoginAt) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return this.role;
    }

    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getLastLoginAt() {
        return this.lastLoginAt;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"user_id\": " + this.user_id + ",\n" +
                "  \"firstName\": \"" + this.firstName + "\",\n" +
                "  \"lastName\": \"" + this.lastName + "\",\n" +
                "  \"email\": \"" + this.email + "\",\n" +
                "  \"role\": \"" + this.role + "\",\n" +
                "  \"accountStatus\": \"" + this.accountStatus + "\",\n" +
                "  \"createdAt\": \"" + this.createdAt + "\",\n" +
                "  \"lastLoginAt\": \"" + this.lastLoginAt + "\"\n" +
                "}";
    }
}
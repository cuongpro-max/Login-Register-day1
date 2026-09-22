package user.dto.response;

public class UserProfileResponse {
    private Long id;
    private String username;
    private String name;
    private String phone;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, String username, String name, String phone) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

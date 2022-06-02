package raf.si.racunovodstvo.user.requests;

import lombok.Data;
import raf.si.racunovodstvo.user.model.Permission;

import java.util.List;

@Data
public class UpdateUserRequest {

    private Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private int preduzeceId;

    private List<Permission> permissions;
}

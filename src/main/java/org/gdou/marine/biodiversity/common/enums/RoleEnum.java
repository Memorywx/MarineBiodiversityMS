package org.gdou.marine.biodiversity.common.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(0, "ADMIN", "管理员"),
    RESEARCHER(1, "RESEARCHER", "科研人员/教师"),
    STUDENT(2, "STUDENT", "学生"),
    PUBLIC(3, "PUBLIC", "公众");

    private final int code;
    private final String role;
    private final String desc;

    RoleEnum(int code, String role, String desc) {
        this.code = code;
        this.role = role;
        this.desc = desc;
    }

    public static RoleEnum getByCode(int code) {
        for (RoleEnum r : values()) {
            if (r.code == code) {
                return r;
            }
        }
        return PUBLIC;
    }
}

package utilities.fileread;

import lombok.AccessLevel;
import lombok.Getter;

public class MyUser {

    @Getter(AccessLevel.PUBLIC)
    private String payee_Name;

    @Getter(AccessLevel.PUBLIC)
    private String account_Number;

    public MyUser() {

    }

    public MyUser(String payee_Name, String account_Number) {
        this.payee_Name = payee_Name;
        this.account_Number = account_Number;
    }

}
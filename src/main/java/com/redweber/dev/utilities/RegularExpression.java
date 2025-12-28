package com.redweber.dev.utilities;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class RegularExpression {
    private static final Pattern USERNAME_PATTERN =
    Pattern.compile("^[A-Za-z][A-Za-z0-9]{5,14}$");

private static final Pattern PASSWORD_PATTERN =
    Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])\\S{8,}$");

public  boolean validUsername(String uname) {
if (uname == null) return false;
return USERNAME_PATTERN.matcher(uname).matches();
}

public  boolean validPassword(String pwd) {
if (pwd == null) return false;
return PASSWORD_PATTERN.matcher(pwd).matches();
}
}

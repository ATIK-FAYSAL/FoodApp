package com.atikfaysal.foodapp.others;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation
{
    private Matcher matcher;
    private Pattern pattern;



    public boolean userNameValidation(String username)
    {
        String regx = "^[a-zA-Z0-9._-]{3,}$";
        pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(username);

        //with regx char ,name length is greater than 5 and less than 25
        return matcher.find() && (username.length() >= 5 && username.length() <= 15);
    }


    /*
     *** Name POLICY ***

     * Name must be at least 5 chars
     * Does not contains no digit
     * Can contains uppercase and lowercase
     * Contains only [.] this special char
     * Maximum 25 chars
     */
    public boolean nameValidation(String name)
    {
        String regx = "^[\\p{L} .'-]+$";
        pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(name);

        //with regx char ,name length is greater than 5 and less than 25
        return matcher.find() && (name.length() >= 5 && name.length() <= 25);
    }

    /*
     *** Email POLICY ***

     * Email must be at least 8 chars
     * Does contains any digit or number
     * Can contains lowercase and uppercase
     * Contains only [. _ % @] this special char
     * Maximum 45 chars
     */

    public boolean emailValidation(String email)
    {
        String regx = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);

        //with regx char ,name length is greater than 5 and less than 25
        return matcher.find() && (email.length() >= 8 && email.length() <= 40);
    }


    /*
     *** PASSWORD POLICY ***

     * password must be at least 8 chars
     * Contains at least one digit
     * Contains at least one lower case and one upper case char
     * Contains at least one char with a set of special chars [@#$%^]
     * Does not contain space or tab
     */
    public boolean passwordValidation(String pass)
    {

        String regx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pass);

        //with regx char ,name length is greater than 5 and less than 25
        return matcher.find() && (pass.length() >= 8 && pass.length() <= 16);
    }


    /*
     *** Url POLICY ***

     * URL must be at least 5 chars
     * Does contains any digit
     * Can contains uppercase and lowercase
     * Should start http:// or https:// or www.
     * Maximum 40 chars
     */

    public boolean linkValidation(String link)
    {
        String regx = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);

        //with regx char ,name length is greater than 5 and less than 25
        return matcher.find() && (link.length() >= 8 && link.length() <= 40);
    }


}

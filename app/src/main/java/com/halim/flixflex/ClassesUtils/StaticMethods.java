package com.halim.flixflex.ClassesUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.halim.flixflex.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that contains methods that are used in different places
 */

public class StaticMethods {

    //Regex for Email validation
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //Regex for Password validation
    public static final String VALID_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]+$";

    //Method that check the stabality of user's internet connection
    public static boolean isInternetAvailable() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


    //Method that display a Snackbar with no internet error
    public static void snackBarNoInternet(Context context) {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_available), Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(context.getColor(R.color.black));
        snackbar.setTextColor(context.getColor(R.color.white));
        snackbar.show();
    }

    //Method that display a Snackbar with error
    public static void snackBarError(Context context) {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.oops_an_error_has_occurred), Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(context.getColor(R.color.thirdColor));
        snackbar.setTextColor(context.getColor(R.color.white));
        snackbar.show();
    }

    //Method that display a date example : format giver 2022-11-12 the result will be 12 Nov 2022
    public static String getDateText(String myDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH);
        Date date = sdf.parse(myDate);
        sdf.applyPattern("d MMM yyyy");
        return sdf.format(date);
    }

    //Method that convert minutes to hours and minutes example : given minute 125 the result is 2h 5min
    public static String convertMinuteToHour(int runtime) {
        return runtime / 60 + "h " + runtime % 60 + "m";
    }

    //Method that launch YouTube with specific video
    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    //Method that launch Vimeo with specific video
    public static void watchVimeoVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://player.vimeo.com/video/" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.vimeo_is_not_installed), Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(context.getColor(R.color.thirdColor));
            snackbar.setTextColor(context.getColor(R.color.white));
            snackbar.show();
        }
    }

    //Method that check if Email is valid
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //Method that check if Password contains at least 8 caracters and numbers
    public static boolean checkPassword(String password) {
        return password.length() >= 8 && password.matches(VALID_PASSWORD_REGEX);
    }
}

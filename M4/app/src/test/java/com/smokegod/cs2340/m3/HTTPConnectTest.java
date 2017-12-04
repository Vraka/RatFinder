package com.smokegod.cs2340.m3;

/**
 * Created by Vraka on 12/4/2017.
 */

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HTTPConnectTest {
    @Test
    public void HTTP_Test() {
        attemptRegister();
    }

    private void attemptRegister() {
        HttpURLConnection client = null;
        try {
            URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/register");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("login_name","username");
            client.setRequestProperty("password","password");
            client.setRequestProperty("contact_info","test@test.com");
            client.setRequestProperty("isAdmin","true");
            client.setDoOutput(true);
            BufferedOutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            String output = "";
            outputPost.write(output.getBytes());
            System.out.println(output);
            outputPost.flush();
            outputPost.close();
        } catch (Exception e) {

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }


        //mAuth.createUserWithEmailAndPassword(usernameET.getEditableText().toString(),
        //        passwordET.getEditableText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //   @Override
        //    public void onComplete(@NonNull Task<AuthResult> task) {
        //        if (!task.isSuccessful()) {
        //            Toast t = Toast.makeText(RegisterActivity.this, "There seems to be an error " +
        //                            "with your registration information! Maybe try a longer password!",
        //                    Toast.LENGTH_LONG);
        //            t.show();
        //        }
        //    }
        //});
    }

}

package com.acme.db.poc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;
import com.dropbox.core.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Ricsi
 * Date: 2013.10.20.
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public class Starter {
    public static final String APP_KEY = "ux99ccbt25ygh0n";
    public static final String APP_SECRET = "x09ykq8wl1vc1hh";


    public static void main(String[] args) throws IOException, DbxException {

        DbxRequestConfig config = new DbxRequestConfig("dp_poc", Locale.getDefault().toString());
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        String accesToken;

        //Enable just if you need a new token
        // accesToken = getAccesToken(webAuth);
        List<String> tokens= Arrays.asList( "gLOPbRh-1-YAAAAAAAAAAYeI0g6k8E-q_hRJhV37IIwl8wIKQwwg4fJBodWm9MWJ","BftrdnPh_LEAAAAAAAAAAWKeYoFzEKPO-0xphYD3MUy-Ar_msUJBMl7ykkwJowTa");
       // accesToken = "gLOPbRh-1-YAAAAAAAAAAYeI0g6k8E-q_hRJhV37IIwl8wIKQwwg4fJBodWm9MWJ";
        for(String actualToken : tokens){
            DbxClient client = loginWithDBApi(config, actualToken);
            //listingFiles(client);
        }
    }

    public static String getAccesToken(DbxWebAuthNoRedirect webAuth) throws IOException, DbxException {
        String authorizeUrl = webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click(you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        DbxAuthFinish authFinish = webAuth.finish(code);
        return authFinish.accessToken;
    }

    private static DbxClient loginWithDBApi(DbxRequestConfig config, String accesToken) throws DbxException {
        DbxClient client = new DbxClient(config, accesToken);
        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        return client;
    }

    public static void listingFiles(DbxClient client) throws DbxException {
        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            listChildFiles(child, client, "");
        }
    }

    public static void listChildFiles(DbxEntry child, DbxClient client, String prefix) throws DbxException {
        if (child.isFolder()) {
            printFileOrFolder(prefix, child);
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(child.path);
            for (DbxEntry actual : listing.children) {
                listChildFiles(actual, client, prefix + "\t");
            }
        } else {
            printFileOrFolder(prefix, child);
        }
    }

    public static void printFileOrFolder(String prefix, DbxEntry child){
        System.out.println(prefix + " " + child.name +" path: "+child.path);
    }
}

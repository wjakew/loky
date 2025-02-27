/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.loky.entity;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Object for storing registered application data
 */
public class Application {
    
    ObjectId id;
    
    String name;
    String description;

    String application_url;               // application url - for showing the application custom login screen
    
    String unique_redirect_url;          // unique redirect url for application - redirect after login
    
    boolean is_active;                     // true if application is active
    boolean is_registration_allowed;       // true if registration is allowed       

    boolean passwordAuthentication;       // true if password authentication is allowed
    boolean ldapAuthentication;             // true if ldap authentication is allowed
    boolean passwordlessAuthentication;     // true if passwordless authentication is allowed

    JSONArray linkedPermissions;

    ObjectId smtp_server_id;             // link to smtp server configuration on database

    ObjectId ldap_server_id;             // link to ldap server configuration on database

    /**
     * Constructor for Application
     * @param document
     */
    public Application(Document document){
        this.id = document.getObjectId("_id");
        this.name = document.getString("name");
        this.description = document.getString("description");
        this.unique_redirect_url = document.getString("unique_redirect_url");
        this.is_active = document.getBoolean("is_active");
        this.is_registration_allowed = document.getBoolean("is_registration_allowed");
        this.passwordAuthentication = document.getBoolean("passwordAuthentication");
        this.ldapAuthentication = document.getBoolean("ldapAuthentication");
        this.passwordlessAuthentication = document.getBoolean("passwordlessAuthentication");
        this.linkedPermissions = new JSONArray(document.getList("linkedPermissions", String.class));
        this.smtp_server_id = document.getObjectId("smtp_server_id");
        this.ldap_server_id = document.getObjectId("ldap_server_id");
    }

    /**
     * Constructor for Application
     */ 
    public Application(){
        this.id = null;
        this.name = "";
        this.description = "";
        this.unique_redirect_url = "";
        this.is_active = false;
        this.is_registration_allowed = false;
        this.passwordAuthentication = false;
        this.ldapAuthentication = false;
        this.passwordlessAuthentication = false;
        this.linkedPermissions = new JSONArray();
        this.smtp_server_id = null;
        this.ldap_server_id = null;
    }

    /**
     * Convert Application to Document
     * @return Document
     */
    public Document toDocument(){
        Document document = new Document();
        document.append("name", this.name);
        document.append("description", this.description);
        document.append("unique_redirect_url", this.unique_redirect_url);
        document.append("is_active", this.is_active);
        document.append("is_registration_allowed", this.is_registration_allowed);
        document.append("passwordAuthentication", this.passwordAuthentication);
        document.append("ldapAuthentication", this.ldapAuthentication);
        document.append("passwordlessAuthentication", this.passwordlessAuthentication);
        document.append("linkedPermissions", this.linkedPermissions);
        document.append("smtp_server_id", this.smtp_server_id);
        document.append("ldap_server_id", this.ldap_server_id);
        return document;
    }

    
}

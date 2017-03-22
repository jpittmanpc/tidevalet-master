
package org.xmlrpc.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.SessionManager;
import com.tidevalet.thread.adapter;
import com.tidevalet.thread.constants;

import org.json.JSONArray;
import org.wordpress.android.MediaFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WebUtils {
    static SessionManager sessionManager;
    static String URL = App.getSiteUrl();
    static long id = 0;
    public static String editPost(String comments, long ID, Context context) throws XMLRPCException {
        id = ID;
        XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
        HashMap<String, Object> struct = new HashMap<String, Object>();
        Hashtable keyvalpair = new Hashtable();
        List<Hashtable> array = new ArrayList<Hashtable>();
        keyvalpair.put("key", constants.POST_COMMENTS);
        keyvalpair.put("value", comments);
        array.add(keyvalpair);
        struct.put("custom_fields", array);
        Object result = null;
        sessionManager = new SessionManager(context);
        String username = sessionManager.getUsername();
        String password = sessionManager.getPassword();
        int violationid = (int) ID;
        Object[] params = new Object[]{1, username, password, violationid, struct, true};
        try {
            result = client.call("wp.editPost", params);
        } catch (XMLRPCException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static HashMap<String, String> uploadPostToWordpress(String image,
                                                                String violation_type,
                                                                String bldg,
                                                                String unit,
                                                                String comments,
                                                                Integer picked_up,
                                                                String type,
                                                                Context context, long ID)
            throws XMLRPCException {
        id = ID;
        List<String> imageURL = new ArrayList<>();
        sessionManager = new SessionManager(context);
        Object[] params;
        for (String img : image.split(",")) {
            if (!img.contains("http")) {
                String result = uploadImage(img, context);
                imageURL.add(result);
            } else if (img.contains("http")) {
                 imageURL.add(img);
            }
        }
        XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
        // Setup Post
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("post_type", "violations");
        content.put("post_title", bldg + " / " + unit);
        content.put("post_content", prepareBodyOfPost(violation_type, imageURL));
        content.put("post_status", "publish");
        // Term Fields
        HashMap<String, List<String>> tax = new HashMap<String, List<String>>();
        List<String> property = new ArrayList<String>();
        property.add(String.valueOf(sessionManager.propertySelected()));
        tax.put("properties", property);
        content.put("terms", tax);
        //Custom Fields
        List<Hashtable> customFieldsList = new ArrayList<Hashtable>();
        Hashtable customField = new Hashtable();
        customField.put("key", constants.POST_LOCAL_IMAGE_PATH);
        customField.put("value", imageURL.toString());
        customFieldsList.add(customField);
        customField = new Hashtable();
        customField.put("key", constants.POST_VIOLATION_TYPE);
        customField.put("value", violation_type);
        customFieldsList.add(customField);
        customField = new Hashtable();
        customField.put("key", constants.POST_BLDG);
        customField.put("value", bldg);
        customFieldsList.add(customField);
        customField = new Hashtable();
        customField.put("key", constants.POST_UNIT);
        customField.put("value", unit);
        customFieldsList.add(customField);
        customField = new Hashtable();
        customField.put("key", constants.POST_COMMENTS);
        customField.put("value", comments);
        customFieldsList.add(customField);
        customField = new Hashtable();
        customField.put("key", constants.PICKEDUP);
        customField.put("value", picked_up);
        customFieldsList.add(customField);
        content.put("custom_fields", customFieldsList);
        String username = sessionManager.getUsername();
        String password = sessionManager.getPassword();
        if (type.equals("editPost")) {
            params = new Object[]{1, username, password, id, content, true};
        }
        else {
            params = new Object[]{
                    1, username, password, content, true
            };
        }
        Object result = null;
        try {
            result = client.call("wp." + type, params);
        } catch (XMLRPCException e) {
            e.printStackTrace();
        }
        Intent sendSnackBar = new Intent("sendSnackBar");
        LocalBroadcastManager.getInstance(App.getAppContext()).sendBroadcast(sendSnackBar);
        Log.d("snackbar", "Sent snackbar");
        StringBuilder images = new StringBuilder();
        String prefix = "";
        for(int i=0;i<imageURL.size();i++) {
            images.append(prefix);
            prefix = ",";
            images.append(imageURL.get(i));
        }
        return getPostUrl(result.toString(), URL, imageURL.toString());
    }
    public static String authWp(String username, String password,Context context) throws XMLRPCException {
        sessionManager = new SessionManager(context);
        XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
        Object result = null;
        Object[] params = { 1, username, password, "", true };
        try {
            Log.d("WebUtils", "params" + params);
            client.call("android.auth", params);
            Log.d("Auth", "results: " + result.toString());
        }
        catch (XMLRPCException e) {
            result = "error";
            e.printStackTrace();
        }
        return getResults(result.toString());
    }
    public static String callWp(String method, Context context)
        throws XMLRPCException, IOException {
        adapter dbAdapter = new adapter(App.getInstance());
        sessionManager = new SessionManager(context);
        String result = null;
        XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
        HashMap<String, Object> theCall = new HashMap<String, Object>();
        Hashtable filter = new Hashtable();
        filter.put("hide_empty", false);
        theCall.put("filter", filter); //struct
        String taxonomy = "properties";
        Object[] params = { 1, sessionManager.getUsername(), sessionManager.getPassword(), taxonomy, theCall };
        Object[] obj = (Object[]) client.call(method, params); //return as array
        Integer propId = 0;
        String name = "", address = "", image = "";
        for (int i = 0; i < obj.length; i++) {
            List<String> contractors = new ArrayList<>();
            List<String> complex_mgrs = new ArrayList<>();
            Map<String, Object> each =  null;
            try {
                each = (Map<String, Object>) obj[i];
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            name = (String) each.get("name");
            address = (String) each.get("address");
            propId = Integer.valueOf(String.valueOf(each.get("term_id")));
            for (Entry<String, Object> entry : each.entrySet()) {
                if (entry.getValue() instanceof String) {
                    if (entry.getKey() == constants.PROPERTY_NAME) {
                        name = (String) entry.getValue();
                    }
                    Log.d("STRING", entry.getKey() + " val:" + entry.getValue().toString());
                }
                if (entry.getValue() instanceof HashMap) {
                    Map<String, String> imageMap = (HashMap<String, String>) entry.getValue();
                    image = (String) imageMap.get("image");
                    Log.d("HASHMAP", entry.getValue().toString() + " for " + entry.getKey());
                }
                else if (entry.getValue() instanceof Object) {
                    String t = entry.getValue().getClass().getName();
                    Log.d("TAG", "Class for " + entry.getKey() + " is: " + t);
                    if (t == "java.lang.String") {
                        Log.d("TAG", "String " + entry.getValue() + " for " + entry.getKey());
                    }
                    else if (t == "java.lang.Boolean") {
                        Log.d("TAG", "Boolean: " + entry.getValue() + " for " + entry.getKey());
                    } else if (t == "java.lang.Integer") {
                        Log.d("TAG", "Int: " + entry.getValue() + " for " + entry.getKey());
                    } else if (t == "java.util.HashMap") {
                        Map<String, String> imageMap = (HashMap<String, String>) entry.getValue();
                        image = (String) imageMap.get("image");
                        Log.d("HASHMAP", entry.getValue().toString() + " for " + entry.getKey());
                    } else {
                        try {
                            Object[] breakdown = (Object[]) entry.getValue();
                            for (int j = 0; j < breakdown.length; j++) {
                                switch (entry.getKey()) {
                                    case "contractor":
                                        contractors.add(breakdown[j].toString());
                                        break;
                                    case "complex_mgr":
                                        complex_mgrs.add(breakdown[j].toString());
                                        break;
                                    default:
                                        Log.d("IDK", entry.getKey() + "< THE KEY ... THE VALUE >>" + breakdown[j].toString());
                                        break;
                                }
                                Log.d("BREAKDOWN", entry.getKey() + " " + breakdown[j].toString());
                            }
                        }
                        catch (Exception e) {
                            Log.d("callWP", e.toString());
                        }

                    }
                    if (entry.getValue() instanceof Map || entry.getValue() instanceof HashMap) {
                        Log.d("MAP", "Fuck if i know" + entry.getValue().toString());
                    }
                }
            }
            String contractorList = String.valueOf(new JSONArray(contractors));
            String complexmgrList = String.valueOf(new JSONArray(complex_mgrs));
            dbAdapter.open();
            dbAdapter.addProperty(propId, name, address, image, contractorList, complexmgrList);
            dbAdapter.close();
            Intent intent = new Intent("updateListView");
            LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
            sessionManager.setNoProperties(false);
         }
       return result;
    }
    public static void getPosts(long IDz)
        throws XMLRPCException, IOException {
            adapter dbAdapter = new adapter(App.getInstance());
            sessionManager = new SessionManager(App.getAppContext());
            String sXmlRpcMethod = "wp.getPosts";
            long prop_id = sessionManager.propertySelected();
            String result = null;
            XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("post_type", "violations");
            filter.put("post_status", "publish");
            filter.put("number", 99999);
            // Term Fields
            HashMap<String, List<String>> tax = new HashMap<String, List<String>>();
            List<String> property = new ArrayList<String>();
            property.add(String.valueOf(sessionManager.propertySelected()));
            tax.put("properties", property);
            //Custom Fields
            List<String> fields = new ArrayList<>();
            fields.add("post_id");
            fields.add("post_title");
            fields.add("link");
            fields.add("post_date");
            fields.add("custom_fields");
            fields.add("post_content");
            fields.add("terms");
            //customField= constants.POST_COMMENTS;
            //customFieldsList.add(customField);
            //customField = constants.PICKEDUP;
            //customFieldsList.add(customField);
            HashMap<String, Object> other = new HashMap<String, Object>();
            other.put("fields", fields);
                //Custom Fields
            Object[] params = { 1, sessionManager.getUsername(), sessionManager.getPassword(), filter, fields };
            Object[] results = (Object[]) client.call(sXmlRpcMethod, params);
            Log.d("WebUtils",results.length + " lengths");
        for (int x = 0; x < results.length; x++) {
            getShit(results[x]);
        }
           /* HashMap<String, Object[]> contentHash = new HashMap<String, Object[]>();
            contentHash = (HashMap<String, Object>) results;
            String Post_ID = contentHash.get("post_id").toString();
            Log.d("TAG",Post_ID + " " + " ...successfulcall?");*/
    }

    private static void getShit(Object result) {
        String bldg = "";
        String unit = "", comments = "", image_path="", viol_type = "";
        long picked_up = 0;
        HashMap<String, Object> each = (HashMap<String, Object>) result;
        String ID = (String) each.get("post_id");
        Object[] terms = (Object[]) each.get("terms");
        Object[] custom_fields = (Object[]) each.get("custom_fields");
        for (int y = 0; y < terms.length; y++) {
            HashMap<String,String> term = (HashMap<String, String>) terms[y];
            for (Map.Entry<String,String> termz : term.entrySet()) {
                if (termz.getValue() instanceof String) {
                    Log.d("Term", termz.getKey() + termz.getValue() + "");
                }
                else { Log.d("Terms", termz.getKey() + " Unknown "); }
            }
        }
        for (Object custom_field : custom_fields) {
            HashMap<String, String> keyvals = (HashMap<String, String>) custom_field;
            for (Entry<String, String> keyval : keyvals.entrySet()) {
                Log.d("custom_fields", keyval.getKey() + " val:" + keyval.getValue());
                // bldg = keyval.getKey()
                unit = keyvals.get("unit");
                comments = keyvals.get(constants.POST_COMMENTS);
                image_path = keyvals.get(constants.POST_IMAGES);
                picked_up = Long.getLong(keyvals.get(constants.PICKEDUP), 0);
                viol_type = keyvals.get(constants.POST_VIOLATION_TYPE);
            }
        }
        Log.d("POST", "Bldg:" + bldg + "Unit: " + unit + " comments: " + comments + " image: " + image_path + " picked: " + picked_up + " viol: " + viol_type);
        /*for (Map.Entry<String, Object> entry : each.entrySet()) {
            if (entry.getValue() instanceof String) {
                Log.d("entry", entry.getKey() + " " + entry.getValue());
            }
            else { Log.d("ENTRY", entry.getKey() + " " + entry.getValue().getClass().getName()); }
        }*/
    }

    private static String uploadImage(String image, Context context)
            throws XMLRPCException {
        String resultUrl = "";
        XMLRPCClient client = new XMLRPCClient(URL + "xmlrpc.php");
        String sXmlRpcMethod = "wp.uploadFile";
        MediaFile mf = new MediaFile();
        String orientation = "1";
        String mimeType = "image/jpeg";
        File jpeg = new File(image);
        mf.setFilePath(jpeg.getPath());
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("name", jpeg.getName());
        m.put("type", mimeType);
        m.put("bits", mf);
        m.put("overwrite", true);
        SessionManager utils = new SessionManager(context);
        Object[] params = {
                1, utils.getUsername(), utils.getPassword(), m
        };

        Object result = null;
        try {
            result = (Object) client.call(sXmlRpcMethod, params);
            jpeg.delete();
        }
        catch (XMLRPCException e) {
            Log.d("ERROR!", "caught error");
            e.printStackTrace();
        }
        HashMap<String, Object> contentHash = new HashMap<String, Object>();
        contentHash = (HashMap<String, Object>) result;
        resultUrl = contentHash.get("url").toString();
        return resultUrl;
    }
    private static String prepareBodyOfPost(String violation_type, List<String> imageURL) {
        StringBuffer body = new StringBuffer();
        for (int i = 0; i < imageURL.size(); i++) {
            body.append("<img style=\"display:block;margin-right:auto;margin-left:auto;\" src=\""
                    + imageURL.get(i) + "\" alt=\"image\" />");
        }
        body.append("\n" + violation_type);
        System.out.println(body.toString());
        String formattedString = body.toString();
        formattedString = formattedString.replace("/\n\n/g", "</p><p>");
        formattedString = formattedString.replace("/\n/g", "<br />");
        return formattedString;
    }
   public static HashMap<String, String> getPostUrl(String postID, String url, String imageURL) {
        HashMap<String, String> pair = new HashMap<>();
        pair.put("url", url + "?p=" + postID);
        pair.put("images", imageURL);
        pair.put("violation_id", postID);
        return pair;
    }
    public static String getResults(String params) {
        return params;
    }


}

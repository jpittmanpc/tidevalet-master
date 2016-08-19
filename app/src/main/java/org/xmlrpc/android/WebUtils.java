
package org.xmlrpc.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Properties;
import com.tidevalet.thread.adapter;
import com.tidevalet.thread.constants;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.wordpress.android.MediaFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class WebUtils {
    static SessionManager sessionManager;

    public static String uploadPostToWordpress(Properties properties, List<String> image, String violation_type, String bldg, String unit, String comments,
            Attributes service, Context context) throws XMLRPCException {
        List<String> imageURL = new ArrayList<>();
        sessionManager = new SessionManager(context);
        service.setUrl(sessionManager.getDefUrl());
        for (int i=0;i<image.size();i++) {
            Log.d("uploading", i + " image");
            String result = uploadImage(image.get(i), service, context);
            imageURL.add(result);
        }
        XMLRPCClient client = new XMLRPCClient(service.getUrl() + "xmlrpc.php");
        // Setup Post
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("post_type", "violations");
        content.put("title", "properties");
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
        content.put("custom_fields", customFieldsList);
        String username = sessionManager.getUsername();
        String password = sessionManager.getPassword();
        Object[] params = {
                1, username, password, content, true
        };
        Object result = null;
        try {
            Log.d("WebUtil_class", "params: " + params);
            result = client.call("wp.newPost", params);
            Log.d("WebUtil_class", "result: " + result.toString());
        } catch (XMLRPCException e) {
            e.printStackTrace();
        }
        return getPostUrl(result.toString(), service.getUrl());
    }
    public static String authWp(String username, String password,Context context) throws XMLRPCException {
        sessionManager = new SessionManager(context);
        XMLRPCClient client = new XMLRPCClient(sessionManager.getDefUrl() + "xmlrpc.php");
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
        throws XMLRPCException, UnsupportedEncodingException, ClientProtocolException, IOException {
        adapter dbAdapter = new adapter(App.getInstance());
        sessionManager = new SessionManager(context);
        String sXmlRpcMethod = method;
        String result = null;
        XMLRPCClient client = new XMLRPCClient(sessionManager.getDefUrl() + "xmlrpc.php");
        HashMap<String, Object> theCall = new HashMap<String, Object>();
        Hashtable filter = new Hashtable();
        filter.put("hide_empty", false);
        theCall.put("filter", filter);
        String taxonomy = "properties";
        Object[] params = {
                1, sessionManager.getUsername(), sessionManager.getPassword(), taxonomy, theCall
        };
        Object[] obj = (Object[]) client.call(method, params);

        Integer propId = 0;
        String name = "", address = "", image = "";
        for (int i = 0; i < obj.length; i++) {
            List<String> contractors = new ArrayList<>();
            List<String> complex_mgrs = new ArrayList<>();
            Map<String, Object> each = (Map<String, Object>) obj[i];
            name = (String) each.get("name");
            address = (String) each.get("address");
            propId = Integer.valueOf(String.valueOf(each.get("term_id")));
            for (Map.Entry<String, Object> entry : each.entrySet()) {
                if (entry.getValue() instanceof String) {
                    if (entry.getKey() == constants.PROPERTY_NAME) {
                        name = (String) entry.getValue();
                    }

                    Log.d("STRING", entry.getKey() + " val:" + entry.getValue().toString());
                } else if (entry.getValue() instanceof Object) {
                    String t = entry.getValue().getClass().getName();
                    Log.d("TAG", "Class for " + entry.getKey() + " is: " + t);
                    if (t == "java.lang.Boolean") {
                        Log.d("TAG", "Boolean: " + entry.getValue() + " for " + entry.getKey());
                    } else if (t == "java.lang.Integer") {
                        Log.d("TAG", "Int: " + entry.getValue() + " for " + entry.getKey());
                    } else if (t == "java.util.HashMap") {
                        Map<String, String> imageMap = (HashMap<String, String>) entry.getValue();
                        image = (String) imageMap.get("image");
                        Log.d("HASHMAP", entry.getValue().toString() + " for " + entry.getKey());
                    } else {
                        Object[] breakdown = (Object[]) entry.getValue();
                        for (int j = 0; j < breakdown.length; j++) {
                            switch (entry.getKey()) {
                                case "contractor":
                                    contractors.add(breakdown[j].toString());
                                    break;
                                case "complex_mgrs":
                                    complex_mgrs.add(breakdown[j].toString());
                                    break;
                                default:
                                    Log.d("IDK", entry.getKey() + "< THE KEY ... THE VALUE >>" + breakdown[j].toString());
                                    break;
                            }
                            Log.d("BREAKDOWN", entry.getKey() + " " + breakdown[j].toString());
                            //Map<String, Object> wtf = (HashMap<String, Object>) breakdown[i];
                        /*for (Map.Entry<String, Object> entries : wtf.entrySet()) {
                            if (entries.getValue() instanceof String) {
                                Log.d("ENTRIES", entries.getKey().toString() + " <key val> " + entries.getValue().toString());
                            }
                            else if (entries.getValue() instanceof Object) {
                                Object[] fuck = (Object[]) entries.getValue();
                                Log.d("FUCK", fuck.toString());
                            }
                        }*/
                        }
                    }
                    if (entry.getValue() instanceof Map || entry.getValue() instanceof HashMap) {
                        Log.d("MAP", "Fuck if i know");
                    }
                }
            }
                String contractorList = String.valueOf(new JSONArray(contractors));
                Log.d("Contractor List", contractorList.toString());
                dbAdapter.open();
                dbAdapter.addProperty(propId, name, address, image, contractorList);
                dbAdapter.close();

        /*for (int i=0; i<obj.length;i++) {

            Class<? extends Object> c = obj[i].getClass();
            Log.d("CLASS", c.toString());
            /*if (c == String.class) {
                String what = obj[i].toString();
                Log.d("TAGwhat", what);
            }
            if (c == Array.class) {

            }
            HashMap<String, Object[]> test = (HashMap<String, Object[]>) obj[i];

        }
        for (Object v : obj) {
            Log.d("TAG2", v.toString());
        }*/

            //String term_id = contentHash.get("term_id").toString();
            //String name = contentHash.get("name").toString();
            //Log.d("WebUtils", "Term ID: " + term_id + " Name:" + name);
            if (sessionManager.noProperties()) {
                Intent intent = new Intent("updateListView");
                LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                sessionManager.setNoProperties(false);
                Log.d("WebUtils", "updating list after retrieval");
            }
        }
       return result;
    }
    public interface ListenerCallback {
        void onUpdate();
    }
    private static void ObjToString(Object[] key) {
        List<Object> list = new ArrayList<Object>();
        String TAG = "TAG";
        Log.d(TAG, list.get(0).toString() + " " + list.get(1).toString());
    }
    private static String uploadImage(String image, Attributes service, Context context)
            throws XMLRPCException {
        String resultUrl = "";
        XMLRPCClient client = new XMLRPCClient(service.getUrl() + "xmlrpc.php");
        String sXmlRpcMethod = "wp.uploadFile";
        Log.i("uploadImage", "Uploading image " + service.getUrl() + image);
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
            throw e;
        }
        HashMap<String, Object> contentHash = new HashMap<String, Object>();
        contentHash = (HashMap<String, Object>) result;
        resultUrl = contentHash.get("url").toString();
        return resultUrl;
    }
    private static String wpGetProps() {
     return null;
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

    /*public static boolean uploadPostToXPArena(PupilServices service, Pupil pupil, String url,
            String score) {
        boolean value = false;
        HttpPost post = new HttpPost(service.getUrl());
        DefaultHttpClient client = new DefaultHttpClient();
        String username = "";
        String password = "";
        if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
            username = IConstants.USER1;
            password = IConstants.PASS1;
        } else {
            username = service.getUsername();
            password = service.getPassword();
        }

        StringBuffer body = new StringBuffer();
        body.append("<?xml version=\"1.0\"?>");
        body.append("<note>");
        body.append("<username>" + username + "</username>");
        body.append("<password>" + password + "</password>");
        body.append("<url>" + url + "</url>");
        body.append("<score>" + score + "</score>");
        body.append("<nick>" + service.getNickname() + "</nick>");
        body.append("</note>");
        try {
            StringEntity entity = new StringEntity(body.toString());
            post.setEntity(entity);
            post.setHeader(HTTP.CONTENT_TYPE, "text/xml");
            // post.setHeader(HTTP.CONTENT_LEN, ""+entity.getContentLength());
            HttpResponse response = client.execute(post);
            value = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }*/
    public static String getPostUrl(String postID, String url) {
        return url + "?p=" + postID;
    }
    public static String getResults(String params) {
        return params;
    }


}

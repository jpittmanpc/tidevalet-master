
package org.xmlrpc.android;

import android.content.Context;
import android.util.Log;

import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Violation;

import org.apache.http.client.ClientProtocolException;
import org.wordpress.android.MediaFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class WebUtils {
    public static String uploadPostToWordpress(Violation violation, String image, String grade,
            Attributes service, Context context) throws XMLRPCException {
        SessionManager utils = new SessionManager(context);
        service.setUrl(utils.getDefUrl());
        String imageURL = uploadImage(image, service, context);
        XMLRPCClient client = new XMLRPCClient(service.getUrl() + "xmlrpc.php");
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("post_type", "post");
        content.put("title", "Test");
        content.put("description", prepareBodyOfPost(grade, imageURL));
        String username = utils.getUsername();
        String password = utils.getPassword();
        Object[] params = {
                1, username, password, content, true
        };
        Object result = null;
        try {
            result = client.call("metaWeblog.newPost", params);
        } catch (XMLRPCException e) {
            e.printStackTrace();
        }
        return getPostUrl(result.toString(), service.getUrl());
    }
    public Object callWp(String method, Context context)
        throws XMLRPCException, UnsupportedEncodingException, ClientProtocolException, IOException {
        SessionManager utils = new SessionManager(context);
        String sXmlRpcMethod = method;
        XMLRPCClient client = new XMLRPCClient(utils.getDefUrl() + "xmlrpc.php");
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("test","test");
        Object[] params = {
                1, utils.getUsername(), utils.getPassword(), m
        };
        Object response = client.call(method, params);
        Log.i("SENT", method + "");
        return response;

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

    private static String prepareBodyOfPost(String grade, String imageURL) {
        StringBuffer body = new StringBuffer();
        body.append("<img style=\"display:block;margin-right:auto;margin-left:auto;\" src=\""
                + imageURL + "\" alt=\"image\" />");
        body.append("\nTest : " + grade);
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
}

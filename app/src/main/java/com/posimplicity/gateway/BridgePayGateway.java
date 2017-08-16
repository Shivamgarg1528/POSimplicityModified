package com.posimplicity.gateway;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.JsonPakage.XML;
import com.utils.Helper;

public class BridgePayGateway {
    Context context;

    public BridgePayGateway(Context context) {
        this.context = context;
    }

    public boolean bridgeGatewayPayment(String username, String password,
                                        String CardNum, String totalAmt, String expMonth, String expYear, String personName, String magData) {

        try {
            HttpPost httppost = new HttpPost("https://gateway.itstgate.com/SmartPayments/transact.asmx/ProcessCreditCard");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserName", username));
            nameValuePairs.add(new BasicNameValuePair("Password", password));
            nameValuePairs.add(new BasicNameValuePair("TransType", "Sale"));
            nameValuePairs.add(new BasicNameValuePair("ExtData", ""));
            nameValuePairs.add(new BasicNameValuePair("CardNum", CardNum));
            nameValuePairs.add(new BasicNameValuePair("ExpDate", expMonth + "" + expYear));
            nameValuePairs.add(new BasicNameValuePair("MagData", magData));
            nameValuePairs.add(new BasicNameValuePair("NameOnCard", personName));
            nameValuePairs.add(new BasicNameValuePair("Amount", totalAmt));
            nameValuePairs.add(new BasicNameValuePair("InvNum", ""));
            nameValuePairs.add(new BasicNameValuePair("PNRef", ""));
            nameValuePairs.add(new BasicNameValuePair("Zip", ""));
            nameValuePairs.add(new BasicNameValuePair("Street", ""));
            nameValuePairs.add(new BasicNameValuePair("CVNum", ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            Log.d("Bridge", "bridgeGatewayPayment: " + Helper.convertXmlToJson(responseString));
            XmlPullParserFactory pullParserFactory;

            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream in_s = new ByteArrayInputStream(responseString.getBytes());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            boolean done = parseXML(parser);
            System.out.println("success?" + Boolean.toString(done));
            if (done) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception : " + e.getMessage());
            return false;
        }

    }

    private boolean parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        String Result = "";
        String responseMessage = "";
        String getCommercialCard = "";
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    //responses = new ArrayList<Response>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equals("Response")) {
                    }
                    if (name.equals("Result")) {
                        Result = parser.nextText().toString();
                        Log.i("REsult is", Result);
                        // Log.d("Result3", name);
                    }
                    if (name.equals("RespMSG")) {
                        responseMessage = parser.nextText().toString();
                        Log.i("Respmsg is", responseMessage);
                    }
                    if (name.equals("Message")) {
                        Result = parser.nextText().toString();
                        Log.i("Msg is", Result);
                    }
                    if (name.equals("Authcode")) {
                        Result = parser.nextText().toString();
                        Log.i("Auth code is", Result);
                    }
                    if (name.equals("PNRef")) {
                        Result = parser.nextText().toString();
                        Log.i("PNref is", Result);
                    }
                    if (name.equals("HostCode")) {
                        Result = parser.nextText().toString();
                        Log.i("Host is", Result);
                    }
                    if (name.equals("HostURL")) {
                        Result = parser.nextText().toString();
                        Log.i("Host Url is", Result);
                    }
                    if (name.equals("ReceiptURL")) {
                        Result = parser.nextText().toString();
                        Log.i("RecieptURl is", Result);
                    }
                    if (name.equals("GetAVSResult")) {
                        Result = parser.nextText().toString();
                        Log.i("AVS result is", Result);
                    }
                    if (name.equals("GetAVSResultTXT")) {
                        Result = parser.nextText().toString();
                        Log.i("AVSresult TEXT is", Result);
                    }
                    if (name.equals("GetStreetMatchTXT")) {
                        Result = parser.nextText().toString();
                        Log.i("GetStreetMatchTXT is", Result);
                    }
                    if (name.equals("GetCommercialCard")) {
                        getCommercialCard = parser.nextText().toString();
                        Log.i("Com card is", getCommercialCard);
                    }
                    if (name.equals("ExtData")) {
                        Result = parser.nextText().toString();
                        Log.i("extData is", Result);
                    }
            }
            eventType = parser.next();
            if (responseMessage.equals("Approved")) {
                System.out.println("Successfull Transaction");
                return true;
            }
        }
        return false;
    }
}

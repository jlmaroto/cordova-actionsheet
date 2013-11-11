package com.phonegap.plugins.actionsheet;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaArgs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CordovaContextMenu {
    private CordovaActivity cordova;
    private JSONObject options;
    private CallbackContext callbackContext;

    public CordovaContextMenu(CordovaActivity cordova, CordovaArgs args,
            CallbackContext callbackContext) throws JSONException {
        this.cordova = cordova;
        this.options = args.getJSONObject(0);
        this.callbackContext = callbackContext;
    }

    boolean onContextItemSelected(MenuItem item) {
        try {
            JSONObject result = new JSONObject();
            result.put("buttonIndex", item.getItemId());
            callbackContext.success(result);
            return true;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    boolean onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        try {
            menu.setHeaderTitle(options.getString("title"));
            JSONArray labels = options.getJSONArray("items");
            int cancelIndex = options.getInt("cancelButtonIndex");
            for(int i = 0; i < labels.length(); i++) {
              if (i == cancelIndex) continue;
              menu.add(Menu.NONE, i, i, labels.getString(i));
            }
            return true;
        } catch (JSONException e) {
            // should never happen, we get the JSON from Cordova
            throw new RuntimeException(e);
        }
    }

    boolean onContextMenuClosed(Menu menu) {
        return true;
    }
}
